package bearmaps;
import java.util.List;

public class KDTree implements PointSet {
    private static final boolean HORIZONTAL = false;
    private Node root;

    public KDTree(List<Point> points) {
        for (Point point: points) {
            insert(point);
        }
    }

    public void insert(Point p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(root,p,HORIZONTAL);
    }

    private Node insert(Node T, Point p, boolean orientation) {
        if (T == null ) return new Node(p, orientation);
        if (T.getPoint().equals(p)) return T;
        int cmp = new Node(p,orientation).compareTo(T);
        if (cmp > 0) {
            T.right = insert(T.right,p,!orientation);
        } else {
            T.left = insert(T.left,p,!orientation);
        }
        return T;
    }


    @Override
    public Point nearest(double x, double y) {
        return nearestHelper(root,root, new Point(x, y), HORIZONTAL).getPoint();
    }

    private Node nearestHelper(Node T,Node bestNode,Point targetPoint,boolean orientation) {
        if (T == null) return bestNode;
        double currDist = Point.distance(T.getPoint(), targetPoint);
        double bestDist = Point.distance(bestNode.getPoint(),targetPoint);
        if (Double.compare(currDist,bestDist) < 0) bestNode = T;
        Node goodSide;
        Node badSide;
        if (new Node(targetPoint,orientation).compareTo(T) > 0) {
                goodSide = T.right;
                badSide = T.left;
        } else {
            goodSide = T.left;
            badSide = T.right;
        }
        bestNode = nearestHelper(goodSide,bestNode,targetPoint,!orientation);
        if (isWorthLook(T,targetPoint,bestDist)) {
            bestNode = nearestHelper(badSide,bestNode,targetPoint,!orientation);
        }
        return bestNode;
    }

    private boolean isWorthLook(Node currNode, Point targetPoint, Double currLength) {
        if (currNode.getOrientation()) {
            return Math.pow(currNode.getPoint().getY()-targetPoint.getY(),2) < currLength;
        } else {
            return Math.pow(currNode.getPoint().getX()-targetPoint.getX(),2) < currLength;
        }
    }


    private class Node implements Comparable{
        private Node left;
        private Node right;
        private Point p;
        private boolean orientation;

        private Node(Point p, boolean orientation) {
            this.p = p;
            this.orientation = orientation;
        }

        private Point getPoint() {
            return p;
        }

        private boolean getOrientation() {
            return orientation;
        }

        @Override
        public int compareTo(Object o) {
            if (getOrientation()) {
                return Double.compare(p.getY(),((Node)o).getPoint().getY());
            } else {
                return Double.compare(p.getX(),((Node)o).getPoint().getX());
            }
        }

    }
}
