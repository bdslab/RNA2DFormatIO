package it.unicam.cs.bdslab.rna2dformatIO.tarnas.listener;

import it.unicam.cs.bdslab.RNASecondaryStructureBaseListener;
import it.unicam.cs.bdslab.RNASecondaryStructureParser;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFile;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAInputFileParserException;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * ANTLR listener that builds an {@link RNAFile} while traversing the parse tree
 * of an RNA secondary structure file.
 * <p>
 * This listener extracts the header, body, sequence, secondary structure bonds. After parsing, the constructed
 * {@code RNAFile} can be retrieved via {@link #getRnaFile()}.
 * </p>
 */
public class RNAFileListener extends RNASecondaryStructureBaseListener {

    private RNAFile rnaFile;
    private RNASecondaryStructure s;
    private StringBuffer sequenceBuffer;
    private StringBuffer edbnsBuffer;
    private List<String> header;
    private String fileName;
    private List<String> content;

    /**
     * Creates a new listener instance. The file to parse must be set via
     * {@link #setFilePath(Path)} before starting the parse walk.
     */
    public RNAFileListener() {}

    /**
     * Sets the path of the file to be parsed and initialises internal buffers.
     *
     * @param filePath path to the RNA file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void setFilePath(Path filePath) throws IOException {
        this.content = Files.readAllLines(filePath);
        this.s = new RNASecondaryStructure();
        this.sequenceBuffer = new StringBuffer();
        this.edbnsBuffer = new StringBuffer();
        this.header = new ArrayList<>();
        this.fileName = String.valueOf(filePath.getFileName());
    }

    /**
     * Returns the {@link RNAFile} built during the parse walk and clears all
     * internal data structures so that the listener is ready for the next file.
     *
     * @return the parsed {@code RNAFile}
     */
    public RNAFile getRnaFile() {
        var rnaFile = this.rnaFile;
        this.clearDataStructures();
        return rnaFile;
    }

    private void clearDataStructures() {
        this.rnaFile = null;
        this.s = null;
        this.sequenceBuffer = null;
        this.edbnsBuffer = null;
        this.header = null;
        this.fileName = null;
        this.content = null;
    }

    // ---------------------------- BPSEQ ----------------------------
    @Override
    public void enterBpseq(RNASecondaryStructureParser.BpseqContext ctx) {
        if (ctx.COMMENT() != null) ctx.COMMENT().forEach(line -> this.header.add(line.getText().trim()));
        if (ctx.BPSEQCTLINES() != null) this.header.addAll(
            Arrays.stream(ctx.BPSEQCTLINES().getText().split("\n")).map(String::trim).toList()
        );
        if (this.s.getSize() == -1) {
            this.s.setSequence("");
            this.s.setSize(0);
        }
    }

    @Override
    public void enterBpseqLineUnpaired(RNASecondaryStructureParser.BpseqLineUnpairedContext ctx) {
        this.sequenceBuffer.append(ctx.NUCLEOTIDE().getText().trim());
    }

    @Override
    public void enterBpseqLineBond(RNASecondaryStructureParser.BpseqLineBondContext ctx) {
        this.sequenceBuffer.append(ctx.NUCLEOTIDE().getText().trim());
        int left = Integer.parseInt(ctx.INDEX(0).getText());
        int right = Integer.parseInt(ctx.INDEX(1).getText());
        if (left < right) {
            this.s.addBond(new WeakBond(left, right));
        }
    }

    @Override
    public void exitBpseq(RNASecondaryStructureParser.BpseqContext ctx) {
        var body = this.content.subList(this.header.size(), this.content.size());
        this.s.setSequence(this.sequenceBuffer.toString());
        this.s.setSize(this.s.getSequence().length());
        this.s.finalise();
        this.rnaFile = new RNAFile(this.fileName, this.header, body, this.s, RNAFormat.BPSEQ);
    }

    // ---------------------------- CT ----------------------------
    @Override
    public void enterCt(RNASecondaryStructureParser.CtContext ctx) {
        if (ctx.COMMENT() != null) ctx.COMMENT().forEach(line -> this.header.add(line.getText().trim()));
        if (ctx.BPSEQCTLINES() != null) this.header.addAll(
            Arrays.stream(ctx.BPSEQCTLINES().getText().split("\n")).map(String::trim).toList()
        );
        if (this.s.getSize() == -1) {
            this.s.setSequence("");
            this.s.setSize(0);
        }
    }

    @Override
    public void enterCtLineUnpaired(RNASecondaryStructureParser.CtLineUnpairedContext ctx) {
        this.sequenceBuffer.append(ctx.NUCLEOTIDE().getText().trim());
    }

    @Override
    public void enterCtLineBond(RNASecondaryStructureParser.CtLineBondContext ctx) {
        this.sequenceBuffer.append(ctx.NUCLEOTIDE().getText().trim());
        int left = Integer.parseInt(ctx.INDEX(0).getText());
        int right = Integer.parseInt(ctx.getChild(4).getText());
        if (left < right) {
            this.s.addBond(new WeakBond(left, right));
        }
    }

    @Override
    public void exitCt(RNASecondaryStructureParser.CtContext ctx) {
        var body = this.content.subList(this.header.size(), this.content.size());
        this.s.setSequence(this.sequenceBuffer.toString());
        this.s.setSize(this.s.getSequence().length());
        this.s.finalise();
        this.rnaFile = new RNAFile(this.fileName, this.header, body, this.s, RNAFormat.CT);
    }

    // ---------------------------- AAS ----------------------------
    @Override
    public void enterAas(RNASecondaryStructureParser.AasContext ctx) {
        ctx.COMMENT().forEach(line -> this.header.add(line.getText().trim()));
    }

    @Override
    public void enterBonds(RNASecondaryStructureParser.BondsContext ctx) {
        if (this.s.getSize() == -1) {
            this.s.setSequence("");
            this.s.setSize(0);
        }
        var indexes = this.getBondTokens(ctx.BOND().getText());
        int left = Integer.parseInt(indexes.get(0));
        int right = Integer.parseInt(indexes.get(1));
        this.s.addBond(new WeakBond(left, right));
    }

    private List<String> getBondTokens(String bondTokenText) {
        var separator = bondTokenText.contains(",") ? ',' : ';';
        var left = bondTokenText.substring(bondTokenText.indexOf('(') + 1, bondTokenText.lastIndexOf(separator));
        var right = bondTokenText.substring(bondTokenText.indexOf(separator) + 1, bondTokenText.lastIndexOf(')'));
        return List.of(left, right);
    }

    @Override
    public void exitAas(RNASecondaryStructureParser.AasContext ctx) {
        var body = this.content.subList(this.header.size(), this.content.size());
        this.s.finalise();
        // create rnafile object with unnecessary empty body
        this.rnaFile = new RNAFile(
            this.fileName,
            this.header,
            body,
            this.s,
            (this.s.getSequence() == null || this.s.getSequence().isEmpty()) ? RNAFormat.AAS_NO_SEQUENCE : RNAFormat.AAS
        );
    }

    // ---------------------------- FASTA ----------------------------
    @Override
    public void enterFasta(RNASecondaryStructureParser.FastaContext ctx) {
        ctx.COMMENT().forEach(line -> this.header.add(line.getText().trim()));
    }

    @Override
    public void exitFasta(RNASecondaryStructureParser.FastaContext ctx) {
        var body = this.content.subList(this.header.size(), this.content.size());
        this.rnaFile = new RNAFile(this.fileName, this.header, body, this.s, RNAFormat.FASTA);
    }

    // ---------------------------- EDBN (Dot‑Bracket) ----------------------------
    @Override
    public void exitEdbn_structure(RNASecondaryStructureParser.Edbn_structureContext ctx) {
        var edbn = ctx.EDBN().stream().map(ParseTree::getText).toList();
        for (var e : edbn) {
            // Check for mis-classified nucleotide sequences
            if (!e.contains(".")) {
                if (
                    !e.contains("(") &&
                    !e.contains(")") &&
                    !e.contains("[") &&
                    !e.contains("]") &&
                    !e.contains("{") &&
                    !e.contains("}")
                ) {
                    if (e.length() >= 5) {
                        // ok, it is not considered edbn, the exception is thrown
                        String m =
                            "Line " +
                            ctx.start.getLine() +
                            " Character " +
                            (ctx.start.getCharPositionInLine() + 1) +
                            ": " +
                            "unrecognised nucleotide code in " +
                            e;
                        throw new RNAInputFileParserException(m);
                    }
                }
            }
            this.edbnsBuffer.append(e);
        }
        if (this.s.getSize() == -1) {
            this.s.setSequence("");
            this.s.setSize(this.edbnsBuffer.length());
        }

        if (
            this.s.getSize() != 0 && this.edbnsBuffer.length() != this.s.getSize()
        ) throw new RNAInputFileParserException(
            "Extended Dot-Bracket Notation Structure is of length " +
                this.edbnsBuffer.length() +
                " while the sequence of nucleotides is of length " +
                this.s.getSize()
        );
    }

    @Override
    public void enterSequence(RNASecondaryStructureParser.SequenceContext ctx) {
        var sequence = ctx.NUCLEOTIDE().stream().map(ParseTree::getText).collect(Collectors.joining());
        this.sequenceBuffer.append(sequence);
        this.s.setSequence(this.sequenceBuffer.toString());
        this.s.setSize(this.s.getSequence().length());
    }

    @Override
    public void enterEdbn(RNASecondaryStructureParser.EdbnContext ctx) {
        ctx.COMMENT().forEach(line -> this.header.add(line.getText().trim()));
    }

    @Override
    public void exitEdbn(RNASecondaryStructureParser.EdbnContext ctx) {
        var bonds = parseEDBN(this.edbnsBuffer.toString());
        for (var wb : bonds) this.s.addBond(wb);
        this.s.finalise();
        if (Objects.equals(this.s.getSequence(), "")) this.rnaFile = new RNAFile(
            this.fileName,
            this.header,
            List.of(this.edbnsBuffer.toString()),
            this.s,
            RNAFormat.DB_NO_SEQUENCE
        );
        else this.rnaFile = new RNAFile(
            this.fileName,
            this.header,
            List.of(this.sequenceBuffer.toString(), this.edbnsBuffer.toString()),
            this.s,
            RNAFormat.DB
        );
    }

    // ---------------------------- EDBN parsing helpers ----------------------------
    /**
     * Parses an Extended Dot-Bracket Notation (EDBN) string and returns a list of weak bonds.
     *
     * @param extendedDotBracketNotation the EDBN string to parse
     * @return a list of {@link WeakBond} objects representing the base pairs
     * @throws RNAInputFileParserException if the notation contains unbalanced brackets
     *                                     or other syntax errors
     */
    private static List<WeakBond> parseEDBN(String extendedDotBracketNotation) {
        var bonds = new ArrayList<WeakBond>();
        var stacks = new HashMap<Character, Stack<Integer>>();
        for (int i = 0; i < extendedDotBracketNotation.length(); i++) {
            var c = extendedDotBracketNotation.charAt(i);
            Character oc = c;
            if (isOpeningChar(c)) {
                if (!stacks.containsKey(oc)) stacks.put(oc, new Stack<>());
                stacks.get(oc).push(i);
            }
            if (isClosingChar(c)) {
                Character opening = getCorrespondingOpening(c);
                if (stacks.get(opening) == null || stacks.get(opening).isEmpty()) {
                    throw new RNAInputFileParserException(
                        "Extended dot-bracket notation parsing: closing character at position " +
                            (i + 1) +
                            " does not have a corresponding opening character"
                    );
                }
                int leftPosition = stacks.get(opening).pop();
                bonds.add(new WeakBond(leftPosition + 1, i + 1));
            }
        }
        var ks = stacks.keySet();
        for (var c : ks)
            if (!stacks.get(c).isEmpty()) {
                var msg = new StringBuilder(
                    "Extended dot-bracket notation parsing: " +
                        stacks.get(c).size() +
                        " missing closing occurrence(s) of " +
                        c +
                        " symbol, left opening symbol(s) at position(s) "
                );
                for (Integer i : stacks.get(c)) msg.append(i + 1).append(" ");
                throw new RNAInputFileParserException(msg.toString());
            }
        return bonds;
    }

    private static boolean isOpeningChar(char c) {
        return c == '(' || c == '[' || c == '{' || c == '<' || Character.isUpperCase(c);
    }

    private static boolean isClosingChar(char c) {
        return c == ')' || c == ']' || c == '}' || c == '>' || Character.isLowerCase(c);
    }

    private static char getCorrespondingOpening(char c) {
        return switch (c) {
            case ')' -> '(';
            case ']' -> '[';
            case '}' -> '{';
            case '>' -> '<';
            default -> Character.toUpperCase(c);
        };
    }
}
