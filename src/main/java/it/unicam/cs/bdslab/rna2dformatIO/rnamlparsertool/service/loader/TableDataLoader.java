package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Abstract class for loading data from files where values are organized in a table (matrix) format.
 * Concrete implementations must provide appropriate values for the protected integer parameters
 * during construction.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class TableDataLoader extends TextDataLoader {

    /**
     * Data object that stores the loaded RNA molecule information.
     */
    private RnaMolecule data;

    /**
     * The expected number of columns in each row of the table.
     */
    protected int dimension;

    /**
     * The column index (0-based) containing the first position of a base pair.
     */
    protected int pairOnePosition;

    /**
     * The column index (0-based) containing the second (paired) position.
     */
    protected int pairTwoPosition;

    /**
     * The column index (0-based) containing the nucleotide character.
     */
    protected int basePosition;

    /**
     * Loads the RNA data from the specified table-format file path.
     *
     * @param path the path to the file
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
        List<Integer> starts = getStartLines(lines);
        if (starts.isEmpty())
            return null;
        for (int i = 0; i < starts.size(); i++) {
            int until = i + 1 == starts.size() ? lines.size() : starts.get(i + 1);
            List<List<String>> chainList = lines.subList(starts.get(i), until).stream()
                    .filter(l -> l.size() == dimension).toList();
            RnaChain chain = getchain(chainList, new RnaChain(i + 1));
            if (chain == null)
                return null;
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
     * Identifies the line indices where a new chain begins.
     * A new chain is assumed to start where the first column contains the value "1".
     *
     * @param lines the list of tokenized lines
     * @return a list of starting indices
     */
    private List<Integer> getStartLines(List<List<String>> lines) {
        List<Integer> starts = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).get(0).equals("1")) {
                starts.add(i);
            }
        }
        return starts;
    }

    /**
     * Populates an RNA chain with nucleotide and base pair data from a list of table rows.
     *
     * @param lines the table rows belonging to this chain
     * @param chain the {@link RnaChain} to populate
     * @return the populated chain, or {@code null} if an error occurs
     */
    private RnaChain getchain(List<List<String>> lines, RnaChain chain) {
        try {
            for (List<String> line : lines) {
                chain.addRibonucleotide(line.get(basePosition).charAt(0));
            }
            List<Integer> toSkip = new ArrayList<>();
            for (List<String> line : lines) {
                int pair1 = Integer.parseInt(line.get(pairOnePosition)),
                        pair2 = Integer.parseInt(line.get(pairTwoPosition));
                if (!(pair2 == 0 || toSkip.contains(pair1))) {
                    chain.addPair(pair1, pair2);
                    toSkip.add(pair2);
                }
            }
            return chain;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}