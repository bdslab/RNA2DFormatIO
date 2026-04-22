package it.unicam.cs.bdslab.rna2dformatIO.tarnas.controller;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.*;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TranslatorController}.
 */
class TranslatorControllerTest {

    private TranslatorController controller;
    private RNAFile bpseqFile;

    @BeforeEach
    void setUp() {
        controller = TranslatorController.getInstance();

        RNASecondaryStructure structure = new RNASecondaryStructure();
        structure.setSequence("AUGCAUGC");
        structure.setSize(8);
        structure.addBond(new WeakBond(1, 8));
        structure.addBond(new WeakBond(2, 7));
        structure.finalise();

        bpseqFile = new RNAFile(
                "test.bpseq",
                List.of("# header"),
                List.of(),
                structure,
                RNAFormat.BPSEQ
        );
    }

    // -----------------------------------------------------------------------
    // Singleton
    // -----------------------------------------------------------------------

    @Test
    void getInstanceReturnsSameObject() {
        assertSame(controller, TranslatorController.getInstance());
    }

    // -----------------------------------------------------------------------
    // getAvailableTranslations
    // -----------------------------------------------------------------------

    @Test
    void bpseqCanTranslateToAAS() {
        assertTrue(controller.getAvailableTranslations(RNAFormat.BPSEQ)
                .contains(RNAFormat.AAS));
    }

    @Test
    void bpseqCanTranslateToDB() {
        assertTrue(controller.getAvailableTranslations(RNAFormat.BPSEQ)
                .contains(RNAFormat.DB));
    }

    @Test
    void dbExcludesSelfTranslation() {
        // DB should not be able to translate to DB
        assertFalse(controller.getAvailableTranslations(RNAFormat.DB)
                .contains(RNAFormat.DB));
    }

    @Test
    void aasExcludesSelfTranslation() {
        assertFalse(controller.getAvailableTranslations(RNAFormat.AAS)
                .contains(RNAFormat.AAS));
    }

    @Test
    void fastaHasNoAvailableTranslations() {
        assertTrue(controller.getAvailableTranslations(RNAFormat.FASTA).isEmpty());
    }

    @Test
    void dbNoSequenceCanOnlyTranslateToAASNoSequence() {
        List<RNAFormat> translations = controller.getAvailableTranslations(RNAFormat.DB_NO_SEQUENCE);
        assertEquals(1, translations.size());
        assertEquals(RNAFormat.AAS_NO_SEQUENCE, translations.getFirst());
    }

    // -----------------------------------------------------------------------
    // translate
    // -----------------------------------------------------------------------

    @Test
    void translateBPSEQToDBProducesDBFile() {
        RNAFile result = controller.translate(bpseqFile, RNAFormat.DB);
        assertEquals(bpseqFile.getStructure(), result.getStructure());
        assertEquals(RNAFormat.DB, result.getFormat());
    }

    @Test
    void translateBPSEQToCTProducesCTFile() {
        RNAFile result = controller.translate(bpseqFile, RNAFormat.CT);
        assertEquals(bpseqFile.getStructure(), result.getStructure());
        assertEquals(RNAFormat.CT, result.getFormat());
    }

    @Test
    void translateBPSEQToAASProducesAASFile() {
        RNAFile result = controller.translate(bpseqFile, RNAFormat.AAS);
        assertEquals(bpseqFile.getStructure(), result.getStructure());
        assertEquals(RNAFormat.AAS, result.getFormat());
    }

    @Test
    void translateBPSEQToFASTAProducesFASTAFile() {
        RNAFile result = controller.translate(bpseqFile, RNAFormat.FASTA);
        assertEquals(bpseqFile.getStructure().getSize(), result.getStructure().getSize());
        assertEquals(RNAFormat.FASTA, result.getFormat());
    }

    @Test
    void translatePreservesNumberOfBonds() {
        RNAFile result = controller.translate(bpseqFile, RNAFormat.CT);
        assertEquals(
                bpseqFile.getStructure().getBonds().size(),
                result.getStructure().getBonds().size()
        );
    }
}