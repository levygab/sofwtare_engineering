package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 *
 * @author gl18
 * @date 01/01/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);


    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("On est dans verifyExpr de la classe AbstractOpCmp");
        Type tBool = compiler.findType("boolean");

        Type typeLeftOperand = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOperand = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        boolean condLeft = typeLeftOperand.isInt() || typeLeftOperand.isFloat();
        boolean condRight = typeRightOperand.isInt() || typeRightOperand.isFloat();
        
        // On verifie que les deux operandes sont float ou int
        if (!(condLeft && condRight)){
            throw new ContextualError("Les opérateurs de comparaison nécessitent deux opérandes de type int ou float ! (3.33)", this.getLocation());
        }else if(typeLeftOperand.isInt() && typeRightOperand.isInt()){
            setType(tBool);
            return tBool;
        } else {
            if (typeLeftOperand.isInt()) {
                setLeftOperand(new ConvFloat(getLeftOperand()));
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if(typeRightOperand.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }
            setType(tBool);
            return tBool;
        }       
    }

    public void codeGenInst(DecacCompiler compiler, int registre, Label inst){
        int registre1 = GPRegister.findRegistre();
        if (registre1==0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new CMP(GPRegister.getR(2),GPRegister.getR(0)));
            getInstruction1(compiler, inst);
        }
        else {
        getLeftOperand().codeGenInst(compiler, registre);
        getRightOperand().codeGenInst(compiler, registre1);
        compiler.addInstruction(new CMP(GPRegister.getR(registre1),GPRegister.getR(registre)));
        GPRegister.freeRegistre(registre1);
            getInstruction1(compiler, inst);
        }
    }

    public void codeGenInstIF(DecacCompiler compiler, int registre, Label fin){
        int registre1 = GPRegister.findRegistre();
        if (registre1==0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new CMP(GPRegister.getR(2),GPRegister.getR(0)));
            getInstruction2(compiler, fin);
        }
        else {
            getLeftOperand().codeGenInst(compiler, registre);
            getRightOperand().codeGenInst(compiler, registre1);
            compiler.addInstruction(new CMP(GPRegister.getR(registre1),GPRegister.getR(registre)));
            GPRegister.freeRegistre(registre1);
            getInstruction2(compiler, fin);
        }
    }

    public void codeGenInstOr(DecacCompiler compiler, int registre, Label inst, Label fin, boolean left){
        int registre1 = GPRegister.findRegistre();
        if (registre1==0){
            super.codeGenInstOPPushPop(compiler);
            if (left){
                compiler.addInstruction(new CMP(GPRegister.getR(registre1),GPRegister.getR(2)));
                getInstruction1(compiler, inst);
            }
            else{
                compiler.addInstruction(new CMP(GPRegister.getR(registre1),GPRegister.getR(2)));
                getInstruction2(compiler, fin);
            }
        }
        else {
            getLeftOperand().codeGenInst(compiler, registre);
            getRightOperand().codeGenInst(compiler, registre1);
            if (left){
                compiler.addInstruction(new CMP(GPRegister.getR(registre1),GPRegister.getR(registre)));
                getInstruction1(compiler, inst);
            }
            else{
                compiler.addInstruction(new CMP(GPRegister.getR(registre1),GPRegister.getR(registre)));
                getInstruction2(compiler, fin);
            }
            GPRegister.freeRegistre(registre1);
        }
    }

    public void codeGenInst(DecacCompiler compiler, int registre ){
        ArrayList<Label> labelsComp = getCompLabels(compiler);
        int registre1 = GPRegister.findRegistre();
        if (registre1==0){
            super.codeGenInstOPPushPop(compiler);
            compiler.addInstruction(new CMP(GPRegister.getR(2),GPRegister.getR(registre1)));
            getInstruction1(compiler, labelsComp.get(0));
            getInstruction2(compiler, labelsComp.get(1));
            setTagsComp(compiler, labelsComp, registre);
        }
        else {
            getLeftOperand().codeGenInst(compiler, registre);
            getRightOperand().codeGenInst(compiler, registre1);
            compiler.addInstruction(new CMP(GPRegister.getR(registre1),GPRegister.getR(registre)));
            GPRegister.freeRegistre(registre1);
            getInstruction1(compiler, labelsComp.get(0));
            getInstruction2(compiler, labelsComp.get(1));
            setTagsComp(compiler, labelsComp, registre);
        }
    }

    public abstract void getInstruction1(DecacCompiler compiler, Label inst);
    public abstract void getInstruction2(DecacCompiler compiler, Label fin);

}
