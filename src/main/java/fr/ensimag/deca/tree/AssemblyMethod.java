package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.io.PrintStream;
import org.apache.log4j.Logger;



public class AssemblyMethod extends AbstractMethod {
    
    private static final Logger LOG = Logger.getLogger(Main.class);

    private StringLiteral assembleur;
    private AbstractIdentifier ident;
    private AbstractIdentifier type;
    private ListParams list;

    public AssemblyMethod(String ass, AbstractIdentifier type, AbstractIdentifier ident, ListParams list){
        this.assembleur = new StringLiteral(ass);
        this.ident = ident;
        this.type = type;
        this.list = list;
    }

    protected void verifyMethod(DecacCompiler compiler, AbstractIdentifier superclass, AbstractIdentifier currentClass) 
                throws ContextualError{
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
        }
        try{
            //TODO : gerer les index
            this.ident.setDefinition(new MethodDefinition(typeRecupere, getLocation(), signRecupere, 0));
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
        
        Type typeMethode = type.verifyType(compiler);
        EnvironmentExp envExpParams = list.verifyListParamsBody(compiler);
        

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, int register) {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void decompile(IndentPrintStream s) {
        String typeVar = type.getName().getName();
        String varNameStr = ident.getName().getName();
        s.print(typeVar + " " + varNameStr+ "(");
        list.decompile(s);
        s.print(") asm (");
        s.indent();
        assembleur.decompileASM(s);
        s.unindent();
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, false);
        list.prettyPrint(s, prefix, false);
        assembleur.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codGenMethod2(DecacCompiler compiler, AbstractIdentifier superclass,
            AbstractIdentifier currentClass) {
        compiler.initNeedStack();
        String LabelNameDebut = "code."+ currentClass.getName().getName()+ "." + ident.getName();
        String LabelNameFin = "fin."+ currentClass.getName().getName()+ "." + ident.getName();
        Label methLabelDebut = new Label(LabelNameDebut);
        ident.getMethodDefinition().setLabel(methLabelDebut);
        ident.getMethodDefinition().setOperand(currentClass.getClassDefinition().getOperand());
        Label methLabelFin = new Label(LabelNameFin);
        compiler.addLabel(methLabelDebut);
        // TODO faire tsto en fonction des registres utilisé et des push à l'intérieur. 
        //compiler.addInstruction(new TSTO(2))
        //compiler.addInstruction(new BOV(new Label("pile pleine")));
        compiler.addInstruction(new PUSH(GPRegister.getR(3)));
        //compiler.addInstruction(new PUSH(RW));
        compiler.addComment("Beginning of method declarations");
        // pas le droit au variables globales
        compiler.addComment("Beginning of method instructions:");
        String asmString = assembleur.getValue().replace('"', ' ');
        InlinePortion asmBody = new InlinePortion(asmString);
        compiler.add(asmBody);
    }

    
}
