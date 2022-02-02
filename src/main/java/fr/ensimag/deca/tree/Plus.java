package fr.ensimag.deca.tree;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;




/**
 * @author gl18
 * @date 01/01/2022
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) { 
        int registre1 = GPRegister.findRegistre();
        int registre2 = registre;
        if(registre1 == 0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new ADD(GPRegister.getR(2),GPRegister.getR(0)));
            erreur(compiler);          
            compiler.addInstruction(new LOAD(GPRegister.getR(0), GPRegister.getR(registre2)));
        }else{
            getLeftOperand().codeGenInst(compiler, registre1);
            getRightOperand().codeGenInst(compiler, registre2);
            compiler.addInstruction(new ADD(GPRegister.getR(registre1),GPRegister.getR(registre2)));
            erreur(compiler);
            GPRegister.freeRegistre(registre1);
        }
    }
}
