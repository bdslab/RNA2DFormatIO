package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

/**
 * Class responsible for loading data contained in a BPSEQ format file.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class BpseqDataLoader extends TableDataLoader {

    /**
     * Constructs a BPSEQ data loader with the appropriate column mappings.
     * <p>
     * For BPSEQ files:
     * <ul>
     *   <li>Column 0 (first) contains the current position index (base position)</li>
     *   <li>Column 1 contains the nucleotide character</li>
     *   <li>Column 2 (third) contains the paired position (0 if unpaired)</li>
     * </ul>
     * The base position (1-based) is used for indexing.
     * </p>
     */
    public BpseqDataLoader() {
        this.basePosition = 1;
        this.dimension = 3;
        this.pairOnePosition = 0;
        this.pairTwoPosition = 2;
    }

}