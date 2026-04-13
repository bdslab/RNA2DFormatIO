package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Utility class for handling improper file paths provided as input to the tool.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaFileNameHandler {

    /**
     * Adjusts a potentially incorrect file path by ensuring it has a valid extension
     * and does not overwrite an existing file when creating a new one.
     *
     * @param exts    currently supported file extensions
     * @param def     default extension to apply if none is valid
     * @param path    the path to validate
     * @param newFile {@code true} if the file is being created (should not exist),
     *                {@code false} if it is an existing file being read
     * @return a safe file path with a valid extension and, if applicable, a unique name
     */
    public String checkExt(String[] exts, String def, String path, boolean newFile) {
        path = authomaticExtension(exts, def, path);
        if (newFile)
            path = checkFileExist(path);
        return path;
    }

    /**
     * Appends the default extension to the path if the current extension is not
     * among the supported ones.
     *
     * @param exts supported extensions
     * @param def  default extension
     * @param path the potentially unsafe path
     * @return a path guaranteed to have a supported extension
     */
    private String authomaticExtension(String[] exts, String def, String path) {
        String[] parts = path.split("\\.");
        String extension = parts[parts.length - 1];
        if (extension.equals("txt") && parts.length > 2)
            extension = parts[parts.length - 2];
        for (String ext : exts) {
            if (ext.equals(extension))
                return path;
        }
        return path + "." + def;
    }

    /**
     * Ensures that a new file does not already exist. If it does, generates a new
     * unique path by appending an incremental suffix.
     *
     * @param path the desired file path
     * @return a path that does not currently exist in the file system
     */
    private String checkFileExist(String path) {
        if (!Files.exists(Paths.get(path))) {
            return path;
        }
        String newPath;
        int count = 1;
        do {
            String[] parts = path.split("\\.");
            parts[parts.length - 2] += "_" + count++;
            newPath = Arrays.asList(parts).stream().reduce("", (a, b) -> a + (a.equals("") ? "" : ".") + b);
        } while (Files.exists(Paths.get(newPath)));
        return newPath;
    }

}