# RNA2DFormatIO — Library User Guide

**Version:** 0.0.1  
**Group:** `it.unicam.cs.bdslab`  
**Artifact:** `rna2d-format-IO`  
**Java:** 21+  
**Repository:** BDSLab — University of Camerino

---

## Table of Contents

1. [Overview](#1-overview)
2. [Requirements and Dependencies](#2-requirements-and-dependencies)
3. [Project Integration](#3-project-integration)
4. [Supported Formats](#4-supported-formats)
    - 4.1 [AAS — Arc Annotated Sequence](#41-aas--arc-annotated-sequence)
    - 4.2 [AAS NO SEQUENCE](#42-aas-no-sequence)
    - 4.3 [BPSEQ](#43-bpseq)
    - 4.4 [CT — Connect Format](#44-ct--connect-format)
    - 4.5 [DB — Dot-Bracket Notation](#45-db--dot-bracket-notation)
    - 4.6 [DB NO SEQUENCE](#46-db-no-sequence)
    - 4.7 [FASTA](#47-fasta)
5. [Library Architecture](#5-library-architecture)
6. [Loading Files](#6-loading-files)
    - 6.1 [Loading a Single File](#61-loading-a-single-file)
    - 6.2 [Loading a Directory](#62-loading-a-directory)
    - 6.3 [Building an RNAFile Without Adding it to the Controller](#63-building-an-rnafile-without-adding-it-to-the-controller)
7. [Format Translation](#7-format-translation)
    - 7.1 [Conversion Matrix](#71-conversion-matrix)
    - 7.2 [Translating a File](#72-translating-a-file)
    - 7.3 [Querying Available Translations](#73-querying-available-translations)
8. [Saving Files](#8-saving-files)
    - 8.1 [Save Options](#81-save-options)
    - 8.2 [Generated Output](#82-generated-output)
9. [Structure Statistics](#9-structure-statistics)
10. [Managing Loaded Files](#10-managing-loaded-files)
11. [Exception Handling](#11-exception-handling)
12. [Complete Practical Examples](#12-complete-practical-examples)
    - 12.1 [Reading and Inspecting a File](#121-reading-and-inspecting-a-file)
    - 12.2 [Converting BPSEQ to Dot-Bracket](#122-converting-bpseq-to-dot-bracket)
    - 12.3 [Batch Conversion of a Directory](#123-batch-conversion-of-a-directory)
    - 12.4 [Saving with Statistics and ZIP](#124-saving-with-statistics-and-zip)
13. [Notes and Limitations](#13-notes-and-limitations)

---

## 1. Overview

**RNA2DFormatIO** is a Java library for reading, writing, and converting files that describe RNA secondary structures. It automatically detects the format of an input file through an ANTLR4-generated parser and allows translation into any other supported format.

Main features:

- **Automatic parsing** of RNA files in AAS, BPSEQ, CT, Dot-Bracket, and FASTA formats.
- **Format translation** with a single method call.
- **Batch loading** of entire directories, with format consistency enforcement.
- **Saving** converted files, with optional generation of statistics and ZIP archives.
- **Structure statistics** computation (nucleotide counts, GC/AU/GU bonds, etc.).

---

## 2. Requirements and Dependencies

| Requirement | Minimum Version |
|-------------|----------------|
| Java        | 21             |
| Maven       | 3.6+           |
| ANTLR4 runtime | 4.13.2      |
| jsoup       | 1.15.4         |
| JUnit Jupiter (test only) | 6.0.1 |

The ANTLR4 parser is generated automatically by Maven during the `generate-sources` phase from the `RNASecondaryStructure.g4` grammar file. No manual generation is required.

---

## 3. Project Integration

Add the dependency to the `pom.xml` of the project that wants to use the library (after installing the JAR into the local repository with `mvn install`):

```xml
<dependency>
    <groupId>it.unicam.cs.bdslab</groupId>
    <artifactId>rna2d-format-IO</artifactId>
    <version>0.0.1</version>
</dependency>
```

To build and install the library locally:

```bash
mvn clean install
```

---

## 4. Supported Formats

The library recognizes and handles the following formats through the `RNAFormat` enum.

### 4.1 AAS — Arc Annotated Sequence

Represents weak bonds as a list of index pairs. The nucleotide sequence is optional and precedes the bond list.

```
ACGUACGU
(1,8);(2,7);(3,6)
```

Pairs are separated by `;` or `,`. Indices start at 1.

### 4.2 AAS NO SEQUENCE

Identical to AAS but without the nucleotide sequence:

```
(1,8);(2,7);(3,6)
```

### 4.3 BPSEQ

A three-column format: position index, nucleotide, bonding partner (0 if unpaired).

```
1 A 8
2 C 7
3 G 6
4 U 0
5 A 0
6 C 3
7 G 2
8 U 1
```

Optionally supports four header lines starting with `Filename`, `Organism`, `Accession`, `Citation`.

### 4.4 CT — Connect Format

A six-column format. The header line must contain the word `ENERGY`, `Energy`, or `dG`.

```
8 ENERGY = -3.5
1 A 0 2 8 1
2 C 1 3 7 2
3 G 2 4 6 3
4 U 3 5 0 4
5 A 4 6 0 5
6 C 5 7 3 6
7 G 6 8 2 7
8 U 7 1 1 8
```

### 4.5 DB — Dot-Bracket Notation

The nucleotide sequence (optional) precedes the structure. The structure uses `.` for unpaired bases and matching bracket pairs for bonds. Supports extended notation for pseudoknots.

```
ACGUACGU
(((...)))
```

Extended notation (pseudoknots):

```
ACGUACGUACGU
((((AAAA))))aaaa
```

Supported bracket symbols: `()`, `[]`, `{}`, `<>`, and matching uppercase/lowercase letter pairs.

### 4.6 DB NO SEQUENCE

Like DB but without the nucleotide sequence:

```
(((...)))
```

### 4.7 FASTA

Standard bioinformatics format. Contains only the nucleotide sequence, with no structural information.

```
>sequence_name
ACGUACGUACGU
```

> **Note:** FASTA carries no bond information, so it **cannot be translated** into any structural format.

---

## 5. Library Architecture

The library is organized into three layers:

```
controller/
  IOController          ← file loading and saving (Singleton)
  TranslatorController  ← format translation (Singleton)

model/
  rnafile/
    RNAFile             ← representation of a loaded RNA file
    RNAFormat           ← enum of supported formats
    RNAFileConstructor  ← builds an RNAFile from disk (Singleton, ANTLR)
    RNAFileTranslator   ← translation logic between formats (static methods)
    RNAInputFileParserException ← parsing exception

  rnastructure/
    RNASecondaryStructure   ← secondary structure model
    WeakBond                ← index pair representing a bond
    NonCanonicalEdgeFamily  ← non-canonical edge family
    NonCanonicalEdgeFamilyValues ← allowed values for edge families

  utils/
    Region                  ← region used in the pseudoknot algorithm
    RNAStatisticsCalculator ← structure statistics computation
```

The main entry points for library users are `IOController` and `TranslatorController`.

---

## 6. Loading Files

### 6.1 Loading a Single File

```java
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.controller.IOController;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFile;
import java.nio.file.Path;

IOController io = IOController.getInstance();

RNAFile file = io.loadFile(Path.of("structure.bpseq"));

System.out.println("Detected format: " + file.getFormat());
System.out.println("Nucleotides: " + file.getStructure().getSize());
```

`loadFile` adds the file to the internal list and sets the recognized format. If a file with a different format is loaded while others are already present, an `IllegalArgumentException` is thrown.

### 6.2 Loading a Directory

```java
io.loadDirectory(Path.of("/data/structures/"));

List<RNAFile> files = io.getLoadedRNAFiles();
System.out.println("Loaded files: " + files.size());
System.out.println("Shared format: " + io.getRecognizedFormat());
```

The method automatically ignores:
- hidden files (whose name starts with `.`)
- files inside subdirectories whose name contains a dot

All files in a directory must share the same format; otherwise an `IllegalArgumentException` is thrown on the first non-conforming file.

### 6.3 Building an RNAFile Without Adding it to the Controller

To parse a file without adding it to the controller's managed list:

```java
RNAFile file = io.getRNAFileOf(Path.of("single_structure.db"));
```

The file is parsed and returned but is not added to `loadedRNAFiles`.

---

## 7. Format Translation

### 7.1 Conversion Matrix

Not all translations are possible. The table below shows the supported targets for each source format:

| Source Format   | Available Targets |
|-----------------|-------------------|
| AAS             | AAS NO SEQUENCE, BPSEQ, CT, DB, DB NO SEQUENCE, FASTA |
| AAS NO SEQUENCE | DB NO SEQUENCE |
| BPSEQ           | AAS, AAS NO SEQUENCE, CT, DB, DB NO SEQUENCE, FASTA |
| CT              | AAS, AAS NO SEQUENCE, BPSEQ, DB, DB NO SEQUENCE, FASTA |
| DB              | AAS, AAS NO SEQUENCE, BPSEQ, CT, DB NO SEQUENCE, FASTA |
| DB NO SEQUENCE  | AAS NO SEQUENCE |
| FASTA           | *(no translation available)* |

> AAS and DB **cannot be translated into themselves** (self-conversion is excluded from the list).

### 7.2 Translating a File

```java
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.controller.TranslatorController;
import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAFormat;

TranslatorController translator = TranslatorController.getInstance();

RNAFile source = io.loadFile(Path.of("structure.bpseq"));
RNAFile translated = translator.translate(source, RNAFormat.DB);

System.out.println("New format: " + translated.getFormat());
System.out.println(String.join("\n", translated.getContent()));
```

`translate` returns a **new** `RNAFile` object; the original file is not modified.

### 7.3 Querying Available Translations

Before translating, you can check which target formats are compatible:

```java
List<RNAFormat> available = translator.getAvailableTranslations(RNAFormat.BPSEQ);
available.forEach(System.out::println);
```

---

## 8. Saving Files

### 8.1 Save Options

```java
io.saveFiles(
    files,                  // List<RNAFile> to save
    Path.of("/output/"),    // destination directory
    true,                   // generate non-canonical pairs CSV?
    true,                   // generate statistics CSV?
    "results"               // ZIP file name (null or "" to skip zipping)
);
```

The method accepts any `List<RNAFile>`, not necessarily those loaded through `IOController`. Translated files can therefore be saved directly:

```java
List<RNAFile> translated = io.getLoadedRNAFiles().stream()
        .map(f -> translator.translate(f, RNAFormat.DB))
        .toList();

io.saveFiles(translated, Path.of("/output/"), false, true, "output_db");
```

### 8.2 Generated Output

For each saved `RNAFile`, an output file is created with the following naming convention:

```
<original_name_without_extension>.<format_extension>.txt
```

For example, `structure.bpseq` translated to DB produces `structure.db.txt`.

If `generateNonCanonicalPairs` is `true`, CSV files with the `_nc.csv` suffix containing detected non-canonical pairs are also included in the ZIP.

If `generateStatistics` is `true`, CSV files with the `_seqInfo.csv` suffix are included, with the following columns:

```
Nucleotide count, Bond count, A count, C count, G count, U count, GC bonds, AU bonds, GU bonds
```

If `zipFileName` is non-blank, all generated files are compressed into a single ZIP archive and the intermediate files are deleted.

---

## 9. Structure Statistics

The `RNAStatisticsCalculator` class exposes static methods to analyze an `RNAFile`:

```java
import static it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.utils.RNAStatisticsCalculator.*;

RNAFile file = io.loadFile(Path.of("structure.bpseq"));

System.out.println("Total nucleotides : " + getNucleotideCount(file));
System.out.println("Total bonds       : " + getBondCount(file));
System.out.println("GC bonds          : " + getGcBonds(file));
System.out.println("AU bonds          : " + getAuBonds(file));
System.out.println("GU bonds          : " + getGuBonds(file));
System.out.println("A count           : " + getACount(file));
System.out.println("C count           : " + getCCount(file));
System.out.println("G count           : " + getGCount(file));
System.out.println("U count           : " + getUCount(file));
```

> **Warning:** statistics on nucleotide and bond types require the nucleotide sequence to be present. For `DB_NO_SEQUENCE`, `AAS_NO_SEQUENCE`, and `FASTA` formats, these methods return `0`.

---

## 10. Managing Loaded Files

### Retrieving the List of Loaded Files

```java
List<RNAFile> loaded = io.getLoadedRNAFiles(); // returns an unmodifiable copy
```

### Removing a Single File

```java
io.deleteFile(aFile);
// If no files remain, the recognized format is reset to null
```

### Fully Resetting the Controller

```java
io.clearAllDataStructures();
// Clears the list and resets the recognized format
```

This is useful when reusing the singleton instance for a new batch of files in a different format.

---

## 11. Exception Handling

| Exception | When it is thrown |
|-----------|-------------------|
| `IOException` | I/O error while reading or writing files |
| `IllegalArgumentException` | A file with a different format from the already-loaded ones is loaded |
| `RNAInputFileParserException` (unchecked) | The file does not conform to the expected format syntax, or a weak bond has invalid indices |

Robust handling example:

```java
try {
    RNAFile file = io.loadFile(Path.of("structure.bpseq"));
    RNAFile translated = translator.translate(file, RNAFormat.DB);
    io.saveFiles(List.of(translated), Path.of("/output/"), false, false, "");
} catch (IOException e) {
    System.err.println("I/O error: " + e.getMessage());
} catch (IllegalArgumentException e) {
    System.err.println("Incompatible format: " + e.getMessage());
} catch (RNAInputFileParserException e) {
    System.err.println("Parsing error: " + e.getMessage());
}
```

---

## 12. Complete Practical Examples

### 12.1 Reading and Inspecting a File

```java
IOController io = IOController.getInstance();
RNAFile file = io.loadFile(Path.of("example.bpseq"));

System.out.println("File: " + file.getFileName());
System.out.println("Format: " + file.getFormat().getName());
System.out.println("Structure size: " + file.getStructure().getSize());
System.out.println("Has pseudoknots: " + file.getStructure().isPseudoknotted());
System.out.println("Sequence: " + file.getStructure().getSequence());

System.out.println("\nFull content:");
file.getContent().forEach(System.out::println);
```

### 12.2 Converting BPSEQ to Dot-Bracket

```java
IOController io = IOController.getInstance();
TranslatorController translator = TranslatorController.getInstance();

RNAFile source = io.loadFile(Path.of("structure.bpseq"));
RNAFile dotBracket = translator.translate(source, RNAFormat.DB);

System.out.println("Structure in Dot-Bracket:");
dotBracket.getContent().forEach(System.out::println);
```

### 12.3 Batch Conversion of a Directory

```java
IOController io = IOController.getInstance();
TranslatorController translator = TranslatorController.getInstance();

// Load all BPSEQ files from the directory
io.loadDirectory(Path.of("/data/bpseq/"));

// Translate all to DB
List<RNAFile> translated = io.getLoadedRNAFiles().stream()
        .map(f -> translator.translate(f, RNAFormat.DB))
        .toList();

// Save results to the output directory
io.saveFiles(translated, Path.of("/data/output/"), false, false, "");

System.out.println("Converted " + translated.size() + " files.");
```

### 12.4 Saving with Statistics and ZIP

```java
IOController io = IOController.getInstance();
TranslatorController translator = TranslatorController.getInstance();

io.loadDirectory(Path.of("/data/ct/"));

List<RNAFile> translated = io.getLoadedRNAFiles().stream()
        .map(f -> translator.translate(f, RNAFormat.AAS))
        .toList();

// Produces: translated files + statistics CSVs + ZIP archive
io.saveFiles(
        translated,
        Path.of("/data/output/"),
        false,                  // no non-canonical pairs CSV
        true,                   // yes statistics CSV
        "batch_aas_with_stats"  // ZIP name
);
```

---

## 13. Notes and Limitations

**Singleton and thread safety.** `IOController` and `TranslatorController` are stateful singletons (`loadedRNAFiles`, `recognizedFormat`). They are not thread-safe: avoid concurrent access without external synchronization.

**Single format per session.** `IOController` enforces that all files loaded in the same session share the same format. To work with different formats sequentially, call `clearAllDataStructures()` between batches.

**FASTA format.** FASTA contains only the nucleotide sequence and carries no structural information (bonds). For this reason it cannot be translated into any structural format.

**Hidden files and subdirectories.** `loadDirectory` ignores files whose name starts with `.` and files inside subdirectories whose name contains a dot. Subdirectories themselves are not visited recursively.

**Non-canonical pairs.** Generating non-canonical pair CSV files depends on an external component that produces `.csv` files in the current working directory (`user.dir`). Make sure the working directory is writable.

**File extension irrelevance.** The format is detected from the file content via the ANTLR parser, **not from the extension**. A `.txt` file containing BPSEQ content will be correctly recognized as BPSEQ.

**1-based indexing.** All position indices in the secondary structure start at 1 (not 0), both internally and in the file formats.
