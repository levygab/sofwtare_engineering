package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;
import java.util.Random;

import org.apache.commons.lang.Validate;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler, int register) {
        // Random rand = new Random();
        // int nombreAleatoire = rand.nextInt(1000);

        Label cond = new Label("while_condition" + compiler.getLabelCounter());
        Label inst = new Label("while_inst" + compiler.getLabelCounter());
        Label fin = new Label("while_fin" + compiler.getLabelCounter());
        compiler.increaseLabelCounter();
        compiler.addInstruction(new BRA(cond));
        compiler.addLabel(inst);
        body.codeGenListInst(compiler);
        compiler.addLabel(cond);
        condition.codeGenInst(compiler, register, inst);
        compiler.addLabel(fin);
        }



    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        for (AbstractInst i : this.body.getList()){
            i.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
