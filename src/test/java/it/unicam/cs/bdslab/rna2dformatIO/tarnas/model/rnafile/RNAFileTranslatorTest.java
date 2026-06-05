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

import static org.junit.jupiter.api.Assertions.*;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.RNASecondaryStructure;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RNAFileTranslator}.
 * <p>
 * Test structure:
 * <pre>
 *   sequence : AUGCAUGC  (8 nt)
 *   bonds    : (1,8) e (2,7)  -> no pseudoknot
 *   dot-bracket expected: ((....))
 * </pre>
 */
class RNAFileTranslatorTest {

    private RNAFile sourceFile;
    private RNASecondaryStructure structure;

    @BeforeEach
    void setUp() {
        structure = new RNASecondaryStructure();
        structure.setSequence("AUGCAUGC");
        structure.setSize(8);
        structure.addBond(new WeakBond(1, 8));
        structure.addBond(new WeakBond(2, 7));
        structure.finalise();

        sourceFile = new RNAFile(
            "test.bpseq",
            List.of("# header line"),
            List.of("1 A 8", "2 U 7", "3 G 0", "4 C 0", "5 A 0", "6 U 0", "7 G 2", "8 C 1"),
            structure,
            RNAFormat.BPSEQ
        );
    }

    // -----------------------------------------------------------------------
    // Translation to DB
    // -----------------------------------------------------------------------

    @Test
    void translateToDBHasCorrectFormat() {
        RNAFile result = RNAFileTranslator.translateToDB(sourceFile);
        assertEquals(RNAFormat.DB, result.getFormat());
    }

    @Test
    void translateToDBHasCorrectExtension() {
        RNAFile result = RNAFileTranslator.translateToDB(sourceFile);
        assertTrue(result.getFileName().endsWith(".db"));
    }

    @Test
    void translateToDBBodyContainsSequenceAndStructure() {
        RNAFile result = RNAFileTranslator.translateToDB(sourceFile);
        List<String> body = result.getBody();
        assertEquals(2, body.size());
        assertEquals("AUGCAUGC", body.get(0));
        assertEquals("((....))".length(), body.get(1).length());
    }

    @Test
    void translateToDBDotBracketIsCorrect() {
        RNAFile result = RNAFileTranslator.translateToDB(sourceFile);
        assertEquals("((....))".length(), result.getBody().get(1).length());
        // Verify that the structure does not contain pseudoknot (only rounded brackets)
        String db = result.getBody().get(1);
        assertTrue(db.matches("[().]+"), "DB without pseudoknot should only contain '(', ')' & '.'");
    }

    // -----------------------------------------------------------------------
    // Translation to DB_NO_SEQUENCE
    // -----------------------------------------------------------------------

    @Test
    void translateToDBNoSequenceBodyHasOneLineOnly() {
        RNAFile result = RNAFileTranslator.translateToDBNoSequence(sourceFile);
        assertEquals(1, result.getBody().size());
    }

    @Test
    void translateToDBNoSequenceHasCorrectFormat() {
        RNAFile result = RNAFileTranslator.translateToDBNoSequence(sourceFile);
        assertEquals(RNAFormat.DB_NO_SEQUENCE, result.getFormat());
    }

    // -----------------------------------------------------------------------
    // Translate to BPSEQ
    // -----------------------------------------------------------------------

    @Test
    void translateToBPSEQHasCorrectFormat() {
        RNAFile result = RNAFileTranslator.translateToBPSEQ(sourceFile);
        assertEquals(RNAFormat.BPSEQ, result.getFormat());
    }

    @Test
    void translateToBPSEQBodyHasOneLinePerNucleotide() {
        RNAFile result = RNAFileTranslator.translateToBPSEQ(sourceFile);
        assertEquals(structure.getSize(), result.getBody().size());
    }

    @Test
    void translateToBPSEQFirstLineIsPaired() {
        RNAFile result = RNAFileTranslator.translateToBPSEQ(sourceFile);
        // position 1, nucleotide A, partner 8
        assertEquals("1 A 8", result.getBody().getFirst());
    }

