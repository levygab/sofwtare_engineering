package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.deca.context.Signature;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class Method extends AbstractMethod{
    private static final Logger LOG = Logger.getLogger(Main.class);


    private AbstractIdentifier ident;
    private AbstractIdentifier type;
    private ListDeclVar declVariables;
    private ListInst insts;
    private ListParams list;
    public Method(AbstractIdentifier type, AbstractIdentifier ident, ListDeclVar declVariables,
            ListInst insts, ListParams list) {
        Validate.notNull(ident);
        Validate.notNull(type);
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.type = type;
        this.ident = ident;
        this.declVariables = declVariables;
        this.insts = insts;
        this.list = list;
    }
    
    
    public AbstractIdentifier getIdent() {
        return ident;
    }


    protected void verifyMethod(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass) 
                throws ContextualError{
        //On commence par vérifier que le type n'est pas void
        LOG.debug("On rentre dans verifyMethod pour vérifier notre declaration de methode");
        Type typeRecupere = this.type.verifyType(compiler);
        Signature signRecupere = this.list.verifyListParams(compiler);
        LOG.debug(signRecupere.size());
        if(superclass.getClassDefinition().getMembers().ContainsSymbol(ident.getName())){
            if(!(superclass.getClassDefinition().getMembers().get(ident.getName()).isMethod())){
                throw new ContextualError("La methode n'est pas une methode dans la superclass (2.7)", this.getLocation());
            }
            MethodDefinition methDef = (MethodDefinition) superclass.getClassDefinition().getMembers().get(ident.getName());
            LOG.debug(methDef.getSignature().size());
            if(!signRecupere.equals(methDef.getSignature())){
                throw new ContextualError("La methode n'as pas la même signature dans la classe mère (2.7)", this.getLocation());
            }
            if(typeRecupere.isClass()){
                ClassType typeRecupClass = (ClassType) typeRecupere;
                if(!typeRecupClass.isSubClassOf((ClassType)superclass.getClassDefinition().getMembers().get(ident.getName()).getType())){
                    throw new ContextualError("Le type de retour n'est pas le même que dans la superClass (ou un sous-type)(2.7)", this.getLocation());
                }
            }
            if(!typeRecupere.sameType(superclass.getClassDefinition().getMembers().get(ident.getName()).getType())){
                throw new ContextualError("Le type de retour n'est pas le même que dans la superClass (ou un sous-type)(2.7)", this.getLocation());
            }
        }
       try{
           //TODO : gerer les index
            this.ident.setDefinition(new MethodDefinition(typeRecupere, getLocation(), signRecupere, currentClass.getClassDefinition().getNumberOfMethods()));
            LOG.debug("On rajoute la methode : "+ident.getName()+" de definition : "+ident.getMethodDefinition());
            currentClass.getClassDefinition().getMembers().declare(this.ident.getName(), this.ident.getMethodDefinition()); 
            currentClass.getClassDefinition().incNumberOfMethods();
            LOG.debug("On a bien déclaré la methode de definition: "+ currentClass.getClassDefinition().getMembers().get(ident.getName()));

        }catch(DoubleDefException e){
            LOG.debug("La methode est deja definie, on ne peut pas la redefinir");
            throw new ContextualError("La methode "+ident.getName()+" est deja définie", getLocation());
        }
    }

    public void verifyMethodBody(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass) 
            throws ContextualError{
        LOG.debug("On rentre dans verifyMethodBody pour déclarer le corps de la méthode");
        Type typeMethode = type.verifyType(compiler);
        EnvironmentExp envExpParams = list.verifyListParamsBody(compiler);
        envExpParams.putAllDisjoint(currentClass.getClassDefinition().getMembers());
        LOG.debug("On a verifie les parametres");
        LOG.debug(envExpParams.keySet());
        declVariables.verifyListDeclVariable(compiler, envExpParams, currentClass.getClassDefinition());
        LOG.debug("On a verifie les declarations de variable");
        insts.verifyListInst(compiler, envExpParams, currentClass.getClassDefinition(), typeMethode);
        LOG.debug("On a verifie les instructions");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int register) {
    }

    protected void codGenMethod2(DecacCompiler compiler,  AbstractIdentifier superclass, AbstractIdentifier currentClass){
        //IMAProgram prog = new IMAProgram();
        compiler.initNeedStack();
        String LabelNameDebut = "code."+ currentClass.getName().getName()+ "." + ident.getName();
        String LabelNameFin = "fin."+ currentClass.getName().getName()+ "." + ident.getName();
        Label methLabelDebut = new Label(LabelNameDebut);
        ident.getMethodDefinition().setLabel(methLabelDebut);
        ident.getMethodDefinition().setOperand(currentClass.getClassDefinition().getOperand());
        Label methLabelFin = new Label(LabelNameFin);
        compiler.setReturnLabel(methLabelFin);
        compiler.addLabel(methLabelDebut);
        // TODO faire tsto en fonction des registres utilisé et des push à l'intérieur. 
        //compiler.addInstruction(new TSTO(2))
        //compiler.addInstruction(new BOV(new Label("pile pleine")));
        compiler.addInstruction(new PUSH(GPRegister.getR(3)));
        //compiler.addInstruction(new PUSH(RW));
        list.codeGenInstListParams(compiler);
        compiler.addComment("Beginning of method declarations");
        // pas le droit au variables globales
        declVariables.codeGenListDeclVar(compiler);
        compiler.addComment("Beginning of method instructions:");
        int registre = GPRegister.findRegistre();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.getR(registre)));
        compiler.setRegistreUseForMeth(registre);
        insts.codeGenListInst(compiler);
        if (!this.type.getDefinition().getType().isVoid()){
            compiler.addInstruction(new WSTR("Erreur : sortie de la methode"+currentClass.getName().getName()+ "." + ident.getName()+"sans return"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
            
        /*while (!compiler.getStackRegister().empty()){
            GPRegister r = compiler.getStackRegister().pop();
            compiler.addInstruction(new POP(r));
            compiler.add(instruction);
        }*/
        compiler.addLabel(methLabelFin);
        compiler.addInstruction(new POP(GPRegister.getR(3)));


        compiler.addInstruction(new RTS());
        GPRegister.freeRegistre(registre);
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        String typeVar = type.getName().getName();
        String varNameStr = ident.getName().getName();
        s.print(typeVar + " " + varNameStr+ "(");
        list.decompile(s);
        s.println(") {");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.print("}");
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        ident.iter(f);
        list.iter(f);
        declVariables.iter(f);
        insts.iter(f);  
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, false);
        list.prettyPrint(s, prefix, false);
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
