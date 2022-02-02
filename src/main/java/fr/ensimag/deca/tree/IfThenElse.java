package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl18
 * @date 01/01/2022
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    @Override
    public boolean isIF(){
        return true; 
    }

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        for (AbstractInst i : thenBranch.getList()){
            i.verifyInst(compiler, localEnv, currentClass, returnType);
        }
        for (AbstractInst i : elseBranch.getList()){
            i.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        this.condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.print("}");
        s.println("else {");
        if (!elseBranch.isEmpty()){
            s.indent();
            elseBranch.decompile(s);
            s.unindent();
        }
        s.print("}");
        s.println();
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }


    protected void codeGenInst(DecacCompiler compiler, int register) {
        Label fin = new Label("if_fin" + compiler.getLabelCounter());
        Label elseLabel = new Label("else" + compiler.getLabelCounter());
        compiler.increaseLabelCounter();
        Iterator<AbstractInst> iteratorInst = elseBranch.iterator();
        if ( iteratorInst.hasNext() && iteratorInst.next().isIF()){
            condition.codeGenInstIF(compiler, register, elseLabel);
            thenBranch.codeGenListInst(compiler);
            elseBranch.codeGenListInst(compiler, elseLabel, fin);
        }
        else{
            condition.codeGenInstIF(compiler, register, elseLabel);
            thenBranch.codeGenListInst(compiler);
            compiler.addInstruction(new BRA(fin));
            compiler.addLabel(elseLabel);
            elseBranch.codeGenListInst(compiler);
        }
        compiler.addLabel(fin);
        GPRegister.freeRegistre(register);
    }

    protected void codeGenInst(DecacCompiler compiler, int register, Label elseLabel, Label fin) {
        compiler.addInstruction(new BRA(fin));
        compiler.addLabel(elseLabel);
        Label elseLabel1 = new Label("else" + compiler.getLabelCounter());
        compiler.increaseLabelCounter();
        boolean elseExiste=false;
        Iterator<AbstractInst> iteratorInst = elseBranch.iterator();
        while(iteratorInst.hasNext()){
            elseExiste=true;
            if (iteratorInst.next().isIF()){
                condition.codeGenInstIF(compiler, register, elseLabel1);
                thenBranch.codeGenListInst(compiler);
                elseBranch.codeGenListInst(compiler, elseLabel1, fin);
            }
            else{
                condition.codeGenInstIF(compiler, register, elseLabel1);
                thenBranch.codeGenListInst(compiler);
                compiler.addInstruction(new BRA(fin));
                compiler.addLabel(elseLabel1);
                elseBranch.codeGenListInst(compiler);
            }
        }
        if (!(elseExiste)){
            condition.codeGenInstIF(compiler, register, fin);
            thenBranch.codeGenListInst(compiler);
        }
        
    }
}
