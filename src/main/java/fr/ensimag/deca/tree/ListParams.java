package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListParams extends TreeList<AbstractParams>{


    public Signature verifyListParams(DecacCompiler compiler)
            throws ContextualError {
        
        Signature signMethod = new Signature();
        Iterator<AbstractParams> iteratorInst = this.iterator();
        while (iteratorInst.hasNext()){
            Params parametres = (Params) iteratorInst.next();
            Type typeParam = parametres.verifyParams(compiler);
            signMethod.add(typeParam);
        }
        return signMethod;
    }

    public EnvironmentExp verifyListParamsBody(DecacCompiler compiler)
            throws ContextualError {
        
        EnvironmentExp envParams = new EnvironmentExp();
        Iterator<AbstractParams> iteratorInst = this.iterator();
        while (iteratorInst.hasNext()){
            Params parametres = (Params) iteratorInst.next();
            parametres.verifyParamsBody(compiler, envParams);
        }
        return envParams;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        int nbreExpressions = getList().size();
        int compteur = 0;
        if(nbreExpressions == 1){
            for(AbstractParams a : getList()){
                a.decompile(s);
            }
        }else{
            for(AbstractParams a : getList()){
                a.decompile(s);
                compteur = compteur + 1;
                if(compteur==nbreExpressions){
                    continue;
                }
                s.print(", ");
            }
        } 
    }

    public void codeGenInstListParams(DecacCompiler compiler){
        Iterator<AbstractParams> iteratorInst = this.iterator();
        int compteur = -3;
        while (iteratorInst.hasNext()){
            Params parametres = (Params) iteratorInst.next();
            parametres.codeGenInstParam(compiler, compteur);
            compteur --; 
        }
    }
}
