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

import it.unicam.cs.bdslab.RNASecondaryStructureLexer;
import it.unicam.cs.bdslab.RNASecondaryStructureParser;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.listener.RNAFileListener;
import java.io.IOException;
import java.nio.file.Path;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Singleton responsible for constructing {@link RNAFile} instances from input files
 * using the ANTLR-generated parser for RNA secondary structure formats.
 *
 * @author Piero Hierro, Piermichele Rosati
 */
public class RNAFileConstructor {

    private static RNAFileConstructor instance;
    private final ParseTreeWalker walker;
    private final RNAFileListener rnaFilelistener;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private RNAFileConstructor() {
        // Create a generic parse tree walker that can trigger callbacks
        this.walker = new ParseTreeWalker();
        // Create the specialised listener for the RNA secondary structure
        this.rnaFilelistener = new RNAFileListener();
    }

    /**
     * Factory method to obtain the singleton {@link RNAFileConstructor} instance.
     *
     * @return the unique instance of this constructor
     */
    public static RNAFileConstructor getInstance() {
        if (instance == null) instance = new RNAFileConstructor();
        return instance;
    }

    /**
     * Parses the specified file and builds the corresponding {@link RNAFile}.
     *
     * @param filePath path to the RNA file to parse
     * @return an {@code RNAFile} instance containing the parsed content
     * @throws IOException if an I/O error occurs while reading the file
     */
    public RNAFile construct(Path filePath) throws IOException {
        CharStream input = CharStreams.fromFileName(String.valueOf(filePath));
        // create a lexer that feeds off of input CharStream
        RNASecondaryStructureLexer lexer = new RNASecondaryStructureLexer(input);
        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer
        RNASecondaryStructureParser structureParser = new RNASecondaryStructureParser(tokens);
        // begin parsing at rna rule
        ParseTree tree = structureParser.rna_format();
        this.rnaFilelistener.setFilePath(filePath);
        // Walk the tree created during the parse, trigger callbacks
        this.walker.walk(rnaFilelistener, tree);
        return this.rnaFilelistener.getRnaFile();
    }
}
