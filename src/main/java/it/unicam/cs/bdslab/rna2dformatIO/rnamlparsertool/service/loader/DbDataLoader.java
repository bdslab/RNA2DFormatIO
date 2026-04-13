package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import java.util.List;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.exception.RnaParsingException;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility.DotBracketSequenceAnalyzator;

/**
 * Class responsible for loading data contained in a DB (Dot-Bracket) format file.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class DbDataLoader extends LineDataLoader {

    private RnaMolecule data;

    /**
     * Analyzer for parsing dot-bracket secondary structure notation.
     */
    private final DotBracketSequenceAnalyzator analyzator = new DotBracketSequenceAnalyzator();

    /**
     * Loads the RNA data from the specified DB file path.
     *
     * @param path the path to the DB file
     * @return an {@link RnaMolecule} object containing the parsed data,
     *         or {@code null} if loading fails
     */
    @Override
    public RnaMolecule getData(String path) {
        this.data = new RnaMolecule();
        List<List<String>> lines = getLines(path);
        if (lines == null || lines.isEmpty())
            return null;
        setFileInfo(data, lines);
        List<Integer> starts = getSequencePositions(lines);
        if (starts.isEmpty())
            return null;
        for (int i = 0; i < starts.size(); i++) {
            String sequence = lines.get(starts.get(i)).get(0);
            String pairs = lines.get(starts.get(i) + 1).get(0);
            RnaChain chain = getchain(i, sequence, pairs);
            if (chain == null) {
                return null;
            }
            data.addchain(chain);
        }
        try {
            data.checkSecondaryStructure();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds an RNA chain from a given sequence and dot-bracket notation string.
     *
     * @param index    the chain number (starting from 0)
     * @param sequence the primary sequence string
     * @param pairs    the dot-bracket secondary structure string
     * @return the constructed {@link RnaChain}, or {@code null} if a parsing error occurs
     */
    private RnaChain getchain(int index, String sequence, String pairs) {
        try {
            RnaChain chain = new RnaChain(index);
            for (char letter : sequence.toCharArray()) {
                chain.addRibonucleotide(letter);
            }
            List<Integer[]> pairData = analyzator.getPairs(pairs);
            for (Integer[] pair : pairData) {
                chain.addPair(pair[0], pair[1]);
            }
            return chain;
        } catch (RnaParsingException e) {
            e.printStackTrace();
            return null;
        }
    }

}