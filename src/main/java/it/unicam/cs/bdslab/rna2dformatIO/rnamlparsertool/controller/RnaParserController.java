package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.controller;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaDataLoader;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaFileWriter;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.OperationResult;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer.TertiaryStructureWriter;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility.DefaultRnaHandlerBuilder;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility.RnaHandlerBuilder;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility.RnaFileNameHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Central Facade controller for this tool.
 * Aggregates all the library functions.
 * Any external usage should go through this class.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaParserController {

    /**
     * Loaded data ready to be written.
     */
    private RnaMolecule chains;

    /**
     * Builder for RNA data handlers.
     */
    private RnaHandlerBuilder builder;

    /**
     * Name of the loaded file.
     */
    private String loadedPath;

    /**
     * Class for handling unexpected file names.
     */
    private RnaFileNameHandler nameHandler;

    private TertiaryStructureWriter tertiaryWriter;

    /**
     * Default constructor for the controller.
     */
    public RnaParserController() {
        this(new DefaultRnaHandlerBuilder());
    }

    /**
     * Constructor for the controller with a custom builder.
     *
     * @param builder extended builder with new formats
     */
    public RnaParserController(RnaHandlerBuilder builder) {
        this.builder = builder;
        this.nameHandler = new RnaFileNameHandler();
        this.tertiaryWriter = new TertiaryStructureWriter();
    }

    /**
     * Loads RNA data contained in the specified file.
     *
     * @param path file name
     * @return result of the operation
     */
    public synchronized OperationResult loadRna(String path) {
        path = checkExt(path, false);
        RnaDataLoader loader = builder.buildDataLoader(path);
        chains = loader.getData(path);
        OperationResult result = new OperationResult();
        if (isLoaded()) {
            loadedPath = path;
            result.result = true;
            result.addInfo("Load " + chains.getchains().size() + " chains.");
            for (int i = 0; i < chains.getchains().size(); i++) {
                result.addInfo("chain n." + i + " with " +
                        chains.getchains().get(i).getLength() + " ribonucleotides.");
            }
        } else {
            result.addInfo("Failure to load data.");
        }
        return result;
    }

    /**
     * Checks whether data is currently loaded.
     *
     * @return true if data is loaded, false otherwise
     */
    public boolean isLoaded() {
        return chains != null;
    }

    /**
     * Clears the loaded data.
     */
    public void clean() {
        this.chains = null;
    }

    /**
     * Saves the loaded data to the specified file.
     *
     * @param path file name
     * @return result of the operation
     */
    public synchronized OperationResult SaveLoadedData(String path) {
        OperationResult result = new OperationResult();
        if (path == null || (!isLoaded())) {
            result.addInfo(path == null ? "Error. Path is null." : "Error. Data to save not loaded.");
            return result;
        }
        var extension = path.substring(path.lastIndexOf('.') + 1);
        if (extension.equals("fasta")) {
            return this.fastaTranslation(this.loadedPath, path);
        } else {
            path = checkExt(path, true);
            RnaFileWriter writer = this.builder.buildFileWriter(path);
            result.result = writer.writeAndSave(chains, path);
            result.addInfo(result.result ? "Saving to file " + path + " was successful."
                    : "Failed to save data in " + path + " file.");
            if (result.result && chains.haveTertiaryData() && (!Files.exists(Paths.get(loadedPath + ".csv")))) {
                result.result = this.tertiaryWriter.writeAndSave(chains, loadedPath + ".csv");
                result.addInfo(result.result ? "Saving to tertiary structure was successful."
                        : "Saving to tertiary structure was failed.");
            }
            return result;
        }
    }

    /**
     * Ensures the file path has a supported extension.
     * Uses the internal name handler to validate or append the default extension.
     *
     * @param path    path to check
     * @param newFile true if it is a new file (to be created/overwritten)
     * @return a safe path with a valid extension
     */
    public String checkExt(String path, boolean newFile) {
        return this.nameHandler.checkExt(this.builder.getSupportedExtensions(), this.builder.getDefaultExtension(), path, newFile);
    }

    /**
     * Returns the builder used by this controller.
     *
     * @return the current {@link RnaHandlerBuilder} instance
     */
    public RnaHandlerBuilder getBuilder() {
        return builder;
    }

    /**
     * Returns the path of the currently loaded file.
     *
     * @return the loaded file path, or null if none
     */
    public String getLoadedPath() {
        return loadedPath;
    }

    private OperationResult fastaTranslation(String input, String output) {
        var r = new OperationResult();
        try {
            var in = Path.of(input);
            var out = Path.of(output);
            var content = String.join("", Files.readAllLines(in)).replaceAll("\\s+", "");
            content = content.replaceAll(".*?<seq-data>(.*?)</seq-data>.*", "$1");
            content = content.isEmpty() ? "" : content;
            Files.writeString(out, content);
            r.result = true;
            return r;
        } catch (IOException e) {
            r.result = false;
            return r;
        }
    }

}