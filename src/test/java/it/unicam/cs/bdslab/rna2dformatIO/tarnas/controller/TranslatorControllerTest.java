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

package it.unicam.cs.bdslab.rna2dformatIO.tarnas.controller;

import static org.junit.jupiter.api.Assertions.*;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.*;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        bpseqFile = new RNAFile("test.bpseq", List.of("# header"), List.of(), structure, RNAFormat.BPSEQ);
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
        assertTrue(controller.getAvailableTranslations(RNAFormat.BPSEQ).contains(RNAFormat.AAS));
    }

    @Test
    void bpseqCanTranslateToDB() {
        assertTrue(controller.getAvailableTranslations(RNAFormat.BPSEQ).contains(RNAFormat.DB));
    }

    @Test
    void dbExcludesSelfTranslation() {
        // DB should not be able to translate to DB
        assertFalse(controller.getAvailableTranslations(RNAFormat.DB).contains(RNAFormat.DB));
    }

    @Test
    void aasExcludesSelfTranslation() {
        assertFalse(controller.getAvailableTranslations(RNAFormat.AAS).contains(RNAFormat.AAS));
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
        assertEquals(bpseqFile.getStructure().getBonds().size(), result.getStructure().getBonds().size());
    }
}
