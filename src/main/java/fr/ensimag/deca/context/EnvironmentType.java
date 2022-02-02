package fr.ensimag.deca.context;

import java.util.HashMap;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's TypeDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentType has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl18
 * @date 01/01/2022
 */
//Classe créée de 0 par nous elle s'inspire de EnvironmentExp et correspond q l'environment des types

public class EnvironmentType {

    HashMap<Symbol,TypeDefinition> dictEnvironment;
    EnvironmentType parentEnvironment;
    
    public EnvironmentType(EnvironmentType parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.dictEnvironment = new HashMap<Symbol, TypeDefinition>();
    }

    public EnvironmentType() {
        this.parentEnvironment = null;
        this.dictEnvironment = new HashMap<Symbol, TypeDefinition>();
    }

    public HashMap<Symbol, TypeDefinition> getDictEnvironment() {
        return this.dictEnvironment;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        if (this.dictEnvironment.containsKey(key)){
            return this.dictEnvironment.get(key);
        }else{
            return null;
        }
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition set(Symbol key, TypeDefinition def) {
        return this.dictEnvironment.replace(key, def);
    }


    public boolean ContainsSymbol(Symbol key){
        return this.dictEnvironment.containsKey(key);
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {
        /*TODO : Programmation defensive et vérification du hash pour symbol */
        this.dictEnvironment.put(name, def);
    }

}
