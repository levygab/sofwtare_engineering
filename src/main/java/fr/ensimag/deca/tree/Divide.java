package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;


/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    protected void codeGenInst(DecacCompiler compiler) {
        }

    protected void codeGenInst(DecacCompiler compiler, int registre) {
        int registre1 = GPRegister.findRegistre();
        int registre2 = registre;
        if(registre1 == 0){
            super.codeGenInstOPPushPop(compiler);
            codeGenInstDiv(compiler, 2, 0);
            compiler.addInstruction(new LOAD(GPRegister.getR(0), GPRegister.getR(registre2)));
        }else{
            getRightOperand().codeGenInst(compiler, registre1);
            getLeftOperand().codeGenInst(compiler, registre2);
            codeGenInstDiv(compiler, registre1, registre2);
            GPRegister.freeRegistre(registre1);
        }
    }

    protected void codeGenInstDiv(DecacCompiler compiler, int registre1, int registre2){
        if(this.getType().isFloat()){
            compiler.addInstruction(new DIV(GPRegister.getR(registre1),GPRegister.getR(registre2)));
            if (!(compiler.getCompilerOptions().getNoCheck())) {
                compiler.addInstruction(new BOV(new Label("io_error_div_O")));
            }
        }else{
            compiler.addInstruction(new QUO(GPRegister.getR(registre1),GPRegister.getR(registre2)));
            if (!(compiler.getCompilerOptions().getNoCheck())) {
                compiler.addInstruction(new BOV(new Label("io_error_div_O")));
                }            
        GPRegister.freeRegistre(registre1);
        }
    }

}
