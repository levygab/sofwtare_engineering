package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl18
 * @date 01/01/2022
 */
public class Body extends AbstractBody {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclMethod declMethod;
    private ListDeclField declFieldSet;
    public Body(ListDeclMethod declMethod,
    ListDeclField declFieldSet) {
        Validate.notNull(declMethod);
        Validate.notNull(declFieldSet);
        this.declMethod = declMethod;
        this.declFieldSet = declFieldSet;
    }
    
/*
    @Override
    protected void verifyBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).

        // Création d'un environnmentExp fils de l'environnement exp du programme
        // crée dans DecacCompiler et donc attribut du paramètre compiler
        EnvironmentExp envExpMain = new EnvironmentExp(compiler.getenvExp());
        this.declVariables.verifyListDeclVariable(compiler, envExpMain, null);
        this.insts.verifyListInst(compiler, envExpMain, null, compiler.findType("void"));
        LOG.debug("verify Main: end");
    }
    
    @Override
    protected void codeGenBody(DecacCompiler compiler) {
        compiler.addComment("Beginning of main declarations");
        // pas le droit au variables globales
        declVariables.codeGenListDeclVar(compiler);
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);
    }
    */
    @Override
    public void decompile(IndentPrintStream s) {/*
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    */}

    @Override
    protected void iterChildren(TreeFunction f) {
        declMethod.iter(f);
        declFieldSet.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix){
        declMethod.prettyPrint(s, prefix, false);
        declFieldSet.prettyPrint(s, prefix, true);
        }
}
