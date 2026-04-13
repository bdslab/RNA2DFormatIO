package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Class responsible for saving data in AAS format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class AasFileWriter extends TextFileWriter {

    /**
     * Writes the given RNA molecule data to the specified file in AAS format.
     *
     * @param chains the RNA molecule containing the data to write
     * @param path   the destination file path
     * @return {@code true} if the file was successfully written, {@code false} otherwise
     */
    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        setFileInfo(chains);
        data += chains.getchains().stream().map(x -> x.getSequence()).reduce("", (a, b) -> a + b) + "\n";
        chains.getchains().stream().forEach(m -> writechain(m));
        return save(path);
    }

    /**
     * Appends the secondary structure data of a single chain to the output buffer
     * in AAS format (comma-separated parenthesized pairs).
     *
     * @param m the RNA chain to write
     */
    private void writechain(RnaChain m) {
        List<Entry<Integer, Integer>> list = m.getPairMap().entrySet().stream()
                .map(x -> x.getKey() < x.getValue() ? x : new SimpleEntry<>(x.getValue(), x.getKey()))
                .distinct().toList();
        for (int i = 0; i < list.size() - 1; i++) {
            data += "(" + list.get(i).getKey() + "," + list.get(i).getValue() + ");";
        }
        if (list.size() != 0)
            data += "(" + list.get(list.size() - 1).getKey() + "," + list.get(list.size() - 1).getValue() + ")";
    }

}