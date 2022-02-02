package fr.ensimag.ima.pseudocode;

/**
 * General Purpose Register operand (R0, R1, ... R15).
 * 
 * Modifié pour permettre de stocker l'emplacement de LB et GB
 * 
 * @author Ensimag
 * @date 01/01/2022
 */
public class GPRegister extends Register {
    /**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }

    private int number;

    GPRegister(String name, int number) {
        super(name);
        this.number = number;
    }

    private int nbre_mots = 1000;

    public int incrOffset(){
        //assert(nbre<nbre_mots) --> est-ce qu'il faudrait gérer la taille de la mémoire? je pense pas parceque c'est une commande ima
        //On augmente le offset et on retourne sa valeur
        this.number = this.number + 1;
        return this.number;
    }

    public int decrOffset(){
        //assert(nbre<nbre_mots) --> est-ce qu'il faudrait gérer la taille de la mémoire? je pense pas parceque c'est une commande ima
        //On augmente le offset et on retourne sa valeur
        this.number = this.number - 1;
        return this.number;
    }

    public void reInitOffset(){
        //assert(nbre<nbre_mots) --> est-ce qu'il faudrait gérer la taille de la mémoire? je pense pas parceque c'est une commande ima
        //On augmente le offset et on retourne sa valeur
        this.number = 0;
    }

}
