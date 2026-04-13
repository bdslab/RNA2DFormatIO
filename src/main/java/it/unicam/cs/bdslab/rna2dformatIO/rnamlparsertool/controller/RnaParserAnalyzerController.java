package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.controller;

import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.abstraction.RnaDataLoader;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.OperationResult;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model.RnaMolecule;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.service.comparator.RnaComparator;
import it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility.RnaHandlerBuilder;

/**
 * Controller that extends the base parser with a save-time check that warns
 * about potential data loss due to format conversion, and provides a new
 * comparison function to verify the correspondence or differences in primary
 * and secondary structure between two RNA files.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class RnaParserAnalyzerController extends RnaParserController {

    /**
     * Service for comparing two RNA data sets.
     */
    private RnaComparator comparator;

    /**
     * Default constructor.
     */
    public RnaParserAnalyzerController() {
        super();
        comparator = new RnaComparator();
    }

    /**
     * Constructor with a custom builder.
     *
     * @param builder the {@link RnaHandlerBuilder} to use for loading and writing
     */
    public RnaParserAnalyzerController(RnaHandlerBuilder builder) {
        super(builder);
        comparator = new RnaComparator();
    }

    /**
     * Checks whether two RNA files contain the same primary and secondary structure.
     *
     * @param path1 path of the first file
     * @param path2 path of the second file
     * @return result of the analysis, including any differences found
     */
    public OperationResult equals(String path1, String path2) {
        path1 = checkExt(path1, false);
        path2 = checkExt(path2, false);
        RnaDataLoader loader = getBuilder().buildDataLoader(path1);
        RnaMolecule data1 = loader.getData(path1);
        loader = getBuilder().buildDataLoader(path2);
        RnaMolecule data2 = loader.getData(path2);
        return this.comparator.areEquals(data1, data2);
    }

    @Override
    public synchronized OperationResult SaveLoadedData(String path) {
        return super.SaveLoadedData(path);
    }

}