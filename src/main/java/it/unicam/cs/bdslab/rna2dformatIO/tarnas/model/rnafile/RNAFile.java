package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A representation of a file that contains an RNA secondary structure.<br>
 * An {@code RNAFile} stores:
 * <ul>
 *   <li>{@code fileName}<br>
 *       The name of this file, including its extension.
 *   </li>
 *   <li>{@code header}<br>
 *       The header lines of this file.
 *   </li>
 *   <li>{@code structure}<br>
 *       The represented {@link RNASecondaryStructure} in this file.
 *   </li>
 *   <li>{@code format}<br>
 *       The {@link RNAFormat} of this file.
 *   </li>
 * </ul>
 * The file extension included in the name is not used to determine the format;
 * the actual format is stored in the {@link RNAFormat} field.
 * The {@code fileName} field stores only the name (including extension) of this
 * {@code RNAFile}; it does not represent the full {@link java.nio.file.Path} to the file.
 *
 * @author Piero Hierro, Piermichele Rosati
 * @see RNASecondaryStructure
 * @see RNAFormat
 */
public class RNAFile {

    private String fileName;
    private final List<String> header;
    private final RNASecondaryStructure structure;
    private RNAFormat format;
    private final List<String> body;
    private final List<String> content;

    /**
     * Creates an {@code RNAFile} with the specified file name, header, body,
     * secondary structure, and format.
     *
     * @param fileName  the name of this file (including extension)
     * @param header    the header lines of this file
     * @param body      the body lines containing the structural data
     * @param structure the {@link RNASecondaryStructure} represented by this file
     * @param format    the {@link RNAFormat} of this file
     */
    public RNAFile(String fileName, List<String> header, List<String> body, RNASecondaryStructure structure, RNAFormat format) {
        this.fileName = fileName;
        this.header = header;
        this.body = body;
        this.structure = structure;
        this.format = format;
        this.content = Stream.concat(this.header.stream(), this.body.stream()).toList();
    }

    /**
     * Returns the name of this file, including its extension.
     *
     * @return the file name
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the name of this file.
     *
     * @param fileName the new file name (including extension)
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the header lines of this file.
     *
     * @return the list of header strings
     */
    public List<String> getHeader() {
        return this.header;
    }

    /**
     * Returns the body lines of this file, which contain the actual structural data.
     *
     * @return the list of body strings
     */
    public List<String> getBody() {
        return this.body;
    }

    /**
     * Returns the {@link RNASecondaryStructure} represented by this file.
     *
     * @return the secondary structure
     */
    public RNASecondaryStructure getStructure() {
        return this.structure;
    }

    /**
     * Returns the {@link RNAFormat} of this file.
     *
     * @return the RNA format
     */
    public RNAFormat getFormat() {
        return this.format;
    }

    /**
     * Returns the complete content of this file as a concatenation of header and body lines.
     *
     * @return the full list of content strings
     */
    public List<String> getContent() {
        return this.content;
    }

    /**
     * Sets the {@link RNAFormat} for this file.
     *
     * @param format the new RNA format
     */
    public void setFormat(RNAFormat format) {
        this.format = format;
    }

    /**
     * Compares this {@code RNAFile} with another object for equality.
     * Two RNA files are considered equal if they have the same file name,
     * header, structure, format, body, and content.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RNAFile rnaFile)) return false;
        return getFileName().equals(rnaFile.getFileName()) &&
                getHeader().equals(rnaFile.getHeader()) &&
                getStructure().equals(rnaFile.getStructure()) &&
                getFormat().equals(rnaFile.getFormat()) &&
                getBody().equals(rnaFile.getBody()) &&
                getContent().equals(rnaFile.getContent());
    }

    /**
     * Returns a hash code value for this {@code RNAFile}.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFileName(), getHeader(), getStructure(), getFormat(), getBody(), getContent());
    }

    /**
     * Returns a string representation of this {@code RNAFile}.
     *
     * @return a descriptive string
     */
    @Override
    public String toString() {
        return "RNAFile{" +
                "fileName='" + fileName + '\'' +
                ", header=" + header +
                ", structure=" + structure +
                ", format=" + format +
                ", body=" + body +
                ", content=" + content +
                '}';
    }
}