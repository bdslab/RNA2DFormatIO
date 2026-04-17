# RNA2DFormatIO

**RNA2DFormatIO** is a Java library for parsing, converting, and analyzing RNA secondary structure file formats commonly used in bioinformatics.

It is designed for researchers and developers working with RNA structure data who need reliable format interoperability and lightweight structural analysis.

---

## 🧬 Overview

RNA secondary structures are represented in multiple incompatible formats across tools and databases (e.g., BPSEQ, CT, Dot-Bracket).  
**RNA2DFormatIO** provides a unified interface to:

- Automatically **detect input formats**
- Convert structures between representations
- Process large datasets in batch
- Compute basic structural statistics

The library uses an **ANTLR4-based parser** to ensure robust and formal parsing of supported formats.

---

## 🔬 Supported Formats

| Format | Description |
|------|-------------|
| **AAS** | Arc Annotated Sequence (bond pairs) |
| **AAS_NO_SEQUENCE** | AAS without nucleotide sequence |
| **BPSEQ** | Position-based pairing format |
| **CT** | Connect Table (energy-based header) |
| **Dot-Bracket (DB)** | Standard structural notation (supports pseudoknots) |
| **DB_NO_SEQUENCE** | Dot-Bracket without sequence |
| **FASTA** | Sequence only (no structural information) |

> **Note:** FASTA files cannot be converted into structural formats due to lack of base-pair information.

---

## ⚙️ Requirements

- Java **21+**
- Maven **3.6+**

---

## 📦 Installation

Build the library locally:

```bash
mvn clean install
```
Add dependency:
```xml
<dependency>
    <groupId>it.unicam.cs.bdslab</groupId>
    <artifactId>rna2d-format-IO</artifactId>
    <version>0.0.1</version>
</dependency>
```
---
## 🚀 Basic Usage
### Load an RNA Structure
```java
IOController io = IOController.getInstance();

RNAFile file = io.loadFile(Path.of("structure.bpseq"));

System.out.println("Format: " + file.getFormat());
System.out.println("Length: " + file.getStructure().getSize());
```
---
### Convert Between Formats
```java
TranslatorController translator = TranslatorController.getInstance();

RNAFile db = translator.translate(file, RNAFormat.DB);
```
---
### Batch Processing
```java
io.loadDirectory(Path.of("/data/bpseq/"));

List<RNAFile> converted = io.getLoadedRNAFiles().stream()
.map(f -> translator.translate(f, RNAFormat.CT))
.toList();
```
---
### Export Results
```java
io.saveFiles(
converted,
Path.of("/output/"),
false,   // non-canonical pairs
true,    // statistics
"dataset_results"
);
```
---
## 📊 Structural Statistics

The library provides basic quantitative descriptors of RNA structures:
- Total nucleotides 
- Number of base pairs 
- Base composition (A, C, G, U)
- Canonical pairs:
  - GC 
  - AU 
  - GU (wobble)

Example:
```java
getNucleotideCount(file);
getBondCount(file);
getGcBonds(file);
```
> Statistics requiring nucleotide identity are not available for formats without sequence data.
---
## 🔄 Format Conversion Capabilities

Not all conversions are biologically meaningful or possible.
- Structural formats (BPSEQ, CT, DB, AAS) are **inter-convertible** 
- Reduced formats (_NO_SEQUENCE) have limited conversion paths 
- **FASTA → structure conversion is not supported**
---
## 🧪 Typical Use Cases
- Converting datasets between RNA structure formats
- Preprocessing input for RNA folding or visualization tools
- Validating structural consistency across formats
- Computing basic statistics for comparative analysis
- Batch normalization of heterogeneous RNA datasets
---
## ⚠️ Limitations
- Controllers are implemented as **stateful singletons** (not thread-safe)
- Only **one format per session** is allowed when batch loading
- Directory loading is **non-recursive**
- Indexing is **1-based** (consistent with biological conventions)
- Non-canonical pair detection depends on external CSV generation
---
## 🧱 Architecture (Simplified)
```text
controller/
IOController
TranslatorController

model/
RNAFile
RNASecondaryStructure
RNAFormat
```
---
## ❗ Error Handling
- `IOException` → file system issues
- `IllegalArgumentException` → mixed formats in batch
- `RNAInputFileParserException` → invalid structure syntax
---
## 🏛️ Project Information
- `Group`: it.unicam.cs.bdslab
- `Version`: 0.0.1
- `Institution`: University of Camerino — BDSLab
---
## 📜 License
