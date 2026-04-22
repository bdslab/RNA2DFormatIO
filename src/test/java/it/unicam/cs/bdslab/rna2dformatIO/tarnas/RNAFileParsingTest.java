package it.unicam.cs.bdslab.rna2dformatIO.tarnas;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFile;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFileConstructor;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test parsing all supported formats.
 * <p>
 * Every test get a example file from the folder
 * {@code src/test/resources/.../tarnas/} and verify that the parser
 * produce a {@link RNAFile} with the expected content.
 * <p>
 * Structure used in the test files:
 * <pre>
 *   Sequence : ACGUACGU  (8 nt)
 *   Bonds    : (1,8) A-U  |  (2,7) C-G  |  (3,6) G-C
 *   dot-bracket: (((..)))
 * </pre>
 */
class RNAFileParsingTest {

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private Path resourcePath(String fileName) throws URISyntaxException {
        var url = getClass().getResource(fileName);
        assertNotNull(url, "Test file not found in classPath: " + fileName);
        return Path.of(url.toURI());
    }

    private RNAFile parse(String fileName) throws IOException, URISyntaxException {
        return RNAFileConstructor.getInstance().construct(resourcePath(fileName));
    }

    private static final List<WeakBond> EXPECTED_BONDS = List.of(
            new WeakBond(1, 8),
            new WeakBond(2, 7),
            new WeakBond(3, 6)
    );

    // -----------------------------------------------------------------------
    // BPSEQ
    // -----------------------------------------------------------------------

    @Test
    void parseBPSEQDetectsCorrectFormat() throws Exception {
        RNAFile file = parse("sample.bpseq");
        assertEquals(RNAFormat.BPSEQ, file.getFormat());
    }

    @Test
    void parseBPSEQReadsCorrectSequence() throws Exception {
        RNAFile file = parse("sample.bpseq");
        assertEquals("ACGUACGU", file.getStructure().getSequence());
    }

    @Test
    void parseBPSEQReadsCorrectSize() throws Exception {
        RNAFile file = parse("sample.bpseq");
        assertEquals(8, file.getStructure().getSize());
    }

    @Test
    void parseBPSEQReadsCorrectBonds() throws Exception {
        RNAFile file = parse("sample.bpseq");
        var bonds = file.getStructure().getBonds();
        assertEquals(3, bonds.size());
        assertTrue(bonds.containsAll(EXPECTED_BONDS),
                "Bonds read different from expected");
    }

    @Test
    void parseBPSEQPointerArrayIsCorrect() throws Exception {
        RNAFile file = parse("sample.bpseq");
        int[] p = file.getStructure().getP();
        assertEquals(8, p[1]);
        assertEquals(7, p[2]);
        assertEquals(6, p[3]);
        assertEquals(0, p[4]); // not bonded
        assertEquals(0, p[5]); // not bonded
        assertEquals(3, p[6]);
        assertEquals(2, p[7]);
        assertEquals(1, p[8]);
    }

    // -----------------------------------------------------------------------
    // CT
    // -----------------------------------------------------------------------

    @Test
    void parseCTDetectsCorrectFormat() throws Exception {
        RNAFile file = parse("sample.ct");
        assertEquals(RNAFormat.CT, file.getFormat());
    }

    @Test
    void parseCTReadsCorrectSequence() throws Exception {
        RNAFile file = parse("sample.ct");
        assertEquals("ACGUACGU", file.getStructure().getSequence());
    }

    @Test
    void parseCTReadsCorrectSize() throws Exception {
        RNAFile file = parse("sample.ct");
        assertEquals(8, file.getStructure().getSize());
    }

    @Test
    void parseCTReadsCorrectBonds() throws Exception {
        RNAFile file = parse("sample.ct");
        var bonds = file.getStructure().getBonds();
        assertEquals(3, bonds.size());
        assertTrue(bonds.containsAll(EXPECTED_BONDS));
    }

    // -----------------------------------------------------------------------
    // DB (with sequence)
    // -----------------------------------------------------------------------

    @Test
    void parseDBDetectsCorrectFormat() throws Exception {
        RNAFile file = parse("sample.db");
        assertEquals(RNAFormat.DB, file.getFormat());
    }

    @Test
    void parseDBReadsCorrectSequence() throws Exception {
        RNAFile file = parse("sample.db");
        assertEquals("ACGUACGU", file.getStructure().getSequence());
    }

    @Test
    void parseDBReadsCorrectSize() throws Exception {
        RNAFile file = parse("sample.db");
        assertEquals(8, file.getStructure().getSize());
    }

    @Test
    void parseDBReadsCorrectBonds() throws Exception {
        RNAFile file = parse("sample.db");
        var bonds = file.getStructure().getBonds();
        assertEquals(3, bonds.size());
        assertTrue(bonds.containsAll(EXPECTED_BONDS));
    }

    // -----------------------------------------------------------------------
    // DB NO SEQUENCE
    // -----------------------------------------------------------------------

    @Test
    void parseDBNoSequenceDetectsCorrectFormat() throws Exception {
        RNAFile file = parse("sample_no_seq.db");
        assertEquals(RNAFormat.DB_NO_SEQUENCE, file.getFormat());
    }

