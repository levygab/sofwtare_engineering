package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;

import org.apache.log4j.Logger;
import java.util.HashSet;
import java.util.Set;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("On est dans verifyExpr de la classe AbstractOpArith");
        
        Type typeLeftOperand = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOperand = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        boolean condLeft = typeLeftOperand.isInt() || typeLeftOperand.isFloat();
        boolean condRight = typeRightOperand.isInt() || typeRightOperand.isFloat();
        
        // On verifie que les deux operandes sont float ou int
        if (!(condLeft && condRight)){
            throw new ContextualError("Les opérateurs arithmétiques nécessitent deux opérandes de type int ou float ! (3.33)", this.getLocation());
        } else if (typeLeftOperand.isInt() && typeRightOperand.isInt()){
            setType(typeLeftOperand);
            return typeLeftOperand;
        }else {
            Type tReturn = typeLeftOperand;
            if (typeLeftOperand.isInt()) {
                setLeftOperand(new ConvFloat(getLeftOperand()));
                tReturn = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if(typeRightOperand.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
                tReturn = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }
            setType(tReturn);
            return tReturn;
        }
    }

    public void erreur(DecacCompiler compiler){
        if (!(compiler.getCompilerOptions().getNoCheck())) {
            if(this.getType().isFloat()){
                compiler.addInstruction(new BOV(new Label("io_debordement_valeur")));
            }
        }
    }
}
