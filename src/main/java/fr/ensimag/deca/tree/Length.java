package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.TableType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import org.apache.log4j.Logger;


public class Length extends AbstractUnaryExpr {

    private static final Logger LOG = Logger.getLogger(Length.class);

    public Length(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("On est dans verifyExpr de la classe Length");
        Type typeOperand = getOperand().verifyExpr(compiler, localEnv, currentClass);        
        // On verifie que le operande est boolean
        if (!(typeOperand.isTable())){
            throw new ContextualError("Length ne peut être appliqué qu'à un tableau !", this.getLocation());
        } else {
            Type tInt = compiler.findType("int");
            setType(tInt);
            return tInt;
        }  
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int registre) {
        this.getOperand().codeGenInst(compiler, registre);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, GPRegister.getR(registre)), GPRegister.getR(registre)));
    }

    @Override
    protected String getOperatorName() {
        return "length";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.print("( ");
        s.print(getOperatorName());
        s.print("(");
        this.getOperand().decompile(s);
        s.print(")");
        s.print(" )");
    }
    
}
