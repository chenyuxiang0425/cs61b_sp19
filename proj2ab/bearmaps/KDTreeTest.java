package bearmaps;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    private static Random R = new Random(500);

    public static List<Point> pointList() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p22 = new Point(4, 2);
        Point p3 = new Point(4, 5);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(1, 5);
        Point p6 = new Point(4, 4);
        return List.of(p1, p2, p22,p3, p4, p5,p6);
    }

        public static KDTree buildLectureTree() {
        KDTree kdTree = new KDTree(pointList());
        return kdTree;
    }

    public static NaivePointSet naivePointSetTest() {
        NaivePointSet naivePointSet = new NaivePointSet(pointList());
        return naivePointSet;
    }

    @Test
    /** test code by using examples from the nearest slides **/
    public void testNearestDemoSlides() {
        KDTree kd = buildLectureTree();
        NaivePointSet naivePointSet = naivePointSetTest();
        Point actual = kd.nearest(0, 7);
        Point expect = naivePointSet.nearest(0,7);
        assertEquals(expect, actual);
    }

    /**
     *  https://www.youtube.com/watch?v=lp80raQvE5c
     */
    @Test
    public void startWith1000Points200Queries() {
        int PointCount = 1000;
        int QueryCount = 200;
        startWithNPointsMQueries(PointCount,QueryCount);
    }

    private void startWithNPointsMQueries(int PointCount, int QueryCount) {
        List<Point> points = randomPoints(PointCount);
        KDTree kdTree = new KDTree(points);
        NaivePointSet naivePointSet = new NaivePointSet(points);

        List<Point> queries = randomPoints(QueryCount);
        for (Point p: queries) {
            Point expected = naivePointSet.nearest(p.getX(),p.getY());
            Point actual = kdTree.nearest(p.getX(),p.getY());
            assertEquals(expected,actual);
        }
    }

    private Point randomPoint() {
        double x = R.nextDouble();
        double y = R.nextDouble();
        return new Point(x,y);
    }

    // return N random points
    private List<Point> randomPoints(int N) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i<= N; i++) {
            points.add(randomPoint());
        }
        return points;
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(KDTreeTest.class);
    }
}
