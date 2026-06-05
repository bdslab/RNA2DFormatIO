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
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WeakBond}.
 */
class WeakBondTest {

    // -----------------------------------------------------------------------
    // Valid Construction
    // -----------------------------------------------------------------------

    @Test
    void constructionWithValidIndices() {
        WeakBond wb = new WeakBond(1, 5);
        assertEquals(1, wb.getLeft());
        assertEquals(5, wb.getRight());
    }

    @Test
    void constructionWithAdjacentIndices() {
        WeakBond wb = new WeakBond(3, 4);
        assertEquals(3, wb.getLeft());
        assertEquals(4, wb.getRight());
    }

    // -----------------------------------------------------------------------
    // Invalid Construction
    // -----------------------------------------------------------------------

    @Test
    void constructionWithLeftZeroThrows() {
        assertThrows(RNAInputFileParserException.class, () -> new WeakBond(0, 5));
    }

    @Test
    void constructionWithNegativeLeftThrows() {
        assertThrows(RNAInputFileParserException.class, () -> new WeakBond(-1, 5));
    }

    @Test
    void constructionWithLeftEqualToRightThrows() {
        assertThrows(RNAInputFileParserException.class, () -> new WeakBond(4, 4));
    }

    @Test
    void constructionWithLeftGreaterThanRightThrows() {
        assertThrows(RNAInputFileParserException.class, () -> new WeakBond(6, 3));
    }

    // -----------------------------------------------------------------------
    // crossesWith
    // -----------------------------------------------------------------------

    @Test
    void crossesWithDetectsCrossingBonds() {
        // (1,5) e (3,7): crosses because 1 < 3 < 5 < 7
        WeakBond wb1 = new WeakBond(1, 5);
        WeakBond wb2 = new WeakBond(3, 7);
        assertTrue(wb1.crossesWith(wb2));
        assertTrue(wb2.crossesWith(wb1));
    }

    @Test
    void crossesWithReturnsFalseForNestedBonds() {
        // (1,8) contains (3,6): do not cross
        WeakBond outer = new WeakBond(1, 8);
        WeakBond inner = new WeakBond(3, 6);
        assertFalse(outer.crossesWith(inner));
        assertFalse(inner.crossesWith(outer));
    }

    @Test
    void crossesWithReturnsFalseForDisjointBonds() {
        // (1,3) and (5,8): disjunct, do not cross
        WeakBond wb1 = new WeakBond(1, 3);
        WeakBond wb2 = new WeakBond(5, 8);
        assertFalse(wb1.crossesWith(wb2));
    }

    @Test
    void crossesWithNullThrows() {
        WeakBond wb = new WeakBond(1, 5);
        assertThrows(NullPointerException.class, () -> wb.crossesWith(null));
    }

    @Test
    void crossesWithEqualBondThrows() {
        WeakBond wb = new WeakBond(1, 5);
        WeakBond same = new WeakBond(1, 5);
        assertThrows(IllegalArgumentException.class, () -> wb.crossesWith(same));
    }

    // -----------------------------------------------------------------------
    // equals e hashCode
    // -----------------------------------------------------------------------

    @Test
    void equalBondsAreEqual() {
        WeakBond wb1 = new WeakBond(2, 6);
        WeakBond wb2 = new WeakBond(2, 6);
        assertEquals(wb1, wb2);
        assertEquals(wb1.hashCode(), wb2.hashCode());
    }

    @Test
    void differentBondsAreNotEqual() {
        WeakBond wb1 = new WeakBond(2, 6);
        WeakBond wb2 = new WeakBond(2, 7);
        assertNotEquals(wb1, wb2);
    }

    // -----------------------------------------------------------------------
    // compareTo
    // -----------------------------------------------------------------------

    @Test
    void compareToOrdersByRightIndex() {
        WeakBond smaller = new WeakBond(1, 4);
        WeakBond larger = new WeakBond(2, 9);
        assertTrue(smaller.compareTo(larger) < 0);
        assertTrue(larger.compareTo(smaller) > 0);
        assertEquals(0, smaller.compareTo(new WeakBond(3, 4)));
    }
}
