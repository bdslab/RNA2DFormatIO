package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaFileWriter;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * Abstract class providing utility methods for writing data
 * in plain-text-like formats.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class TextFileWriter implements RnaFileWriter {

    /**
     * Buffer that accumulates the data to be written to the file.
     */
    protected String data = "";

    /**
     * Saves the previously accumulated data to the specified file path,
     * overwriting any existing content at that location.
     *
     * @param path the destination file path
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     */
    protected boolean save(String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Appends optional RNA metadata to the output buffer, if present.
     * This includes organism name, accession number, and reference link.
     *
     * @param rnaData the RNA molecule containing the metadata
     */
    protected void setFileInfo(RnaMolecule rnaData) {
        if (rnaData.getOrganism() != null) {
            data += "# Organism: " + rnaData.getOrganism() + "\n";
        }
        if (rnaData.getAccessionNumber() != null) {
            data += "# Accession Number: " + rnaData.getAccessionNumber() + "\n";
        }
        if (rnaData.getReferenceLink() != null) {
            data += "# Citation and related information available at " + rnaData.getReferenceLink() + "\n";
        }
    }

}