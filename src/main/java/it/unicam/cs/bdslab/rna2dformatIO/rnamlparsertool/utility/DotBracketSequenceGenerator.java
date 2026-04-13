package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.DbPair;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * Class for generating Dot-Bracket (DB) secondary structure notation sequences.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class DotBracketSequenceGenerator extends DotBracketTranslator {

    /**
     * Internal array representing the final DB sequence to be written.
     */
    private int[] array;

    /**
     * Generates the Dot-Bracket sequence for the given RNA molecule.
     *
     * @param chain the RNA molecule to analyze
     * @return the Dot-Bracket notation string representing the secondary structure
     */
    public String writeSequence(RnaMolecule chain) {
        if (chain != null) {
            this.array = new int[chain.getLength()];
            analyze(chain);
        }
        String data = "";
        for (int i : array) {
            data += getDbBracket(i);
        }
        return data;
    }

    /**
     * Analyzes the RNA molecule to determine the secondary structure encoding.
     *
     * @param chain the RNA molecule to analyze
     */
    private void analyze(RnaMolecule chain) {
        List<DbPair> pairs = chain.getchains().stream()
                .flatMap(x -> x.getPairMap().entrySet().stream())
                .map(x -> x.getKey() < x.getValue() ? x :
                        new SimpleEntry<Integer, Integer>(x.getValue(), x.getKey()))
                .distinct()
                .map(x -> new DbPair(x.getKey(), x.getValue()))
                .toList();
        pairs = sortPairsByStartPoint(pairs);
        setPairsOrder(pairs);
        encodeBasePairs(pairs);
    }

    /**
     * Sorts base pairs by their left (start) position.
     *
     * @param pairs the list of pairs to sort
     * @return a new list sorted by left position
     */
    private List<DbPair> sortPairsByStartPoint(List<DbPair> pairs) {
        List<DbPair> tmp = new ArrayList<>(pairs);
        tmp.sort(Comparator.comparingInt(r -> r.getLeft()));
        return tmp;
    }

    /**
     * Assigns an order level to each base pair, resolving conflicts for
     * proper bracket nesting representation.
     *
     * @param pairs the list of base pairs (already sorted by start position)
     */
    private void setPairsOrder(List<DbPair> pairs) {
        if (pairs.size() < 2) return;
        pairs.get(0).setOrder(0);
        for (int i = 1; i < pairs.size(); i++) {
            int globalOrder = 0;
            for (int j = 0; j <= i - 1; j++) {
                if (pairs.get(j).getOrder() == globalOrder && arePairsConflicting(pairs.get(i), pairs.get(j))) {
                    globalOrder += 1;
                    j = 0;
                }
            }
            pairs.get(i).setOrder(globalOrder);
        }
    }

    /**
     * Determines whether two base pairs are conflicting (i.e., they form a pseudoknot).
     *
     * @param p1 the first pair
     * @param p2 the second pair
     * @return {@code true} if the pairs conflict, {@code false} otherwise
     */
    private boolean arePairsConflicting(DbPair p1, DbPair p2) {
        boolean firstCase = p1.getLeft() < p2.getLeft() && p1.getRight() > p2.getLeft() && p2.getRight() > p1.getRight();
        boolean secondCase = p2.getLeft() < p1.getLeft() && p2.getRight() > p1.getLeft() && p1.getRight() > p2.getRight();
        return firstCase || secondCase;
    }

    /**
     * Encodes the base pairs into the internal array using the assigned order levels.
     *
     * @param pairs the list of base pairs with assigned orders
     */
    private void encodeBasePairs(List<DbPair> pairs) {
        for (DbPair p : pairs) {
            array[p.getLeft() - 1] = 1 + (p.getOrder() * 2);
            array[p.getRight() - 1] = 2 + (p.getOrder() * 2);
        }
    }

}