package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.utils;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFile;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link RNAStatisticsCalculator}.
 * <p>
 * Test sequence: AUGCGUGU (8 nt)
 * Bonds: (1,2) A-U, (3,4) G-C, (5,8) G-U (wobble)
 */
class RNAStatisticsCalculatorTest {

    private RNAFile rnaFile;

    @BeforeEach
    void setUp() {
        // AUGCGUGU
        // pos: 1234 5678
        // Bonds: AU(1-2), GC(3-4), GU(5-8)
        RNASecondaryStructure structure = new RNASecondaryStructure();
        structure.setSequence("AUGCGUGU");
        structure.setSize(8);
        structure.addBond(new WeakBond(1, 2));  // A-U
        structure.addBond(new WeakBond(3, 4));  // G-C
        structure.addBond(new WeakBond(5, 8));  // G-U (wobble)
        structure.finalise();

        rnaFile = new RNAFile(
                "test.bpseq",
                List.of(),
                List.of(),
                structure,
                RNAFormat.BPSEQ
        );
    }

    // -----------------------------------------------------------------------
    // Nucleotides count
    // -----------------------------------------------------------------------

    @Test
    void getNucleotideCountReturnsCorrectSize() {
        assertEquals(8, RNAStatisticsCalculator.getNucleotideCount(rnaFile));
    }

    @Test
    void getACountIsCorrect() {
        // A in position 1 -> 1 A
        assertEquals(1, RNAStatisticsCalculator.getACount(rnaFile));
    }

    @Test
    void getUCountIsCorrect() {
        // U in positions 2, 6, 8 -> 3 U
        assertEquals(3, RNAStatisticsCalculator.getUCount(rnaFile));
    }

    @Test
    void getGCountIsCorrect() {
        // G in positions 3, 5, 7 -> 3 G
        assertEquals(3, RNAStatisticsCalculator.getGCount(rnaFile));
    }

    @Test
    void getCCountIsCorrect() {
        // C in positions 4 -> 1 C
        assertEquals(1, RNAStatisticsCalculator.getCCount(rnaFile));
    }

    // -----------------------------------------------------------------------
    // Bonds count
    // -----------------------------------------------------------------------

    @Test
    void getBondCountIsCorrect() {
        assertEquals(3, RNAStatisticsCalculator.getBondCount(rnaFile));
    }

    @Test
    void getAuBondsIsCorrect() {
        // Only (1,2) A-U
        assertEquals(1, RNAStatisticsCalculator.getAuBonds(rnaFile));
    }

    @Test
    void getGcBondsIsCorrect() {
        // Only (3,4) G-C
        assertEquals(1, RNAStatisticsCalculator.getGcBonds(rnaFile));
    }

    @Test
    void getGuBondsIsCorrect() {
        // Only (5,8) G-U
        assertEquals(1, RNAStatisticsCalculator.getGuBonds(rnaFile));
    }

    // -----------------------------------------------------------------------
    // Formats without sequence: the counts of bonds/nucleotides should be 0
    // -----------------------------------------------------------------------

    @Test
    void nucleotideCountZeroForDBNoSequence() {
        RNAFile noSeq = buildNoSequenceFile(RNAFormat.DB_NO_SEQUENCE);
        assertEquals(0, RNAStatisticsCalculator.getACount(noSeq));
        assertEquals(0, RNAStatisticsCalculator.getUCount(noSeq));
        assertEquals(0, RNAStatisticsCalculator.getGCount(noSeq));
        assertEquals(0, RNAStatisticsCalculator.getCCount(noSeq));
    }

    @Test
    void bondStatsZeroForAASNoSequence() {
        RNAFile noSeq = buildNoSequenceFile(RNAFormat.AAS_NO_SEQUENCE);
        assertEquals(0, RNAStatisticsCalculator.getAuBonds(noSeq));
        assertEquals(0, RNAStatisticsCalculator.getGcBonds(noSeq));
        assertEquals(0, RNAStatisticsCalculator.getGuBonds(noSeq));
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private RNAFile buildNoSequenceFile(RNAFormat format) {
        RNASecondaryStructure s = new RNASecondaryStructure();
        s.setSize(8);
        s.addBond(new WeakBond(1, 8));
        s.finalise();
        return new RNAFile("test.db", List.of(), List.of(), s, format);
    }
}