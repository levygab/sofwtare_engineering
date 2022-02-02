package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;


/**
 * @author gl18
 * @date 01/01/2022
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        int registre1 = GPRegister.findRegistre();
        int registre2 = registre;
        if(registre1 == 0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new SUB(GPRegister.getR(2),GPRegister.getR(registre1)));
            if (!(compiler.getCompilerOptions().getNoCheck())) {
                if(this.getType().isFloat()){
                    compiler.addInstruction(new BOV(new Label("io_debordement_valeur")));
                }
            }
            compiler.addInstruction(new LOAD(GPRegister.getR(registre1), GPRegister.getR(registre2)));
        }else{
            getRightOperand().codeGenInst(compiler, registre1);
            getLeftOperand().codeGenInst(compiler, registre2);
            compiler.addInstruction(new SUB(GPRegister.getR(registre1),GPRegister.getR(registre2)));
            if (!(compiler.getCompilerOptions().getNoCheck())) {
                if(this.getType().isFloat()){
                    compiler.addInstruction(new BOV(new Label("io_debordement_valeur")));
                }
            }
            GPRegister.freeRegistre(registre1);
        }
    }

    
    
}
