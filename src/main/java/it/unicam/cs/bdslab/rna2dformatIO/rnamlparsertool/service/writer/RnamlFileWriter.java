package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Class responsible for saving data in RNAML format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class RnamlFileWriter extends XmlFileWriter {

    /**
     * Root element of the XML document; in this case it will be an {@code <rnaml>} node.
     */
    private Element root;

    /**
     * Writes the given RNA molecule data to the specified file in RNAML format.
     *
     * @param chains the RNA molecule containing the data to write
     * @param path   the destination file path
     * @return {@code true} if the file was successfully written, {@code false} otherwise
     */
    @Override
    public boolean writeAndSave(RnaMolecule chains, String path) {
        createNewDocument();
        this.root = xmlDoc.createElement("rnaml");
        root.setAttribute("version", "1.0");
        xmlDoc.appendChild(root);
        for (RnaChain chain : chains.getchains()) {
            addchain(chain, chains.getAccessionNumber(), chains.getOrganism());
            setgetReferenceLink(chains.getReferenceLink(), chain.getchainId());
        }
        return save(path, "rnaml.dtd");
    }

    /**
     * Adds a chain to the root element of the XML document.
     *
     * @param chain          the RNA chain to add
     * @param accessionNumer the database accession number (may be {@code null})
     * @param organism       the organism name (may be {@code null})
     */
    private void addchain(RnaChain chain, String accessionNumer, String organism) {
        Element mol = xmlDoc.createElement("molecule");
        mol.setAttribute("id", "" + chain.getchainId());
        if (accessionNumer != null) {
            mol.setAttribute("database-ids", accessionNumer);
        }
        root.appendChild(mol);
        setIdentity(mol, organism);
        Element seq = xmlDoc.createElement("sequence");
        mol.appendChild(seq);
        Element seq_data = xmlDoc.createElement("seq-data");
        seq_data.appendChild(xmlDoc.createTextNode(sequenceStyle(chain.getSequence())));
        seq.appendChild(seq_data);
        Element struct = xmlDoc.createElement("structure");
        mol.appendChild(struct);
        Element model = xmlDoc.createElement("model");
        struct.appendChild(model);
        Element str_ann = xmlDoc.createElement("str-annotation");
        model.appendChild(str_ann);
        List<Entry<Integer, Integer>> pairs = chain.getPairMap().entrySet().stream()
                .map(x -> x.getKey() < x.getValue() ? x : new SimpleEntry<Integer, Integer>(x.getValue(), x.getKey()))
                .distinct().toList();
        for (Entry<Integer, Integer> pair : pairs) {
            Element base_pair = xmlDoc.createElement("base-pair");
            str_ann.appendChild(base_pair);
            addBase(base_pair, "base-id-5p", pair.getKey());
            addBase(base_pair, "base-id-3p", pair.getValue());
        }
    }

    /**
     * Sets the reference link element if a URL is provided.
     *
     * @param referenceLink the reference URL (may be {@code null})
     * @param chainId       the identifier of the associated chain
     */
    private void setgetReferenceLink(String referenceLink, int chainId) {
        if (referenceLink != null) {
            Element reference = xmlDoc.createElement("reference");
            reference.setAttribute("id", "" + chainId);
            root.appendChild(reference);
            Element pa = xmlDoc.createElement("path");
            reference.appendChild(pa);
            Element url = xmlDoc.createElement("url");
            url.appendChild(xmlDoc.createTextNode(referenceLink));
            pa.appendChild(url);
        }
    }

    /**
     * Sets the identity (organism) element if an organism name is provided.
     *
     * @param mol      the molecule element to which the identity should be appended
     * @param organism the organism name (may be {@code null})
     */
    private void setIdentity(Element mol, String organism) {
        if (organism != null) {
            Element identity = xmlDoc.createElement("identity");
            mol.appendChild(identity);
            Element name = xmlDoc.createElement("name");
            name.appendChild(xmlDoc.createTextNode(organism));
            identity.appendChild(name);
        }
    }

    /**
     * Formats the nucleotide sequence string for better readability in the XML output,
     * inserting line breaks and spaces at regular intervals.
     *
     * @param sequence the raw nucleotide sequence
     * @return the formatted sequence string
     */
    private String sequenceStyle(String sequence) {
        String result = "\n        ";
        int count = 0;
        for (char c : sequence.toCharArray()) {
            if (count != 0) {
                if (count % 60 == 0)
                    result += "\n        ";
                else if (count % 10 == 0)
                    result += ' ';
            }
            result += c;
            count++;
        }
        return result + "\n      ";
    }

    /**
     * Adds one side of a base pair annotation to the secondary structure.
     *
     * @param base_pair the parent base-pair element
     * @param id        the tag name for this side (e.g., "base-id-5p" or "base-id-3p")
     * @param pos       the nucleotide position
     */
    private void addBase(Element base_pair, String id, int pos) {
        Element base_p = xmlDoc.createElement(id);
        base_pair.appendChild(base_p);
        Element base_id = xmlDoc.createElement("base-id");
        base_p.appendChild(base_id);
        Element position = xmlDoc.createElement("position");
        position.appendChild(xmlDoc.createTextNode(pos + ""));
        base_id.appendChild(position);
    }

}