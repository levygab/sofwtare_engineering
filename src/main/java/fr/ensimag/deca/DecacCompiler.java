package fr.ensimag.deca;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentType.DoubleDefException;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import java.lang.Runnable;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl18
 * @date 01/01/2022
 */
public class DecacCompiler implements Runnable {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 
    private EnvironmentType envType = new EnvironmentType();
    
    private SymbolTable symbolTable = new SymbolTable();

    private int RegReturn; 
    
    public int getRegReturn() {
        return RegReturn;
    }

    public void setRegReturn(int regReturn) {
        RegReturn = regReturn;
    }

    private Label returnLabel;


    public Label getReturnLabel() {
        return returnLabel;
    }

    public void setReturnLabel(Label returnLabel) {
        this.returnLabel = returnLabel;
    }

    // Pour TSTO
    private int needStackCurrent = 0;
    private int needStackMax = 0;

    // Pour les labels
    private int labelCounter = 0;

    /**
     * Increase labelCounter
     */
    public void increaseLabelCounter(){
        this.labelCounter++;
    }

    /**
     * Get labelCounter
     */
    public int getLabelCounter() {
        return this.labelCounter;
    }
    
    /**
     * Increase needStackCurrent and update needStackMax if needed
     */
    public void increaseNeedStack(){
        needStackCurrent++;
        if (needStackMax < needStackCurrent) {
            needStackMax = needStackCurrent;
        }
    }

    public void initNeedStack(){
        needStackCurrent = 0;
    }

    private Stack<GPRegister> stackRegister = new Stack<GPRegister>();

    
    public Stack<GPRegister> getStackRegister() {
        return stackRegister;
    }

    private int registreUseForMeth = 0;

    public int getRegistreUseForMeth(){
        return registreUseForMeth;
    }
    public void setRegistreUseForMeth(int i){
        registreUseForMeth = i ;
    }


    /**
     * Decrease needStackCurrent
     */
    public void decreaseNeedStack() {
        needStackCurrent--;
    }

    public int getNeedStackMax() {
        return needStackMax;
    }


    /**
     * Extension tableau : permet de créer un type tableau de sous type 
     * passé en paramètre
     * @param env
     */
    public void createTableType(EnvironmentType env) {
        Iterator iterator = env.getDictEnvironment().entrySet().iterator();
        HashMap<String, Type> listNewTypes = new HashMap<String, Type>();
        while (iterator.hasNext()) {
            Map.Entry<Symbol, TypeDefinition> mapEntry = (Map.Entry) iterator.next();
            Type subType = mapEntry.getValue().getType();
            String sType = subType.getName().getName();
            String sTypeTable = sType + "[]";
            listNewTypes.put(sTypeTable, subType);
        }
        try {
            for (String stringType: listNewTypes.keySet()) {
                SymbolTable.Symbol symbolT = this.symbolTable.create(stringType);
                Type subType = listNewTypes.get(stringType);
                envType.declare(symbolT, new TypeDefinition(new TableType(symbolT, subType), Location.BUILTIN));
            }
        } catch (DoubleDefException e) {
            e.printStackTrace();
        }
    }


    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        try {
            // Initialisations de la table des symboles et de environmentType
            SymbolTable.Symbol symbol = symbolTable.create("void");
            this.envType.declare(symbol, new TypeDefinition(new VoidType(symbol), Location.BUILTIN));

            symbol = symbolTable.create("string");
            this.envType.declare(symbol, new TypeDefinition(new StringType(symbol), Location.BUILTIN));

            symbol = symbolTable.create("boolean");
            Type boolType = new BooleanType(symbol);
            this.envType.declare(symbol, new TypeDefinition(boolType, Location.BUILTIN));

            symbol = symbolTable.create("float");
            Type floatType = new FloatType(symbol);
            this.envType.declare(symbol, new TypeDefinition(floatType, Location.BUILTIN));

            symbol = symbolTable.create("int");
            Type intType = new IntType(symbol);
            this.envType.declare(symbol, new TypeDefinition(intType, Location.BUILTIN));

            // createTableAndMatrixType(envType);

            symbol = symbolTable.create("int[]");
            Type TableIntType = new TableType(symbol, intType);
            this.envType.declare(symbol, new TypeDefinition(TableIntType, Location.BUILTIN));

            symbol = symbolTable.create("float[]");
            Type TableFloatType = new TableType(symbol, floatType);
            this.envType.declare(symbol, new TypeDefinition(TableFloatType, Location.BUILTIN));

            symbol = symbolTable.create("int[][]");
            Type MatrixIntType = new TableType(symbol, TableIntType);
            this.envType.declare(symbol, new TypeDefinition(MatrixIntType, Location.BUILTIN));

            symbol = symbolTable.create("float[][]");
            Type MatrixFloatType = new TableType(symbol, TableFloatType);
            this.envType.declare(symbol, new TypeDefinition(MatrixFloatType, Location.BUILTIN));

            symbol = symbolTable.create("boolean[]");
            Type TableBoolType = new TableType(symbol, boolType);
            this.envType.declare(symbol, new TypeDefinition(TableBoolType, Location.BUILTIN));

            symbol = symbolTable.create("boolean[][]");
            Type MatrixBoolType = new TableType(symbol, TableBoolType);
            this.envType.declare(symbol, new TypeDefinition(MatrixBoolType, Location.BUILTIN));

            //createTableType(this.envType);



            // null pas à implémenter pour sans objet
            // symbol = symbolTable.create("null");
            // this.envType.declare(symbol, new TypeDefinition(new NullType(symbol), Location.BUILTIN));

            symbol = symbolTable.create("null");
            this.envType.declare(symbol, new TypeDefinition(new NullType(symbol), Location.BUILTIN));

            //TODO : vérifier le bon fonctionnement de la création
            symbol = symbolTable.create("Object");
            ClassDefinition objectDef = new ClassDefinition(new ClassType(symbol, Location.BUILTIN, null), Location.BUILTIN, null);
            
            objectDef.setOperand(new RegisterOffset(1, Register.GB));
            this.envType.declare(symbol, objectDef);
            BooleanType bool = (BooleanType) this.envType.get(symbolTable.create("boolean")).getType();
            Signature sign = new Signature();
            sign.add(this.envType.get(symbolTable.create("Object")).getType());
            MethodDefinition defMethodeEquals = new MethodDefinition(bool, Location.BUILTIN, sign, 0);
            defMethodeEquals.setLabel(new Label("code.Object.equals"));
            objectDef.getMembers().declare(symbolTable.create("equals"), defMethodeEquals);
            objectDef.setNumberOfMethods(1);
    
        }
        catch(EnvironmentType.DoubleDefException e){
            e.printStackTrace();
        }
        catch(EnvironmentExp.DoubleDefException exception2){
            
        }
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addLabel(java.lang.String)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

