import java.lang.reflect.Array;
import java.util.Arrays;

public class BruteCollinearPoints {

    private static final int MIN_NUMBER_OF_POINTS_HANDLED = 4;

    private int mSegmentsNumber;

    private LineSegment[] mLineSegments;

    private Point[] mPointsArray;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (ArePointsValid(points)) {
            // valid data provided = no nulls, no duplicates, # of poins >= 4
            Point firstPoint, secondPoint, thirdPoint, forthPoint;
            for (int i = 0; i < mPointsArray.length; i++) {
                firstPoint = mPointsArray[i];
                for (int j = i + 1; j < mPointsArray.length; j++) {
                    secondPoint = mPointsArray[j];
                }
            }
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

    private boolean ArePointsValid(Point[] points) {
        if (points == null) {
            throw new NullPointerException("Array of point cannot be null");
        }
        if (points.length < MIN_NUMBER_OF_POINTS_HANDLED) {
            return false;
        }
        mPointsArray = Arrays.copyOf(points, points.length);
        Arrays.sort(mPointsArray);
        for (int i = 0; i < mPointsArray.length; i++) {
            if (mPointsArray[i] == null) {
                throw new NullPointerException("Cannot handle null point data!")
            }
            if (i > 1) {
                if (mPointsArray[i] == mPointsArray[i-1]) {
                    throw new IllegalArgumentException("No duplicates allowed!");
                }
            }
        }
        return true;
    }
}
