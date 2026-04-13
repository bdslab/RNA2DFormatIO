package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.comparator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.OperationResult;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaBase;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Service for comparing and analyzing two RNA files.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaComparator {
    /**
     * Result of the analysis, returned at the end of the process.
     */
    private OperationResult result;
    /**
     * Helper counter used internally by the methods.
     */
    private int molN;

    /**
     * Entry point to start the comparison service.
     *
     * @param data1 data from the first file
     * @param data2 data from the second file
     * @return result of the analysis, including details of any differences
     */
    public OperationResult areEquals(RnaMolecule data1, RnaMolecule data2) {
        this.result = new OperationResult();
        if (data1 == null || data2 == null) {
            result.addInfo("Failure to load data.");
            return result;
        }
        result.result = compareChain(data1.getchains(), data2.getchains());
        if (result.result)
            result.addInfo("RNAs are the same.");
        return result;
    }

    /**
     * Compares the primary and secondary structures of two lists of chains.
     *
     * @param chains1 first list of chains
     * @param chains2 second list of chains
     * @return true if they contain the same structures, false otherwise
     */
    private boolean compareChain(List<RnaChain> chains1, List<RnaChain> chains2) {
        boolean check = comparechainNumber(chains1, chains2);
        for (molN = 0; check && molN < chains1.size(); molN++) {
            String sequence1 = chains1.get(molN).getSequence(),
                    sequence2 = chains2.get(molN).getSequence();
            Map<Integer, Integer> pairs1 = chains1.get(molN).getPairMap(),
                    pairs2 = chains2.get(molN).getPairMap();
            check = compareSequence(sequence1, sequence2)
                    && comparePairs(pairs1, pairs2, "second")
                    & comparePairs(pairs2, pairs1, "first");
        }
        return check;
    }

    /**
     * Checks whether the two lists have a different number of chains and logs the result.
     *
     * @param chains1 first list of chains
     * @param chains2 second list of chains
     * @return true if the number of chains is equal, false otherwise
     */
    private boolean comparechainNumber(List<RnaChain> chains1, List<RnaChain> chains2) {
        if (chains1.size() != chains2.size()) {
            result.addInfo("Different number of chains!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Compares the primary structures of two chains,
     * taking into account ambiguous nomenclature where one nucleotide
     * may represent another.
     *
     * @param sequence1 first sequence
     * @param sequence2 second sequence
     * @return true if the sequences may correspond, false otherwise
     */
    private boolean compareSequence(String sequence1, String sequence2) {
        if (!RnaBase.maybeEquals(sequence1, sequence2)) {
            result.addInfo("Sequences not corresponding to chain n." + molN);
            if (sequence1.length() != sequence2.length()) {
                result.addInfo("The first sequence is long: " + sequence1.length());
                result.addInfo("The second sequence is long: " + sequence2.length());
            } else
                findDifferentBase(sequence1, sequence2);
            return false;
        }
        return true;
    }

    /**
     * Helper method that appends detailed difference information to a negative result.
     *
     * @param sequence1 first sequence
     * @param sequence2 second sequence
     */
    private void findDifferentBase(String sequence1, String sequence2) {
        for (int j = 0; j < sequence1.length(); j++) {
            char a = sequence1.charAt(j), b = sequence2.charAt(j);
            if (a != b) {
                result.addInfo("In position " + j + " in the first file there is <"
                        + a + "> and in the second file there is <" + b + ">.");
            }
        }
    }

    /**
     * Checks whether every pair in the first map is present in the second map.
     * The method is called twice to perform the reciprocal check as well.
     *
     * @param pairs1 first map of pairs
     * @param pairs2 second map of pairs
     * @param focus  indicates which file is the current focus for the log message ("first" or "second")
     * @return true if all pairs match, false otherwise
     */
    private boolean comparePairs(Map<Integer, Integer> pairs1, Map<Integer, Integer> pairs2, String focus) {
        boolean check = true;
        for (Entry<Integer, Integer> pair : pairs1.entrySet()) {
            if (!(pair.getValue().equals(pairs2.get(pair.getKey()))
                    || pair.getKey().equals(pairs2.get(pair.getValue())))) {
                result.addInfo("The " + pair.getKey() + " - " + pair.getValue()
                        + " pair is not present in chain n." + molN + " of the " + focus + " file.");
                result.addInfo(pairs2.get(pair.getKey()) + "!=" + pair.getValue());
                check = false;
            }
        }
        return check;
    }

}