package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 * @author gl18
 * @date 01/01/2022
 */
public class UnaryMinus extends AbstractUnaryExpr {

    private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);
    
    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("On est dans verifyExpr de la classe UnaryMinus");

        Type typeOperand = getOperand().verifyExpr(compiler, localEnv, currentClass);
        boolean cond = typeOperand.isInt() || typeOperand.isFloat();
        if (!(cond)){
            throw new ContextualError("L'UnaryMinus nécessite un opérande de type int ou float (3.37)!", this.getLocation());
        } else {
            setType(typeOperand);
            return typeOperand;
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        int registre2 = GPRegister.findRegistre();
        getOperand().codeGenInst(compiler, registre2);
        if(registre == 0){
            if (this.getType().isInt()){
                compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(2)));
            }else{
                compiler.addInstruction(new LOAD(new ImmediateFloat(0), GPRegister.getR(2)));
            }
            compiler.addInstruction(new SUB(GPRegister.getR(registre2), GPRegister.getR(2)));
            compiler.addInstruction(new LOAD(GPRegister.getR(2), GPRegister.getR(registre)));
        }else{
            if (this.getType().isInt()){
                compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(registre)));
            }else{
                compiler.addInstruction(new LOAD(new ImmediateFloat(0), GPRegister.getR(registre)));
            }
            compiler.addInstruction(new SUB(GPRegister.getR(registre2), GPRegister.getR(registre)));
        }
        GPRegister.freeRegistre(registre2);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
