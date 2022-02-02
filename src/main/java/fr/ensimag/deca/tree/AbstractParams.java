package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class AbstractParams extends AbstractExpr{

    private AbstractIdentifier ident;
    private AbstractIdentifier typeParam;

    public AbstractParams(AbstractIdentifier type, AbstractIdentifier ident){
        this.typeParam = type;
        this.ident = ident;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass){
        //TO override by params
        return null;
    }

    public AbstractIdentifier getIdent() {
        return ident;
    }

    public AbstractIdentifier getTypeParams() {
        return typeParam;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        typeParam.iter(f);
        ident.iterChildren(f);
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        typeParam.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, true);
        
    }
    
}
