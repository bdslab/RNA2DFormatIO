package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.exception.RnaParsingException;

/**
 * Class that contains all data related to a specific RNA file.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaMolecule {

    private List<RnaChain> chains;
    private String organism, accessionNumber, referenceLink;

    /**
     * Constructs an empty RNA molecule.
     */
    public RnaMolecule() {
        this.chains = new ArrayList<>();
    }

    /**
     * Adds an RNA chain to the molecule.
     *
     * @param chain the RnaChain to add
     */
    public void addchain(RnaChain chain) {
        this.chains.add(chain);
    }

    /**
     * Returns a copy of the list of chains in this molecule.
     *
     * @return a list of RnaChain objects
     */
    public List<RnaChain> getchains() {
        List<RnaChain> list = new ArrayList<>();
        list.addAll(this.chains);
        return list;
    }

    /**
     * Returns the organism name associated with this RNA molecule.
     *
     * @return the organism name, or null if not set
     */
    public String getOrganism() {
        return organism;
    }

    /**
     * Returns the accession number associated with this RNA molecule.
     *
     * @return the accession number, or null if not set
     */
    public String getAccessionNumber() {
        return accessionNumber;
    }

    /**
     * Returns the reference link associated with this RNA molecule.
     *
     * @return the reference link, or null if not set
     */
    public String getReferenceLink() {
        return referenceLink;
    }

    /**
     * Sets the organism name for this RNA molecule.
     *
     * @param organism the organism name
     */
    public void setOrganism(String organism) {
        this.organism = organism;
    }

    /**
     * Sets the accession number for this RNA molecule.
     *
     * @param accessionNumber the accession number
     */
    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    /**
     * Sets the reference link for this RNA molecule.
     *
     * @param referenceLink the reference link
     */
    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    /**
     * Validates the secondary structure references for all chains.
     * Throws an exception if a referenced position exceeds the chain length.
     *
     * @throws RnaParsingException if an invalid reference is found
     */
    public void checkSecondaryStructure() throws RnaParsingException {
        for (RnaChain chain : this.chains) {
            if (chain.getMaxReference() > getLength()) {
                throw new RnaParsingException(chain.getchainId(), chain.getMaxReference());
            }
        }
    }

    /**
     * Returns the total length of all chains in the molecule.
     *
     * @return the sum of the lengths of all chains
     */
    public int getLength() {
        return this.chains.stream().map(RnaChain::getLength).reduce(0, Integer::sum);
    }

    /**
     * Indicates whether tertiary structure data is present in any chain.
     *
     * @return true if at least one chain has tertiary data, false otherwise
     */
    public boolean haveTertiaryData() {
        return this.chains.stream().anyMatch(RnaChain::haveTertiaryData);
    }

    /**
     * Returns a consolidated list of tertiary structure descriptions from all chains.
     *
     * @return a list of string arrays describing tertiary pairs; empty if none exist
     */
    public List<String[]> getTertiaryStructure() {
        return this.chains.stream()
                .map(RnaChain::getTertiaryStructure)
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

}