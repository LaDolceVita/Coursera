
/**
 * @author Seweryn Fornalik
 * Date: 21-03-2017
 *
 * This assignment is a part of the Algorithm I online course
 * Week 1: Percolation
 *
 * Compilation: javac Percolation.java
 *
 * Usage:
 * Create percolation grid of size n x n with #Percolation(int) Percolation constructor
 * Open new site with #open(int, int).
 *
 */
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The Class Percolation.
 */
public class Percolation {

    /** Minimum number of elements. */
    private static final int NUMBER_ELEMENTS_MIN = 0;

    /** Index of the first element. */
    private static final int FIRST_ELEMENT_INDEX = 1;

    /** The grid size. */
    private final int mGridSize;

    /** The 2D grid element. */
    private boolean[][] mGrid;

    /** The grid element storing site "full". */
    private boolean[][] mFullSiteGrid;

    /** The grid element storing site "bottom connected". */
    private boolean[][] mBottomConnectedSiteGrid;

    /** The weighted quick union UF. */
    private WeightedQuickUnionUF mWeightedQuickUnionUF;

    /** Total number of open sites. */
    private int mOpenSites;

    /** Boolean indicator denoting percolation status. */
    private boolean mIsPercolating;

    /**
     * Constructor Instantiates a new percolation grid (n x n).
     *
     * @param n
     *            the grid size
     */
    public Percolation(int n) {
        if (n > 0) {
            mGridSize = n;
            mOpenSites = 0;
            mIsPercolating = false;
            mWeightedQuickUnionUF = new WeightedQuickUnionUF(mGridSize * mGridSize);
            mGrid = new boolean[mGridSize][mGridSize];
            mFullSiteGrid = new boolean[mGridSize][mGridSize];
            mBottomConnectedSiteGrid = new boolean[mGridSize][mGridSize];
            // initialize all grid sites to false
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mGrid[i][j] = false;
                    // set all sites in first raw as full (by default)
                    if (i == 0) {
                        mFullSiteGrid[i][j] = true;
                    }
                    // set all sites in bottom raw to true
                    if (i == n - 1) {
                        mBottomConnectedSiteGrid[i][j] = true;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Provide parameter \"n\" greater than zero!");
        }
    }

    /**
     * Open site referenced by (row, col) pair if not already open.
     *
     * @param row
     *            the row (indices start from 1)
     * @param col
     *            the column (indices start from 1)
     */
    public void open(int row, int col) {
        checkParameters(row, col);
        if (!mGrid[row - 1][col - 1]) {
            mGrid[row - 1][col - 1] = true;
            unionNeighbours(row, col);
            mOpenSites++;

            // when opening last row - check if system percolates
            if (!mIsPercolating && isBottomConnectedAndFull(row, col)) {
                mIsPercolating = true;
            }
        }
    }

    /**
     * Checks if site is open.
     *
     * @param row
     *            site row (indices start from 1)
     * @param col
     *            site column (indices start from 1)
     * @return true, if is open
     */
    public boolean isOpen(int row, int col) {
        checkParameters(row, col);
        return mGrid[row - 1][col - 1];
    }

