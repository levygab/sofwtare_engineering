package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.deca.tools.IndentPrintStream;

import org.apache.log4j.Logger;

public class Params extends AbstractParams{

    private static final Logger LOG = Logger.getLogger(StringLiteral.class);

    public Params(AbstractIdentifier type, AbstractIdentifier ident) {
        super(type, ident);
    }

    public Type verifyParams(DecacCompiler compiler)
            throws ContextualError {
        //spécifique au cas d'une listinst qui est dans le main, bon pour le premier langage uniquement
        // uniquement le verifyInst des print est fait
        LOG.debug("On rentre dans verifyParams");
        Type typeParametre = getTypeParams().verifyType(compiler);
        if (typeParametre.isVoid()){
            //TODO : verifier syntaxe des erreurs
            throw new ContextualError("On ne peut pas avoir un void en parametre (2.9)", this.getLocation());
        }
        this.setType(typeParametre);
        LOG.debug("On sort de verifyParams");
        return typeParametre;
    }

    public void verifyParamsBody(DecacCompiler compiler, EnvironmentExp envExpParams)
            throws ContextualError {
        
        LOG.debug("On rentre dans verifyParamsBody");
        Type typeParametre = getType();
        ParamDefinition defParam = new ParamDefinition(typeParametre, getLocation());
        try{
            envExpParams.declare(getIdent().getName(), defParam);
            getIdent().setDefinition(defParam);
        }catch(DoubleDefException e){
            throw new ContextualError("On définit deux fois le parametre", getLocation());
        }
        LOG.debug("On sort de verifyParamsBody");
    }

    public void codeGenInstParam(DecacCompiler compiler, int compteur){
        RegisterOffset rOffset = new RegisterOffset(compteur, GPRegister.LB);
        getIdent().getExpDefinition().setOperand(rOffset);
    }
    
    public void decompile(IndentPrintStream s) {
        String typeVar = getTypeParams().getName().getName();
        String varNameStr = getIdent().getName().getName();
        s.print(typeVar + " " + varNameStr);
    }
}
