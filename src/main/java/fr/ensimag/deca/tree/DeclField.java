package fr.ensimag.deca.tree;

import java.io.PrintStream;
import org.apache.log4j.Logger;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;


public class DeclField extends AbstractDeclField {

    private static final Logger LOG = Logger.getLogger(Main.class);


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private Visibility visibility;
    final private AbstractInitialization initialization;

    public boolean hasInit(){
        return (initialization.isInitialized()); 
    }

    public DeclField(AbstractIdentifier type, AbstractIdentifier varName, Visibility visibility, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(visibility);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.visibility = visibility;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass)
            throws ContextualError {
       //On commence par vérifier que le type n'est pas void
       LOG.debug("On rentre dans verifyDelcField pour vérifier notre declaration de champ");
       Type typeRecupere = this.type.verifyType(compiler);
       if (typeRecupere.isVoid()){
           //TODO : verifier syntaxe des erreurs
           throw new ContextualError("On ne peut pas déclarer un void (2.5)", this.getLocation());
       } else if(superclass.getClassDefinition().getMembers().ContainsSymbol(varName.getName())){
            if(!(superclass.getClassDefinition().getMembers().get(varName.getName()).isField())){
                throw new ContextualError("Le champ n'est pas un champ dans la superclass (2.5)", this.getLocation());
            }
       }
       try{
            //TODO:Vérifier index pour constructeur de FielDef
            this.varName.setDefinition(new FieldDefinition(typeRecupere, getLocation(), this.visibility, currentClass.getClassDefinition(), currentClass.getClassDefinition().getNumberOfFields()));
            LOG.debug("On rajoute la variable : "+varName.getName()+" de definition : "+varName.getFieldDefinition());
            currentClass.getClassDefinition().getMembers().declare(this.varName.getName(), this.varName.getFieldDefinition());
            currentClass.getClassDefinition().incNumberOfFields();
            LOG.debug("On a bien déclaré le champ x de definition: "+ currentClass.getClassDefinition().getMembers().get(varName.getName()));
            
        }catch(DoubleDefException e){
            LOG.debug("Le champ est deja defini, on ne peut pas la redefinir");
            throw new ContextualError("La variable "+varName.getName()+" est deja définie", getLocation());
        }
    }


    protected void verifyDeclFieldBody(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass)
            throws ContextualError {
       //On commence par récupérer le type
       LOG.debug("On rentre dans verifyDelcFielBody pour vérifier notre declaration de variable");
       Type typeRecupere = this.type.verifyType(compiler);
       
       //Puis on s'occupe de vérifier l'initialization
       this.initialization.verifyInitialization(compiler, typeRecupere, currentClass.getClassDefinition().getMembers(), currentClass.getClassDefinition());

    }

    public void codeGenField1(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass){
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        varName.getFieldDefinition().setOperand(new RegisterOffset(varName.getFieldDefinition().getIndex()+1, Register.R1));
        compiler.addInstruction(new STORE(Register.R0, varName.getFieldDefinition().getOperand()));
    }

    public void codeGenInstField(DecacCompiler compiler){
        this.initialization.codeGenInst(compiler, this.varName);
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        String vis;
        if(this.visibility == Visibility.PROTECTED){
            vis = "protected ";
        }else{
            vis = "";
        }
        String typeVar = type.getName().getName();
        String varNameStr = this.varName.getName().getName();
        s.print(vis + typeVar + " " + varNameStr);
        if(initialization.isInitialized()){
            s.print(" = ");
            this.initialization.decompile(s);
        }else{
            s.println(";");
        }  
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
        
    }

    @Override
    protected String prettyPrintNode(){
        return "[visibility = "+this.visibility.toString()+"] "+super.prettyPrintNode();
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
    }
    
}
