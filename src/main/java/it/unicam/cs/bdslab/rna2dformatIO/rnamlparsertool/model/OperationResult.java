package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds the outcome and details of an operation.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class OperationResult {

    /**
     * Details of the operation.
     */
    private List<String> info;

    /**
     * Outcome of the operation.
     */
    public boolean result;

    /**
     * Constructs an empty OperationResult with a default failed outcome.
     */
    public OperationResult() {
        this.info = new ArrayList<>();
        result = false;
    }

    /**
     * Returns a copy of the list containing the operation details.
     *
     * @return a list of informational messages
     */
    public List<String> getInfo() {
        return new ArrayList<>(info);
    }

    /**
     * Adds an informational message to the operation details.
     *
     * @param info the message to add
     */
    public void addInfo(String info) {
        this.info.add(info);
    }

}