package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import org.apache.log4j.Logger;


/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpBool.class);

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("On est dans verifyExpr de la classe AbstractOpBool");
        
        Type typeLeftOperand = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOperand = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        boolean cond = typeLeftOperand.isBoolean() && typeRightOperand.isBoolean();

        if (!cond) {
            throw new ContextualError("Pour un opérateur booleen les deux operandes doivent être des booleens (3.33)", this.getLocation());
        } else {
            // Type sBool = compiler.findType("boolean");
            setType(typeLeftOperand);
            return typeLeftOperand;
        }
        
    }

}