    /**
     * Checks if site is full.
     *
     * @param row
     *            site row (indices start from 1)
     * @param col
     *            site column (indices start from 1)
     * @return true, if is full
     */
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }
        final int currentElement = getElementIndex(row, col);
        final int elementRoot = mWeightedQuickUnionUF.find(currentElement);
        return mFullSiteGrid[getElementRow(elementRoot)][getElementColumn(elementRoot)];
    }

    /**
     * Check if grid percolates.
     *
     * @return true, if successful
     */
    public boolean percolates() {
        return mIsPercolating;
    }

    /**
     * Gets number of open sites.
     *
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return mOpenSites;
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        // basic naive test
        Percolation percolation = new Percolation(2);
        StdOut.println("Starting test with no open sites");
        StdOut.println("Number of open sites: " + percolation.numberOfOpenSites() + " [EXPECTED: 0]");
        StdOut.println("Does system percolate: " + percolation.percolates() + " [EXPECTED: false]");

        StdOut.println("\n Open site at (1,1)");
        percolation.open(1, 1);
        StdOut.println("Is site (1,1) open: " + percolation.isOpen(1, 1) + " [EXPECTED: true]");
        StdOut.println("Is site (1,2) open: " + percolation.isOpen(1, 2) + " [EXPECTED: false]");
        StdOut.println("Is site (1,1) full: " + percolation.isFull(1, 1) + " [EXPECTED: false]");
        StdOut.println("Is site (1,2) full: " + percolation.isFull(1, 2) + " [EXPECTED: false]");
        StdOut.println("Number of open sites: " + percolation.numberOfOpenSites() + " [EXPECTED: 1]");
        StdOut.println("Does system percolate: " + percolation.percolates() + " [EXPECTED: false]");

        StdOut.println("\n Open site at (2,2)");
        percolation.open(2, 2);
        StdOut.println("Is site (2,2) open: " + percolation.isOpen(1, 1) + " [EXPECTED: true]");
        StdOut.println("Is site (1,2) open: " + percolation.isOpen(1, 2) + " [EXPECTED: false]");
        StdOut.println("Is site (2,2) full: " + percolation.isFull(1, 1) + " [EXPECTED: false]");
        StdOut.println("Is site (1,2) full: " + percolation.isFull(1, 2) + " [EXPECTED: false]");
        StdOut.println("Number of open sites: " + percolation.numberOfOpenSites() + " [EXPECTED: 2]");
        StdOut.println("Does system percolate: " + percolation.percolates() + " [EXPECTED: false]");

        StdOut.println("\n Open site at (2,1)");
        percolation.open(2, 1);
        StdOut.println("Is site (2,1) open: " + percolation.isOpen(1, 1) + " [EXPECTED: true]");
        StdOut.println("Is site (1,2) open: " + percolation.isOpen(1, 2) + " [EXPECTED: false]");
        StdOut.println("Is site (2,1) full: " + percolation.isFull(2, 1) + " [EXPECTED: true]");
        StdOut.println("Is site (1,2) full: " + percolation.isFull(1, 2) + " [EXPECTED: false]");
        StdOut.println("Number of open sites: " + percolation.numberOfOpenSites() + " [EXPECTED: 3]");
        StdOut.println("Does system percolate: " + percolation.percolates() + " [EXPECTED: true]");

        StdOut.println("\n Open site at (1,2)");
        percolation.open(1, 2);
        StdOut.println("Is site (1,2) open: " + percolation.isOpen(1, 2) + " [EXPECTED: true]");
        StdOut.println("Is site (2,1) full: " + percolation.isFull(2, 1) + " [EXPECTED: true]");
        StdOut.println("Is site (1,2) full: " + percolation.isFull(1, 2) + " [EXPECTED: false]");
        StdOut.println("Number of open sites: " + percolation.numberOfOpenSites() + " [EXPECTED: 4]");
        StdOut.println("Does system percolate: " + percolation.percolates() + " [EXPECTED: true]");

    }

    /**
     * Check parameters.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     */
    private void checkParameters(final int row, final int col) {
        if (row <= NUMBER_ELEMENTS_MIN || col <= NUMBER_ELEMENTS_MIN || row > mGridSize || col > mGridSize) {
            final String errMsg = String.format("Please provide paramters that match range: <%d,%d>",
                    FIRST_ELEMENT_INDEX, mGridSize);
            throw new ArrayIndexOutOfBoundsException(errMsg);
        }
    }

    /**
     * Gets the element index.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     * @return the element index
     */
    private int getElementIndex(final int row, final int col) {
        return mGridSize * (row - 1) + (col - 1);
    }

    /**
     * Gets the element row.
     *
     * @param elementSeqNumber
     *            the element seq number
     * @return the element row
     */
    private int getElementRow(final int elementSeqNumber) {
        return Math.floorDiv(elementSeqNumber, mGridSize);
    }

    /**
     * Gets the element column.
     *
     * @param elementSeqNumber
     *            the element seq number
     * @return the element column
     */
    private int getElementColumn(final int elementSeqNumber) {
        return elementSeqNumber % mGridSize;
    }

    /**
     * Checks if is full.
     *
     * @param element
     *            the element
     * @return true, if is full
     */
    private boolean isFull(final int element) {
        return isFull(getElementRow(element) + 1, getElementColumn(element) + 1);
    }

    /**
     * Checks if is bottom connected.
     *
     * @param element
     *            the root element
     * @return true, if is root element bottom connected
     */
    private boolean isBottomConnected(final int elementRoot) {
        final int row = getElementRow(elementRoot);
        final int column = getElementColumn(elementRoot);
        return mBottomConnectedSiteGrid[row][column];
    }

    /**
     * Checks if is bottom connected.
     *
     * @param row
     *            the row (indices start from 1)
     * @param col
     *            the col (indices start from 1)
     * @return true, if is bottom connected
     */
    private boolean isBottomConnectedAndFull(final int row, final int col) {
        final int currentElement = getElementIndex(row, col);
        final int elementRoot = mWeightedQuickUnionUF.find(currentElement);

        final int rootRow = getElementRow(elementRoot);
        final int rootColumn = getElementColumn(elementRoot);
        return mFullSiteGrid[rootRow][rootColumn] && mBottomConnectedSiteGrid[rootRow][rootColumn];
    }

    /**
     * Union neighbours.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     */
    private void unionNeighbours(final int row, final int col) {
        final int currentElement = getElementIndex(row, col);

        // union left element if exists && is open
        if (col > FIRST_ELEMENT_INDEX && isOpen(row, col - 1)) {
            updateStatus(currentElement - 1, currentElement);
        }
        // union upper element if exists && is open
        if (row > FIRST_ELEMENT_INDEX && isOpen(row - 1, col)) {
            updateStatus(currentElement - mGridSize, currentElement);
        }
        // union right element if exists && is open
        if (col < mGridSize && isOpen(row, col + 1)) {
            updateStatus(currentElement + 1, currentElement);
        }
        // union bottom element if exists && is open
        if (row < mGridSize && isOpen(row + 1, col)) {
            updateStatus(currentElement + mGridSize, currentElement);
        }
    }

    /**
     * Update status.
     *
     * @param firstElement
     *            the first element
     * @param secondElement
     *            the second element
     */
    private void updateStatus(final int firstElement, final int secondElement) {
        final int firstRoot = mWeightedQuickUnionUF.find(firstElement);
        final int secondRoot = mWeightedQuickUnionUF.find(secondElement);
        updateFullStatus(firstRoot, secondRoot);
        updateBottomConnectedStatus(firstRoot, secondRoot);
        mWeightedQuickUnionUF.union(firstElement, secondElement);
    }

    /**
     * Update full status.
     *
     * @param firstElement
     *            the first root element
     * @param secondElement
     *            the second root element
     */
    private void updateFullStatus(final int firstElement, final int secondElement) {
        if (isFull(firstElement) || isFull(secondElement)) {
            mFullSiteGrid[getElementRow(firstElement)][getElementColumn(firstElement)] = true;
            mFullSiteGrid[getElementRow(secondElement)][getElementColumn(secondElement)] = true;
        }
    }

    /**
     * Update bottom connected status.
     *
     * @param firstElement
     *            the first root element
     * @param secondElement
     *            the second root element
     */
    private void updateBottomConnectedStatus(final int firstElement, final int secondElement) {
        if (isBottomConnected(firstElement) || isBottomConnected(secondElement)) {
            mBottomConnectedSiteGrid[getElementRow(firstElement)][getElementColumn(firstElement)] = true;
            mBottomConnectedSiteGrid[getElementRow(secondElement)][getElementColumn(secondElement)] = true;
        }
    }
}
