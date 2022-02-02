package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutionException;
import java.lang.Thread;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl18
 * @date 01/01/2022
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing: "
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            String banniere = "Equipe 18 : \n"
                                + "- Nathan Gicquel\n"
                                + "- Grégoire Rabusson\n"
                                + "- Lucas Block\n"
                                + "- Nicolas Ferlut\n"
                                + "- Gabriel Lévy";
            System.out.println(banniere);
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            System.err.println("\nErreur : decac attend au moins un fichier source \n"+ options.getMessage());
        }
        if (options.getParallel()) {
            LOG.debug("On passe dans l'option parallèle ! ");
            for (File source : options.getSourceFiles()) {
                Thread thread = new Thread(new DecacCompiler(options, source));
                thread.run();
            }
            

        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
