package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;



/**
 * @author gl18
 * @date 01/01/2022
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        int registre1 = GPRegister.findRegistre();
        int registre2 = registre;
        if(registre1 == 0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new MUL(GPRegister.getR(2),GPRegister.getR(registre1)));
            erreur(compiler);
            compiler.addInstruction(new LOAD(GPRegister.getR(registre1), GPRegister.getR(registre2)));
        }else{
            getRightOperand().codeGenInst(compiler, registre1);
            getLeftOperand().codeGenInst(compiler, registre2);
            compiler.addInstruction(new MUL(GPRegister.getR(registre1),GPRegister.getR(registre2)));
            erreur(compiler);
            GPRegister.freeRegistre(registre1);
        }
    }

}
