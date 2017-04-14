import java.util.Arrays;

public class Board {

    private static final int MIN_BAORD_SIZE = 2;

    private int[][] mBoard;

    public Board(int[][] blocks) {
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
        checkInputBlocks(blocks);
        mBoard = Arrays.copyOf(blocks, blocks.length);
    }

    public int dimension() {
        // board dimension n
        return mBoard.length;
    }
    /*public int hamming()                   // number of blocks out of place
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    public boolean isGoal()                // is this board the goal board?
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    @Override
    public boolean equals(Object y)        // does this board equal y?
    public Iterable<Board> neighbors()     // all neighboring boards
    @Override
    public String toString()               // string representation of this board (in the output format specified below)
*/
    public static void main(String[] args) {
        // unit tests (not graded)
        int blocks[][] = {{1,2}, {2, 3}};
        Board testBoard = new Board(blocks);
    }

    private void checkInputBlocks(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("Cannot creat board out of null blocks structure");
        }
        int rowLength = blocks[0].length;
        int rowNumbers = blocks.length;
        if (rowLength < MIN_BAORD_SIZE || rowNumbers < MIN_BAORD_SIZE) {
            throw new IllegalArgumentException("Block size must be greater than " + MIN_BAORD_SIZE);
        }

        if (rowLength != rowNumbers) {
            throw new IllegalArgumentException("Provided matrix is not square");
        }
        // check if all row are same length
        for (int i = 1; i < blocks.length; i++) {
            if (rowNumbers != blocks[i].length) {
                throw new IllegalArgumentException("Provided matrix is not square");
            }
        }
    }
}
