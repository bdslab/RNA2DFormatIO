package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.writer;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaFileWriter;

/**
 * Abstract class providing utility methods for creating an XML document
 * and saving data in an XML-based format.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class XmlFileWriter implements RnaFileWriter {

    /**
     * The XML document being built.
     */
    protected Document xmlDoc;

    /**
     * Creates a new empty XML document.
     */
    protected void createNewDocument() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            this.xmlDoc = dBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current XML document to the specified path, associating it with
     * an external DTD.
     *
     * @param path the destination file path
     * @param dtd  the DTD file reference (e.g., "rnaml.dtd")
     * @return {@code true} if the save operation succeeds, {@code false} otherwise
     */
    protected boolean save(String path, String dtd) {
        try {
            Source source = new DOMSource(xmlDoc);
            File xmlFile = new File(path);

            // Use try-with-resources to ensure the stream is closed properly
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(xmlFile), "ISO-8859-1")) {
                StreamResult result = new StreamResult(writer);
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
                xformer.setOutputProperty(OutputKeys.INDENT, "yes");
                xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                xformer.transform(source, result);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}