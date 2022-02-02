package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.Main;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
/**
 * Type defined by a class.
 *
 * @author gl18
 * @date 01/01/2022
 */
public class ClassType extends Type {

    private static final Logger LOG = Logger.getLogger(Main.class);
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isClass();
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        //throw new UnsupportedOperationException("not yet implemented"); 
        ClassDefinition classeMereDef = this.getDefinition();
        ClassType classeMereType = classeMereDef.getType();
        Symbol classeMere = classeMereType.getName();
        while(!(classeMere.getName() == potentialSuperClass.getName().getName())){
            LOG.debug("On tourne");
            classeMereDef = classeMereDef.getSuperClass();
            classeMereType = classeMereDef.getType();
            classeMere = classeMereType.getName();
            LOG.debug(classeMere.getName());
            if(classeMere.getName() == "Object"){
                return false;
            }
        }
        LOG.debug("On tourne plus");
        return true;
    }


}
