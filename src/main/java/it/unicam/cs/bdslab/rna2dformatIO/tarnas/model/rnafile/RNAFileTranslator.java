/*
 * Copyright 2026 Francesco Palozzi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile;

import static it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat.*;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.utils.Region;
import java.util.*;

/**
 * A representation of an RNA files translator.<br>
 * This class consists exclusively of static methods that operate on RNA secondary structure format translations.<br>
 * Every static method has only an {@link RNAFile} parameter and translates it to the destination {@link RNAFormat}.<br>
 * The output of every static method is a {@link RNAFile}.
 *
 * @author Piero Hierro, Piermichele Rosati
 * @see RNAFile
 * @see RNAFormat
 */
public class RNAFileTranslator {

    /**
     * Translates an {@link RNAFile} of any {@link RNAFormat} to an {@link RNAFile} with {@link RNAFormat#DB} format.
     *
     * @param rnaFile the {@code RNAFile} to translate to the {@link RNAFormat#DB} format
     * @return the translated {@code RNAFile} in {@link RNAFormat#DB} format
     */
    public static RNAFile translateToDB(RNAFile rnaFile) {
        // create DB header
        var header = createHeader(rnaFile.getHeader(), DB);
        // create DB body
        var body = createDBBody(rnaFile.getStructure(), true);
        // return a formatted rna file object
        return new RNAFile(
            getFileNameWithDstExtension(rnaFile.getFileName(), "db"),
            header,
            body,
            rnaFile.getStructure(),
            RNAFormat.DB
        );
    }

    /**
     * Translates an {@link RNAFile} of any {@link RNAFormat} to an {@link RNAFile} with {@link RNAFormat#DB_NO_SEQUENCE} format.
     *
     * @param rnaFile the {@code RNAFile} to translate to the {@link RNAFormat#DB_NO_SEQUENCE} format
     * @return the translated {@code RNAFile} in {@link RNAFormat#DB_NO_SEQUENCE} format
     */
    public static RNAFile translateToDBNoSequence(RNAFile rnaFile) {
        // create DB no sequence header
        var header = createHeader(rnaFile.getHeader(), DB_NO_SEQUENCE);
        // create DB no sequence body
        var body = createDBBody(rnaFile.getStructure(), false);
        // return a formatted rna file object
        return new RNAFile(
            getFileNameWithDstExtension(rnaFile.getFileName(), "db"),
            header,
            body,
            rnaFile.getStructure(),
            DB_NO_SEQUENCE
        );
    }

    /**
     * Translates an {@link RNAFile} of any {@link RNAFormat} to an {@link RNAFile} with {@link RNAFormat#BPSEQ} format.
     *
     * @param rnaFile the {@code RNAFile} to translate to the {@link RNAFormat#BPSEQ} format
     * @return the translated {@code RNAFile} in {@link RNAFormat#BPSEQ} format
     */
    public static RNAFile translateToBPSEQ(RNAFile rnaFile) {
        // create BPSEQ header
        var header = createHeader(rnaFile.getHeader(), BPSEQ);
        // create BPSEQ body
        var body = createBPSEQBody(rnaFile.getStructure());
        // return a formatted rna file object
        return new RNAFile(
            getFileNameWithDstExtension(rnaFile.getFileName(), "bpseq"),
            header,
            body,
            rnaFile.getStructure(),
            BPSEQ
        );
    }

    /**
     * Translates an {@link RNAFile} of any {@link RNAFormat} to an {@link RNAFile} with {@link RNAFormat#CT} format.
     *
     * @param rnaFile the {@code RNAFile} to translate to the {@link RNAFormat#CT} format
     * @return the translated {@code RNAFile} in {@link RNAFormat#CT} format
     */
    public static RNAFile translateToCT(RNAFile rnaFile) {
        // create CT header
        var header = createHeader(rnaFile.getHeader(), CT);
        // create CT body
        var body = createCTBody(rnaFile.getStructure());
        // return a formatted rna file object
        return new RNAFile(
            getFileNameWithDstExtension(rnaFile.getFileName(), "ct"),
            header,
            body,
            rnaFile.getStructure(),
            CT
        );
    }

