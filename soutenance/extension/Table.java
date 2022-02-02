package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.nio.file.AccessDeniedException;
import java.util.Iterator;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.TableType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.RegisterOffsetDouble;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.log4j.Logger;


public class Table extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(StringLiteral.class);


    private AbstractExpr size;
    private ListExpr listExpr; // List Expr bien à utliser
    private AbstractIdentifier subTypeIdent;
    private Type subType;
    

    public Type getSubType(){
        return this.subType;
    }

    public AbstractExpr getSize() {
        return this.size;
    }

    public Table(ListExpr listExpr){
        this.listExpr = listExpr;
        this.subTypeIdent = null;
        //this.subType = listExpr.getList().get(0).getType();
        this.subType = null;
        this.size = new IntLiteral(listExpr.size());
    }

    public Table(AbstractIdentifier subTypeIdent, AbstractExpr abstractExpr){
        this.listExpr = null;
        this.size = abstractExpr;
        this.subTypeIdent = subTypeIdent;
        this.subType = null;
    }

    public ListExpr getListExpr(){
        return this.listExpr;
    }

    /**
     * @return Iterateur sur les elements du tableau
     */
    public Iterator<AbstractExpr> getListIterator() {
        Iterator<AbstractExpr> itElems = this.getListExpr().iterator();
        return itElems;
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("On rentre dans le verifyExpr de Table");
        // Verification du type de la taille du tableau
        Type tSize = this.size.verifyExpr(compiler, localEnv, currentClass);
        if (!(tSize.isInt())) {
            throw new ContextualError("Le type de la taille du tableau doit être int !", getLocation());
        }
        
        Type tTable;
        if (this.subTypeIdent != null) {
            this.subType = compiler.findType(subTypeIdent.getName().getName());
            String subTypeString = this.subType.toString();
            String typeTableString = subTypeString + "[]";
            tTable = compiler.findType(typeTableString);
            setType(tTable);
            TableType tCastTable = (TableType) tTable;
            tCastTable.setSubType(this.subType);
        }
        else { // if (this.getListExpr() != null) {
            LOG.debug("On rentre dans la verifcation des elements");
            Iterator<AbstractExpr> itElems = getListIterator();
            AbstractExpr firstElem = itElems.next();
            Type tFirstElem = firstElem.verifyExpr(compiler, localEnv, currentClass);
            this.subType = tFirstElem;
            String subTypeString = this.subType.toString();
            String typeTableString = subTypeString + "[]";
            tTable = compiler.findType(typeTableString);
            setType(tTable);
            TableType tCastTable = (TableType) tTable;
            tCastTable.setSubType(this.subType);
            while (itElems.hasNext()) {
                AbstractExpr elem = itElems.next();
                Type tExpr = elem.verifyExpr(compiler, localEnv, currentClass);
                verifyTableTypeValue(elem, tExpr);
            }
        }
        return tTable;
    }

    /**
     * Verification (contextuelle) que les valeurs d'un tableau sont du type declare
     * @param compiler
     * @param localEnv
     * @param currentClass
     * @throws ContextualError
     */
    public void verifyTableTypeValue(AbstractExpr elem, Type tElem) throws ContextualError {
        LOG.debug(this.listExpr.getList().get(0).getType());
        if (tElem != this.getSubType()) {
            throw new ContextualError("Le type des expressions du tableau doivent être identique !", getLocation());
        } 
    }

    protected void codeGenInst(DecacCompiler compiler, int registre) {
        int register = GPRegister.findRegistre();
        this.size.codeGenInst(compiler, register);
        compiler.addInstruction(new LOAD(1, GPRegister.getR(registre)));
        compiler.addInstruction(new ADD(GPRegister.getR(register), GPRegister.getR(registre)));
        compiler.addInstruction(new NEW(GPRegister.getR(registre), GPRegister.getR(registre)));
        if (this.listExpr != null){
            int compteur = 0;
            compiler.addInstruction(new STORE(GPRegister.getR(register), new RegisterOffset(compteur, GPRegister.getR(registre))));
            compteur +=1;
            for (AbstractExpr e : this.listExpr.getList()){
                e.codeGenInst(compiler, register);
                compiler.addInstruction(new STORE(GPRegister.getR(register), new RegisterOffset(compteur, GPRegister.getR(registre))));
                compteur+=1;
            }
        } else {
            compiler.addInstruction(new STORE(GPRegister.getR(register), new RegisterOffset(0, GPRegister.getR(registre))));
            int labelInit = compiler.getLabelCounter();
            compiler.increaseLabelCounter();
            int labelInstruction = compiler.getLabelCounter();
            compiler.increaseLabelCounter();
            int register3 = GPRegister.findRegistre();
            int register4 = GPRegister.findRegistre();
            compiler.addInstruction(new LOAD(1, GPRegister.getR(register4)));
            compiler.addInstruction(new BRA(new Label("while_cond_init_tableau"+labelInit)));
            compiler.addLabel(new Label("while_instruction_tableau" + labelInstruction));
            compiler.addInstruction(new LOAD(0, GPRegister.getR(register3)));
            compiler.addInstruction(new STORE(GPRegister.getR(register3), new RegisterOffsetDouble(0, GPRegister.getR(registre), GPRegister.getR(register4))));
            compiler.addInstruction(new ADD(new ImmediateInteger(1),GPRegister.getR(register4)));
            compiler.addLabel(new Label("while_cond_init_tableau" + labelInit));
            compiler.addInstruction(new CMP(new RegisterOffset(0, GPRegister.getR(registre)),GPRegister.getR(register4)));
            compiler.addInstruction(new BLE(new Label("while_instruction_tableau"+labelInstruction)));
            GPRegister.freeRegistre(register3);
            GPRegister.freeRegistre(register4);
        }
        GPRegister.freeRegistre(register);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (this.listExpr != null) {
            Iterator<AbstractExpr> itElems = getListIterator();
            s.print("{");
            AbstractExpr elem;
            if (itElems.hasNext()) {
                elem = itElems.next();
                elem.decompile(s);
            }
            while (itElems.hasNext()) {
                s.print(", ");
                elem = itElems.next();
                elem.decompile(s);
            }
            s.print("}");
        } else {
            s.print("new ");
            this.subTypeIdent.decompile(s);
            s.print("[");
            this.size.decompile(s);
            s.print("]");
        }    
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (this.listExpr != null){
            this.listExpr.prettyPrint(s, prefix,false);
        }
        this.size.prettyPrint(s, prefix, true);
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }
   
}
