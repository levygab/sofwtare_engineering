package fr.ensimag.deca.tree;

import static org.mockito.Mockito.never;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

public class MethodCall extends AbstractLValue{

    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private AbstractExpr expression;
    private AbstractIdentifier methodeOuChamp;
    private ListExpr arguments;

    
    public MethodCall(AbstractExpr expr, AbstractIdentifier ident, ListExpr args){
        this.expression = expr;
        this.methodeOuChamp =ident;
        this.arguments = args;
    }

    public MethodCall(AbstractExpr expr, AbstractIdentifier ident){
        this.expression = expr;
        this.methodeOuChamp =ident;
    }

    public MethodCall(AbstractIdentifier ident, ListExpr args){
        this.methodeOuChamp = ident;
        this.arguments = args;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        if (arguments == null){
            LOG.debug("On appele un attribut");
            return verifyExprLValue(compiler, localEnv, currentClass);
        }else{
            LOG.debug("On appele une methode");
            return verifyExprMethode(compiler, localEnv, currentClass);
        }
    }
    
    public Type verifyExprMethode(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type typeMethode;
        if(!(expression == null)){
            Type typeExpr = expression.verifyExpr(compiler, localEnv, currentClass);
            if(!typeExpr.isClass()){
                throw new ContextualError("On ne peut pas lancer un appel a methode sur un attribut sans classe", getLocation());
            }
            ClassType typeClassExpr = (ClassType) typeExpr;
            ClassDefinition def = (ClassDefinition)compiler.getenvType().get(typeClassExpr.getName());
            LOG.debug(def.getMembers().keySet());
            typeMethode = methodeOuChamp.verifyExpr(compiler, def.getMembers(), currentClass);
        }else{
            typeMethode = methodeOuChamp.verifyExpr(compiler, localEnv, currentClass);
        }
        //On vérifie qu'il s'agit bien d'un champ
        if(!methodeOuChamp.getDefinition().isMethod()){
            throw new ContextualError("Vous avez appelé un élément qui n'est pass une methode avec la syntaxe d'une methode", getLocation());
        }
        //methode.getMethodDefinition()
        Signature signMethod = methodeOuChamp.getMethodDefinition().getSignature();
        if(!(signMethod.size() == arguments.size())){
            throw new ContextualError("Vous n'avez pas donné le bon nombre d'arguments à la méthode", getLocation());
        }
        int compteur = 0;
        ListExpr argsRevus = new ListExpr();
        for(AbstractExpr expr : arguments.getList()){
            AbstractExpr ExprArg = expr.verifyRValue(compiler, localEnv, currentClass, signMethod.paramNumber(compteur));
            argsRevus.add(ExprArg);
            compteur = compteur + 1;
        }
        this.arguments = argsRevus; // On set les nouvelles expr pour introduire des ConvFloat
        setType(typeMethode);
        return typeMethode;
    }

    public Type verifyExprLValue(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        
        Type typeExpr = expression.verifyExpr(compiler, localEnv, currentClass);
        if(!(typeExpr.isClass())){
            throw new ContextualError("L'expression n'est pas une classe ", getLocation());
        }
        ClassType typeClassExpr = (ClassType) typeExpr;
        //TODO : mettre bien
        ClassDefinition def = (ClassDefinition)compiler.getenvType().get(typeClassExpr.getName());
        LOG.debug(def.getMembers().keySet());
        Type typeIdent = methodeOuChamp.verifyExpr(compiler, def.getMembers(), currentClass);
        if(!methodeOuChamp.getDefinition().isField()){
            throw new ContextualError("Vous avez appelé un élément qui n'est pass un champ avec la syntaxe d'un champ", getLocation());
        }
        Definition defIdent = methodeOuChamp.getFieldDefinition();
        LOG.debug(defIdent);
        if(!defIdent.isField()){
            LOG.debug("On va lancer une erreur");
            throw new ContextualError("L'identifiant n'est pas un champ ", getLocation());
        }
        FieldDefinition defFieldIdent = (FieldDefinition) defIdent;
        LOG.debug(defFieldIdent);
        if(defFieldIdent.getVisibility() == Visibility.PROTECTED){
            //Si l'identifiant est protected il faut faire des vérifications en plus
            if(currentClass == null){
                throw new ContextualError("Un champ protégé ne peut pas être appelé depuis le main", getLocation());
            }else if(!(currentClass.getType().isSubClassOf(defFieldIdent.getContainingClass().getType())) || !(typeClassExpr.isSubClassOf(currentClass.getType()))){
                throw new ContextualError("La classe actuelle n'as pas accès au paramètre", getLocation());
            }
        }
        setType(typeIdent);
        LOG.debug("On a fini la methodcall");
        return typeIdent;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if(!(expression == null)){
            expression.decompile(s);
            s.print(".");
        }
        methodeOuChamp.decompile(s);
        if(arguments != null){
            s.print("(");
            int nbreExpressions = arguments.getList().size();
            int compteur = 0;
            if(arguments.getList().size() == 1){
                for(AbstractExpr a : arguments.getList()){
                    a.decompile(s);
                }
            }else{
                for(AbstractExpr a : arguments.getList()){
                    a.decompile(s);
                    compteur = compteur + 1;
                    if(compteur==nbreExpressions){
                        continue;
                    }
                    s.print(", ");
                }
            }
            s.print(")");
        }
        //s.print(";");
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if(!(expression == null)){
            this.expression.prettyPrint(s, prefix, false);
        }
        if (!(this.arguments == null)){
            s.print(prefix);
            s.println("DOT");
            this.methodeOuChamp.prettyPrint(s, prefix, false);
            this.arguments.prettyPrint(s, prefix, true);
        }else {
            s.print(prefix);
            s.println("DOT");
            this.methodeOuChamp.prettyPrint(s, prefix, true);
        }
    }

