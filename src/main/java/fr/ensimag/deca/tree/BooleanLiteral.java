package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BNE;

import java.io.PrintStream;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type tBool = compiler.findType("boolean");
        setType(tBool);
        return tBool;     
    }


    @Override
    public void codeGenInst(DecacCompiler compiler, int register){
        if(this.value){
            compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.getR(register)));
        }else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(register)));
        }   
    }

    @Override
    protected  void codeGenInstIF(DecacCompiler compiler, int registre, Label fin){
        if(this.value){
            compiler.addInstruction(new LOAD(new ImmediateInteger(1),GPRegister.getR(registre)));
        }else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(registre)));
        } 
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        compiler.addInstruction(new BNE(fin));
    }

    
    public void codeGenInstOr(DecacCompiler compiler, int registre, Label ins, Label fin, boolean left){
        if(this.value){
            compiler.addInstruction(new LOAD(new ImmediateInteger(1),GPRegister.getR(registre)));
        }else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(registre)));
        } 
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        if (left){
            compiler.addInstruction(new BEQ(ins));
        }
        else{
            compiler.addInstruction(new BNE(fin));
        }
    } 

    

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

}
