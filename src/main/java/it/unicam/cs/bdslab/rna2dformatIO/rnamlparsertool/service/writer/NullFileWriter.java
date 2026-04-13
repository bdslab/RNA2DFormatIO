package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaFileWriter;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * A symbolic class indicating that no suitable file writer
 * was found for the requested format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class NullFileWriter implements RnaFileWriter {

    /**
     * Always returns {@code false}, as no data can be written.
     *
     * @param chains the RNA molecule containing the data (ignored)
     * @param path   the destination file path (ignored)
     * @return {@code false}
     */
    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        return false;
    }

}