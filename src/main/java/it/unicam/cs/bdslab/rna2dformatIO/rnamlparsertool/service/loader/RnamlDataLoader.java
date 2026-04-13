package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.exception.RnaParsingException;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility.RnamlPairsLoader;

/**
 * Class responsible for loading data from an RNAML format file.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public final class RnamlDataLoader extends XmlDataLoader {

    /**
     * Loader for extracting base pairs from RNAML structure annotations.
     */
    private RnamlPairsLoader pairsLoader = new RnamlPairsLoader();

    /**
     * Loads the RNA data from the specified RNAML file path.
     *
     * @param path the path to the RNAML file
     * @return an {@link RnaMolecule} object containing the parsed data,
     *         or {@code null} if loading fails or validation errors occur
     */
    @Override
    public RnaMolecule getData(String path) {
        RnaMolecule data = new RnaMolecule();
        Document doc = loadXmlDocument(path, "/rnaml.dtd");
        if (doc == null) {
            return null;
        }
        NodeList chainList = doc.getElementsByTagName("molecule");
        for (int i = 0; i < chainList.getLength(); i++) {
            Element node = getElement(chainList.item(i));
            RnaChain chain = getchain(node, i);
            if (chain == null)
                return null;
            data.addchain(chain);
        }
        checkInfoData(doc, data);
        try {
            data.checkSecondaryStructure();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extracts an RNA chain from an XML element node.
     *
     * @param chainData the XML element containing molecule data
     * @param index     the chain index (used to assign an identifier)
     * @return the constructed {@link RnaChain}, or {@code null} if parsing fails
     */
    public RnaChain getchain(Element chainData, int index) {
        RnaChain chain = new RnaChain(index + 1);
        try {
            loadSequence(chainData, chain);
            this.pairsLoader.loadPairs(chainData, chain);
            return chain;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the nucleotide sequence from the XML element into the given chain.
     *
     * @param chainData the XML element containing the sequence data
     * @param chain     the {@link RnaChain} to populate
     * @throws RnaParsingException if an invalid nucleotide character is encountered
     */
    private void loadSequence(Element chainData, RnaChain chain) throws RnaParsingException {
        Element sequenceNode = getElement(chainData.getElementsByTagName("sequence")
                .item(0));
        NodeList list = sequenceNode.getElementsByTagName("seq-data");

        String sequence = getElement(list.item(0)).getTextContent()
                .replace(" ", "").replace("\n", "")
                .replace("\t", "").replace("\r", "").toUpperCase();
        for (char c : sequence.toCharArray()) {
            chain.addRibonucleotide(c);
        }
    }

    /**
     * Extracts auxiliary information (reference link, accession number, organism)
     * from the XML document and stores it in the RNA molecule.
     *
     * @param doc  the parsed XML document
     * @param data the {@link RnaMolecule} to populate with metadata
     */
    private void checkInfoData(Document doc, RnaMolecule data) {
        NodeList urls = doc.getElementsByTagName("url");
        if (urls.getLength() > 0) {
            data.setReferenceLink(urls.item(0).getTextContent());
        }
        NodeList chainList = doc.getElementsByTagName("chain");
        if (chainList.getLength() > 0) {
            Element mol = getElement(chainList.item(0));
            String db = mol.getAttribute("database-ids");
            if (!"".equals(db)) {
                data.setAccessionNumber(db);
            }
            NodeList identities = mol.getElementsByTagName("identity");
            if (identities.getLength() > 0) {
                String name = getElement(identities.item(0)).getElementsByTagName("name").item(0).getTextContent();
                data.setOrganism(name);
            }
        }
    }

}