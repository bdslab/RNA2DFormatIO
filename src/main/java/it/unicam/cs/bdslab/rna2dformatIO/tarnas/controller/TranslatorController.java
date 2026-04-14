package it.unicam.cs.bdslab.rna2dformatIO.tarnas.controller;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFile;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFileTranslator;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat.*;

/**
 * An implementation of the Translator Controller that accepts input from the
 * {@link IOController} and converts it to commands for the Model or View.
 * <p>
 * This controller handles translation operations as well as file and directory
 * loading, saving, and deletion. It supports parallel translation when multiple
 * files are loaded to improve performance, and it performs I/O error checking.
 * </p>
 *
 * @author Piero Hierro, Piermichele Rosati, Francesco Palozzi
 * @see RNAFile
 * @see Stream#parallel()
 */
public class TranslatorController {

    private final static TranslatorController instance = new TranslatorController();

    /**
     * Conversion matrix that maps a source {@link RNAFormat} to the list of
     * possible destination {@code RNAFormat}s to which it can be translated.
     */
    private final Map<RNAFormat, List<RNAFormat>> conversionMatrix;

    /**
     * Creates the Translator Controller and initializes the conversion matrix.
     */
    private TranslatorController() {
        conversionMatrix = Map.of(
                AAS, List.of(AAS, AAS_NO_SEQUENCE, BPSEQ, CT, DB, DB_NO_SEQUENCE, FASTA),
                AAS_NO_SEQUENCE, List.of(DB_NO_SEQUENCE),
                BPSEQ, List.of(AAS, AAS_NO_SEQUENCE, CT, DB, DB_NO_SEQUENCE, FASTA),
                CT, List.of(AAS, AAS_NO_SEQUENCE, BPSEQ, DB, DB_NO_SEQUENCE, FASTA),
                DB, List.of(AAS, AAS_NO_SEQUENCE, DB, BPSEQ, CT, DB_NO_SEQUENCE, FASTA),
                DB_NO_SEQUENCE, List.of(AAS_NO_SEQUENCE),
                FASTA, List.of());
    }

    /**
     * Returns the list of formats to which a given source format can be translated.
     *
     * @param rnaFormat the source {@link RNAFormat}
     * @return a list of possible destination {@code RNAFormat}s
     */
    public List<RNAFormat> getAvailableTranslations(RNAFormat rnaFormat) {
        return (rnaFormat == AAS || rnaFormat == DB)
                ? this.conversionMatrix.get(rnaFormat).stream()
                .filter(t -> t != rnaFormat)
                .toList()
                : this.conversionMatrix.get(rnaFormat);
    }

    /**
     * Factory method to obtain the singleton instance of {@code TranslatorController}.
     *
     * @return the unique instance of this controller
     */
    public static TranslatorController getInstance() {
        return instance;
    }

    /**
     * Translates the specified RNA file to the given destination format.
     *
     * @param file         the {@link RNAFile} to translate
     * @param dstRNAFormat the destination {@link RNAFormat}
     * @return a new {@code RNAFile} representing the translation of the input file
     */
    public RNAFile translate(RNAFile file, RNAFormat dstRNAFormat) {
        return noCheckingTranslateTo(file, dstRNAFormat);
    }

    /**
     * Performs a direct translation to the specified format without any
     * pre- or post-translation checks.
     *
     * @param rnaFile   the {@code RNAFile} to translate
     * @param rnaFormat the destination {@link RNAFormat}
     * @return the translated {@code RNAFile}
     */
    private RNAFile noCheckingTranslateTo(RNAFile rnaFile, RNAFormat rnaFormat) {
        return switch (rnaFormat) {
            case AAS -> RNAFileTranslator.translateToAAS(rnaFile);
            case AAS_NO_SEQUENCE -> RNAFileTranslator.translateToAASNoSequence(rnaFile);
            case BPSEQ -> RNAFileTranslator.translateToBPSEQ(rnaFile);
            case CT -> RNAFileTranslator.translateToCT(rnaFile);
            case DB -> RNAFileTranslator.translateToDB(rnaFile);
            case DB_NO_SEQUENCE -> RNAFileTranslator.translateToDBNoSequence(rnaFile);
            case FASTA -> RNAFileTranslator.translateToFASTA(rnaFile);
        };
    }
}