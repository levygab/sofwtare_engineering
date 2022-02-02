package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import static org.mockito.Mockito.never;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import org.apache.log4j.Logger;
/**
 * 
 * @author gl18
 * @date 01/01/2022
 */
public class ListInst extends TreeList<AbstractInst> {
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */    
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        //spécifique au cas d'une listinst qui est dans le main, bon pour le premier langage uniquement
        // uniquement le verifyInst des print est fait
        Iterator<AbstractInst> iteratorInst = this.iterator();
        while (iteratorInst.hasNext()){
            AbstractInst instruction = iteratorInst.next();
            instruction.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            int register = GPRegister.findRegistre();
            i.codeGenInst(compiler, register);
            GPRegister.freeRegistre(register);
        }
    }


    public void codeGenListInst(DecacCompiler compiler, Label fin) {
        for (AbstractInst i : getList()) {
            int register = GPRegister.findRegistre();
            i.codeGenInst(compiler, register, fin);
            GPRegister.freeRegistre(register);
        }
    }

    public void codeGenListInst(DecacCompiler compiler, Label elseLabel1, Label fin){
        for (AbstractInst i : getList()) {
            int register = GPRegister.findRegistre();
            i.codeGenInst(compiler, register, elseLabel1, fin);
            GPRegister.freeRegistre(register);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }
}
