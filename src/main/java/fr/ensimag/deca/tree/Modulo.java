package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.REM;

import static org.mockito.ArgumentMatchers.booleanThat;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //Type tInt = compiler.findType("int");

        Type typeLeftOperand = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOperand = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        boolean cond = typeLeftOperand.isInt() && typeRightOperand.isInt();
        if (!cond) {
            throw new ContextualError("Pour un modulo il faut deux int (3.33)", this.getLocation());
        } else {
            setType(typeLeftOperand);
            return typeLeftOperand;
        }
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    protected void codeGenInst(DecacCompiler compiler, int registre) {
        int registre1 = GPRegister.findRegistre();
        int registre2 = registre;
        if(registre1 == 0){
            super.codeGenInstOPPushPop(compiler);
            codeGenInstModulo(compiler, 2, 0);
            compiler.addInstruction(new LOAD(GPRegister.getR(0), GPRegister.getR(registre2)));
        }else{
            getRightOperand().codeGenInst(compiler, registre1);
            getLeftOperand().codeGenInst(compiler, registre2);
            codeGenInstModulo(compiler, registre1, registre2);
            GPRegister.freeRegistre(registre1);
        }
    }

    protected void codeGenInstModulo(DecacCompiler compiler, int registre1, int registre2){
        if(this.getType().isFloat()){
            compiler.addInstruction(new REM(GPRegister.getR(registre1),GPRegister.getR(registre2)));
            if (!(compiler.getCompilerOptions().getNoCheck())) {
                compiler.addInstruction(new BOV(new Label("io_error_div_O")));
            }
        }else{
            compiler.addInstruction(new REM(GPRegister.getR(registre1),GPRegister.getR(registre2)));
            if (!(compiler.getCompilerOptions().getNoCheck())) {
                compiler.addInstruction(new BOV(new Label("io_error_div_O")));
            }
        }
        GPRegister.freeRegistre(registre1);
        }
}