    /**
     * Translates an {@link RNAFile} of any {@link RNAFormat} to an {@link RNAFile} with {@link RNAFormat#AAS} format.
     *
     * @param rnaFile the {@code RNAFile} to translate to the {@link RNAFormat#AAS} format
     * @return the translated {@code RNAFile} in {@link RNAFormat#AAS} format
     */
    public static RNAFile translateToAAS(RNAFile rnaFile) {
        // create AAS header
        var header = createHeader(rnaFile.getHeader(), AAS);
        // create AAS body
        var body = createAASBody(rnaFile.getStructure(), true);
        // return a formatted rna file object
        return new RNAFile(
            getFileNameWithDstExtension(rnaFile.getFileName(), "aas"),
            header,
            body,
            rnaFile.getStructure(),
            RNAFormat.AAS
        );
    }

    /**
     * Translates an {@link RNAFile} of any {@link RNAFormat} to an {@link RNAFile} with {@link RNAFormat#AAS_NO_SEQUENCE} format.
     *
     * @param rnaFile the {@code RNAFile} to translate to the {@link RNAFormat#AAS_NO_SEQUENCE} format
     * @return the translated {@code RNAFile} in {@link RNAFormat#AAS_NO_SEQUENCE} format
     */
    public static RNAFile translateToAASNoSequence(RNAFile rnaFile) {
        // create ASS no sequence header
        var header = createHeader(rnaFile.getHeader(), AAS_NO_SEQUENCE);
        // create ASS no sequence body
        var body = createAASBody(rnaFile.getStructure(), false);
        // return a formatted rna file object
        var secondaryStructureWithoutSequence = new RNASecondaryStructure();
        secondaryStructureWithoutSequence.setSize(rnaFile.getStructure().getSize());
        secondaryStructureWithoutSequence.setBonds(rnaFile.getStructure().getBonds());
        return new RNAFile(
            getFileNameWithDstExtension(rnaFile.getFileName(), "aas"),
            header,
            body,
            secondaryStructureWithoutSequence,
            RNAFormat.AAS_NO_SEQUENCE
        );
    }

    /**
     * Translates an {@link RNAFile} of any {@link RNAFormat} to an {@link RNAFile} with {@link RNAFormat#FASTA} format.
     *
     * @param rnaFile the {@code RNAFile} to translate to the {@link RNAFormat#FASTA} format
     * @return the translated {@code RNAFile} in {@link RNAFormat#FASTA} format
     */
    public static RNAFile translateToFASTA(RNAFile rnaFile) {
        var rnaSecondaryStructure = rnaFile.getStructure();
        // create FASTA header
        var header = createHeader(rnaFile.getHeader(), FASTA);
        // create FASTA body
        var body = List.of(rnaFile.getStructure().getSequence());
        // return a formatted rna file object
        var secondaryStructureOnlySize = new RNASecondaryStructure();
        secondaryStructureOnlySize.setSize(rnaSecondaryStructure.getSize());
        return new RNAFile(
            getFileNameWithDstExtension(rnaFile.getFileName(), "fasta"),
            header,
            body,
            secondaryStructureOnlySize,
            RNAFormat.FASTA
        );
    }

    /**
     * Creates the {@link RNAFormat#DB} body for the specified {@code rnaSecondaryStructure}.
     * If {@code addSequence} is {@code true}, adds the sequence to the body.
     *
     * @param rnaSecondaryStructure the {@link RNASecondaryStructure} from which to create the body
     * @param addSequence           whether to include the nucleotide sequence
     * @return the {@link RNAFormat#DB} body as a {@link List} of {@code String}
     */
    private static List<String> createDBBody(RNASecondaryStructure rnaSecondaryStructure, boolean addSequence) {
        var regs = findAllPairedRegions(rnaSecondaryStructure);
        var n = regs.size();
        regs = sortRegionsByStartPoint(regs);
        setRegionsOrder(regs, n);
        var structure = encodeBasePairs(regs, rnaSecondaryStructure.getSize());
        return addSequence ? List.of(rnaSecondaryStructure.getSequence(), structure) : List.of(structure);
    }

