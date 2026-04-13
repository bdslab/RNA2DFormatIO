package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

/**
 * Class responsible for loading data contained in a CT (Connect) format file.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class CtDataLoader extends TableDataLoader {

    /**
     * Constructs a CT data loader with the appropriate column mappings.
     * <p>
     * For CT files:
     * <ul>
     *   <li>Column 0 (first) contains the current position index (base position)</li>
     *   <li>Column 1 contains the nucleotide character</li>
     *   <li>Column 4 (fifth) contains the paired position (0 if unpaired)</li>
     * </ul>
     * The base position (1-based) is used for indexing.
     * </p>
     */
    public CtDataLoader() {
        this.basePosition = 1;
        this.dimension = 6;
        this.pairOnePosition = 0;
        this.pairTwoPosition = 4;
    }

}