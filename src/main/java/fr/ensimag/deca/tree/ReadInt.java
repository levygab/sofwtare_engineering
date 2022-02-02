package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;

import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class ReadInt extends AbstractReadExpr {

    private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("On est dans verifyExpr de la classe ReadInt");
        Type tReadInt = compiler.findType("int");
        LOG.debug("Le type est : "+ tReadInt);
        setType(tReadInt);
        return tReadInt;
    }  


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    protected void codeGenInst(DecacCompiler compiler) {
        //Jamais appelé
    }
    protected void codeGenInst(DecacCompiler compiler, int register) {
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new BOV(new Label("io_error")));
        compiler.addInstruction(new LOAD((GPRegister.R1), GPRegister.getR(register))); 
    }  
}
