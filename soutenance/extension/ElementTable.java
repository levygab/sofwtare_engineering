package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.TableType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.RegisterOffsetDouble;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import org.apache.log4j.Logger;


public class ElementTable extends Identifier {

    public ElementTable(AbstractIdentifier identTable, Symbol name, AbstractExpr index) {
        super(name);
        this.identTable = identTable;
        this.index = index;
    }

    private AbstractIdentifier identTable;
    private AbstractExpr index;
    
    private static final Logger LOG = Logger.getLogger(ElementTable.class);

    public AbstractIdentifier getIdentTable() {
        return this.identTable;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("ON passe dans le verifyExpr de ElementTable, on récupèere le type de lvalue");
        LOG.debug("ON passe dans le verifyExpr de ElementTable, le nom de la variable est : "+this.getName());
        Type tIndex = this.index.verifyExpr(compiler, localEnv, currentClass);
        if (!tIndex.isInt()) {
            throw new ContextualError("L'indice passé pour accéder à un élément d'un tableau doit être un entier", getLocation());
        }
        // } else if(!localEnv.ContainsSymbol(this.identTable.getName()) && this.identTable.getClass() == Identifier.class){
        //     throw new ContextualError("Le tableau "+this.identTable.getName().getName()+" n'appartient pas à l'environment (0.1)", getLocation());
        // }
        // Definition defIdentTable = localEnv.get(this.identTable.getName());
        // TableType tTable = (TableType) defIdentTable.getType();
        TableType tTable = (TableType) this.identTable.verifyExpr(compiler, localEnv, currentClass);
        Type subType = tTable.getSubType();
        if (subType == null) {
            throw new ContextualError("Le tableau n'a pas été initialisé", getLocation());
        }
        // Type tIdentTable = this.identTable.verifyExpr(compiler, localEnv, currentClass);
        // if (tIdentTable != subType) {
        //     throw new ContextualError("Le type du sous-tableau n'est pas bon!", getLocation());
        // }
        setType(subType);
        setDefinition(new VariableDefinition(getType(), getLocation()));
        return subType;
    }

    @Override
    String prettyPrintNode() {
        return "ElementTable (" + getName() + ")";
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        identTable.prettyPrint(s,prefix,false);
        index.prettyPrint(s, prefix, true);
    }

    void verifTaille(DecacCompiler compiler, int registre, int register){
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new CMP(new ImmediateInteger(0),GPRegister.getR(register)));
            compiler.addInstruction(new BLT(new Label("io_error_index_out_of_range")));
            compiler.addInstruction(new CMP(new RegisterOffset(0, GPRegister.getR(registre)), GPRegister.getR(register)));
            compiler.addInstruction(new BGE(new Label("io_error_index_out_of_range")));
        }
    }

    @Override
    public void codeGenInst(DecacCompiler compiler, int registre){
        //On Load en registre 1 la valeur du registre associé a la variable
        int register = GPRegister.findRegistre();
        this.identTable.codeGenInst(compiler, registre);
        this.index.codeGenInst(compiler, register);
        verifTaille(compiler, registre,register);
        compiler.addInstruction(new ADD(new ImmediateInteger(1),GPRegister.getR(register)));    
        compiler.addInstruction(new LOAD(new RegisterOffsetDouble(0, GPRegister.getR(registre), GPRegister.getR(register)),GPRegister.getR(registre)));
        GPRegister.freeRegistre(register);
    }

@Override
    public void codeGenPrint(DecacCompiler compiler, boolean hex){
        //On Load en registre 1 la valeur du registre associé a la variable
        int register = GPRegister.findRegistre();
        int register3 = GPRegister.findRegistre();
        this.identTable.codeGenInst(compiler, register3);
        this.index.codeGenInst(compiler, register);
        verifTaille(compiler, register3,register);
        compiler.addInstruction(new ADD(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new LOAD(new RegisterOffsetDouble(0, GPRegister.getR(register3), GPRegister.getR(register)),GPRegister.R1));
        if(this.getType().isInt()){
            compiler.addInstruction(new WINT());
        }else if(this.getType().isFloat()){
            if(hex){
                compiler.addInstruction(new WFLOATX());
            }else{
                compiler.addInstruction(new WFLOAT());
            }
        }
        GPRegister.freeRegistre(register);
        GPRegister.freeRegistre(register3);
    }

    @Override
    public void codeGenAssign(DecacCompiler compiler, int registre){
        int register = GPRegister.findRegistre();
        int register3 = GPRegister.findRegistre();
        this.identTable.codeGenInst(compiler, register3);
        this.index.codeGenInst(compiler, register);
        verifTaille(compiler, register3,register);
        compiler.addInstruction(new ADD(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new STORE(GPRegister.getR(registre), new RegisterOffsetDouble(0, GPRegister.getR(register3), GPRegister.getR(register))));
        GPRegister.freeRegistre(register);
        GPRegister.freeRegistre(register3);
    }

    @Override
    protected  void codeGenInst(DecacCompiler compiler, int registre, Label inst, Label fin){
        int register = GPRegister.findRegistre();
        this.identTable.codeGenInst(compiler, registre);
        this.index.codeGenInst(compiler, register);
        verifTaille(compiler, registre,register);
        compiler.addInstruction(new ADD(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new LOAD(new RegisterOffsetDouble(0, GPRegister.getR(registre), GPRegister.getR(register)),GPRegister.getR(registre)));
        GPRegister.freeRegistre(register); 
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        compiler.addInstruction(new BEQ(inst));
    }

    @Override
    protected  void codeGenInstIF(DecacCompiler compiler, int registre, Label fin){
        int register = GPRegister.findRegistre();
        this.identTable.codeGenInst(compiler, registre);
        this.index.codeGenInst(compiler, register);
        verifTaille(compiler, registre,register);
        compiler.addInstruction(new ADD(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new LOAD(new RegisterOffsetDouble(0, GPRegister.getR(registre), GPRegister.getR(register)),GPRegister.getR(registre)));
        GPRegister.freeRegistre(register);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        compiler.addInstruction(new BNE(fin));
    }

    public void codeGenInstOr(DecacCompiler compiler, int registre, Label ins, Label fin, boolean left){
        int register = GPRegister.findRegistre();
        this.identTable.codeGenInst(compiler, registre);
        this.index.codeGenInst(compiler, register);
        verifTaille(compiler, registre,register);
        compiler.addInstruction(new ADD(new ImmediateInteger(1),GPRegister.getR(register)));
        compiler.addInstruction(new LOAD(new RegisterOffsetDouble(0, GPRegister.getR(registre), GPRegister.getR(register)),GPRegister.getR(registre)));
        GPRegister.freeRegistre(register);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new CMP(GPRegister.R0,GPRegister.getR(registre)));
        if (left){
            compiler.addInstruction(new BEQ(ins));
        }
        else{
            compiler.addInstruction(new BNE(fin));
        }
    } 
}
