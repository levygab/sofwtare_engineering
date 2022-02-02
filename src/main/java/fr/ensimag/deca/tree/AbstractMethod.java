package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Instruction
 *
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractMethod extends Tree {
    
    /**
     * Implements non-terminal "inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to the "return" attribute (void in the main bloc).
     */    
    
    protected abstract void verifyMethod(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass) throws ContextualError;

    protected abstract void verifyMethodBody(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass) throws ContextualError;

    protected abstract void codGenMethod2(DecacCompiler compiler,  AbstractIdentifier superclass, AbstractIdentifier currentClass);
    
    /**
     * Generate assembly code for the instruction.
     * 
     * @param compiler
     */
    protected abstract void codeGenInst(DecacCompiler compiler, int register);
    protected void codeGenInst(DecacCompiler compiler, int register, Label fin){}
    protected void codeGenInst(DecacCompiler compiler, int register, Label elseLabel, Label fin){}


    /**
     * Decompile the tree, considering it as an instruction.
     *
     * In most case, this simply calls decompile(), but it may add a semicolon if needed
     */
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
    }

    public boolean isIF(){
        return false;
    }
}
