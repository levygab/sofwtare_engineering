package fr.ensimag.deca.tree;

import java.util.ArrayList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void getInstruction1(DecacCompiler compiler, Label inst){
        compiler.addInstruction(new BGT(inst));
    }

    public void getInstruction2(DecacCompiler compiler, Label inst){
        compiler.addInstruction(new BLE(inst));
    }
  

    @Override
    protected String getOperatorName() {
        return ">";
    }

}
