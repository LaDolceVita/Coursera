import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The Class Permutation.
 */
public class Permutation {

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        verifyArgs(args);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int outputLimit = Integer.parseInt(args[0]);
        String[] allStrings = StdIn.readAllStrings();
        for (int i = 0; i < allStrings.length; i++) {
            queue.enqueue(allStrings[i]);
        }
        /*
         * while ((inputString = StdIn.readString()) != null) {
         * queue.enqueue(inputString); }
         */
        if (queue.size() > 0) {
            int limit = outputLimit < queue.size() ? outputLimit : queue.size();
            for (int i = 0; i < limit; i++) {
                StdOut.println(queue.dequeue());
            }
        }
    }

    /**
     * Verify args.
     *
     * @param args
     *            the args
     */
    private static void verifyArgs(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Bad program execution, please try again");
        }
        if (args.length < 1) {
            throw new IllegalArgumentException("Usage: java Permutation <k> <string_sequence>");
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Usage: java Permutation <k> <string_sequence>");
        }
    }

}
