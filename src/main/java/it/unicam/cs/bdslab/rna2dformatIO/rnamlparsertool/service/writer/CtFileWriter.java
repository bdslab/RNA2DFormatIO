package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.util.Map;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Class responsible for writing data in CT (Connect) format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class CtFileWriter extends TextFileWriter {

    private int count = 1;

    /**
     * Writes the given RNA molecule data to the specified file in CT format.
     *
     * @param chains the RNA molecule containing the data to write
     * @param path   the destination file path
     * @return {@code true} if the file was successfully written, {@code false} otherwise
     */
    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        setFileInfo(chains);
        data += chains.getchains().stream().map(RnaChain::getLength).reduce(0, (a, b) -> a + b) + " Energy = 0\n";
        chains.getchains().forEach(this::writechain);
        return save(path);
    }

    /**
     * Appends the data of a single chain to the output buffer in CT format.
     *
     * @param m the RNA chain to write
     */
    private void writechain(RnaChain m) {
        char[] array = m.getSequence().toCharArray();
        Map<Integer, Integer> pairs = m.getPairMap();
        for (int i = 1; i <= array.length; i++) {
            int pair = pairs.getOrDefault(i, -1);
            data += count + " " + array[i - 1] + " "
                    + (i - 1) + " " + (i + 1)
                    + " " + (pair == -1 ? "0" : pair) + " " + count++ + "\n";
        }
    }

}