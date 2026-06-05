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
