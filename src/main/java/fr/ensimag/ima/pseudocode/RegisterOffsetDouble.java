package fr.ensimag.ima.pseudocode;

/**
 * Operand representing a register indirection with offset, e.g. 42(R3).
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class RegisterOffsetDouble extends RegisterOffset {

    private final Register register2;

    public RegisterOffsetDouble(int offset, Register register, Register register2){
        super(offset, register);
        this.register2 = register2;
    }

    public Register getRegister2(){
        return this.register2;
    }

    @Override
    public String toString() {
        return this.getOffset() + "(" + this.getRegister() + ", " + this.getRegister2()  +  ")";
    }
}
