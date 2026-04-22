package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnafile.RNAInputFileParserException;

import java.util.*;

/**
 * Representation of an RNA secondary structure that may contain any kind of pseudoknot.
 * <p>
 * It consists of an optional nucleotide primary sequence and a list of weak bonds
 * given as pairs of positions in the primary sequence. Positions start at 1 and end
 * at the length of the primary sequence.
 * </p>
 *
 * @author Luca Tesei, Piero Hierro, Piermichele Rosati
 */
public class RNASecondaryStructure {

    // primary structure; when null this secondary structure has only the structural
    // representation. It can still be used to generate the structural RNA tree and
    // aligned with another structure.
    private String sequence;

    // list of the weak bonds of this structure
    private List<WeakBond> bonds;

    // length of the sequence; if this structure has no sequence a lower bound
    // is inferred from the bonds
    private int size;

    // auxiliary array to represent weak bonds with pointers; allows constant‑time
    // access to bond partners. 0 means no bond; valid indices start at 1.
    // If p[i] = j and i < j then there is a weak bond (i,j), otherwise (j,i).
    // Each position can be involved in at most one bond, so a single array suffices.
    private int[] p;

    private List<NonCanonicalEdgeFamily> edgeFamilies;

    /**
     * Creates an empty secondary structure.
     */
    public RNASecondaryStructure() {
        this.sequence = null;
        this.size = -1;
        this.bonds = new ArrayList<>();
        this.p = null;
        this.edgeFamilies = new ArrayList<>();
    }

    /**
     * Returns the primary nucleotide sequence, if available.
     *
     * @return the sequence string, or {@code null} if not set
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the primary nucleotide sequence for this structure.
     *
     * @param sequence the sequence string
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Returns the length of the structure.
     *
     * @return the number of nucleotides (size)
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the length of the structure.
     *
     * @param size the size (must be positive)
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Returns the list of weak bonds in this structure.
     *
     * @return an unmodifiable view? (currently returns the internal list;
     *         consider defensive copying)
     */
    public List<WeakBond> getBonds() {
        return bonds;
    }

    /**
     * Adds a weak bond to this structure.
     *
     * @param b the bond to add
     * @throws RNAInputFileParserException if the bond's indices conflict with existing bonds
     *                                     or exceed the structure size (when a sequence is present)
     */
    public void addBond(WeakBond b) {
        // check if the indexes of the new bond are already present in the current
        // list of bonds
        for (WeakBond wb : this.bonds)
            if (b.getLeft() == wb.getLeft())
                throw new RNAInputFileParserException("Weak Bond left index " + b.getLeft() + " is equal to bond ("
                        + wb.getLeft() + ", " + wb.getRight() + ") left index");
            else if (b.getLeft() == wb.getRight())
                throw new RNAInputFileParserException("Weak Bond left index " + b.getLeft() + " is equal to bond ("
                        + wb.getLeft() + ", " + wb.getRight() + ") right index");
            else if (b.getRight() == wb.getLeft())
                throw new RNAInputFileParserException("Weak Bond right index " + b.getRight() + " is equal to bond ("
                        + wb.getLeft() + ", " + wb.getRight() + ") left index");
            else if (b.getRight() == wb.getRight())
                throw new RNAInputFileParserException("Weak Bond right index " + b.getRight() + " is equal to bond ("
                        + wb.getLeft() + ", " + wb.getRight() + ") right index");
        // check or increase right limit
        if (!Objects.equals(this.sequence, "") && this.sequence != null) { // Sequence still not set
            // the size is fixed to the length of the sequence
            if (b.getRight() > this.size)
                throw new RNAInputFileParserException(
                        "Weak Bond right index " + b.getRight() + " is greater than the structure size " + this.size);
        } else
            // the size could increase by adding bonds because
            // in this structure the sequence was not set
            if (b.getRight() > this.size)
                this.size = b.getRight();
        // checks done: add the bond
        this.bonds.add(b);
    }

    /**
     * Determines whether this secondary structure contains a pseudoknot.
     *
     * @return {@code true} if at least two weak bonds cross each other, {@code false} otherwise
     */
    public boolean isPseudoknotted() {
        for (int i = 0; i < this.bonds.size(); i++)
            for (int j = i + 1; j < this.bonds.size(); j++)
                if (this.bonds.get(i).crossesWith(this.bonds.get(j)))
                    return true;
        return false;
    }