     /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addFirst(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addFirstInst(Instruction instruction) {
        program.addFirst(instruction);
    }

    public void addFirstComment(String comment) {
        program.addFirst(new Line(comment));
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    

    public EnvironmentType getenvType() {
        return envType;
    }
    
    public SymbolTable getsymbolTable() {
        return symbolTable;
    }

    /**
     * 
     * @param sType
     * @return 
     * Retourne le type de envType passé en paramètre
     * Crée pour l'étape B de S-O (10 janvier)
     */
    public Type findType(String sType) {
        // On recupere le symbole dans tableSymbol
        Symbol s = this.getsymbolTable().create(sType);
        // On recupere le type associe envType
        Type t = this.getenvType().get(s).getType();
        return t;
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = null;
        // Calcul de nom du fichier .ass généré
        destFile = sourceFile.substring(0,sourceFile.length()-4)+"ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        
        // Options -r X : on limite le nombre de registres accesibles
        if (getCompilerOptions().getRegistres() != 16){
            GPRegister.nbreRegistres = getCompilerOptions().getRegistres();
        }
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    @Override
    public void run() {
        this.compile();
    }

    /**
     * Use to generate code for errors
     */
    private void addInstructionsError() {
        addInstruction(new WNL());
        addInstruction(new ERROR());
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        if(this.getCompilerOptions().getParse()){
            prog.decompile(out);
            return false;
        }
        assert(prog.checkAllLocations());


        prog.verifyProgram(this);
        if(this.getCompilerOptions().getVerification()){
            return false;
        }
        assert(prog.checkAllDecorations());
        addInstruction(new BRA(new Label("DebutMain")));
        addLabel(new Label("code.Object.equals"));
        addLabel(new Label("init.Object"));
        addInstruction(new RTS());
        GPRegister.LB.reInitOffset();
        prog.codeGenProgram(this);
        addComment("end main program");
        if (!getCompilerOptions().getNoCheck()) {
            addFirstInst(new BOV(new Label("pile_pleine")));
            addFirstInst(new TSTO(getNeedStackMax()));
        }
        addFirstComment("start main program");
        
        if (!getCompilerOptions().getNoCheck()) {
            addLabel(new Label("io_error"));
            addInstruction(new WSTR("Error: Input/Output error"));
            addInstructionsError();
            addLabel(new Label("io_error_div_O"));
            addInstruction(new WSTR("Division par 0 interdite"));
            addInstructionsError();
            addLabel(new Label("io_debordement_valeur"));
            addInstruction(new WSTR("Error: Debordement flottant nombre trop grand"));
            addInstructionsError();
            addLabel(new Label("pile_pleine"));
            addInstruction(new WSTR("Erreur : la pile est pleine"));
            addInstructionsError();
            addLabel(new Label("tas_plein"));
            addInstruction(new WSTR("Erreur : le tas est plein"));
            addInstructionsError();
            addLabel(new Label("Deferencement_de_null"));
            addInstruction(new WSTR("L'identifier est null"));
            addInstructionsError();
            addLabel(new Label("io_error_index_out_of_range"));
            addInstruction(new WSTR("Erreur : Index out of range"));
            addInstructionsError();
            addLabel(new Label("io_incorrect_sizes"));
            addInstruction(new WSTR("Erreur : Les tableaux passées en paramètres doivent être de bonnes tailles"));
            addInstructionsError();
        }

        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When andécorerror prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }



}
