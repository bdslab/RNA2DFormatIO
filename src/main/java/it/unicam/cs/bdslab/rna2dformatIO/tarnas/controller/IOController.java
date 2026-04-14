package it.unicam.cs.bdslab.rna2dformatIO.tarnas.controller;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFile;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFileConstructor;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.utils.RNAStatisticsCalculator.*;

/**
 * Singleton controller responsible for loading, saving, and packaging RNA files.
 * All I/O operations are centralized here, and exceptions are propagated to the caller.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class IOController {

    private static final IOController instance = new IOController();
    private RNAFormat recognizedFormat;  // Tracks the format of loaded files
    private final List<RNAFile> loadedRNAFiles;

    private IOController() {
        this.loadedRNAFiles = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the controller.
     *
     * @return the unique {@code IOController} instance
     */
    public static IOController getInstance() {
        return instance;
    }

    /**
     * Returns an unmodifiable list of the currently loaded RNA files.
     *
     * @return a list of loaded {@link RNAFile} objects
     */
    public List<RNAFile> getLoadedRNAFiles() {
        return List.copyOf(loadedRNAFiles);
    }

    /**
     * Returns the RNA format recognized from the loaded files.
     *
     * @return the {@link RNAFormat} of the loaded files, or {@code null} if none are loaded
     */
    public RNAFormat getRecognizedFormat() {
        return recognizedFormat;
    }

    /**
     * Builds an {@link RNAFile} instance from the file at the specified path.
     *
     * @param srcFilePath path to the source RNA file
     * @return the constructed {@code RNAFile}
     * @throws IOException if an I/O error occurs during reading or parsing
     */
    public RNAFile getRNAFileOf(Path srcFilePath) throws IOException {
        return RNAFileConstructor.getInstance().construct(srcFilePath);
    }

    /**
     * Loads a single RNA file and ensures format consistency with any previously loaded files.
     *
     * @param srcFilePath path to the RNA file to load
     * @return the loaded {@link RNAFile}
     * @throws IOException              if an I/O error occurs during loading
     * @throws IllegalArgumentException if the format of the new file differs from already loaded files
     */
    public RNAFile loadFile(Path srcFilePath) throws IOException {
        var rnaFile = getRNAFileOf(srcFilePath);

        if (recognizedFormat == null || recognizedFormat.equals(rnaFile.getFormat())) {
            loadedRNAFiles.add(rnaFile);
            recognizedFormat = rnaFile.getFormat();
        } else {
            throw new IllegalArgumentException(
                    "All loaded files must share the same format: " + recognizedFormat +
                            ", but got: " + rnaFile.getFormat()
            );
        }

        return rnaFile;
    }

    /**
     * Loads all regular files from a directory, ignoring hidden files and files within
     * directories whose names contain a dot.
     *
     * @param srcDirectoryPath path to the directory to load
     * @throws IOException if an I/O error occurs while traversing the directory
     */
    public void loadDirectory(Path srcDirectoryPath) throws IOException {
        try (var directoryStream = Files.newDirectoryStream(srcDirectoryPath)) {
            var pathSelectedDir = srcDirectoryPath.getFileName().toString();
            for (var path : directoryStream) {
                var pathFromDirName = path.toString().substring(path.toString().indexOf(pathSelectedDir));
                var pathBetweenDirAndFile = pathFromDirName.substring(0, pathFromDirName.lastIndexOf(path.getFileName().toString()));
                if (Files.isRegularFile(path) && !path.getFileName().toString().startsWith(".") && !pathBetweenDirAndFile.contains(".")) {
                    loadFile(path);
                }
            }
        }
    }

    /**
     * Saves a list of RNA files to the specified destination directory, optionally generates
     * non-canonical pair data and sequence statistics, and creates a ZIP archive of all
     * generated files.
     *
     * @param files                     the RNA files to save
     * @param dstPath                   destination directory for the generated files
     * @param generateNonCanonicalPairs whether to include non-canonical pair CSV files
     * @param generateStatistics        whether to generate sequence statistics CSV files
     * @param zipFileName               base name for the output ZIP file (without extension);
     *                                  if blank, no ZIP is created
     * @throws IOException if an I/O error occurs during file writing or zipping
     */
    public void saveFiles(List<RNAFile> files,
                          Path dstPath,
                          boolean generateNonCanonicalPairs,
                          boolean generateStatistics,
                          String zipFileName) throws IOException {

        var generatedFiles = new ArrayList<Path>();

        saveFiles(files, dstPath, generatedFiles);
        processNonCanonicalPairs(generateNonCanonicalPairs, dstPath, generatedFiles);
        generateStatistics(files, dstPath, generateStatistics, generatedFiles);
        createZipFile(dstPath, zipFileName, generatedFiles);
    }

    private void saveFiles(List<RNAFile> files, Path dstPath, List<Path> generatedFiles) throws IOException {
        for (var file : files) {
            var baseFileName = file.getFileName().split("\\.")[0];
            var extension = "." + file.getFormat().getExtension() + ".txt";
            var fileName = baseFileName + extension;
            var destinationPath = dstPath.resolve(fileName);

            Files.write(destinationPath, file.getContent());
            generatedFiles.add(destinationPath);
        }
    }

    /**
     * Processes non-canonical pair CSV files generated in the current working directory.
     * If requested, they are renamed and moved to the destination directory with "_nc" suffix
     * and their content is adjusted (spaces replaced with commas). Otherwise, they are deleted.
     */
    private void processNonCanonicalPairs(boolean generateNonCanonicalPairs,
                                          Path dstPath,
                                          List<Path> generatedFiles) throws IOException {
        var currentDir = Paths.get(System.getProperty("user.dir"));
        try (var csvStream = Files.list(currentDir)) {
            var csvFiles = csvStream
                    .filter(path -> path.toString().endsWith(".csv"))
                    .toList();

            for (var csvFile : csvFiles) {
                if (generateNonCanonicalPairs) {

                    var newFileName = csvFile.getFileName().toString().split("\\.")[0] + "_nc.csv";
                    var destinationPath = dstPath.resolve(newFileName);

                    Files.move(csvFile, destinationPath);

                    // Replace spaces with commas in each line
                    var updatedLines = Files.readAllLines(destinationPath).stream()
                            .map(line -> line.replace(" ", ","))
                            .collect(Collectors.toList());
                    Files.write(destinationPath, updatedLines);

                    generatedFiles.add(destinationPath);
                } else {
                    Files.delete(csvFile);
                }
            }
        }
    }

    private void generateStatistics(List<RNAFile> files,
                                    Path dstPath,
                                    boolean generateStatistics,
                                    List<Path> generatedFiles) throws IOException {
        if (!generateStatistics) return;

        for (var file : files) {
            var statsFileName = file.getFileName().split("\\.")[0] + "_seqInfo.csv";
            var statsFilePath = dstPath.resolve(statsFileName);

            var header = "Nucleotide count, Bond count, A count, C count, G count, U count, GC bonds, AU bonds, GU bonds";
            var statsData = String.join(", ",
                    String.valueOf(getNucleotideCount(file)),
                    String.valueOf(getBondCount(file)),
                    String.valueOf(getACount(file)),
                    String.valueOf(getCCount(file)),
                    String.valueOf(getGCount(file)),
                    String.valueOf(getUCount(file)),
                    String.valueOf(getGcBonds(file)),
                    String.valueOf(getAuBonds(file)),
                    String.valueOf(getGuBonds(file))
            );

            Files.write(statsFilePath, List.of(header, statsData));
            generatedFiles.add(statsFilePath);
        }
    }

    private void createZipFile(Path dstPath, String zipFileName, List<Path> generatedFiles) throws IOException {
        if (zipFileName == null || zipFileName.isBlank()) return;

        var zipFilePath = dstPath.resolve(zipFileName + ".zip");

        try (var zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            for (var filePath : generatedFiles) {
                var zipEntry = new ZipEntry(filePath.getFileName().toString());
                zipOutputStream.putNextEntry(zipEntry);

                Files.copy(filePath, zipOutputStream);
                zipOutputStream.closeEntry();

                Files.delete(filePath);  // Remove original file after adding to ZIP
            }
        }
    }

    /**
     * Removes the specified RNA file from the list of loaded files.
     * If no files remain loaded, the recognized format is reset to {@code null}.
     *
     * @param rnaFile the file to remove
     */
    public void deleteFile(RNAFile rnaFile) {
        loadedRNAFiles.remove(rnaFile);
        if (loadedRNAFiles.isEmpty()) {
            recognizedFormat = null;
        }
    }

    /**
     * Clears all loaded RNA files and resets the recognized format.
     */
    public void clearAllDataStructures() {
        loadedRNAFiles.clear();
        recognizedFormat = null;
    }
}