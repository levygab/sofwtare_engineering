package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

    protected ArrayList<Label> getCompLabels(DecacCompiler compiler){
        Label trueLabel = new Label("True" + compiler.getLabelCounter());
        Label falseLabel = new Label("False" + compiler.getLabelCounter());
        Label finLabel = new Label("Fin" + compiler.getLabelCounter());
        compiler.increaseLabelCounter();        
        ArrayList<Label> liste = new ArrayList<Label>();
        liste.add(trueLabel);
        liste.add(falseLabel);
        liste.add(finLabel);
        return liste;
    }

    protected void setTagsComp(DecacCompiler compiler, ArrayList<Label> labelsComp, int registre){
        compiler.addLabel(labelsComp.get(0));
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.getR(registre)));
        compiler.addInstruction(new BRA(labelsComp.get(2)));
        compiler.addLabel(labelsComp.get(1));
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.getR(registre)));
        compiler.addInstruction(new BRA(labelsComp.get(2)));
        compiler.addLabel(labelsComp.get(2));
    }

    public void codeGenInstOPPushPop(DecacCompiler compiler){
        getRightOperand().codeGenInst(compiler,0);
        compiler.addInstruction(new PUSH(GPRegister.R0));
        compiler.increaseNeedStack();
        getLeftOperand().codeGenInst(compiler, 0);
        compiler.addInstruction(new POP(GPRegister.getR(2)));
        compiler.decreaseNeedStack();
}
}
