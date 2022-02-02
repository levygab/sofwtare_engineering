package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import static org.mockito.Mockito.never;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Identifier extends AbstractIdentifier {
    private static final Logger LOG = Logger.getLogger(Identifier.class);

    private Symbol name;
    private Definition definition;
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    public void codeGenInstClass(DecacCompiler compiler, int registre){
        compiler.addInstruction(new LEA(getExpDefinition().getOperand(), GPRegister.getR(registre)));
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("ON passe dans le verifyExpr de identifier, on récupèere le type de lvalue");
        LOG.debug("ON passe dans le verifyExpr de identifier, le nom de la variable est : "+this.name);
        if(!localEnv.ContainsSymbol(this.name)){
            throw new ContextualError("La variable "+this.name+" n'appartient pas à l'environment (0.1)", getLocation());
        }
        Definition defIdent = localEnv.get(this.name);
        setDefinition(defIdent);
        return defIdent.getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        // Definition definitionIdentifier =  compiler.getenvType().get(this.getName());
        LOG.debug("ON passe dans le verifyType de identifier, on récupèere le type de la valeur que l'on déclare");
        LOG.debug(name);
        if( !compiler.getenvType().ContainsSymbol(name)){
            throw new ContextualError("Le type n'appartient pas a l'environment (0.2)", getLocation());
        }
        // // this.setDefinition(definitionIdentifier);
        // // return definitionIdentifier.getType();
        Type tRecup = compiler.findType(name.getName());
        if(compiler.getenvType().ContainsSymbol(name)){
            setDefinition(compiler.getenvType().get(name));
        }else{
            setDefinition(new TypeDefinition(tRecup, getLocation()));
        }
        return tRecup;

    }
    
    @Override
    public void codeGenPrint(DecacCompiler compiler, boolean hex){
        //On Load en registre 1 la valeur du registre associé a la variable
        ExpDefinition identDef = this.getExpDefinition();
        DAddr identAdr = identDef.getOperand();
        compiler.addInstruction(new LOAD(identAdr, GPRegister.R1));
        if(identDef.getType().isInt()){
            compiler.addInstruction(new WINT());
        }else if(identDef.getType().isFloat()){
            if(hex){
                compiler.addInstruction(new WFLOATX());
            }else{
                compiler.addInstruction(new WFLOAT());
            }
        }
    }

    @Override
    public void codeGenInst(DecacCompiler compiler, int registre){
        //On Load en registre 1 la valeur du registre associé a la variable
        if(!getDefinition().isField()){
            ExpDefinition identDef = this.getExpDefinition();
            DAddr identAdr = identDef.getOperand();
            compiler.addInstruction(new LOAD(identAdr,GPRegister.getR(registre)));
        }else{
            RegisterOffset addr = new RegisterOffset(getFieldDefinition().getIndex(), GPRegister.getR(compiler.getRegistreUseForMeth()));
            compiler.addInstruction(new LOAD(addr,GPRegister.getR(registre)));
        }
        
    }


    @Override
    protected  void codeGenInst(DecacCompiler compiler, int registre, Label inst, Label fin){
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), GPRegister.getR(registre)));
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        compiler.addInstruction(new BEQ(inst));
    }

    @Override
    protected  void codeGenInstIF(DecacCompiler compiler, int registre, Label fin){
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), GPRegister.getR(registre)));
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        compiler.addInstruction(new BNE(fin));
    }

    @Override
    public void codeGenAssign(DecacCompiler compiler, int registre){
        if(!getDefinition().isField()){
            ExpDefinition identDef = this.getExpDefinition();
            DAddr identAdr = identDef.getOperand();
            compiler.addInstruction(new STORE(GPRegister.getR(registre),identAdr));
            GPRegister.freeRegistre(registre);
        }else{
            RegisterOffset addr = new RegisterOffset(getFieldDefinition().getIndex(), GPRegister.getR(compiler.getRegistreUseForMeth()));
            compiler.addInstruction(new STORE(GPRegister.getR(registre), addr));
        }
    }

    public void codeGenInstOr(DecacCompiler compiler, int registre, Label ins, Label fin, boolean left){
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), GPRegister.getR(registre)));
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        if (left){
            compiler.addInstruction(new BEQ(ins));
        }
        else{
            compiler.addInstruction(new BNE(fin));
        }
    } 
    
    


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

}
