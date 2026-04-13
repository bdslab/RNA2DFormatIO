package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model;

/**
 * Class representing a single ribonucleotide within an RNA chain.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaRibonucleotide {

    private int chainId;
    private int position;
    private RnaBase base;

    /**
     * Constructs a ribonucleotide with the specified chain identifier, position, and base.
     *
     * @param chainId  the identifier of the chain this ribonucleotide belongs to
     * @param position the position of this ribonucleotide within the chain (1-based)
     * @param base     the RNA base type
     */
    public RnaRibonucleotide(int chainId, int position, RnaBase base) {
        this.chainId = chainId;
        this.position = position;
        this.base = base;
    }

    /**
     * Returns the identifier of the chain this ribonucleotide belongs to.
     *
     * @return the chain identifier
     */
    public int getchainId() {
        return chainId;
    }

    /**
     * Returns the position of this ribonucleotide within its chain (1-based).
     *
     * @return the position index
     */
    public int getPosition() {
        return position;
    }

    /**
     * Returns the RNA base type of this ribonucleotide.
     *
     * @return the RnaBase enum value
     */
    public RnaBase getBase() {
        return base;
    }

    /**
     * Returns the single-letter representation of this ribonucleotide's base.
     *
     * @return the character code for the base (e.g., 'A', 'U', 'C', 'G')
     */
    @Override
    public String toString() {
        return RnaBase.getBaseLetter(base) + "";
    }

}