package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * Class with the responsibility of saving data
 * to a specified file, which may be created or overwritten.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public interface RnaFileWriter {

    /**
     * Method for writing a new file containing the information.
     *
     * @param chains the information to be written
     * @param path   the file in which to write the information
     * @return true if the file writing was successful, false otherwise
     */
    boolean writeAndSave(RnaMolecule chains, String path);

}