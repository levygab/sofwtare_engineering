package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;
import java.util.Iterator;
import java.lang.Iterable;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(StringLiteral.class);
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    public void codeGenInstClass(DecacCompiler compiler, int registre){}

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;


    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        LOG.debug("ON passe dans le verifyRValue de AbstractExpr, on récupère l'expression");
        Type typeExpr = this.verifyExpr(compiler, localEnv, currentClass);
        LOG.debug("Type :" +typeExpr);
        LOG.debug("Type attendu :" +expectedType);
        if(expectedType.isFloat() & typeExpr.isInt()){
            LOG.debug("On passe dans le cas Convfloat");
            ConvFloat convF = new ConvFloat(this);
            convF.verifyExpr(compiler, localEnv, currentClass);
            return convF;
        } else if (expectedType.sameType(typeExpr)){
            if(expectedType.isClass()){
                ClassType typeExprClass = (ClassType) typeExpr;
                ClassType expectedTypeClass = (ClassType) expectedType;
                if (typeExprClass.isSubClassOf(expectedTypeClass)){
                    return this;
                }else if(typeExpr.getName().getName() == expectedType.getName().getName()){
                    return this;
                }else{
                    throw new ContextualError("Le type de la valeur à assigner ne se correspond pas au type de la variable (3.28)", getLocation());
                }
            } else if (typeExpr.isTable() && this.getClass().equals(Table.class)) {
                LOG.debug("On passe dans la partie spéciale pour les tableaux");
                Table table = (Table) this;
                Type subType = table.getSubType();
                if (table.getListExpr() != null) {
                    Iterator<AbstractExpr> itTable = table.getListExpr().iterator();
                    while (itTable.hasNext()) {
                        itTable.next().verifyRValue(compiler, localEnv, currentClass, subType);
                    }
                }
                return this;
            } else {
                setType(typeExpr);
                return this;
            }
        } else {
            throw new ContextualError("Le type de la valeur à assigner ne se correspond pas au type de la variable (3.28)", getLocation());
        }
    }

    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        //Je pense que cette instruction doit boucler avec un verifyExpr de la même expression, parceque ce n'est pas un print dans la grammaire
        LOG.debug("ON passe dans le verifyInst de AbstractExpr, on lance verifyexpr");
        Type exprType = this.verifyExpr(compiler, localEnv, currentClass);
        LOG.debug("Le type de l'expression est : "+exprType);
        LOG.debug("Le type attendu est : "+returnType);
        //setType(exprType);
        // if(!exprType.sameType(returnType)){
        //     throw new ContextualError("Les types ne se correspondents pas", this.getLocation());
        // }
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("ON passe dans le verifyCondition de AbstractExpr");
        Type exprType = this.verifyExpr(compiler, localEnv, currentClass);
        if (!exprType.isBoolean()){
            throw new ContextualError("La condition doit être un booleen (3.29)", this.getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        //On prend le registre R1 (car il sert a print et ne sera pas utilisé) et on le passe à l'expression pour avoir sa valeur stockée
        LOG.debug("On rentre dans la fonction print d'une expression abstraite");
        this.codeGenInst(compiler, 1);
        if(getType().isInt()){
            LOG.debug("On ecrit la commande pour print int");
            compiler.addInstruction(new WINT());
        }else if(getType().isFloat()){
            if(hex){
                LOG.debug("On ecrit la commande pour print floathex");
                compiler.addInstruction(new WFLOATX());
            }else{
                LOG.debug("On ecrit la commande pour print floatdec");
                compiler.addInstruction(new WFLOAT());
            }
        }
        LOG.debug("On sort de la fonction print d'une expression abstraite");
    }


    protected abstract void codeGenInst(DecacCompiler compiler, int registre);
    protected  void codeGenInst(DecacCompiler compiler, int registre, Label inst, Label fin){
        LOG.debug("On passe dans AbtractExpr, il ya donc une erreur");
    }
    protected  void codeGenInstOr(DecacCompiler compiler, int registre, Label inst, Label fin, boolean left){
        LOG.debug("On passe dans AbtractExpr, il ya donc une erreur");
    }
    protected  void codeGenInstIF(DecacCompiler compiler, int registre, Label fin){
        LOG.debug("On passe dans AbtractExpr, il ya donc une erreur");
    }
    
    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    protected boolean isIdentifier(){
        return false;
    }

    protected boolean isMethodCall(){
        return false;
    }
}
