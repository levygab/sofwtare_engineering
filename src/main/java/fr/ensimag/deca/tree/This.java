package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class This extends AbstractExpr{

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        // TODO Auto-generated method stub  
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        //On vérifie que class n'est pas null (on est pas dans le main)
        if(currentClass == null){
            throw new ContextualError("This ne peut pas etre appelé dans le main (3.43)", getLocation());
        }else{
            setType(currentClass.getType());
            return getType();
        }

        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //Je mets rien
    }

    
}