    @Test
    void parseDBNoSequenceHasNullOrEmptySequence() throws Exception {
        RNAFile file = parse("sample_no_seq.db");

        var seq = file.getStructure().getSequence();
        assertTrue(seq == null || seq.isEmpty(),
                "The sequence should be not present in the file DB_NO_SEQUENCE");
    }

    @Test
    void parseDBNoSequenceReadsCorrectBonds() throws Exception {
        RNAFile file = parse("sample_no_seq.db");
        var bonds = file.getStructure().getBonds();
        assertEquals(3, bonds.size());
        assertTrue(bonds.containsAll(EXPECTED_BONDS));
    }

    // -----------------------------------------------------------------------
    // AAS (with sequence)
    // -----------------------------------------------------------------------

    @Test
    void parseAASDetectsCorrectFormat() throws Exception {
        RNAFile file = parse("sample.aas");
        assertEquals(RNAFormat.AAS, file.getFormat());
    }

    @Test
    void parseAASReadsCorrectSequence() throws Exception {
        RNAFile file = parse("sample.aas");
        assertEquals("ACGUACGU", file.getStructure().getSequence());
    }

    @Test
    void parseAASReadsCorrectSize() throws Exception {
        RNAFile file = parse("sample.aas");
        assertEquals(8, file.getStructure().getSize());
    }

    @Test
    void parseAASReadsCorrectBonds() throws Exception {
        RNAFile file = parse("sample.aas");
        var bonds = file.getStructure().getBonds();
        assertEquals(3, bonds.size());
        assertTrue(bonds.containsAll(EXPECTED_BONDS));
    }

    // -----------------------------------------------------------------------
    // AAS NO SEQUENCE
    // -----------------------------------------------------------------------

    @Test
    void parseAASNoSequenceDetectsCorrectFormat() throws Exception {
        RNAFile file = parse("sample_no_seq.aas");
        assertEquals("", file.getStructure().getSequence());
        assertEquals(RNAFormat.AAS_NO_SEQUENCE, file.getFormat());
    }

    @Test
    void parseAASNoSequenceReadsCorrectBonds() throws Exception {
        RNAFile file = parse("sample_no_seq.aas");
        var bonds = file.getStructure().getBonds();
        assertEquals(3, bonds.size());
        assertTrue(bonds.containsAll(EXPECTED_BONDS));
    }

    // -----------------------------------------------------------------------
    // FASTA
    // -----------------------------------------------------------------------

    @Test
    void parseFASTADetectsCorrectFormat() throws Exception {
        RNAFile file = parse("sample.fasta");
        assertEquals(RNAFormat.FASTA, file.getFormat());
    }

    @Test
    void parseFASTAReadsCorrectSequence() throws Exception {
        RNAFile file = parse("sample.fasta");
        assertEquals("ACGUACGU", file.getStructure().getSequence());
    }

    @Test
    void parseFASTAHasNoBonds() throws Exception {
        RNAFile file = parse("sample.fasta");
        assertTrue(file.getStructure().getBonds().isEmpty(),
                "FASTA do not contain structural information, bonds should be empty");
    }

    // -----------------------------------------------------------------------
    // BPSEQ, CT e AAS should produce the same structure
    // -----------------------------------------------------------------------

    @Test
    void bpseqAndCTProduceSameStructure() throws Exception {
        var bpseq = parse("sample.bpseq").getStructure();
        var ct    = parse("sample.ct").getStructure();
        assertEquals(bpseq.getSize(),  ct.getSize());
        assertEquals(bpseq.getBonds(), ct.getBonds());
        assertEquals(bpseq.getSequence(), ct.getSequence());
    }

    @Test
    void bpseqAndAASProduceSameStructure() throws Exception {
        var bpseq = parse("sample.bpseq").getStructure();
        var aas   = parse("sample.aas").getStructure();
        assertEquals(bpseq.getSize(),  aas.getSize());
        assertEquals(bpseq.getBonds(), aas.getBonds());
        assertEquals(bpseq.getSequence(), aas.getSequence());
    }

    @Test
    void bpseqAndDBProduceSameStructure() throws Exception {
        var bpseq = parse("sample.bpseq").getStructure();
        var db    = parse("sample.db").getStructure();
        assertEquals(bpseq.getSize(),  db.getSize());
        assertEquals(bpseq.getBonds(), db.getBonds());
        assertEquals(bpseq.getSequence(), db.getSequence());
    }

    // -----------------------------------------------------------------------
    // Pseudoknot
    // -----------------------------------------------------------------------

    @Test
    void pseudoknotFileIsParsedCorrectly() throws Exception {
        RNAFile file = parse("pseudoknot.bpseq");
        assertTrue(file.getStructure().isPseudoknotted(),
                "The structure in the file pseudoknot.bpseq should be a pseudoknot");
    }

    @Test
    void pseudoknotFileHasTwoBonds() throws Exception {
        RNAFile file = parse("pseudoknot.bpseq");
        assertEquals(2, file.getStructure().getBonds().size());
    }
}