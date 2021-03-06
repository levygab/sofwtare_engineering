package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl18
 * @date 01/01/2022
 */
public class ClassDefinition extends TypeDefinition {


    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }


    protected RegisterOffset operand; 

    public RegisterOffset getOperand(){
        return operand; 
    }

    public void setOperand(RegisterOffset operand){
        this.operand = operand ; 
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }
    
    public void addMethodList(DecacCompiler compiler, Stack<Label> listMethod){
        EnvironmentExp env = getMembers();
        Set<Symbol> tri = env.dictEnvironment.keySet(); 
        for (Symbol i : tri){
            if (getMembers().get(i).isMethod()){
                String LabelName = "code."+ getType().toString()+ "." + i.getName();
                MethodDefinition methdef = (MethodDefinition) getMembers().get(i);
                Label methLabel = new Label(LabelName);
                methdef.setLabel(methLabel);
                methLabel.setNameForClass(i.getName());
                if (listMethod.search(methLabel)==-1){ // search se fait a partir de 'NameForClass', piur savoir si une methode du m??me nom existe dans les classes inf??rieur.
                    listMethod.push(methLabel);
                }
            }
        }
    }
}
