package fr.ensimag.deca.tree;

import java.util.Random;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    protected void codeGenInst(DecacCompiler compiler, int register, Label inst, Label fin) {
        getLeftOperand().codeGenInstOr(compiler, register, inst, fin, true);
        compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new BEQ(inst));
        getRightOperand().codeGenInstOr(compiler, register, inst, fin, false);
        compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new BEQ(inst));
    }

    public void codeGenInstOr(DecacCompiler compiler, int registre, Label inst, Label fin, boolean left){
        int registre1 = GPRegister.findRegistre();
        getLeftOperand().codeGenInstOr(compiler, registre, inst, fin, true);
        if (left){
            getRightOperand().codeGenInstOr(compiler, registre1, inst, fin, true);
        }
        else{
            getRightOperand().codeGenInstOr(compiler, registre1, inst, fin, false);
        }
        GPRegister.freeRegistre(registre1);
    }


    protected void codeGenInstIF(DecacCompiler compiler, int register, Label fin) {
        int registre1 = GPRegister.findRegistre();
        Label ins = new Label("instOr" + compiler.getLabelCounter());
        compiler.increaseLabelCounter();            
        getLeftOperand().codeGenInstOr(compiler, register, ins, fin, true);
        getRightOperand().codeGenInstOr(compiler, registre1, ins, fin, false);
        compiler.addLabel(ins);
        GPRegister.freeRegistre(registre1);
    }

    protected void codeGenInst(DecacCompiler compiler, int register) {
        int registre1 = GPRegister.findRegistre();
        
        Label labelTrue = new Label("Ortrue" + compiler.getLabelCounter());
        Label labelfin = new Label("Orfin" + compiler.getLabelCounter());
        compiler.increaseLabelCounter();
        if (registre1==0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(2)));
            compiler.addInstruction(new BEQ(labelTrue));
            compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(0)));
            compiler.addInstruction(new BEQ(labelTrue)); 
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(register)));
            compiler.addInstruction(new BRA(labelfin)); 
        }
        else {
            getLeftOperand().codeGenInst(compiler, register);
            compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(register)));
            compiler.addInstruction(new BEQ(labelTrue));
            getRightOperand().codeGenInst(compiler, registre1);
            compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(registre1)));
            compiler.addInstruction(new BEQ(labelTrue)); 
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(register)));
            compiler.addInstruction(new BRA(labelfin)); 
        }
        compiler.addLabel(labelTrue);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.getR(register)));
        compiler.addLabel(labelfin);
        GPRegister.freeRegistre(registre1);
    }


}
