package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.utils;

import it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure.WeakBond;

/**
 * Represents a region (a base pair) used in the pseudo‑algorithm described in
 * <a href="https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5905660/">PMC5905660</a>.
 * <p>
 * Each region wraps a {@link WeakBond} and maintains an order level used for
 * pseudoknot resolution and dot‑bracket generation.
 * </p>
 */
public class Region {

    private final WeakBond weakBond;
    private int order;

    /**
     * Constructs a region for the given weak bond with an initial order of zero.
     *
     * @param weakBond the base pair represented by this region
     */
    public Region(WeakBond weakBond) {
        this.weakBond = weakBond;
        this.order = 0;
    }

    /**
     * Returns the weak bond associated with this region.
     *
     * @return the underlying {@link WeakBond}
     */
    public WeakBond getWeakBond() {
        return this.weakBond;
    }

    /**
     * Returns the order level of this region.
     *
     * @return the current order value
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * Sets the order level for this region.
     *
     * @param order the new order value
     */
    public void setOrder(int order) {
        this.order = order;
    }
}