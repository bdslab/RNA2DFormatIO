package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.exception.RnaParsingException;

/**
 * Class that contains all data related to an RNA chain.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaChain {

    private int chainId;
    private Map<Integer, RnaRibonucleotide> chain;
    private Map<Integer, Integer> pairs;
    private List<String[]> tertiaryPairs;
    private int maxReference = 0;

    /**
     * Constructs an RNA chain with the specified identifier.
     *
     * @param chainId the unique identifier for this chain
     */
    public RnaChain(int chainId) {
        this.chainId = chainId;
        this.chain = new HashMap<>();
        this.pairs = new HashMap<>();
        this.tertiaryPairs = new ArrayList<>();
    }

    /**
     * Returns the identifier of this chain.
     *
     * @return the chain identifier
     */
    public int getchainId() {
        return this.chainId;
    }

    /**
     * Returns the maximum reference position encountered in this chain.
     *
     * @return the maximum reference position
     */
    public int getMaxReference() {
        return maxReference;
    }

    /**
     * Adds a ribonucleotide to the chain based on its character representation.
     *
     * @param c the character representing the RNA base
     * @throws RnaParsingException if the character cannot be recognized or the position is invalid
     */
    public void addRibonucleotide(char c) throws RnaParsingException {
        int pos = getLength() + 1;
        try {
            RnaBase base = RnaBase.getBase(c);
            RnaRibonucleotide obj = new RnaRibonucleotide(this.chainId, pos, base);
            this.chain.put(pos, obj);
        } catch (Exception e) {
            throwException(pos);
        }
    }

    /**
     * Adds a base pair between two positions using default Watson-Crick edge types.
     *
     * @param first  the first position in the pair
     * @param second the second position in the pair
     * @throws RnaParsingException if the positions are invalid or a tertiary pair is formed
     */
    public void addPair(int first, int second) throws RnaParsingException {
        addPair(first, second, "W", "W");
    }

    /**
     * Adds a base pair between two positions with specified edge types.
     *
     * @param first  the first position in the pair
     * @param second the second position in the pair
     * @param bt1    the edge type for the first base (e.g., "W" for Watson-Crick)
     * @param bt2    the edge type for the second base
     * @throws RnaParsingException if the positions are invalid or a tertiary pair is formed
     */
    public void addPair(int first, int second, String bt1, String bt2) throws RnaParsingException {
        if (first < 1)
            throwException(first);
        if (first == second || second < 1)
            throwException(second);
        if (this.pairs.containsKey(first) || this.pairs.containsKey(second)) {
            addTertiaryPair(first, second, true, bt1, bt2);
        } else {
            pairs.put(first, second);
            pairs.put(second, first);
        }
    }

    /**
     * Adds a tertiary base pair with detailed annotation.
     *
     * @param first  the first position in the pair
     * @param second the second position in the pair
     * @param isCis  true if the interaction is cis, false if trans
     * @param bt1    the edge type for the first base
     * @param bt2    the edge type for the second base
     * @throws RnaParsingException if the positions are invalid
     */
    public void addTertiaryPair(int first, int second, boolean isCis, String bt1, String bt2) throws RnaParsingException {
        if (first < 1)
            throwException(first);
        if (first == second || second < 1)
            throwException(second);
        String firstPair = getBaseOf(first) + first;
        String secondPair = getBaseOf(second) + second;
        String canon = isCanonical(first, second) ? "Canonical-Pair" : "Not-Canonical-Pair";
        String bond = getBond(bt1, bt2, isCis);
        tertiaryPairs.add(new String[]{firstPair, secondPair, canon, bond});
    }

    private String getBond(String bt1, String bt2, boolean isCis) {
        return getLate(bt1) + "-" + getLate(bt2) + (isCis ? " cis" : " trans");
    }

    private String getLate(String letter) {
        return switch (letter) {
            case "H" -> "Hoogsteen";
            case "S" -> "Sugar";
            case "W" -> "Watson-Crick";
            case "+" -> "Watson-Crick";
            case "-" -> "Watson-Crick";
            default -> "???";
        };
    }

    /**
     * Returns the single-letter code of the base at the specified position.
     *
     * @param index the position in the chain (1-based)
     * @return the character representing the base, or "N" if not found
     */
    public String getBaseOf(int index) {
        RnaRibonucleotide ribo = this.chain.get(index);
        return ribo == null ? "N" : "" + RnaBase.getBaseLetter(ribo.getBase());
    }

    /**
     * Checks whether the pair formed by two positions is a canonical Watson-Crick pair.
     *
     * @param i1 the first position
     * @param i2 the second position
     * @return true if the pair is canonical, false otherwise or if positions are invalid
     */
    public boolean isCanonical(int i1, int i2) {
        RnaRibonucleotide ribo1 = this.chain.get(i1);
        RnaRibonucleotide ribo2 = this.chain.get(i2);
        if (ribo1 == null || ribo2 == null)
            return false;
        return RnaBase.canonicalPair(ribo1.getBase(), ribo2.getBase());
    }

    /**
     * Returns the length of the RNA chain (number of nucleotides).
     *
     * @return the length of the chain
     */
    public int getLength() {
        return this.chain.size();
    }

    /**
     * Returns the nucleotide sequence as a string of single-letter codes.
     *
     * @return the RNA sequence
     */
    public String getSequence() {
        StringBuilder out = new StringBuilder();
        final int len = getLength();
        for (int i = 1; i <= len; i++) {
            out.append(this.chain.get(i));
        }
        return out.toString();
    }

    /**
     * Returns a map of the secondary structure base pairs.
     *
     * @return a map where each key is a position and its value is the paired position
     */
    public Map<Integer, Integer> getPairMap() {
        Map<Integer, Integer> map = new HashMap<>();
        for (Entry<Integer, Integer> pair : pairs.entrySet()) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    /**
     * Indicates whether tertiary structure data is present for this chain.
     *
     * @return true if at least one tertiary pair exists, false otherwise
     */
    public boolean haveTertiaryData() {
        return !this.tertiaryPairs.isEmpty();
    }

    /**
     * Returns a list of tertiary structure pairs as string arrays.
     * Each array contains: [first residue descriptor, second residue descriptor,
     * canonical status, bond type].
     *
     * @return a list of tertiary pair descriptions; an empty list if none exist
     */
    public List<String[]> getTertiaryStructure() {
        List<String[]> list = new ArrayList<>();
        if (!haveTertiaryData())
            return list;
        list.addAll(tertiaryPairs);
        return list;
    }

    private void throwException(int pos) throws RnaParsingException {
        throw new RnaParsingException(this.chainId, pos);
    }

}