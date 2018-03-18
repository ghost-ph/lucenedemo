package com.patterncat.lucene.pageRank;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author David Perrett
 */
public class PageRank {

    // Command line flags and defaults
    private static final String CHECK_COMMAND = "check";
    private static final String RUN_COMMAND = "run";
    private static final int DEFAULT_NUM_ITERATIONS = 50;
    private static final double DEFAULT_DECAY_FACTOR = 0.85;
    private static final int DEFAULT_PRECISION = 2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new PageRank(args);
    }

    /**
     * Constructor
     *
     * @param args the command line arguments
     */
    public PageRank(String[] args) {

        try {

            // Initialise some ranking params
            int numIterations = this.DEFAULT_NUM_ITERATIONS;
            double decayFactor = this.DEFAULT_DECAY_FACTOR;
            int precision = this.DEFAULT_PRECISION;
            String fileName = "";
            String runMode = "";

            // Validate the inputted params
            if (args.length == 2 && args[0].trim().toLowerCase().equals(CHECK_COMMAND)) {
                runMode = CHECK_COMMAND;
                fileName = args[1].trim();

            } else if (args.length >= 2 && args.length <= 5 &&
                    args[0].trim().toLowerCase().equals(RUN_COMMAND)) {

                runMode = RUN_COMMAND;
                fileName = args[1].trim();

                // Validate the num-iterations argument
                if (args.length >= 3) {
                    try {
                        numIterations = Integer.parseInt(args[2].trim());
                        if (numIterations <= 0) {
                            exitWithError("Error : num-iterations argument must be greater than 0");
                        }
                    } catch (NumberFormatException e) {
                        exitWithError("Error : num-iterations argument is not a valid number");
                    }
                }

                // Validate the decay-factor argument
                if (args.length >= 4) {
                    try {
                        decayFactor = Double.parseDouble(args[3].trim());
                        if (decayFactor < 0.0 || decayFactor > 1.0) {
                            exitWithError("Error : decay-factor must be between 0.0 and 1.0 (inclusive)");
                        }
                    } catch (NumberFormatException e) {
                        exitWithError("Error : decay-factor argument is not a valid decimal number");
                    }
                }

                // Validate the precision argument
                if (args.length == 5) {
                    try {
                        precision = Integer.parseInt(args[4].trim());
                        if (precision <= 0) {
                            exitWithError("Error : precision argument must be greater than 0");
                        }
                    } catch (NumberFormatException e) {
                        exitWithError("Error : precision argument is not a valid number");
                    }
                }

            } else {
                exitWithError("Error : Invalid arguments");
            }

            // Read in the link-pair list from the input file
            ArrayList<String[]> links = this.parseInputFile(fileName);

            // Instantiate the manager
            PageRankManager manager = new PageRankManager(links);

            // Run in 'check' mode
            if (runMode.equals(RUN_COMMAND)) {
                manager.run(numIterations, decayFactor, precision);
            }

        } catch (Exception e) {
            this.exitWithError(e.getMessage());
        }
    }

    /**
     * Read in the link-pairs from the input file and create a list
     * of link-pairs
     *
     * @param fileName Path to the file containing the link-list
     */
    private ArrayList<String[]> parseInputFile(String fileName) throws IOException {

        // Read in the file line-by-line and create a tree of pages
        FileInputStream fileStream = new FileInputStream(fileName);
        DataInputStream inputStream = new DataInputStream(fileStream);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        ArrayList<String[]> links = new ArrayList<String[]> ();

        int lineNum = 0;
        while ((line = buffer.readLine()) != null)   {
            lineNum++;

            // Split the line into tokens
            String[] tokens = line.trim().split("\\s+");

            // Add the tokens to the link list
            links.add(tokens);
        }

        //Close the input stream
        inputStream.close();

        return links;
    }

    /**
     * Output a message describing usage and options
     */
    private void printUsage() {
        System.out.println("Usage: java -jar PageRank.jar run-mode filename [[[num-iterations] decay-factor] precision]");
        System.out.println("    run-mode");
        System.out.println("        check      : Check the file [filename] for rank-sinks and rank-leaks");
        System.out.println("        run        : Calculate the page-ranks of the links provided in [filename]");
        System.out.println("    filename       : File containing pairs of urls, one pair per line");
        System.out.println("    num-iterations : Number of iterations of the page-rank algorithm to run");
        System.out.println("    decay-factor   : Simulates random jumps in the link-graph to avoid sink and leak problems");
        System.out.println("    precision      : Number of decimal places to round the page-ranks to");
    }

    /**
     * Output a message describing usage and options, and print an error message
     *
     * @param error error message to print
     */
    private void exitWithError(String error) {
        printUsage();
        System.err.println("Error :");
        System.err.println(error);
        System.exit(1);
    }

}

