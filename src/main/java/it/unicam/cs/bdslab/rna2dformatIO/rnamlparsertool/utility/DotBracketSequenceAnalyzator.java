package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;

/**
 * Utility class for analyzing a Dot-Bracket (DB) notation sequence.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class DotBracketSequenceAnalyzator extends DotBracketTranslator {

    /**
     * Analyzes a Dot-Bracket sequence and extracts the list of base pairs.
     *
     * @param data the Dot-Bracket sequence string to analyze
     * @return a list of base pairs, where each pair is represented as a two-element
     *         array containing the 1-based positions of the paired nucleotides.
     *         Returns {@code null} if the sequence contains unbalanced brackets.
     */
    public List<Integer[]> getPairs(String data) {
        List<Integer[]> pairs = new ArrayList<>();
        int[] symbols = data.chars().map(c -> getDbNumber((char) c)).toArray();
        int max = 0;
        for (int symbol : symbols) {
            if (symbol > max)
                max = symbol;
        }
        if (max % 2 == 1)
            return null;
        Stack<Integer> stack = new Stack<>();
        for (int i = 1; i < max; i += 2) {
            for (int j = 0; j < symbols.length; j++) {
                if (symbols[j] == i) {
                    stack.push(j);
                } else if (symbols[j] == i + 1) {
                    int p = stack.pop();
                    pairs.add(new Integer[]{p + 1, j + 1});
                }
            }
            if (stack.size() != 0) {
                return null;
            }
        }
        return pairs;
    }

}