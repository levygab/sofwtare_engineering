package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Variable declaration
 *
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractDeclField extends Tree {

    //TODO: changer les définitions de la classe
    
    /**
     * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */    
    protected abstract void verifyDeclField(DecacCompiler compiler,
            AbstractIdentifier superClass, AbstractIdentifier currentClass)
            throws ContextualError;
    
    protected abstract void verifyDeclFieldBody(DecacCompiler compiler,
            AbstractIdentifier superClass, AbstractIdentifier currentClass)
            throws ContextualError;

    public abstract boolean hasInit();

    protected abstract void codeGenDeclVar(DecacCompiler compiler);

    public abstract void codeGenField1(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass);

    public abstract void codeGenInstField(DecacCompiler compiler);
    
}
