package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;

/**
 * Interfaccia che definisce la responsabilità
 * di ottenere i dati da un file situato in un dato path
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public interface RnaDataLoader {

    /**
     * metodo per ottenere i file
     * @param path path del file
     * @return il file contenente tutti i dati caricati
     */
    RnaMolecule getData(String path);

}