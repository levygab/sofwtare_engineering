package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import org.apache.log4j.Logger;
/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {

    private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("On est dans verifyExpr de la classe Not");
        Type typeOperand = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(typeOperand.isBoolean())){
            throw new ContextualError("Le Not ne peut être appliqué qu'à un boolean (3.37)", this.getLocation());
        } else {
            setType(typeOperand);
            return typeOperand;
        }       
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        int registre2 = GPRegister.findRegistre();
        getOperand().codeGenInst(compiler, registre2);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.getR(registre)));
        compiler.addInstruction(new SUB(GPRegister.getR(registre2), GPRegister.getR(registre)));
        GPRegister.freeRegistre(registre2);
    }


    @Override
    protected  void codeGenInstIF(DecacCompiler compiler, int registre, Label fin){
        codeGenInst(compiler, registre);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        compiler.addInstruction(new BNE(fin));
    }

    @Override
    protected  void codeGenInst(DecacCompiler compiler, int registre, Label inst){
        codeGenInst(compiler, registre);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        compiler.addInstruction(new BEQ(inst));
    }



    public void codeGenInstOr(DecacCompiler compiler, int registre, Label ins, Label fin, boolean left){
        codeGenInst(compiler, registre);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        if (left){
            compiler.addInstruction(new BEQ(ins));
        }
        else{
            compiler.addInstruction(new BNE(fin));
        }
    } 
}
