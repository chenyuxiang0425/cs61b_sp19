package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {
    private List<Point> points;


    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    private static double distance(double x1, double x2, double y1, double y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    @Override
    public Point nearest(double x, double y) {
        double minDist = Integer.MAX_VALUE;
        Point thispoint = null;
        for (Point point : points) {
            double currDist = NaivePointSet.distance(x, point.getX(), y, point.getY());
            if (currDist < minDist) {
                minDist = currDist;
                thispoint = point;
            }
        }
        return thispoint;
    }

    public static void main(String[] args) {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        ret.getX(); // evaluates to 3.3
        ret.getY(); // evaluates to 4.4

        System.out.println(ret.equals(p2));
        System.out.println(ret.getX());
        System.out.println(ret.getY());


    }

}