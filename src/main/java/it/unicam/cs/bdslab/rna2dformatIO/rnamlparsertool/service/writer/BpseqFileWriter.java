package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.util.Map;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Classe per scrivere dai dati in formato BPSEQ
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class BpseqFileWriter extends TextFileWriter {

    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        data = "";
        setFileInfo(chains);
        chains.getchains().forEach(this::writechain);
        return save(path);
    }

    /**
     * Scrive i dati contenuti nella catena nel formato BPSEQ
     *
     * @param m catena da scrivere
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