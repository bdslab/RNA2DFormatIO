package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure;

import java.util.Objects;

/**
 * Represents a non‑canonical edge family describing a tertiary interaction
 * between two nucleotides in an RNA structure.
 * <p>
 * Each instance holds the identities and positions of the interacting residues,
 * as well as the edge types and the cis/trans orientation of the interaction.
 * </p>
 */
public class NonCanonicalEdgeFamily {

    private String base_id_5p;
    private int base_id_5p_index;
    private String base_id_3p;
    private int base_id_3p_index;
    private NonCanonicalEdgeFamilyValues bond_type1;
    private NonCanonicalEdgeFamilyValues bond_type2;
    private NonCanonicalEdgeFamilyValues bondOrientation;

    /**
     * Constructs a non‑canonical edge family with the specified parameters.
     *
     * @param base_id_5p        the nucleotide character at the 5' side
     * @param base_id_5p_index  the 1‑based position of the 5' nucleotide
     * @param base_id_3p        the nucleotide character at the 3' side
     * @param base_id_3p_index  the 1‑based position of the 3' nucleotide
     * @param bond_type1        edge type for the 5' nucleotide
     * @param bond_type2        edge type for the 3' nucleotide
     * @param bondOrientation   cis/trans orientation of the interaction
     */
    public NonCanonicalEdgeFamily(String base_id_5p, int base_id_5p_index,
                                  String base_id_3p, int base_id_3p_index,
                                  NonCanonicalEdgeFamilyValues bond_type1,
                                  NonCanonicalEdgeFamilyValues bond_type2,
                                  NonCanonicalEdgeFamilyValues bondOrientation) {
        this.base_id_5p = base_id_5p;
        this.base_id_5p_index = base_id_5p_index;
        this.base_id_3p = base_id_3p;
        this.base_id_3p_index = base_id_3p_index;
        this.bond_type1 = bond_type1;
        this.bond_type2 = bond_type2;
        this.bondOrientation = bondOrientation;
    }

    /**
     * Returns the nucleotide character at the 5' side.
     *
     * @return the 5' nucleotide character (e.g., "A", "C", "G", "U")
     */
    public String getBase_id_5p() {
        return base_id_5p;
    }

    /**
     * Returns the 1‑based position of the 5' nucleotide.
     *
     * @return the 5' nucleotide index
     */
    public int getBase_id_5p_index() {
        return base_id_5p_index;
    }

    /**
     * Returns the nucleotide character at the 3' side.
     *
     * @return the 3' nucleotide character
     */
    public String getBase_id_3p() {
        return base_id_3p;
    }

    /**
     * Returns the 1‑based position of the 3' nucleotide.
     *
     * @return the 3' nucleotide index
     */
    public int getBase_id_3p_index() {
        return base_id_3p_index;
    }

    /**
     * Returns the edge type for the 5' nucleotide.
     *
     * @return the bond type of the 5' side
     */
    public NonCanonicalEdgeFamilyValues getBond_type1() {
        return bond_type1;
    }

    /**
     * Returns the edge type for the 3' nucleotide.
     *
     * @return the bond type of the 3' side
     */
    public NonCanonicalEdgeFamilyValues getBond_type2() {
        return bond_type2;
    }

    /**
     * Returns the cis/trans orientation of this interaction.
     *
     * @return {@link NonCanonicalEdgeFamilyValues#CIS} or
     *         {@link NonCanonicalEdgeFamilyValues#TRANS}
     */
    public NonCanonicalEdgeFamilyValues getBondOrientation() {
        return bondOrientation;
    }

    /**
     * Sets the nucleotide character at the 5' side.
     *
     * @param base_id_5p the new 5' nucleotide character
     */
    public void setBase_id_5p(String base_id_5p) {
        this.base_id_5p = base_id_5p;
    }

    /**
     * Sets the position of the 5' nucleotide.
     *
     * @param base_id_5p_index the new 1‑based index
     */
    public void setBase_id_5p_index(int base_id_5p_index) {
        this.base_id_5p_index = base_id_5p_index;
    }

    /**
     * Sets the nucleotide character at the 3' side.
     *
     * @param base_id_3p the new 3' nucleotide character
     */
    public void setBase_id_3p(String base_id_3p) {
        this.base_id_3p = base_id_3p;
    }

    /**
     * Sets the position of the 3' nucleotide.
     *
     * @param base_id_3p_index the new 1‑based index
     */
    public void setBase_id_3p_index(int base_id_3p_index) {
        this.base_id_3p_index = base_id_3p_index;
    }

    /**
     * Sets the edge type for the 5' nucleotide.
     *
     * @param bond_type1 the new bond type
     */
    public void setBond_type1(NonCanonicalEdgeFamilyValues bond_type1) {
        this.bond_type1 = bond_type1;
    }

    /**
     * Sets the edge type for the 3' nucleotide.
     *
     * @param bond_type2 the new bond type
     */
    public void setBond_type2(NonCanonicalEdgeFamilyValues bond_type2) {
        this.bond_type2 = bond_type2;
    }

    /**
     * Sets the cis/trans orientation of the interaction.
     *
     * @param bondOrientation the new orientation
     */
    public void setBondOrientation(NonCanonicalEdgeFamilyValues bondOrientation) {
        this.bondOrientation = bondOrientation;
    }

    /**
     * Compares this edge family to another object for equality.
     * Two edge families are considered equal if they have the same nucleotide
     * characters, positions, edge types, and orientation.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NonCanonicalEdgeFamily that = (NonCanonicalEdgeFamily) o;
        return getBase_id_5p_index() == that.getBase_id_5p_index() &&
                getBase_id_3p_index() == that.getBase_id_3p_index() &&
                Objects.equals(getBase_id_5p(), that.getBase_id_5p()) &&
                Objects.equals(getBase_id_3p(), that.getBase_id_3p()) &&
                getBond_type1() == that.getBond_type1() &&
                getBond_type2() == that.getBond_type2() &&
                getBondOrientation() == that.getBondOrientation();
    }

    /**
     * Returns a hash code value for this edge family.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getBase_id_5p(), getBase_id_5p_index(),
                getBase_id_3p(), getBase_id_3p_index(),
                getBond_type1(), getBond_type2(), getBondOrientation());
    }

    /**
     * Returns a string representation of this edge family.
     *
     * @return a descriptive string containing all field values
     */
    @Override
    public String toString() {
        return "NonCanonicalEdgeFamily{" +
                "base_id_5p='" + base_id_5p + '\'' +
                ", base_id_5p_index=" + base_id_5p_index +
                ", base_id_3p='" + base_id_3p + '\'' +
                ", base_id_3p_index=" + base_id_3p_index +
                ", bond_type1=" + bond_type1 +
                ", bond_type2=" + bond_type2 +
                ", bondOrientation=" + bondOrientation +
                '}';
    }
}