package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.util.Map;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Class responsible for writing data in BPSEQ format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class BpseqFileWriter extends TextFileWriter {

    /**
     * Writes the given RNA molecule data to the specified file in BPSEQ format.
     *
     * @param chains the RNA molecule containing the data to write
     * @param path   the destination file path
     * @return {@code true} if the file was successfully written, {@code false} otherwise
     */
    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        data = "";
        setFileInfo(chains);
        chains.getchains().forEach(this::writechain);
        return save(path);
    }

    /**
     * Appends the data of a single chain to the output buffer in BPSEQ format.
     *
     * @param m the RNA chain to write
     */
    private void writechain(RnaChain m) {
        char[] array = m.getSequence().toCharArray();
        Map<Integer, Integer> pairs = m.getPairMap();
        for (int i = 1; i <= array.length; i++) {
            int pair = pairs.getOrDefault(i, -1);
            data += i + " " + array[i - 1] + " "
                    + (pair == -1 ? "0" : pair) + "\n";
        }
    }

}