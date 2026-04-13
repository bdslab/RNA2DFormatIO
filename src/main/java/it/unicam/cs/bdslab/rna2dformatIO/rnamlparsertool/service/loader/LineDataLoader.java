package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import java.util.List;
import java.util.ArrayList;

/**
 * Abstract class containing utility methods for loaders that handle
 * line-based data formats.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class LineDataLoader extends TextDataLoader {

    /**
     * Finds all line indices that contain a nucleotide sequence.
     *
     * @param lines the list of line tokens to examine
     * @return a list of indices where sequences are located
     */
    protected List<Integer> getSequencePositions(List<List<String>> lines) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).size() == 1 && isSequence(lines.get(i).get(0)))
                list.add(i);
        }
        return list;
    }

    /**
     * Checks whether a given word represents a valid nucleotide sequence.
     *
     * @param word the word to check
     * @return {@code true} if the word is a sequence, {@code false} otherwise
     */
    private boolean isSequence(String word) {
        if (word == null || word.length() == 0)
            return false;
        word = word.toUpperCase();
        for (char letter : word.toCharArray()) {
            if (isNotSequenceLetter(letter))
                return false;
        }
        return true;
    }

    /**
     * Verifies whether a character can be part of a nucleotide sequence.
     *
     * @param letter the character to check
     * @return {@code true} if the character is invalid, {@code false} otherwise
     */
    private boolean isNotSequenceLetter(char letter) {
        String l = ("" + letter).toUpperCase();
        return !"ACUGNWRMYKSBVDH".contains(l);
    }

}