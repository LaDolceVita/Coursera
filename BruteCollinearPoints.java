import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private static final int MIN_NUMBER_OF_POINTS_HANDLED = 4;

    private ArrayList<LineSegment> mLineSegments;

    private Point[] mPointsArray;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (arePointsValid(points)) {
            // valid data provided <=> no nulls, no duplicates, # of points >= 4
            Point firstPoint, secondPoint, thirdPoint, forthPoint;
            for (int i = 0; i < mPointsArray.length; i++) {
                firstPoint = mPointsArray[i];
                for (int j = i + 1; j < mPointsArray.length; j++) {
                    secondPoint = mPointsArray[j];
                    for (int k = j + 1; k < mPointsArray.length; k++) {
                        thirdPoint = mPointsArray[k];
                        for (int l = k + 1; l < mPointsArray.length; l++) {
                            forthPoint = mPointsArray[l];
                            checkIfPointsColinear(firstPoint, secondPoint, thirdPoint, forthPoint);
                        }
                    }
                }
            }
        }

    }

    public int numberOfSegments() {
        // the number of line segments
        return (mLineSegments != null ? mLineSegments.size() : 0);
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] tmpArray = new LineSegment[numberOfSegments()];
        return mLineSegments.toArray(tmpArray);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private void checkIfPointsColinear(final Point first, final Point second, final Point third, final Point fourth) {
        if (first.slopeTo(second) == first.slopeTo(third) && first.slopeTo(third) == first.slopeTo(fourth)) {
            if (mLineSegments == null) {
                mLineSegments = new ArrayList<>();
            }
            mLineSegments.add(new LineSegment(first, fourth));
        }
    }

    private boolean arePointsValid(Point[] points) {
        if (points == null) {
            throw new NullPointerException("Array of points cannot be null");
        }
        if (points.length < MIN_NUMBER_OF_POINTS_HANDLED) {
            return false;
        }
        mPointsArray = Arrays.copyOf(points, points.length);
        Arrays.sort(mPointsArray);
        for (int i = 0; i < mPointsArray.length; i++) {
            if (mPointsArray[i] == null) {
                throw new NullPointerException("Cannot handle null point data!");
            }
            if (i > 1) {
                if (mPointsArray[i] == mPointsArray[i - 1]) {
                    throw new IllegalArgumentException("No duplicates allowed!");
                }
            }
        }
        return true;
    }
}
