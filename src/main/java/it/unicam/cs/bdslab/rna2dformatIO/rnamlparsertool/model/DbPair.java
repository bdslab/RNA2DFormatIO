package it.unicam.cs.bdslab.rna2dformatIO.rnamlparsertool.model;

/**
 * Class that holds a pair of positions along with an order level.
 * This is essential for the application of the DB file writing algorithm.
 *
 * @author Marvin Sincini - Università di Informatica di Camerino - matricola 118311
 */
public class DbPair implements Comparable<DbPair> {

    private final int left;
    private final int right;
    private int order;

    /**
     * Constructs a DbPair with the specified left and right positions.
     *
     * @param left  the left position index
     * @param right the right position index
     */
    public DbPair(int left, int right) {
        this.left = left;
        this.right = right;
        this.order = 0;
    }

    /**
     * Returns the left position index.
     *
     * @return the left position
     */
    public int getLeft() {
        return left;
    }

    /**
     * Returns the right position index.
     *
     * @return the right position
     */
    public int getRight() {
        return right;
    }

    /**
     * Returns the order level assigned to this pair.
     *
     * @return the order value
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order level for this pair.
     *
     * @param order the order value to assign
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + left;
        result = prime * result + right;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DbPair)) {
            return false;
        }
        DbPair other = (DbPair) obj;
        return this.left == other.left && this.right == other.right;
    }

    @Override
    public int compareTo(DbPair o) {
        Integer right, oright;
        right = this.right;
        oright = o.right;
        return right.compareTo(oright);
    }

}