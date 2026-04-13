package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility.DotBracketSequenceGenerator;

/**
 * Class responsible for writing data in DB (Dot-Bracket) format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class DbFileWriter extends TextFileWriter {

    /**
     * Generator for the secondary structure dot-bracket notation.
     */
    private final DotBracketSequenceGenerator sequenceGenerator = new DotBracketSequenceGenerator();

    /**
     * Writes the given RNA molecule data to the specified file in DB format.
     *
     * @param chains the RNA molecule containing the data to write
     * @param path   the destination file path
     * @return {@code true} if the file was successfully written, {@code false} otherwise
     */
    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        data = "";
        setFileInfo(chains);
        chains.getchains().stream().forEach(m -> data += m.getSequence());
        data += "\n" + sequenceGenerator.writeSequence(chains);
        return save(path);
    }

}