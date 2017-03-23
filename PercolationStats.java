/**
 * @author Seweryn Fornalik
 * Date: 22-03-2017
 *
 * This assignment is a part of the Algorithm I online course
 * Week 1: Percolation
 *
 * Compilation: javac PercolationStats.java
 *
 * Usage:
 * java PercolationStats <grid_size> <number_of_trials>
 *
 */
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * The Class PercolationStats.
 */
public class PercolationStats {

    /** The Constant REQUIRED_ARGUMENTS_NUMBER. */
    private static final int REQUIRED_ARGUMENTS_NUMBER = 2;

    /** The Constant GRID_SIZE_ARGUMENT. */
    private static final int GRID_SIZE_ARGUMENT = 0;

    /** The Constant TRIALS_NUMBER_ARGUMENT. */
    private static final int TRIALS_NUMBER_ARGUMENT = 1;

    /** The Constant MIN_TRIALS_NUMBER. */
    private static final int MIN_TRIALS_NUMBER = 1;

    /** The Constant COFIDENCE_COFACTOR. */
    private static final double COFIDENCE_COFACTOR = 1.96;

    /** The m grid size. */
    private int mGridSize;

    /** The m trials number. */
    private int mTrialsNumber;

    /** The m threshold values. */
    private double[] mTresholdValues;

    /** The m standard mean. */
    private double mStandardMean;

    /** The m standard deviation. */
    private double mStandardDeviation;

    /** The m confidence hi. */
    private double mConfidenceHi;

    /** The m confidence lo. */
    private double mConfidenceLo;

    /**
     * Instantiates a new percolation stats.
     *
     * @param n the n
     * @param trials the trials
     */
    public PercolationStats(int n, int trials) {

        validateParameters(n, trials);

        mGridSize = n;
        mTrialsNumber = trials;
        mTresholdValues = new double[mTrialsNumber];
        int[] openSequenceTable = new int[mGridSize * mGridSize];

        for (int k = 0; k < mTrialsNumber; k++) {
            Percolation percolation = new Percolation(mGridSize);

            // initialize table that store sequence of site opening
            for (int i = 0; i < mGridSize * mGridSize; i++) {
                openSequenceTable[i] = i;
            }
            // shuffle table
            StdRandom.shuffle(openSequenceTable);

            for (int j = 0; j < mGridSize * mGridSize; j++) {
                final int row = Math.floorDiv(openSequenceTable[j], mGridSize) + 1;
                final int col = openSequenceTable[j] % mGridSize + 1;
                percolation.open(row, col);
                if (percolation.percolates()) {
                    mTresholdValues[k] = ((double) percolation.numberOfOpenSites()) / (mGridSize * mGridSize);
                    break;
                }
            }
        }
        calculateStatistics(mTresholdValues, mTrialsNumber);

    }
    // perform trials independent experiments on an n-by-n grid

    /**
     * Mean.
     *
     * @return the double
     */
    public double mean() {
        return mStandardMean;
    }

    /**
     * Stddev.
     *
     * @return the double
     */
    public double stddev() {
        return mStandardDeviation;
    }

    /**
     * Confidence lo.
     *
     * @return the double
     */
    public double confidenceLo() {
        return mConfidenceLo;
    }

    /**
     * Confidence hi.
     *
     * @return the double
     */
    public double confidenceHi() {
        return mConfidenceHi;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        if (args == null) {
            StdOut.println("Bad program execution, please try again...");
            return;
        }
        if (args.length < REQUIRED_ARGUMENTS_NUMBER) {
            StdOut.println("Usage: java " + PercolationStats.class.getSimpleName() + " <gridSizeParam> "
                    + " <numberOfTrialsParam>");
            return;
        }

        final int gridSize;
        final int trialsNumber;

        try {
            gridSize = Integer.parseInt(args[GRID_SIZE_ARGUMENT]);
            trialsNumber = Integer.parseInt(args[TRIALS_NUMBER_ARGUMENT]);
        } catch (NumberFormatException exception) {
            StdOut.println("Illegal argument(s) error: one or both parameters is not integer type");
            StdOut.println("example usage: java PercolationStats 100 50");
            return;
        }

        PercolationStats percolationStats = new PercolationStats(gridSize, trialsNumber);

        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");
    }

    /**
     * Validate parameters.
     *
     * @param gridSize the grid size
     * @param trialsNumber the trials number
     */
    private void validateParameters(final int gridSize, final int trialsNumber) {
        if (gridSize <= 0 || trialsNumber <= 0) {
            throw new IllegalArgumentException("Parameters should be greater than zero");
        }
    }

    /**
     * Calculate statistics.
     *
     * @param sample the sample
     * @param trials the trials
     */
    private void calculateStatistics(double[] sample, final int trials) {
        mStandardMean = StdStats.mean(sample);

        if (trials > MIN_TRIALS_NUMBER) {
            mStandardDeviation = StdStats.stddev(sample);
            mConfidenceLo = calculateConfidenceLo(mStandardMean, trials, mStandardDeviation);
            mConfidenceHi = calculateConfidenceHi(mStandardMean, trials, mStandardDeviation);
        } else if (trials == MIN_TRIALS_NUMBER) {
            mStandardDeviation = Double.NaN;
            mConfidenceLo = Double.NaN;
            mConfidenceHi = Double.NaN;
        }
    }

    /**
     * Calculate confidence lo.
     *
     * @param sampleMean the sample mean
     * @param trials the trials
     * @param stdDev the std dev
     * @return the double
     */
    private double calculateConfidenceLo(final double sampleMean, final double trials, double stdDev) {
        double result = 0;
        if (trials > 0) {
            result = sampleMean - (COFIDENCE_COFACTOR * stdDev / Math.sqrt(trials));
        }
        return result;
    }

    /**
     * Calculate confidence hi.
     *
     * @param sampleMean the sample mean
     * @param trials the trials
     * @param stdDev the std dev
     * @return the double
     */
    private double calculateConfidenceHi(final double sampleMean, final double trials, double stdDev) {
        double result = 0;
        if (trials > 0) {
            result = sampleMean + (COFIDENCE_COFACTOR * stdDev / Math.sqrt(trials));
        }
        return result;
    }

}
