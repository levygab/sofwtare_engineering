package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author gl18
 * @date 01/01/2022
 */
public class NoOperation extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(StringLiteral.class);

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
                //TODO : faire un LOG
        LOG.debug("On rentre dans le verify de la classe NoOperation");
        //Rien à faire; il n'y a pas d'operation donc pas de texte à faire
    }

    protected void codeGenInst(DecacCompiler compiler, int registre) {
        // rien à faire
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
