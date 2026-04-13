package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaFileWriter;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * Class responsible for writing tertiary structure data to a CSV file.
 * The output includes columns for the two interacting residues, whether the
 * pair is canonical, the bond type, and the cis/trans orientation.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class TertiaryStructureWriter implements RnaFileWriter {

    /**
     * Writes the tertiary structure information of the given RNA molecule
     * to the specified file in CSV format.
     *
     * @param chains the RNA molecule containing the tertiary data to write
     * @param path   the destination file path
     * @return {@code true} if the file was successfully written, {@code false} otherwise
     */
    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        try (FileWriter fw = new FileWriter(path, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("base-id-5p base-id-3p canonical-bond bond-type cis-or-trans");
            for (String[] pair : chains.getTertiaryStructure()) {
                String line = pair[0];
                for (int i = 1; i < pair.length; i++) {
                    line += " " + pair[i];
                }
                out.println(line);
            }
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}