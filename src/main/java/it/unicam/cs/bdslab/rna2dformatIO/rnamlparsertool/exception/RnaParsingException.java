package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.exception;

/**
 * Exception used to report the location of a parsing error within an RNA structure.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaParsingException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception for an error involving a single ribonucleotide.
     *
     * @param chain          the chain number where the error occurred
     * @param ribonucleotide the position of the ribonucleotide within the chain
     */
    public RnaParsingException(int chain, int ribonucleotide) {
        super("Error in chain n." + chain
                + " and ribonucleotide in pos." + ribonucleotide);
    }

    /**
     * Constructs an exception for an error involving two ribonucleotides
     * (e.g., an invalid base pair).
     *
     * @param chain           the chain number where the error occurred
     * @param ribonucleotide1 the position of the first ribonucleotide
     * @param ribonucleotide2 the position of the second ribonucleotide
     */
    public RnaParsingException(int chain, int ribonucleotide1, int ribonucleotide2) {
        super("Error in chain n." + chain
                + ", ribonucleotide in pos." + ribonucleotide1
                + " and ribonucleotide in pos." + ribonucleotide2);
    }

}