package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumerazione contenente tutte le possibili nomenclature dell'rna
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public enum RnaBase {
    ADENINE,
    URACIL,
    CYTOSINE,
    GUANINE,
    ADENINEorURACIL,
    ADENINEorCYTOSINE,
    ADENINEorGUANINE,
    URACILorCYTOSINE,
    URACILorGUANINE,
    CYTOSINEorGUANINE,
    notADENINE,
    notURACIL,
    notCYTOSINE,
    notGUANINE,
    UNIDENTIFIED;

    public static RnaBase getBase(int n) {
        return getBase("" + ((char) n));
    }

    public static RnaBase getBase(String s) {
        return getBase(s.toUpperCase().charAt(0));
    }

    public static RnaBase getBase(char c) {
        if(c >= 'a' && c <= 'z') {
            return getBase(""+c);
        }
        return switch (c) {
            case 'A' -> ADENINE;
            case 'U' -> URACIL;
            case 'C' -> CYTOSINE;
            case 'G' -> GUANINE;
            case 'W' -> ADENINEorURACIL;
            case 'M' -> ADENINEorCYTOSINE;
            case 'R' -> ADENINEorGUANINE;
            case 'Y' -> URACILorCYTOSINE;
            case 'K' -> URACILorGUANINE;
            case 'S' -> CYTOSINEorGUANINE;
            case 'B' -> notADENINE;
            case 'V' -> notURACIL;
            case 'D' -> notCYTOSINE;
            case 'H' -> notGUANINE;
            default -> UNIDENTIFIED;
        };
    }

    public static char getBaseLetter (RnaBase base) {
        return switch (base) {
            case ADENINE -> 'A';
            case URACIL -> 'U';
            case CYTOSINE -> 'C';
            case GUANINE -> 'G';
            case ADENINEorCYTOSINE -> 'R';
            case ADENINEorGUANINE -> 'Y';
            case ADENINEorURACIL -> 'M';
            case CYTOSINEorGUANINE -> 'B';
            case URACILorCYTOSINE -> 'K';
            case URACILorGUANINE -> 'S';
            case notADENINE -> 'V';
            case notCYTOSINE -> 'D';
            case notGUANINE -> 'H';
            case notURACIL -> 'V';
            default -> 'N';
        };
    }

    public static boolean maybeEquals(String seq1, String seq2){
        char [] list1 = seq1.toCharArray(), list2 = seq2.toCharArray();
        if(list1.length != list2.length)
            return false;
        for(int i = 0; i < list1.length; i++){
            if(!getBase(list1[i]).maybeEquals(getBase(list2[i])))
                return false;
        }
        return true;
    }


    public boolean maybeEquals(RnaBase other){
        if(this == UNIDENTIFIED || other == UNIDENTIFIED)
            return true;
        List<Character> list1 = getBases(this);
        List<Character> list2 = getBases(other);
        for(Character c : list1) {
            if(list2.contains(c))
                return true;
        }
        return false;
    }

    private List<Character> getBases(RnaBase base) {
        List<Character> list = new ArrayList<>();
        if(base == ADENINE || base == ADENINEorCYTOSINE || base == ADENINEorGUANINE || base == ADENINEorURACIL
                || base == notCYTOSINE || base == notGUANINE || base == notURACIL)
            list.add('A');
        if(base == URACIL || base == URACILorCYTOSINE || base == URACILorGUANINE || base == ADENINEorURACIL
                || base == notCYTOSINE || base == notGUANINE || base == notADENINE)
            list.add('U');
        if(base == CYTOSINE || base == ADENINEorCYTOSINE || base == CYTOSINEorGUANINE || base == URACILorCYTOSINE
                || base == notADENINE || base == notGUANINE || base == notURACIL)
            list.add('C');
        if(base == GUANINE || base == URACILorGUANINE || base == ADENINEorGUANINE || base == CYTOSINEorGUANINE
                || base == notCYTOSINE || base == notURACIL || base == notADENINE)
            list.add('G');
        return list;
    }

    public static boolean canonicalPair(RnaBase base1, RnaBase base2) {
        return checkCanonicalPatterns(base1, base2) || checkCanonicalPatterns(base2, base1);
    }

    private static boolean checkCanonicalPatterns(RnaBase base1, RnaBase base2) {
        return (base1.maybeEquals(RnaBase.ADENINE) && base2.maybeEquals(RnaBase.URACIL))
                || (base1.maybeEquals(RnaBase.GUANINE) && base2.maybeEquals(RnaBase.CYTOSINE));
    }

}