    /**
     * Creates the {@link RNAFormat#AAS} body for the specified {@code rnaSecondaryStructure}.
     * If {@code addSequence} is {@code true}, adds the sequence to the body.
     *
     * @param rnaSecondaryStructure the {@link RNASecondaryStructure} from which to create the body
     * @param addSequence           whether to include the nucleotide sequence
     * @return the {@link RNAFormat#AAS} body as a {@link List} of {@code String}
     */
    private static List<String> createAASBody(RNASecondaryStructure rnaSecondaryStructure, boolean addSequence) {
        var structure = new StringBuilder();
        for (var b : rnaSecondaryStructure.getBonds())
            structure.append("(").append(b.getLeft()).append(",").append(b.getRight()).append(");");
        // remove the character ';'
        if (structure.length() > 0) structure.setLength(structure.length() - 1);
        return addSequence
            ? List.of(rnaSecondaryStructure.getSequence(), structure.toString())
            : List.of(structure.toString());
    }

    /**
     * Creates the {@link RNAFormat#BPSEQ} body for the specified {@code rnaSecondaryStructure}.
     *
     * @param rnaSecondaryStructure the {@link RNASecondaryStructure} from which to create the body
     * @return the {@link RNAFormat#BPSEQ} body as a {@link List} of {@code String}
     */
    private static List<String> createBPSEQBody(RNASecondaryStructure rnaSecondaryStructure) {
        var body = new ArrayList<String>();
        // i=1 because bpseq indexes starts from 1
        for (int i = 1; i <= rnaSecondaryStructure.getSequence().length(); i++) {
            var line =
                i + " " + rnaSecondaryStructure.getSequence().charAt(i - 1) + " " + rnaSecondaryStructure.getP()[i];
            body.add(line);
        }
        return body;
    }

    /**
     * Creates the {@link RNAFormat#CT} body for the specified {@code rnaSecondaryStructure}.
     *
     * @param rnaSecondaryStructure the {@link RNASecondaryStructure} from which to create the body
     * @return the {@link RNAFormat#CT} body as a {@link List} of {@code String}
     */
    private static List<String> createCTBody(RNASecondaryStructure rnaSecondaryStructure) {
        var body = new ArrayList<String>();
        // add energy line to ct body
        body.add(rnaSecondaryStructure.getSize() + " dG = 0.00 [ initially 0.0 ]");
        var rnaSequence = rnaSecondaryStructure.getSequence();
        var p = rnaSecondaryStructure.getP();
        for (int i = 1; i <= rnaSecondaryStructure.getSize(); i++) {
            var line = i + " " + rnaSequence.charAt(i - 1) + " " + (i - 1) + " " + (i + 1) + " " + p[i] + " " + i;
            body.add(line);
        }
        return body;
    }

    /**
     * Creates the header for the file with the destination {@link RNAFormat}.
     * Reads every string in the original {@code header} and prepends the '#' symbol
     * if the destination format expects comment lines.
     *
     * @param header            the original list of header strings
     * @param destinationFormat the destination {@code RNAFormat}
     * @return the adjusted header for the destination format
     */
    private static List<String> createHeader(List<String> header, RNAFormat destinationFormat) {
        var newHeader = new ArrayList<String>();
        for (var line : header) {
            var isNotComment = !line.startsWith("#");
            if (isNotComment) {
                // add '#' if necessary
                if (destinationFormat != BPSEQ && destinationFormat != CT) {
                    newHeader.add("#" + line);
                }
                // does not modify the header if the destination format is bpseq or ct
                else {
                    newHeader.addAll(header);
                    break;
                }
            }
            // is already a comment, nothing needs to be done
            else {
                newHeader.add(line);
            }
        }
        return newHeader;
    }

    /**
     * Extracts all paired regions (weak bonds) from the given secondary structure.
     *
     * @param rnaSecondaryStructure the secondary structure to analyze
     * @return a list of {@link Region} objects, one per weak bond
     */
    private static List<Region> findAllPairedRegions(RNASecondaryStructure rnaSecondaryStructure) {
        return rnaSecondaryStructure.getBonds().stream().map(Region::new).toList();
    }

