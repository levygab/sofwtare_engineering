package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.log4j.Logger;

public class New extends AbstractExpr{

    private static final Logger LOG = Logger.getLogger(Main.class);

    private AbstractIdentifier className;

    public New(AbstractIdentifier ident){
        this.className = ident;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        // TODO Auto-generated method stub
        codeGenInstClass(compiler, registre);
        
    }

    public void codeGenInstClass(DecacCompiler compiler, int registre){
        ClassDefinition classe = (ClassDefinition) compiler.getenvType().get(className.getName());
        //EnvironmentExp setFieldAndMeth =  classe.getMembers();
        int compteur = classe.getNumberOfFields();
        /*for( Symbol i  : setFieldAndMeth.keySet()){
            if (setFieldAndMeth.get(i).isField()){
                compteur++;
            }
        }*/
        compiler.addInstruction(new PUSH(GPRegister.getR(1)));
        compiler.addInstruction(new NEW(compteur+1, GPRegister.getR(registre)));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new BOV(new Label("tas_plein")));
        }
        compiler.addInstruction(new LEA(className.getClassDefinition().getOperand(), GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(0, GPRegister.getR(registre))));
        compiler.addInstruction(new PUSH(GPRegister.getR(registre)));
        compiler.addInstruction(new BSR(new Label("init."+className.getName().getName())));
        compiler.addInstruction(new POP(GPRegister.getR(registre)));
        compiler.addInstruction(new POP(GPRegister.getR(1)));
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("On passe dans verifyExpr de New");
        Type typeNew = className.verifyType(compiler);
        setType(typeNew);
        return typeNew;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        className.decompile(s);
        s.print("()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.className.prettyPrint(s, prefix, true);
    }
    
}
