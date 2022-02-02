package fr.ensimag.deca.tree;

import java.util.Random;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    protected void codeGenInst(DecacCompiler compiler, int register, Label inst, Label fin) {
        getLeftOperand().codeGenInst(compiler, register, inst, fin);
        compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new BNE(fin));
        getRightOperand().codeGenInst(compiler, register, inst, fin);
        compiler.addInstruction(new CMP(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new BEQ(inst));
    }

    public void codeGenInstOr(DecacCompiler compiler, int registre, Label inst, Label fin, boolean left){
        int registre1 = GPRegister.findRegistre();
        Label finAnd = new Label("FinAnd" + compiler.getLabelCounter());
        compiler.increaseLabelCounter();
        if (left){
            getLeftOperand().codeGenInstIF(compiler, registre, finAnd);
            getRightOperand().codeGenInstIF(compiler, registre, finAnd);
        }
        else{
            getLeftOperand().codeGenInstIF(compiler, registre, fin);
            getRightOperand().codeGenInstIF(compiler, registre, fin);
        }
        GPRegister.freeRegistre(registre1);
        compiler.addInstruction(new BRA(inst));
        compiler.addLabel(finAnd);
    }

    protected void codeGenInstIF(DecacCompiler compiler, int register, Label fin) {
        getLeftOperand().codeGenInstIF(compiler, register, fin);
        getRightOperand().codeGenInstIF(compiler, register, fin);
    }

    protected void codeGenInst(DecacCompiler compiler, int register) {
        int registre1 = GPRegister.findRegistre();
        if (registre1==0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new MUL(GPRegister.getR(2),GPRegister.getR(0)));
            compiler.addInstruction(new LOAD(GPRegister.getR(0),GPRegister.getR(register)));
        }
        else {
        getRightOperand().codeGenInst(compiler, register);
        getLeftOperand().codeGenInst(compiler, registre1);
        compiler.addInstruction(new MUL(GPRegister.getR(registre1),GPRegister.getR(register)));
        }

    }

}
