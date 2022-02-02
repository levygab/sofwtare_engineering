package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl18
 * @date 01/01/2022
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    public boolean equals(Object o){
        if (o instanceof Signature){
            Signature sign = (Signature) o;
            if (sign.size() == this.size()){
                for(int i = 0; i<sign.size(); i++){
                    if(!sign.paramNumber(i).sameType(this.paramNumber(i))){
                        return false;
                    }
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
