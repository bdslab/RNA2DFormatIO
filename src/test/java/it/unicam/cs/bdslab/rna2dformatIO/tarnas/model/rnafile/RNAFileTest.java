package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RNAFile}.
 */
class RNAFileTest {

    private RNASecondaryStructure structure;
    private RNAFile rnaFile;

    @BeforeEach
    void setUp() {
        structure = new RNASecondaryStructure();
        structure.setSequence("AUGC");
        structure.setSize(4);
        structure.addBond(new WeakBond(1, 4));
        structure.finalise();

        rnaFile = new RNAFile(
                "sample.bpseq",
                List.of("# comment"),
                List.of("1 A 4", "2 U 0", "3 G 0", "4 C 1"),
                structure,
                RNAFormat.BPSEQ
        );
    }

    // -----------------------------------------------------------------------
    // Getter
    // -----------------------------------------------------------------------

    @Test
    void getFileNameReturnsCorrectName() {
        assertEquals("sample.bpseq", rnaFile.getFileName());
    }

    @Test
    void getHeaderReturnsCorrectHeader() {
        assertEquals(List.of("# comment"), rnaFile.getHeader());
    }

    @Test
    void getBodyReturnsCorrectBody() {
        assertEquals(4, rnaFile.getBody().size());
    }

    @Test
    void getFormatReturnsCorrectFormat() {
        assertEquals(RNAFormat.BPSEQ, rnaFile.getFormat());
    }

    @Test
    void getStructureReturnsCorrectStructure() {
        assertEquals(structure, rnaFile.getStructure());
    }

    // -----------------------------------------------------------------------
    // getContent = header + body
    // -----------------------------------------------------------------------

    @Test
    void getContentIsConcatenationOfHeaderAndBody() {
        List<String> content = rnaFile.getContent();
        assertEquals(5, content.size()); // 1 header + 4 body
        assertEquals("# comment", content.get(0));
        assertEquals("1 A 4", content.get(1));
    }

    // -----------------------------------------------------------------------
    // Setter
    // -----------------------------------------------------------------------

    @Test
    void setFileNameUpdatesName() {
        rnaFile.setFileName("renamed.ct");
        assertEquals("renamed.ct", rnaFile.getFileName());
    }

    @Test
    void setFormatUpdatesFormat() {
        rnaFile.setFormat(RNAFormat.CT);
        assertEquals(RNAFormat.CT, rnaFile.getFormat());
    }

    // -----------------------------------------------------------------------
    // equals & hashCode
    // -----------------------------------------------------------------------

    @Test
    void equalFilesAreEqual() {
        RNAFile copy = new RNAFile(
                "sample.bpseq",
                List.of("# comment"),
                List.of("1 A 4", "2 U 0", "3 G 0", "4 C 1"),
                structure,
                RNAFormat.BPSEQ
        );
        assertEquals(rnaFile, copy);
        assertEquals(rnaFile.hashCode(), copy.hashCode());
    }

    @Test
    void filesWithDifferentNameAreNotEqual() {
        RNAFile other = new RNAFile(
                "other.bpseq",
                List.of("# comment"),
                List.of("1 A 4", "2 U 0", "3 G 0", "4 C 1"),
                structure,
                RNAFormat.BPSEQ
        );
        assertNotEquals(rnaFile, other);
    }
}