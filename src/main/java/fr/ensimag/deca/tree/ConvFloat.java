package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl18
 * @date 01/01/2022
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        Type tFloat = compiler.findType("float");
        setType(tFloat);
        return tFloat;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    public void codeGenInst(DecacCompiler compiler, int register){
        getOperand().codeGenInst(compiler, register);
        compiler.addInstruction(new FLOAT(GPRegister.getR(register), GPRegister.getR(register)));
    }

}
