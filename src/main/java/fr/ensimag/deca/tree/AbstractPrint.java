package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Print statement (print, println, ...).
 *
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractPrint extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(StringLiteral.class);

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("On rentre dans le verifyInts du Print:");
        Iterator<AbstractExpr> iteratorExpr = this.arguments.iterator();
        while (iteratorExpr.hasNext()){
            Type expressionType = iteratorExpr.next().verifyExpr(compiler, localEnv, currentClass);
            if(!expressionType.isInt() && !expressionType.isFloat() && !expressionType.isString()){
                throw new ContextualError("Ce type ne peut pas être print, il doit être int, float ou string (3.31)", getLocation());
            }
        }
        LOG.debug("On sors du verifyInts du Print:");
    }

    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, getPrintHex());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int register) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, getPrintHex());
        }
    }

    public void codeGenInstmeth(DecacCompiler compiler, int register){
        
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        String commandePrint = "print"+getSuffix();
        if(getPrintHex()){
            commandePrint = commandePrint+"x";
        }
        commandePrint = commandePrint + "(";
        s.print(commandePrint);
        int nbreExpressions = getArguments().getList().size();
        int compteur = 0;
        if(getArguments().getList().size() == 1){
            for(AbstractExpr a : getArguments().getList()){
                a.decompile(s);
            }
        }else{
            for(AbstractExpr a : getArguments().getList()){
                a.decompile(s);
                compteur = compteur + 1;
                if(compteur==nbreExpressions){
                    continue;
                }
                s.print(", ");
            }
        }
        s.println(");");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