    @Override
    public void codeGenAssign(DecacCompiler compiler, int registre) {
        RegisterOffset addr = new RegisterOffset(methodeOuChamp.getFieldDefinition().getIndex()+1, GPRegister.getR(compiler.getRegistreUseForMeth()));
        if(expression.isMethodCall()){
            int reg = GPRegister.findRegistre();
            MethodCall exprMeth = (MethodCall) expression;
            AbstractIdentifier identMethod = exprMeth.methodeOuChamp;
            int index = identMethod.getDefinition().isField()?identMethod.getFieldDefinition().getIndex()+1 : identMethod.getFieldDefinition().getIndex()+1;
            addr = new RegisterOffset(index, GPRegister.getR(compiler.getRegistreUseForMeth()));
            compiler.addInstruction(new LOAD( addr, GPRegister.getR(reg)));
            addr = new RegisterOffset(methodeOuChamp.getFieldDefinition().getIndex()+1, GPRegister.getR(reg));
            compiler.addInstruction(new STORE(GPRegister.getR(registre), addr));
        }else{
            compiler.addInstruction(new STORE(GPRegister.getR(registre), addr));
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        if (arguments == null){
            LOG.debug("On appele un attribut codegen");
            codeGenAttribut(compiler, registre);
        }else{
            LOG.debug("On appele une methode codegen");
            codeGenMethCall(compiler, registre);
        }
    }

    public void codeGenAttribut(DecacCompiler compiler, int registre){
        if(compiler.getRegistreUseForMeth() == 0){
            compiler.addInstruction(new LOAD(methodeOuChamp.getExpDefinition().getOperand(), GPRegister.getR(registre)));
        }else{
            RegisterOffset addr = new RegisterOffset(methodeOuChamp.getFieldDefinition().getIndex()+1, GPRegister.getR(compiler.getRegistreUseForMeth()));
            compiler.addInstruction(new LOAD(addr, GPRegister.getR(registre)));
        }
        
    }

    public void codeGenMethCall(DecacCompiler compiler, int registre){
        compiler.addInstruction(new ADDSP(arguments.getList().size()+1));
        /*MethodDefinition methDef = this.methodeOuChamp.getMethodDefinition();
        Symbol sym = methDef.getType().getName();
        ClassDefinition defClass = (ClassDefinition)compiler.getenvType().get(sym);*/
        //TODO : gérer le cas this ou rien
        if((expression != null) && (expression.isIdentifier())){
            AbstractIdentifier ident = (AbstractIdentifier) expression;
            compiler.addInstruction(new LOAD(ident.getExpDefinition().getOperand(), GPRegister.getR(2)));
            compiler.addInstruction(new STORE(GPRegister.getR(2), new RegisterOffset(0, GPRegister.SP)));
        }else if((expression != null) && (expression.isMethodCall())){
            MethodCall exprMeth = (MethodCall) expression;
            AbstractIdentifier identMethod = exprMeth.methodeOuChamp;
            if (identMethod.getDefinition().isField()){
                int index = identMethod.getFieldDefinition().getIndex()+1;
                DVal addr = new RegisterOffset(index, GPRegister.getR(compiler.getRegistreUseForMeth()));
                compiler.addInstruction(new LOAD(addr, GPRegister.getR(2)));
                compiler.addInstruction(new STORE(GPRegister.getR(2), new RegisterOffset(0, GPRegister.SP)));
            }else{
                exprMeth.codeGenInst(compiler, registre);
                compiler.addInstruction(new STORE(GPRegister.getR(compiler.getRegistreUseForMeth()), new RegisterOffset(0, GPRegister.SP)));
            }
        }else{
            /*RegisterOffset addr = (RegisterOffset) methodeOuChamp.getMethodDefinition().getOperand();
            int offsetMethode = addr.getOffset() + methodeOuChamp.getMethodDefinition().getIndex();
            RegisterOffset addrMethode = new RegisterOffset(offsetMethode, addr.getRegister());
            System.out.println(addrMethode);*/
            compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getRegistreUseForMeth()), GPRegister.getR(2)));
            compiler.addInstruction(new STORE(GPRegister.getR(2), new RegisterOffset(0, GPRegister.SP)));
        }
        
        //ClassDefinition defClass = (ClassDefinition) compiler.getenvType().get(ident.getDefinition().getType().getName()); 
        int compteur=-1;
        for ( AbstractExpr a : arguments.getList()){
            a.codeGenInst(compiler, 2);
            compiler.addInstruction(new STORE(GPRegister.getR(2), new RegisterOffset(compteur, GPRegister.SP)));
            compteur --; 
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(0, GPRegister.SP), GPRegister.getR(2)));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), GPRegister.getR(2)));
            compiler.addInstruction(new BEQ(new Label("Deferencement_de_null")));
        }
        Label labelMeth = methodeOuChamp.getMethodDefinition().getLabel();
        compiler.addInstruction(new BSR(labelMeth));
        compiler.addInstruction(new LOAD(GPRegister.getR(compiler.getRegReturn()), GPRegister.getR(registre)));
        compiler.addInstruction(new SUBSP(arguments.getList().size()+1));
    }

    public void codeGenInstClass(DecacCompiler compiler, int registre){
        codeGenMethCall(compiler, registre);
    }

    @Override
    protected boolean isMethodCall(){
        return true;
    }
}
