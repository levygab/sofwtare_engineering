package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacFatalError;

/**
 * Register operand (including special registers like SP).
 * 
 * @author Ensimag
 * @date 01/01/2022
 */
public class Register extends DVal {
    private String name;
    public static int nbreRegistres = 16;
    
    protected Register(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static int getNbreRegistres(){
        return nbreRegistres;
    }

    /**
     * Global Base register
     */
    public static final GPRegister GB = new GPRegister("GB", 0);
    /**
     * Local Base register
     */
    public static final GPRegister LB = new GPRegister("LB", 0);
    /**
     * Stack Pointer
     */
    public static final Register SP = new Register("SP");
    /**
     * General Purpose Registers. Array is private because Java arrays cannot be
     * made immutable, use getR(i) to access it.
     */
    private static final GPRegister[] R = initRegisters();
    /**
     * General Purpose Registers
     */
    public static GPRegister getR(int i) {
        return R[i];
    }
    /**
     * Convenience shortcut for R[0]
     */
    public static final GPRegister R0 = R[0];
    /**
     * Convenience shortcut for R[1]
     */
    public static final GPRegister R1 = R[1];
    static private GPRegister[] initRegisters() {
        GPRegister [] res = new GPRegister[16];
        for (int i = 0; i <= 15; i++) {
            res[i] = new GPRegister("R" + i, i);
        }
        return res;
    }

    private static int[] RegisterUsed = new int[getNbreRegistres()]; 

    public static int findRegistre(){ //throws DecacFatalError{
        int compteur =3; 
        boolean trouve = false;
        while (!trouve){
            if(compteur>=getNbreRegistres()){
                //throw new DecacFatalError("Manque de registres pour compiler le programme");
                return 0;
            }
            if (RegisterUsed[compteur]==0){
                RegisterUsed[compteur]=1;
                trouve=true; 
            }
            else {compteur ++;} 
        }
        return compteur; 
    }

    public static void freeRegistre(int i){
        RegisterUsed[i]=0;
    }
    
}

