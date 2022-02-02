package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl18
 * @date 01/01/2022
 */
public class NoInitialization extends AbstractInitialization {
    private static final Logger LOG = Logger.getLogger(StringLiteral.class);

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        //Aucune vérification à faire, parceq'on donne pas de valeur à notre variable initialisée
    }


    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public boolean isInitialized(){
        return false;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, AbstractIdentifier var){
        //On fait rien, cette fontion ne devrait être jamais appelée
        LOG.debug("On passe dans codeGen de NoInitialization, il y a un probleme");
    }
}
