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

package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure;

import static org.junit.jupiter.api.Assertions.*;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAInputFileParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests per {@link RNASecondaryStructure}.
 */
class RNASecondaryStructureTest {

    private RNASecondaryStructure structure;

    @BeforeEach
    void setUp() {
        structure = new RNASecondaryStructure();
    }

    // -----------------------------------------------------------------------
    // Empty Structure / Initialization
    // -----------------------------------------------------------------------

    @Test
    void newStructureHasNullSequence() {
        assertNull(structure.getSequence());
    }

    @Test
    void newStructureHasNoBonds() {
        assertTrue(structure.getBonds().isEmpty());
    }

    // -----------------------------------------------------------------------
    // setSize / setSequence
    // -----------------------------------------------------------------------

    @Test
    void setSizeAndGetSize() {
        structure.setSize(10);
        assertEquals(10, structure.getSize());
    }

    @Test
    void setSequenceAndGetSequence() {
        structure.setSequence("AUGC");
        assertEquals("AUGC", structure.getSequence());
    }

    // -----------------------------------------------------------------------
    // addBond – valid cases
    // -----------------------------------------------------------------------

    @Test
    void addBondIncreasesSize() {
        structure.addBond(new WeakBond(1, 8));
        assertEquals(8, structure.getSize());
    }

    @Test
    void addMultipleBonds() {
        structure.addBond(new WeakBond(1, 8));
        structure.addBond(new WeakBond(2, 7));
        assertEquals(2, structure.getBonds().size());
    }

    // -----------------------------------------------------------------------
    // addBond – conflicts
    // -----------------------------------------------------------------------

    @Test
    void addDuplicateLeftIndexThrows() {
        structure.addBond(new WeakBond(1, 8));
        assertThrows(RNAInputFileParserException.class, () -> structure.addBond(new WeakBond(1, 5)));
    }

    @Test
    void addDuplicateRightIndexThrows() {
        structure.addBond(new WeakBond(1, 8));
        assertThrows(RNAInputFileParserException.class, () -> structure.addBond(new WeakBond(3, 8)));
    }

    @Test
    void addBondExceedingSizeWithSequenceThrows() {
        structure.setSequence("AUGCAUGC"); // length = 8
        structure.setSize(8);
        assertThrows(RNAInputFileParserException.class, () -> structure.addBond(new WeakBond(1, 9)));
    }

    // -----------------------------------------------------------------------
    // finalise
    // -----------------------------------------------------------------------

    @Test
    void finaliseBuildsPointerArray() {
        structure.setSequence("AUGCAUGC");
        structure.setSize(8);
        structure.addBond(new WeakBond(1, 8));
        structure.addBond(new WeakBond(2, 7));
        structure.finalise();

        int[] p = structure.getP();
        assertEquals(8, p[1]);
        assertEquals(1, p[8]);
        assertEquals(7, p[2]);
        assertEquals(2, p[7]);
        assertEquals(0, p[3]); // non legato
    }

    @Test
    void finaliseWithoutSizeThrows() {
        // size mai impostato (-1) e nessun bond
        assertThrows(RNAInputFileParserException.class, () -> structure.finalise());
    }

    // -----------------------------------------------------------------------
    // isPseudoknotted
    // -----------------------------------------------------------------------

    @Test
    void noPseudoknotForNestedBonds() {
        structure.addBond(new WeakBond(1, 10));
        structure.addBond(new WeakBond(3, 7));
        assertFalse(structure.isPseudoknotted());
    }

    @Test
    void pseudoknotDetectedForCrossingBonds() {
        structure.addBond(new WeakBond(1, 5));
        structure.addBond(new WeakBond(3, 8));
        assertTrue(structure.isPseudoknotted());
    }

    // -----------------------------------------------------------------------
    // checkBasePairs
    // -----------------------------------------------------------------------

    @Test
    void validBasePairsPassCheck() {
        // A-U, G-C, G-U (wobble) sono tutti validi
        structure.setSequence("AUGCGU");
        structure.setSize(6);
        structure.addBond(new WeakBond(1, 2)); // A-U
        structure.addBond(new WeakBond(3, 4)); // G-C
        assertDoesNotThrow(() -> structure.checkBasePairs());
    }

    @Test
    void invalidBasePairThrows() {
        structure.setSequence("AAGC");
        structure.setSize(4);
        structure.addBond(new WeakBond(1, 2)); // A-A -> non valida
        assertThrows(RNAInputFileParserException.class, () -> structure.checkBasePairs());
    }

    @Test
    void checkBasePairsWithNullSequenceDoesNothing() {
        // sequence null -> il metodo esce subito senza lanciare eccezioni
        structure.setSize(4);
        structure.addBond(new WeakBond(1, 4));
        assertDoesNotThrow(() -> structure.checkBasePairs());
    }

    // -----------------------------------------------------------------------
    // equals
    // -----------------------------------------------------------------------

    @Test
    void equalStructuresAreEqual() {
        RNASecondaryStructure s1 = new RNASecondaryStructure();
        s1.setSequence("AUGC");
        s1.setSize(4);
        s1.addBond(new WeakBond(1, 4));
        s1.finalise();

        RNASecondaryStructure s2 = new RNASecondaryStructure();
        s2.setSequence("AUGC");
        s2.setSize(4);
        s2.addBond(new WeakBond(1, 4));
        s2.finalise();

        assertEquals(s1, s2);
    }
}
