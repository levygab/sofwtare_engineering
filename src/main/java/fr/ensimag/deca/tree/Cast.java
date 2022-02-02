package fr.ensimag.deca.tree;

import static org.mockito.Mockito.never;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

public class Cast extends AbstractExpr{

    private AbstractIdentifier type;
    private AbstractExpr expression;

    public Cast(AbstractIdentifier ident, AbstractExpr expr){
        this.type = ident;
        this.expression = expr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        if(getType().isInt()){
            expression.codeGenInst(compiler, registre);
            compiler.addInstruction(new INT(GPRegister.getR(registre), GPRegister.getR(registre)));
        }else if(getType().isFloat()){
            expression.codeGenInst(compiler, registre);
            compiler.addInstruction(new FLOAT(GPRegister.getR(registre), GPRegister.getR(registre)));
        }
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type castType = type.verifyType(compiler);
        Type exprType = expression.verifyExpr(compiler, localEnv, currentClass);
                //TODO : Décider si on fait des cast de int a float;

        if((castType.isInt() && exprType.isFloat()) || (castType.isFloat() && exprType.isInt()) ){
            setType(castType);
            return castType;
        }
        //On vérifie qu'ils sont de type Class
        if((!(castType.isClass() && exprType.isClass()))){
            if(castType.sameType(exprType)){
                setType(castType);
                return castType;
            }else{
                throw new ContextualError("Les deux classes ne sont pas compatibles, on peut pas Cast 3.39", getLocation());
            }
            
        }
        //On downcast vers des classType
        ClassType castClassType = (ClassType) castType;
        ClassType exprClassType = (ClassType) exprType;

        //On vérifie qu'elles sont compatibles
        if(!(castClassType.isSubClassOf(exprClassType)) && !(exprClassType.isSubClassOf(castClassType))){
            throw new ContextualError("Les deux classes ne sont pas compatibles, on peut pas Cast 3.39", getLocation());
        }else{
            setType(castType);
            return castType;
        }


        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") (");
        expression.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, false);
        this.expression.prettyPrint(s, prefix, true);
    }
    
}
