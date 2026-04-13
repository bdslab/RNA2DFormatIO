package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile;

import java.io.Serial;

/**
 * Exception to signal any syntax error in input files containing the
 * description of an RNA secondary structure in any supported format.
 *
 * @author Luca Tesei, Piero Hierro, Piermichele Rosati
 */
public class RNAInputFileParserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4540612561494091099L;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     */
    public RNAInputFileParserException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public RNAInputFileParserException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public RNAInputFileParserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public RNAInputFileParserException(String message, Throwable cause) {
        super(message, cause);
    }

}