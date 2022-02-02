package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl18
 * @date 01/01/2022
 */
public class Initialization extends AbstractInitialization {

    private static final Logger LOG = Logger.getLogger(Main.class);


    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        AbstractExpr expressionRValue = this.expression.verifyRValue(compiler, localEnv, currentClass, t);
        LOG.debug("On finit dans Initialization avec expr : "+ expressionRValue.getType().getName().getName());
        setExpression(expressionRValue);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, AbstractIdentifier var){
        if(testType()){
            int register = GPRegister.findRegistre();
            expression.codeGenInst(compiler, 3);
            compiler.addInstruction(new STORE(GPRegister.getR(register), var.getExpDefinition().getOperand()));
            compiler.increaseNeedStack();
            compiler.addInstruction(new ADDSP(1));
            GPRegister.freeRegistre(register);
        }else if (expression.getType().isClass()){
            expression.codeGenInstClass(compiler, 3);
            compiler.addInstruction(new STORE(GPRegister.getR(3), var.getExpDefinition().getOperand()));
            compiler.addInstruction(new ADDSP(1));
        }else if(expression.getType().isTable()) {
            int register = GPRegister.findRegistre();
            expression.codeGenInst(compiler, 3);
            compiler.addInstruction(new STORE(GPRegister.getR(register), var.getExpDefinition().getOperand()));
            compiler.increaseNeedStack();
            compiler.addInstruction(new ADDSP(1));
            GPRegister.freeRegistre(register);
            
        }else{
            expression.codeGenInst(compiler, 3);
            compiler.addInstruction(new STORE(GPRegister.getR(3), var.getFieldDefinition().getOperand()));
            //TODO gerer les TSTO pour methode. 
            //compiler.increaseNeedStack();
        }
    }

    private boolean testType(){
        Type typeExpression = expression.getType();
        if(typeExpression == null){
            Identifier expr = (Identifier) this.expression;
            typeExpression = expr.getExpDefinition().getType();
        }
        if(typeExpression.isInt() || typeExpression.isFloat() || typeExpression.isBoolean()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.expression.decompile(s);
        s.println(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
