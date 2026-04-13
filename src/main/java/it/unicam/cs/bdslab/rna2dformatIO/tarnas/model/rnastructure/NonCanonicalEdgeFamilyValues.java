package it.unicam.cs.bdslab.rna2dformatIO.tarnas.model.rnastructure;

/**
 * Enumeration representing the possible values for non‑canonical edge families
 * in RNA tertiary interactions. Each value corresponds to a specific edge type
 * (Sugar, Hoogsteen, Watson‑Crick) or a bond orientation (cis, trans).
 */
public enum NonCanonicalEdgeFamilyValues {
    /** Sugar edge (S). */
    SUGAR("Sugar"),
    /** Hoogsteen edge (H). */
    HOOGSTEEN("Hoogsteen"),
    /** Watson‑Crick edge (W). */
    WATSON_CRICK("Watson-Crick"),
    /** Trans orientation (t). */
    TRANS("trans"),
    /** Cis orientation (c). */
    CIS("cis"),
    /** Exclamation mark (!), used as a placeholder for unknown or unspecified edges. */
    EXCLAMATION("!"),
    /** Unknown or unsupported edge type. */
    NONE("???");

    private final String label;

    NonCanonicalEdgeFamilyValues(String label) {
        this.label = label;
    }

    /**
     * Returns the human‑readable label associated with this edge family value.
     *
     * @return the label string
     */
    public String getLabel() {
        return label;
    }

    /**
     * Converts a short single‑character label (as used in RNAML files) to the
     * corresponding {@code NonCanonicalEdgeFamilyValues} enum constant.
     * <p>
     * Recognised short labels are:
     * <ul>
     *   <li>{@code "S"} or {@code "s"} → {@link #SUGAR}</li>
     *   <li>{@code "W"} → {@link #WATSON_CRICK}</li>
     *   <li>{@code "H"} → {@link #HOOGSTEEN}</li>
     *   <li>{@code "!"} → {@link #EXCLAMATION}</li>
     *   <li>{@code "t"} → {@link #TRANS}</li>
     *   <li>{@code "c"} → {@link #CIS}</li>
     * </ul>
     * Any other input returns {@link #NONE}.
     *
     * @param shortLabel the short label (case‑sensitive except for 'S'/'s')
     * @return the corresponding enum constant, or {@code NONE} if not recognised
     */
    public static NonCanonicalEdgeFamilyValues fromShortLabel(String shortLabel) {
        return switch (shortLabel) {
            case "S", "s" -> SUGAR;
            case "W" -> WATSON_CRICK;
            case "H" -> HOOGSTEEN;
            case "!" -> EXCLAMATION;
            case "t" -> TRANS;
            case "c" -> CIS;
            default -> NONE;
        };
    }
}