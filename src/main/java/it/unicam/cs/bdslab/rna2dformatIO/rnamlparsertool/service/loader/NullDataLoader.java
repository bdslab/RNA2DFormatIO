package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaDataLoader;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * A symbolic class indicating that no suitable data loader
 * was found for the specified format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class NullDataLoader implements RnaDataLoader {

    /**
     * Always returns {@code null}, as no data can be loaded.
     *
     * @param path the file path (ignored)
     * @return {@code null}
     */
    @Override
    public RnaMolecule getData(String path) {
        return null;
    }

}