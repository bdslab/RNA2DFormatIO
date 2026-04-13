package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaDataLoader;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * Abstract class providing utility methods for operating on text files
 * and extracting informational metadata from them.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class TextDataLoader implements RnaDataLoader {

    /**
     * Reads all lines from the specified file and splits each line into tokens
     * based on whitespace.
     *
     * @param path the file path
     * @return a list of tokenized lines, or {@code null} if an I/O error occurs
     */
    protected List<List<String>> getLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path)).stream()
                    .map(l -> Arrays.asList((l.trim().split("\\s+"))))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Scans the tokenized lines for optional metadata (organism, accession number,
     * reference link) and sets them on the provided {@link RnaMolecule} object.
     *
     * @param file  the RNA molecule to populate with metadata
     * @param lines the tokenized lines from the file
     */
    protected void setFileInfo(RnaMolecule file, List<List<String>> lines) {
        boolean findOrganism = true, findNumber = true, findLink = true;
        for (List<String> line : lines) {
            if (line.size() > 1) {
                if (findOrganism && containAtIndex(line, 0, "organism")) {
                    file.setOrganism(getWordsFrom(line, 1));
                    findOrganism = false;
                } else if (findNumber && containAtIndex(line, 0, "accession") && containAtIndex(line, 1, "number")) {
                    file.setAccessionNumber(getWordsFrom(line, 2));
                    findNumber = false;
                } else if (findLink) {
                    String url = findFirstUrl(line);
                    if (url != null) {
                        file.setReferenceLink(url);
                        findLink = false;
                    }
                }
            }
        }
    }

    /**
     * Joins tokens from the specified starting index onward into a single string
     * separated by spaces.
     *
     * @param line the list of tokens
     * @param from the starting index (inclusive)
     * @return the concatenated string
     */
    private String getWordsFrom(List<String> line, int from) {
        String s = "";
        for (int i = from; i < line.size(); i++) {
            s += (i != 1 ? " " : "") + line.get(i);
        }
        return s;
    }

    /**
     * Checks whether the token at the given index contains the specified word
     * (case-insensitive).
     *
     * @param line  the list of tokens
     * @param index the index to check
     * @param word  the word to search for
     * @return {@code true} if the token contains the word, {@code false} otherwise
     */
    private boolean containAtIndex(List<String> line, int index, String word) {
        return line.get(index).toLowerCase().contains(word.toLowerCase());
    }

    /**
     * Finds the first token in the line that appears to be an HTTP/HTTPS URL.
     *
     * @param line the list of tokens
     * @return the URL string, or {@code null} if none is found
     */
    private String findFirstUrl(List<String> line) {
        for (String word : line) {
            if (word.contains("http://") || word.contains("https://")) {
                return word;
            }
        }
        return null;
    }

}