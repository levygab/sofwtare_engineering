package fr.ensimag.deca.tree;

import java.util.ArrayList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 * Operator "x >= y"
 * 
 * @author gl18
 * @date 01/01/2022
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void getInstruction1(DecacCompiler compiler, Label inst){
        compiler.addInstruction(new BGE(inst));
    }

    public void getInstruction2(DecacCompiler compiler, Label inst){
        compiler.addInstruction(new BLT(inst));
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
