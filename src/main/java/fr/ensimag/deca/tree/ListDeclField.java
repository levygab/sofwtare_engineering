package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import org.apache.log4j.Logger;
/**
 * 
 * @author gl18
 * @date 01/01/2022
 */
public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * Implements non-terminal "list_Decl_field" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains "env_types" attribute
     * 
     */    
    public void verifyListDeclField(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass)
            throws ContextualError {
        //spécifique au cas d'une listinst qui est dans le main, bon pour le premier langage uniquement
        // uniquement le verifyInst des print est fait
        
        Iterator<AbstractDeclField> iteratorInst = this.iterator();
        while (iteratorInst.hasNext()){
            iteratorInst.next().verifyDeclField(compiler, superclass, currentClass);
        }
    }
    /**
     * Implements non-terminal "list_Decl_field" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains "env_types" attribute
     * 
     */    
    public void verifyListDeclFieldBody(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass)
            throws ContextualError {
        //spécifique au cas d'une listinst qui est dans le main, bon pour le premier langage uniquement
        // uniquement le verifyInst des print est fait
        Iterator<AbstractDeclField> iteratorInst = this.iterator();
        while (iteratorInst.hasNext()){
            iteratorInst.next().verifyDeclFieldBody(compiler, superclass, currentClass);
        }
    }

    public void codeGenListField(DecacCompiler compiler, AbstractIdentifier classExtension, AbstractIdentifier name){
        //TODO : faire TSTO ; 
        Label ClassInit = new Label("init."+name.getClassDefinition().getType().getName().getName());
        compiler.addLabel(ClassInit);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1 ));
        Iterator<AbstractDeclField> iteratorInst = this.iterator();
        while (iteratorInst.hasNext()){
            iteratorInst.next().codeGenField1(compiler, classExtension, name);
        }
        compiler.addInstruction(new PUSH(Register.R1));
        Label supClassInit = new Label("init."+classExtension.getClassDefinition().getType().getName().getName());
        compiler.addInstruction(new BSR(supClassInit));
        compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1 ));
        for (AbstractDeclField f : this.getList()){
            if (f.hasInit()){
                f.codeGenInstField(compiler);
            }
        }
        compiler.addInstruction(new RTS());
    }

/*
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
    */
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
    }
}