    /**
     * Verifies that all weak bonds in this structure correspond to canonical Watson‑Crick
     * or wobble base pairs, provided a sequence has been set.
     *
     * @throws RNAInputFileParserException if any bond does not match the allowed pairs
     */
    public void checkBasePairs() {
        if (this.sequence == null)
            // do nothing
            return;
        // check all the pairs
        for (WeakBond b : this.bonds) {
            // base pair check
            int index1 = b.getLeft() - 1; // adjustment of indexes wrt the zero-starting indexes of strings
            int index2 = b.getRight() - 1;
            switch (this.sequence.charAt(index1)) {
                case 'A':
                    if (this.sequence.charAt(index2) != 'U')
                        throw new RNAInputFileParserException("Base pair not allowed in RNA: " + this.sequence.charAt(index1)
                                + "-" + this.sequence.charAt(index2) + " at weak bond (" + b.getLeft() + ", " + b.getRight() + ")");
                    break;
                case 'U':
                    if (this.sequence.charAt(index2) != 'A' && this.sequence.charAt(index2) != 'G')
                        throw new RNAInputFileParserException("Base pair not allowed in RNA: " + this.sequence.charAt(index1)
                                + "-" + this.sequence.charAt(index2) + " at weak bond (" + b.getLeft() + ", " + b.getRight() + ")");
                    break;
                case 'C':
                    if (this.sequence.charAt(index2) != 'G')
                        throw new RNAInputFileParserException("Base pair not allowed in RNA: " + this.sequence.charAt(index1)
                                + "-" + this.sequence.charAt(index2) + " at weak bond (" + b.getLeft() + ", " + b.getRight() + ")");
                    break;
                case 'G':
                    if (this.sequence.charAt(index2) != 'C' && this.sequence.charAt(index2) != 'U')
                        throw new RNAInputFileParserException("Base pair not allowed in RNA: " + this.sequence.charAt(index1)
                                + "-" + this.sequence.charAt(index2) + " at weak bond (" + b.getLeft() + ", " + b.getRight() + ")");
                    break;
            }
        }
    }

    /**
     * Finalises the structure after all bonds have been added.
     * <p>
     * This method sorts the bonds, validates the size, and builds the pointer array
     * {@code p} for fast bond lookup.
     * </p>
     *
     * @throws RNAInputFileParserException if the structure size is still undefined
     */
    public void finalise() {
        Collections.sort(this.bonds);
        if (this.size == -1)
            throw new RNAInputFileParserException(
                    "Error in determining the size of the secondary structure");
        // create the array p
        this.p = new int[this.size + 1]; // position 0 is not used
        // initialise the array p
        for (WeakBond b : this.bonds) {
            p[b.getLeft()] = b.getRight();
            p[b.getRight()] = b.getLeft();
        }
    }

    /**
     * Returns the pointer array that maps each position to its bonded partner.
     * A value of 0 indicates no bond.
     *
     * @return the internal {@code p} array (1‑based indexing)
     */
    public int[] getP() {
        return this.p;
    }

    /**
     * Compares this secondary structure with another object for equality.
     *
     * @param o the object to compare
     * @return {@code true} if the structures have the same size, sequence, bonds, and pointer array
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RNASecondaryStructure that)) return false;
        return getSize() == that.getSize() && Objects.equals(getSequence(), that.getSequence()) && Objects.equals(getBonds(), that.getBonds()) && Arrays.equals(getP(), that.getP());
    }

    /**
     * Returns a hash code value for this structure.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(getSequence(), getBonds(), getSize());
        result = 31 * result + Arrays.hashCode(getP());
        return result;
    }

    /**
     * Returns a string representation of this secondary structure.
     *
     * @return a descriptive string
     */
    @Override
    public String toString() {
        return "RNASecondaryStructure{" +
                "sequence='" + sequence + '\'' +
                ", bonds=" + bonds +
                ", size=" + size +
                ", p=" + Arrays.toString(p) +
                '}';
    }

    /**
     * Replaces the list of weak bonds with the given list.
     *
     * @param bonds the new list of weak bonds
     */
    public void setBonds(List<WeakBond> bonds) {
        this.bonds = bonds;
    }

    /**
     * Returns the list of non‑canonical edge families associated with this structure.
     *
     * @return the edge families list
     */
    public List<NonCanonicalEdgeFamily> getEdgeFamilies() {
        return edgeFamilies;
    }

    /**
     * Sets the list of non‑canonical edge families.
     *
     * @param edgeFamilies the new edge families list
     * @return the updated list (for chaining)
     */
    public List<NonCanonicalEdgeFamily> setEdgeFamilies(List<NonCanonicalEdgeFamily> edgeFamilies) {
        return this.edgeFamilies = edgeFamilies;
    }
}