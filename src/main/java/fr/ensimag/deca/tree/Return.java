package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

public class Return extends AbstractInst{

    private AbstractExpr exprRetournee;

    public Return(AbstractExpr expr){
        this.exprRetournee = expr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int register) {
        // TODO Auto-generated method stub
        exprRetournee.codeGenInst(compiler, register);
        Label fin = compiler.getReturnLabel();
        compiler.setRegReturn(register);
        compiler.addInstruction(new BRA(fin));
    }

    public boolean isReturn(){
        return true;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        if(returnType.isVoid()){
            throw new ContextualError("On ne peut pas retourner un void", getLocation());
        }
        this.exprRetournee.verifyRValue(compiler, localEnv, currentClass, returnType);
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        exprRetournee.decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.exprRetournee.prettyPrint(s, prefix, true);
    }
}
