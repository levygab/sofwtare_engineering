package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;

import static org.mockito.ArgumentMatchers.isNotNull;

import java.beans.Expression;

import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Assign extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(StringLiteral.class);

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("ON passe dans le verifyExpr de Assign, on récupère le type de la valeur à assigner");
        LOG.debug("L'environment contient : "+localEnv.keySet());
        Type expectedType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr rightExpr = getRightOperand().verifyRValue(compiler, localEnv, currentClass, expectedType); 
        setRightOperand(rightExpr);
        if (expectedType.isTable() && getLeftOperand().getClass() == ElementTable.class) {
            LOG.debug("Tableau : On rentre dans la partie Assignation et Déclaration à la fois ");
            ElementTable elemTable = (ElementTable) getLeftOperand();
            try {
                elemTable.setDefinition(new VariableDefinition(expectedType, localEnv.get(elemTable.getIdentTable().getName()).getLocation()));
                localEnv.declare(elemTable.getName(), elemTable.getVariableDefinition());
            } catch(DoubleDefException e){
                LOG.debug("La variable est deja definie, on ne peut pas la redefinir");
                throw new ContextualError("La variable "+elemTable.getName()+" est deja définie (3.17)", getLocation());
            }
        }
        // if (expectedType.isTable()) {
        //     int size = ((Table)getLeftOperand()).getSize().((IntLiteral)getValue());
        // }
        this.setType(expectedType);
        return expectedType;
    }

    @Override
    public void codeGenInst(DecacCompiler compiler, int register){
        int registre = register;
        getRightOperand().codeGenInst(compiler, registre);
        getLeftOperand().codeGenAssign(compiler, registre);
        
        //compiler.addInstruction(new LOAD(GPRegister.getR(registre2),GPRegister.getR(registre2)));
        GPRegister.freeRegistre(registre);
    }



    @Override
    protected String getOperatorName() {
        return "=";
    }

}
