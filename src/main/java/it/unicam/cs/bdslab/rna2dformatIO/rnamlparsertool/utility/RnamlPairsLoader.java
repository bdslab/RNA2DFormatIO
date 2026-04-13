package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.exception.RnaParsingException;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaChain;

/**
 * Utility class for loading base pair information from an RNAML file.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnamlPairsLoader {

    /**
     * Loads all base pairs from the given XML element and populates the provided RNA chain.
     *
     * @param chainData the XML element containing the base pair annotations
     * @param chain     the {@link RnaChain} to populate with pair data
     * @throws RnaParsingException if an error occurs while parsing the pair positions
     */
    public void loadPairs(Element chainData, RnaChain chain) throws RnaParsingException {
        NodeList pairs = chainData.getElementsByTagName("base-pair");
        for (int i = 0; i < pairs.getLength(); i++) {
            Element pair = getElement(pairs.item(i));
            if (pair != null)
                loadPair(pair, chain, isCanonical(pair));
        }
    }

    private boolean isCanonical(Element pair) {
        NodeList bol = pair.getElementsByTagName("bond-orientation");
        if (bol.getLength() == 0)
            return true;
        Element bo = getElement(bol.item(0));
        if (bo != null)
            return bo.getTextContent().equals("c");
        return true;
    }

    /**
     * Processes the data of a single base pair and adds it to the chain.
     *
     * @param pair        the XML element representing the base pair
     * @param chain       the RNA chain to update
     * @param isCanonical whether the pair is canonical (secondary structure) or tertiary
     * @throws RnaParsingException if the pair data is malformed
     */
    private void loadPair(Element pair, RnaChain chain, boolean isCanonical) throws RnaParsingException {
        NodeList positions = pair.getElementsByTagName("position");
        if (positions.getLength() == 2) {
            String first = positions.item(0).getTextContent();
            String second = positions.item(1).getTextContent();
            int firstInt = Integer.parseInt(first);
            int secondInt = Integer.parseInt(second);
            String edge1 = getEdge(pair, 5);
            String edge2 = getEdge(pair, 3);
            if (isCanonical)
                chain.addPair(firstInt, secondInt, edge1, edge2);
            else {
                chain.addTertiaryPair(firstInt, secondInt, false, edge1, edge2);
            }
        }
    }

    private String getEdge(Element pair, int num) {
        NodeList bol = pair.getElementsByTagName("edge-" + num + "p");
        if (bol.getLength() == 0)
            return "???";
        Element bo = getElement(bol.item(0));
        if (bo != null)
            return bo.getTextContent();
        return "???";
    }

    /**
     * Safely casts a DOM {@link Node} to an {@link Element} if it is an element node.
     *
     * @param node the DOM node to check
     * @return the node as an {@link Element}, or {@code null} if it is not an element node
     */
    private Element getElement(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return (Element) node;
        } else {
            return null;
        }
    }

}