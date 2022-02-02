package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl18
 * @date 01/01/2022
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verification = false;
    private boolean noCheck = false;
    private int registres = 16;
    private List<File> sourceFiles = new ArrayList<File>();
    private static final String MESSAGE_OPTIONS = "\nUTILISATION : decac [[-p | -v ] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]\n\n"
                                                +"-b (banner) : affiche une banière indiquant le nom de l'équipe\n"
                                                +"-p (parse) : arrête decac après construction de l'arbre et affiche la décompilation\n"
                                                +"-v (verification) : arrête decac après l'étape de vérification\n"
                                                +"-n (no check) : supprime les tests à l'éxécution\n"
                                                +"-r X (registers) : limite les registres banalisés, 4 <= X <= 16\n"
                                                + "-d (debug) : active les traces de debug. Répéter plusieurs fois pour plus de traces\n"
                                                + "-P (parallel) : s'il y a plusieurs fichiers sources, lance la compilation en parallèle\n";

    
    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public boolean getParse() {
        return parse;
    }

    public boolean getVerification() {
        return verification;
    }

    public boolean getNoCheck() {
        return noCheck;
    }

    public int getRegistres() {
        return registres;
    }

    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }
    
    public String getMessage() {
        return MESSAGE_OPTIONS;
    }
    
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        Logger logger = Logger.getRootLogger();
        // if (args.length == 0) {
        //     throw new CLIException("decac attend au moins un fichier source");
        // }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-b")) {
                    this.printBanner = true;
                    if (args.length > 1) {
                        throw new CLIException("L'option -b s'utilise toute seule");
                    }
            } else if (args[i].equals("-p")) {
                this.parse = true;
            } else if (args[i].equals("-v")) {
                this.verification = true;
                if (this.parse == true) {
                    throw new CLIException("Les options -v et -p sont incompatibles");
                }
            } else if (args[i].equals("-n")) {
                this.noCheck = true;
            } else if (args[i].equals("-r")) {
                try{
                    this.registres = Integer.parseInt(args[i+1]);
                    if (!(4 <= this.registres && this.registres <= 16)) {
                        throw new CLIException("L'option -r nécessite un entier entre 4 et 16");
                    }
                } catch(NumberFormatException e) {
                    throw new CLIException("Erreur dans la ligne de commande, -r doit être suivi d'un entier entre 4 et 16");
                }
            } else if (args[i].equals("-d")){
                if (this.debug == 4) {
                    continue;
                } else {
                    debug++;
                }
            } else if (args[i].equals("-P")) {
                this.parallel = true;
            } else if(args[i].endsWith(".deca")) {
                File f = new File(args[i]);
                sourceFiles.add(f);
            } else {
                try {
                    Integer.parseInt(args[i]);
                } catch(NumberFormatException e) {
                    throw new CLIException("Vous ne pouvez pas écrire cela !");
                }
            }

            
        }
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

    }

    protected void displayUsage() {
        System.out.println(MESSAGE_OPTIONS);
        System.exit(1);
    }
}
