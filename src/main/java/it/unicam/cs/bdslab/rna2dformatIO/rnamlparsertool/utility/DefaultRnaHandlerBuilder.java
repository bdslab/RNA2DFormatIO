package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaDataLoader;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaFileWriter;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader.NullDataLoader;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer.NullFileWriter;

/**
 * Default implementation for unsupported cases.
 * To add new formats, extend the abstract class in a different manner.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class DefaultRnaHandlerBuilder extends RnaHandlerBuilder {

    /**
     * Returns a {@link NullFileWriter} when an unexpected file extension is encountered.
     *
     * @param path the destination file path
     * @return a {@code NullFileWriter} instance
     */
    @Override
    protected RnaFileWriter buildUnexpectedFileWriter(String path) {
        return new NullFileWriter();
    }

    /**
     * Returns a {@link NullDataLoader} when an unexpected file extension is encountered.
     *
     * @param path the source file path to load
     * @return a {@code NullDataLoader} instance
     */
    @Override
    protected RnaDataLoader buildUnexpectedDataLoader(String path) {
        return new NullDataLoader();
    }

    /**
     * Returns the list of file extensions supported by this builder.
     *
     * @return an array containing "rnaml", "xml", "bpseq", "ct", "aas", "db"
     */
    @Override
    public String[] getSupportedExtensions() {
        return new String[]{"rnaml", "xml", "bpseq", "ct", "aas", "db"};
    }

    /**
     * Returns the default extension to use when none is provided or recognized.
     *
     * @return "xml"
     */
    @Override
    public String getDefaultExtension() {
        return "xml";
    }

}