    @Test
    void translateToBPSEQUnpairedPositionHasZero() {
        RNAFile result = RNAFileTranslator.translateToBPSEQ(sourceFile);
        // position 3 (G, not bonded) -> third value = 0
        String line = result.getBody().get(2);
        assertTrue(line.endsWith("0"), "Not bonded nucleotide should have partner 0");
    }

    // -----------------------------------------------------------------------
    // Translate to CT
    // -----------------------------------------------------------------------

    @Test
    void translateToCTHasCorrectFormat() {
        RNAFile result = RNAFileTranslator.translateToCT(sourceFile);
        assertEquals(RNAFormat.CT, result.getFormat());
    }

    @Test
    void translateToCTBodyHasSizePlusOneLines() {
        // the first row is energetic header, then n rows
        RNAFile result = RNAFileTranslator.translateToCT(sourceFile);
        assertEquals(structure.getSize() + 1, result.getBody().size());
    }

    @Test
    void translateToCTFirstLineContainsEnergyKeyword() {
        RNAFile result = RNAFileTranslator.translateToCT(sourceFile);
        assertTrue(result.getBody().getFirst().contains("dG"));
    }

    // -----------------------------------------------------------------------
    // Translate to AAS
    // -----------------------------------------------------------------------

    @Test
    void translateToAASHasCorrectFormat() {
        RNAFile result = RNAFileTranslator.translateToAAS(sourceFile);
        assertEquals(RNAFormat.AAS, result.getFormat());
    }

    @Test
    void translateToAASBodyContainsSequenceAndBondList() {
        RNAFile result = RNAFileTranslator.translateToAAS(sourceFile);
        assertEquals(2, result.getBody().size());
        assertEquals("AUGCAUGC", result.getBody().get(0));
        // The second element contains the list of bonds
        String bonds = result.getBody().get(1);
        assertTrue(bonds.contains("(1,8)"), "List AAS should contain (1,8)");
        assertTrue(bonds.contains("(2,7)"), "List AAS should contain (2,7)");
    }

    // -----------------------------------------------------------------------
    // Translate to AAS_NO_SEQUENCE
    // -----------------------------------------------------------------------

    @Test
    void translateToAASNoSequenceBodyHasOneLine() {
        RNAFile result = RNAFileTranslator.translateToAASNoSequence(sourceFile);
        assertEquals(1, result.getBody().size());
    }

    // -----------------------------------------------------------------------
    // Translate to FASTA
    // -----------------------------------------------------------------------

    @Test
    void translateToFASTAHasCorrectFormat() {
        RNAFile result = RNAFileTranslator.translateToFASTA(sourceFile);
        assertEquals(RNAFormat.FASTA, result.getFormat());
    }

    @Test
    void translateToFASTABodyIsSequence() {
        RNAFile result = RNAFileTranslator.translateToFASTA(sourceFile);
        assertEquals(List.of("AUGCAUGC"), result.getBody());
    }

    @Test
    void translateToFASTAHasCorrectExtension() {
        RNAFile result = RNAFileTranslator.translateToFASTA(sourceFile);
        assertTrue(result.getFileName().endsWith(".fasta"));
    }

    // -----------------------------------------------------------------------
    // Structure Preservation
    // -----------------------------------------------------------------------

    @Test
    void translationPreservesStructure() {
        // The structure should be the same after every translation
        RNAFile db = RNAFileTranslator.translateToDB(sourceFile);
        RNAFile bpseq = RNAFileTranslator.translateToBPSEQ(sourceFile);
        assertEquals(db.getStructure().getBonds(), bpseq.getStructure().getBonds());
    }

    // -----------------------------------------------------------------------
    // Header
    // -----------------------------------------------------------------------

    @Test
    void translateToDBPreservesCommentHeader() {
        // Header already start with '#', it should not be duplicated
        RNAFile result = RNAFileTranslator.translateToDB(sourceFile);
        assertTrue(result.getHeader().getFirst().startsWith("#"));
    }
}
