package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaDataLoader;

/**
 * Abstract class providing utility methods to load an XML document from a file path
 * and to operate on XML nodes.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class XmlDataLoader implements RnaDataLoader {

    /**
     * Loads and parses an XML document from the specified file path, using a local
     * DTD resource to avoid external network access during validation.
     *
     * @param path    the file path of the XML document
     * @param dtdPath the classpath resource path to the DTD file (e.g., "/rnaml.dtd")
     * @return the parsed and normalized {@link Document}, or {@code null} if an error occurs
     */
    protected Document loadXmlDocument(String path, String dtdPath) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    if (systemId.contains(".dtd")) {
                        InputStream dtdStream = getClass().getResourceAsStream(dtdPath);
                        return new InputSource(dtdStream);
                    } else {
                        return null;
                    }
                }
            });
            Document doc = builder.parse(new File(path));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Safely casts a DOM {@link Node} to an {@link Element} if it is an element node.
     *
     * @param node the DOM node to check
     * @return the node as an {@link Element}, or {@code null} if it is not an element node
     */
    protected Element getElement(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return (Element) node;
        } else {
            return null;
        }
    }

}