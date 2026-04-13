package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.utility;

/**
 * Abstract class for translating numbers to Dot-Bracket notation characters and vice versa.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public abstract class DotBracketTranslator {

    /**
     * Base symbols of the Dot-Bracket notation.
     */
    private final char[] symbols = {'.', '(', ')', '[', ']', '{', '}', '<', '>'};

    /**
     * Returns the Dot-Bracket character corresponding to the given number.
     *
     * @param n the number to translate (0-62). Values outside this range are clamped to 0.
     * @return the corresponding character (e.g., 0 = '.', 1 = '(', 2 = ')', etc.)
     */
    public char getDbBracket(int n) {
        n = n < 0 || n > 62 ? 0 : n;
        return n < 9 ? symbols[n] : ((char)
                ((n % 2 != 0 ? 'A' : 'a')
                        + (n - 9) / 2));
    }

    /**
     * Returns the number corresponding to the given Dot-Bracket character.
     *
     * @param c the character to translate
     * @return the corresponding number (e.g., '.' = 0, '(' = 1, ')' = 2, etc.),
     *         or 0 if the character is not recognized
     */
    public int getDbNumber(char c) {
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] == c)
                return i;
        }
        boolean maiusc = c >= 'A' && c <= 'Z';
        boolean minusc = c >= 'a' && c <= 'z';
        if (!(maiusc || minusc))
            return 0;
        int base = (c - (maiusc ? 'A' : 'a'));
        return ((base * 2) + 9) + (maiusc ? 0 : 1);
    }

}