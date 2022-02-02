package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.TableType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import net.bytebuddy.asm.Advice.Local;

import static org.mockito.Mockito.never;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl18
 * @date 01/01/2022
 */
public class DeclVar extends AbstractDeclVar {

    private static final Logger LOG = Logger.getLogger(Main.class);
    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        //On commence par vérifier que le type n'est pas void
        LOG.debug("On rentre dans verifyDelcVar pour vérifier notre declaration de variable");
        Type typeRecupere = this.type.verifyType(compiler);
        
        if (typeRecupere.isVoid()){
            //TODO : verifier syntaxe des erreurs
            throw new ContextualError("On ne peut pas déclarer un void (3.17)", this.getLocation());
        }
        else if (typeRecupere.isString()) {
            // A VERIFIER
            throw new ContextualError("On ne peut pas déclarer un string (3.1)", this.getLocation());

        } else {
            //Pour identifier on n'as rien à vérifier, c'est fait avant
            // On initialise notre variable
            // Pas sur pour les getDefinition, il faut vérifier que ça crée la bonne variable (c' est la location qui me fait douter)
            try{
                if (typeRecupere.isTable() && this.varName.getClass() == ElementTable.class) {
                    LOG.debug("On passe dans la partie pour les ElementTable");
                    AbstractIdentifier identTable = (((ElementTable)this.varName)).getIdentTable();
                    LOG.debug("Le nom de l'identifiant du tableau associé est: " + identTable.getName().getName());
                    TableType tTable = (TableType)localEnv.get(identTable.getName()).getType();
                    Type subTypeTable = tTable.getSubType();
                    if (subTypeTable != this.type.getType()) {
                        throw new ContextualError("Le sous-type n'est pas correct!", getLocation());
                    }
                }
                this.varName.setDefinition(new VariableDefinition(typeRecupere, getLocation()));
                LOG.debug("On rajoute la variable : "+varName.getName()+" de definition : "+varName.getExpDefinition());
                localEnv.declare(this.varName.getName(), this.varName.getVariableDefinition()); //On pourrait pt etre remplacer par getVariableDefinition
                LOG.debug("On a bien déclaré la variable x de definition: "+ localEnv.get(varName.getName()));
            }catch(DoubleDefException e){
                LOG.debug("La variable est deja definie, on ne peut pas la redefinir");
                throw new ContextualError("La variable "+varName.getName()+" est deja définie (3.17)", getLocation());
            }
            //On vérifie pour initialisation, pour Noinitialisation ça ne fera rien
            this.initialization.verifyInitialization(compiler, typeRecupere, localEnv, currentClass);
            LOG.debug("Tout s'est bien passé lors de la déclaration de variable");
        }
        //
    }

    @Override
    public void codeGenDeclVar(DecacCompiler compiler){
        //Gestion du registre GB et LB, il faut gerer le offset et prendre l'emplacement
        //Gestion de NOinitialization en mettant a zero
        //Gestion de initialization en partant sur un nouveau codegen qui va donner la bonne variable au bon registre
        ExpDefinition defVar = varName.getExpDefinition();
        //Il faudra g'erer si on est dans GB ou LB quand il y aura des classes, pour l'instant on ne traîte pas
        //Pour l'instant on a pas de methodes donc on a pas de diff entre LB et GB
        int offset = GPRegister.LB.incrOffset();
        defVar.setOperand(new RegisterOffset(offset, Register.LB));
        if(initialization.isInitialized()){
            this.initialization.codeGenInst(compiler, this.varName);
        }else if(defVar.getType().isTable()){
            //Si c'est un tableau sans initialisation, on ne fait rien
        }else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), GPRegister.R0));
            compiler.addInstruction(new STORE(GPRegister.R0, defVar.getOperand()));
            compiler.increaseNeedStack();
            compiler.addInstruction(new ADDSP(1));
        }
        
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        String typeVar = type.getName().getName();
        String varNameStr = this.varName.getName().getName();
        s.print(typeVar + " " + varNameStr);
        if(initialization.isInitialized()){
            s.print(" = ");
            this.initialization.decompile(s);
        }else{
            s.println(";");
        }
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
