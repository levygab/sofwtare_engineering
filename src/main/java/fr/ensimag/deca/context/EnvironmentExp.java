package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Main;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
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
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    private static final Logger LOG = Logger.getLogger(Main.class);

    HashMap<Symbol,ExpDefinition> dictEnvironment;
    EnvironmentExp parentEnvironment;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.dictEnvironment = new HashMap<Symbol, ExpDefinition>();
    }

    public EnvironmentExp() {
        this.parentEnvironment = null;
        this.dictEnvironment = new HashMap<Symbol, ExpDefinition>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        if(this.dictEnvironment.containsKey(key)){
            return this.dictEnvironment.get(key);
        }else{
            if(parentEnvironment == null){
                return this.dictEnvironment.get(key);
            }else{
                return parentEnvironment.get(key);
            }
        }
    }

    public boolean ContainsSymbol(Symbol key){
        if(this.dictEnvironment.containsKey(key)){
            return true;
        }else{
            if(parentEnvironment == null){
                return false;
            }else{
                LOG.debug(parentEnvironment.dictEnvironment.keySet());
                return parentEnvironment.ContainsSymbol(key);
            }
        }
    }

    public Set<Symbol> keySet(){
        return this.dictEnvironment.keySet();
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
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        /*TODO : Programmation defensive et vérification du hash pour symbol */
        if(this.dictEnvironment.containsKey(name)){
            throw new DoubleDefException();
        }else{
            this.dictEnvironment.put(name, def);
        }
    }


    public void putAllDisjoint(EnvironmentExp env){
        HashMap<Symbol,ExpDefinition> mapEnvDonne = env.dictEnvironment;
        Set<Symbol> ensembleCles = mapEnvDonne.keySet();
        for(Symbol a : ensembleCles){
            if(this.dictEnvironment.containsKey(a)){
                continue;
            }else{
                try{
                    this.declare(a, env.get(a));
                }catch(DoubleDefException e){
                    //Cette erreur ne devrait arriver jamais a cause la ligne 116
                }
                
            }
        }
    }
}
