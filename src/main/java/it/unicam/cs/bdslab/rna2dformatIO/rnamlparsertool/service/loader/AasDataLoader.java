package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import java.util.Arrays;
import java.util.List;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.exception.RnaParsingException;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Class responsible for loading data contained in an AAS format file.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class AasDataLoader extends LineDataLoader {

    /**
     * Data object that stores the loaded RNA molecule information.
     */
    private RnaMolecule data;

    /**
     * Loads the RNA data from the specified AAS file path.
     *
     * @param path the path to the AAS file
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
            String pairs = lines.size() > starts.get(i) + 1 && lines.get(starts.get(i) + 1).size() == 1 ?
                    lines.get(starts.get(i) + 1).get(0) : "";
            RnaChain chain = getchain(i + 1, sequence, pairs);
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
     * Builds an RNA chain from a given sequence, index, and pair definition string.
     *
     * @param index    the chain number (starting from 1)
     * @param sequence the nucleotide sequence string
     * @param pairs    the secondary structure pairs definition string (e.g., "(1,10);(2,9)")
     * @return the constructed {@link RnaChain}, or {@code null} if a parsing error occurs
     */
    private RnaChain getchain(int index, String sequence, String pairs) {
        try {
            RnaChain chain = new RnaChain(index);
            for (char letter : sequence.toCharArray()) {
                chain.addRibonucleotide(letter);
            }
            if (!(pairs == null || pairs.equals(""))) {
                for (String pair : pairs.split(";")) {
                    if (pair.length() > 4) {
                        List<Integer> positions = Arrays.asList(pair.replace("(", "")
                                        .replace(")", "").split(","))
                                .stream().map(s -> Integer.parseInt(s)).toList();
                        chain.addPair(positions.get(0), positions.get(1));
                    }
                }
            }
            return chain;
        } catch (RnaParsingException e) {
            e.printStackTrace();
            return null;
        }
    }

}