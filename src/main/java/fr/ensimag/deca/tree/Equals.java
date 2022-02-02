package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.util.ArrayList;

import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    } 

    public void getInstruction1(DecacCompiler compiler, Label inst){
        compiler.addInstruction(new BEQ(inst));
    }

    public void getInstruction2(DecacCompiler compiler, Label inst){
        compiler.addInstruction(new BNE(inst));
    }
    
}
