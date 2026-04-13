package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaDataLoader;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaFileWriter;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader.*;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer.*;

/**
 * Abstract class that produces the appropriate handler for a given context,
 * based on the called method and the file extension.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class RnaHandlerBuilder {

    /**
     * Returns the appropriate {@link RnaFileWriter} implementation for the
     * specified file path, determined by its extension.
     *
     * @param path the destination file path
     * @return a suitable file writer instance
     */
    public final RnaFileWriter buildFileWriter(String path) {
        String extension = getExtension(path);
        return switch (extension) {
            case "rnaml", "xml" -> new RnamlFileWriter();
            case "bpseq" -> new BpseqFileWriter();
            case "ct" -> new CtFileWriter();
            case "aas" -> new AasFileWriter();
            case "db" -> new DbFileWriter();
            default -> buildUnexpectedFileWriter(path);
        };
    }

    /**
     * Hook method to handle unsupported or unrecognized file extensions
     * when building a file writer. Subclasses may override to provide a fallback.
     *
     * @param path the destination file path
     * @return a fallback file writer (default may be {@link NullFileWriter})
     */
    protected abstract RnaFileWriter buildUnexpectedFileWriter(String path);

    /**
     * Returns the appropriate {@link RnaDataLoader} implementation for the
     * specified file path, determined by its extension.
     *
     * @param path the source file path to load
     * @return a suitable data loader instance
     */
    public final RnaDataLoader buildDataLoader(String path) {
        String extension = getExtension(path);
        return switch (extension) {
            case "rnaml", "xml" -> new RnamlDataLoader();
            case "bpseq" -> new BpseqDataLoader();
            case "ct" -> new CtDataLoader();
            case "aas" -> new AasDataLoader();
            case "db" -> new DbDataLoader();
            default -> buildUnexpectedDataLoader(path);
        };
    }

    /**
     * Hook method to handle unsupported or unrecognized file extensions
     * when building a data loader. Subclasses may override to provide a fallback.
     *
     * @param path the source file path to load
     * @return a fallback data loader (default may be {@link NullDataLoader})
     */
    protected abstract RnaDataLoader buildUnexpectedDataLoader(String path);

    /**
     * Internal utility to extract the file extension from a path.
     *
     * @param path the file path
     * @return the lowercased extension, or an empty string if none exists
     */
    private String getExtension(String path) {
        String[] parts = path.split("\\.");
        if (parts.length < 2)
            return "";
        String ext = parts[parts.length - 1];
        if (ext.equals("txt") && parts.length > 2)
            return parts[parts.length - 2];
        return ext;
    }

    /**
     * Returns the list of file extensions supported by this builder.
     *
     * @return an array of supported extensions (e.g., {"db", "bpseq", "ct", "aas", "rnaml"})
     */
    public abstract String[] getSupportedExtensions();

    /**
     * Returns the default extension to be used when none is provided or
     * when the provided extension is not recognized.
     *
     * @return the default file extension (e.g., "db")
     */
    public abstract String getDefaultExtension();

}