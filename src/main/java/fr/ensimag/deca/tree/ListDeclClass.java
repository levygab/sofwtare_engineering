package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.log4j.Logger;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        // throw new UnsupportedOperationException("not yet implemented");
        for ( AbstractDeclClass i : getList()){
            i.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for ( AbstractDeclClass i : getList()){
            i.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        for ( AbstractDeclClass i : getList()){
            i.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody: end");
    }

    public void codeGenListClass(DecacCompiler compiler){
        int offset = GPRegister.GB.incrOffset();
        RegisterOffset offset2 = new RegisterOffset(offset, Register.GB);
        compiler.addInstruction(new LOAD(new NullOperand(), GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, offset2));
        Label equal = new Label("code.Object.equals");
        LabelOperand equalOperand = new LabelOperand(equal);
        offset = GPRegister.GB.incrOffset();
        offset2 = new RegisterOffset(offset, Register.GB);
        compiler.addInstruction(new LOAD(equalOperand, GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, offset2));
        
        for ( AbstractDeclClass i : getList()){
            i.codeGenClassPass1(compiler);
        }
        LOG.debug("Codegen listClassPass1: end");
        for ( AbstractDeclClass i : getList()){
            i.codeGenClassPass2(compiler);
        }
        LOG.debug("Codegen listClassPass2: end");
    }

    

}
