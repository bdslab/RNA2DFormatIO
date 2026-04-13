package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * Interface that defines the responsibility of obtaining data
 * from a file located at a given path.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public interface RnaDataLoader {

    /**
     * Method to obtain the data from a file.
     *
     * @param path file path
     * @return the RnaMolecule object containing all loaded data
     */
    RnaMolecule getData(String path);

}