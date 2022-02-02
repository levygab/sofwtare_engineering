package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentType.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import java.util.AbstractList;
import java.util.Stack;

import org.apache.log4j.Logger;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl18
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {

    private static final Logger LOG = Logger.getLogger(Main.class);
    

    final private AbstractIdentifier name;
    final private AbstractIdentifier classExtension;
    final private ListDeclMethod listMethod;
    final private ListDeclField listField;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier classExtension, ListDeclMethod listMethod, ListDeclField listField ) {
        Validate.notNull(name);
        Validate.notNull(classExtension);
        Validate.notNull(listMethod);
        Validate.notNull(listField);
        this.name = name;
        this.classExtension = classExtension;
        this.listField = listField;
        this.listMethod = listMethod; 
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        s.print(" extends ");
        classExtension.decompile(s);
        s.indent();
        s.println("{");
        s.println();
        listField.decompile(s);
        listMethod.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        TypeDefinition def = compiler.getenvType().get(classExtension.getName());
        if(def == null || !(def.isClass())){
            throw new ContextualError("La classe mère n'est pas définie 1.3", getLocation());
        }else if(compiler.getenvType().ContainsSymbol(name.getName())){
            throw new ContextualError("La classe "+name.getName()+" est deja définie ! ", getLocation());
        }else{
            try{
                ClassDefinition defClass = (ClassDefinition) def;
                //On déclare la définition de notre classe
                ClassType nouvClassType = new ClassType(name.getName(), getLocation(), (ClassDefinition) compiler.getenvType().get(classExtension.getName()));
                this.name.setDefinition(new ClassDefinition(nouvClassType, getLocation(), (ClassDefinition) compiler.getenvType().get(classExtension.getName())));
                compiler.getenvType().declare(name.getName(), name.getClassDefinition());
                LOG.debug("On rajoute le type : "+name.getName()+" de definition : "+name.getClassDefinition());
                compiler.createTableType(compiler.getenvType());
            }catch(DoubleDefException e){
                LOG.debug("Le type est deja definie, on ne peut pas la redefinir");
                throw new ContextualError("La variable "+name.getName()+" est deja définie (3.17)", getLocation());         
            }
        }
        classExtension.setDefinition(def);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {

        //On recupere notre environment exp
        EnvironmentExp localEnv = name.getClassDefinition().getMembers();

        //On modifie met a jour le nombre de methodes et de champs dans nos sous-classes
        TypeDefinition def = compiler.getenvType().get(classExtension.getName());
        ClassDefinition defClass = (ClassDefinition) def;
        this.name.getClassDefinition().setNumberOfFields(defClass.getNumberOfFields());
        this.name.getClassDefinition().setNumberOfMethods(defClass.getNumberOfMethods());
        LOG.debug("Nombre de Champs :" + this.name.getClassDefinition().getNumberOfFields());
        LOG.debug("Nombre de methodes :" + this.name.getClassDefinition().getNumberOfMethods());

        //On vérifie nos declField et nos declMethod
        listField.verifyListDeclField(compiler, classExtension, name);
        LOG.debug("Nombre de champs apres init:" + name.getClassDefinition().getNumberOfFields());
        listMethod.verifyListDeclMethod(compiler, classExtension, name);
        LOG.debug("Nombre de methodes apres init:" + name.getClassDefinition().getNumberOfMethods());
        
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        //On vérifie que la classe a bien été définie précédement
        if(!compiler.getenvType().ContainsSymbol(name.getName())){
            throw new ContextualError("La classe n'as pas été définie lors des passes précédentes (3.5)", getLocation());
        }

        //On lance les verify sur les declarations
        listField.verifyListDeclFieldBody(compiler, classExtension, name);
        listMethod.verifyListDeclMethodBody(compiler, classExtension, name);
    }

    @Override
    protected void codeGenClassPass1(DecacCompiler compiler){
       //listMethod.codeGenListMethod1(compiler, classExtension, name);
       Stack<Label> pileMethod = new Stack<Label>();
       name.getClassDefinition().addMethodList(compiler, pileMethod);
       ClassDefinition supDef = (ClassDefinition) compiler.getenvType().get(classExtension.getName());
       while (supDef != null){
           supDef.addMethodList(compiler,pileMethod);
           supDef = supDef.getSuperClass(); 
       }

       int offset = GPRegister.GB.incrOffset();
       RegisterOffset offset2 = new RegisterOffset(offset, Register.GB);
       compiler.addInstruction(new LEA(classExtension.getClassDefinition().getOperand(), GPRegister.R0));
       compiler.addInstruction(new STORE(GPRegister.R0, offset2));
       name.getClassDefinition().setOperand(offset2);
       while (!pileMethod.empty()){
           Label meth = pileMethod.pop();
           LabelOperand methOperand = new LabelOperand(meth);
           offset = GPRegister.GB.incrOffset();
           offset2 = new RegisterOffset(offset, Register.GB);
           
           compiler.addInstruction(new LOAD(methOperand, GPRegister.R0));
           compiler.addInstruction(new STORE(GPRegister.R0, offset2));
       } 
   }

    @Override
    protected void codeGenClassPass2(DecacCompiler compiler){
        listField.codeGenListField(compiler, classExtension, name);
        listMethod.codeGenListMethod2(compiler, classExtension, name);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        classExtension.prettyPrint(s, prefix, false);
        listField.prettyPrint(s, prefix, false);
        listMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        classExtension.iter(f);
        listField.iter(f);
        listMethod.iter(f);
    }

}