    /**
     * Sorts a list of regions by their start (left) position.
     *
     * @param regs the list of regions to sort
     * @return a new list sorted by left position
     */
    private static List<Region> sortRegionsByStartPoint(List<Region> regs) {
        var tmp = new ArrayList<>(regs);
        tmp.sort(Comparator.comparingInt(r -> r.getWeakBond().getLeft()));
        return tmp;
    }

    /**
     * Assigns an order (nesting level) to each region, resolving conflicts for
     * pseudoknot representation.
     *
     * @param regs the list of regions (must be sorted by start point)
     * @param n    the number of regions
     */
    private static void setRegionsOrder(List<Region> regs, int n) {
        if (n < 2) return;
        regs.get(0).setOrder(0);
        for (int i = 1; i < n; i++) {
            var globalOrder = 0;
            for (int j = 0; j <= i - 1; j++) {
                if (regs.get(j).getOrder() == globalOrder && areRegionsConflicting(regs.get(i), regs.get(j))) {
                    globalOrder += 1;
                    j = 0;
                }
            }
            regs.get(i).setOrder(globalOrder);
        }
    }

    /**
     * Determines whether two regions (base pairs) conflict (i.e., form a pseudoknot).
     *
     * @param r1 the first region
     * @param r2 the second region
     * @return {@code true} if the regions conflict, {@code false} otherwise
     */
    private static boolean areRegionsConflicting(Region r1, Region r2) {
        var wb1 = r1.getWeakBond();
        var wb2 = r2.getWeakBond();
        // ( [ ) ]
        var firstCase =
            wb1.getLeft() < wb2.getLeft() && wb1.getRight() > wb2.getLeft() && wb2.getRight() > wb1.getRight();
        // [ ( ] )
        var secondCase =
            wb2.getLeft() < wb1.getLeft() && wb2.getRight() > wb1.getLeft() && wb1.getRight() > wb2.getRight();
        return firstCase || secondCase;
    }

    /**
     * Encodes the base pairs into an Extended Dot-Bracket Notation string
     * using the assigned orders.
     *
     * @param regs the list of regions with assigned orders
     * @param size the total length of the structure
     * @return the encoded dot-bracket string
     */
    private static String encodeBasePairs(List<Region> regs, int size) {
        var structure = new StringBuilder();
        structure.append(".".repeat(size));
        for (var r : regs) {
            structure.setCharAt(r.getWeakBond().getLeft() - 1, getOpeningBracket(r.getOrder()));
            structure.setCharAt(r.getWeakBond().getRight() - 1, getClosingBracket(r.getOrder()));
        }
        return structure.toString();
    }

    /**
     * Returns the opening bracket character corresponding to the given order.
     *
     * @param order the nesting order (0-8)
     * @return the opening bracket character
     * @throws IllegalArgumentException if the order is greater than 8
     */
    private static Character getOpeningBracket(int order) {
        return switch (order) {
            case 0 -> '(';
            case 1 -> '[';
            case 2 -> '{';
            case 3 -> '<';
            case 4 -> 'A';
            case 5 -> 'B';
            case 6 -> 'C';
            case 7 -> 'D';
            case 8 -> 'E';
            default -> throw new IllegalArgumentException("Maximum order is 8!");
        };
    }

    /**
     * Returns the closing bracket character corresponding to the given order.
     *
     * @param order the nesting order (0-8)
     * @return the closing bracket character
     * @throws IllegalArgumentException if the order is greater than 8
     */
    private static Character getClosingBracket(int order) {
        return switch (order) {
            case 0 -> ')';
            case 1 -> ']';
            case 2 -> '}';
            case 3 -> '>';
            case 4 -> 'a';
            case 5 -> 'b';
            case 6 -> 'c';
            case 7 -> 'd';
            case 8 -> 'e';
            default -> throw new IllegalArgumentException("Maximum order is 8!");
        };
    }

    /**
     * Replaces the extension of the given file name with the destination extension.
     *
     * @param fileName      the original file name
     * @param dstExtension  the new extension (without leading dot)
     * @return the file name with the new extension
     */
    private static String getFileNameWithDstExtension(String fileName, String dstExtension) {
        return fileName.substring(0, fileName.lastIndexOf('.') + 1) + dstExtension;
    }
}
