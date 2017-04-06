
public class BruteCollinearPoints {

    private static final int MIN_NUMBER_OF_POINTS_HANDLED = 4;

    private int mSegmentsNumber;

    private LineSegment[] mLineSegments;

    public BruteCollinearPoints(Point[] points) {
    // finds all line segments containing 4 points
        if (points == null) {
            throw new NullPointerException("Array of point cannot be null");
        if (points.length < MIN_NUMBER_OF_POINTS_HANDLED) {
            // TODO - think if checking this condition can speed things up a bit
        }



    }

    public int numberOfSegments() {
        // the number of line segments
        return mSegmentsNumber;
    }

    public LineSegment[] segments() {
        // the line segments
        return mLineSegments;
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
