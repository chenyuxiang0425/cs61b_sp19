package bearmaps.proj2c;

import bearmaps.hw4.AStarSolver;
import bearmaps.hw4.WeightedEdge;
import bearmaps.hw4.WeirdSolver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class acts as a helper for the RoutingAPIHandler.
 * @author Josh Hug, ______
 */
public class Router {

    /**
     * Overloaded method for shortestPath that has flexibility to specify a solver
     * and returns a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(AugmentedStreetMapGraph g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long src = g.closest(stlon, stlat);
        long dest = g.closest(destlon, destlat);
        return new AStarSolver<>(g, src, dest, 20).solution();
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    // https://github.com/zangsy/cs61b_sp19/blob/master/proj2c/bearmaps/proj2c/Router.java
    public static List<NavigationDirection> routeDirections(AugmentedStreetMapGraph g, List<Long> route) {
        /* fill in for part IV */
        double currEdgeDistance = 0;
        int currDir = 0;
        List<NavigationDirection> directionList = new ArrayList<>();
        List<WeightedEdge<Long>> ways = getWays(g, route);
        // only have less than two vertexes
        if (ways.size() == 1) {
            NavigationDirection nadir = setNaviDir(currDir,ways.get(0).getName(),ways.get(0).weight());
            directionList.add(nadir);
            return directionList;
        }
        // more than two vertexes
        for (int i = 1; i < ways.size(); i++) {
            WeightedEdge<Long> prevEdge = ways.get(i - 1);
            WeightedEdge<Long> nextEdge = ways.get(i);

            long prevVertex = prevEdge.from();
            long currVertex = prevEdge.to();
            long nextVertex = nextEdge.to();

            Map<String,Double> prevPos = getPos(g,prevVertex);
            Map<String,Double> currPos = getPos(g,currVertex);
            Map<String,Double> nextPos = getPos(g,nextVertex);

            String prevWayName = prevEdge.getName() != null ? prevEdge.getName(): "unknown road";
            String nextWayName = nextEdge.getName() != null ? nextEdge.getName(): "unknown road";

            currEdgeDistance += prevEdge.weight();

            if (!prevWayName.equals(nextWayName)) {
                double prevBearing = NavigationDirection.bearing(prevPos.get("lon"),currPos.get("lon"),prevPos.get("lat"),currPos.get("lat"));
                double nextBearing = NavigationDirection.bearing(currPos.get("lon"),nextPos.get("lon"),currPos.get("lat"),nextPos.get("lat"));

                int currDirToChange = NavigationDirection.getDirection(prevBearing,nextBearing);
                NavigationDirection naviDir = setNaviDir(currDir, prevWayName, currEdgeDistance);
                directionList.add(naviDir);

                currDir = currDirToChange;
                currEdgeDistance = 0;
            }

            //lastVertex: to the target
            if (i == ways.size() -1) {
                currEdgeDistance += nextEdge.weight();
                NavigationDirection naviDir = setNaviDir(currDir, nextWayName, currEdgeDistance);
                directionList.add(naviDir);
            }
        }
        return directionList;
    }

    //get the lat and log of the vertex
    private static Map<String,Double> getPos(AugmentedStreetMapGraph g, long Vertex) {
        Map<String,Double> pos = new HashMap<>();
        pos.put("lat",g.lat(Vertex));
        pos.put("lon",g.lon(Vertex));
        return pos;
    }

    /**
     *
     * @param g helper method. Here is used to get the vertex's neighbors.
     * @param route list of vertex
     * @return list of the WeightedEdge<Long> Objects. this is the distance of two vertexes
     */
    private static List<WeightedEdge<Long>> getWays(AugmentedStreetMapGraph g, List<Long> route) {
        List<WeightedEdge<Long>> ways = new ArrayList<>();

        for (int i = 1 ; i < route.size(); i++) {
            long currVertex = route.get(i - 1);
            long nextVertex = route.get(i);
            for (WeightedEdge<Long> edge : g.neighbors(currVertex)) {
                if (edge.to().equals(nextVertex)) {
                    ways.add(edge);
                }
            }
        }
        return ways;
    }


    /**
     * Create a new NavigationDirection object from given direction, way, distance.
     * @param direction direction
     * @param wayName wayName
     * @param distance distance
     * @return a new NavigationDirection
     */
    private static NavigationDirection setNaviDir(int direction, String wayName, double distance) {
        NavigationDirection naviDir = new NavigationDirection();
        naviDir.direction = direction;
        naviDir.way = wayName;
        naviDir.distance = distance;
        return naviDir;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for. This is only
     * useful for Part IV of the project.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        /** Checks that a value is between the given ranges.*/
        private static boolean numInRange(double value, double from, double to) {
            return value >= from && value <= to;
        }

        /**
         * Calculates what direction we are going based on the two bearings, which
         * are the angles from true north. We compare the angles to see whether
         * we are making a left turn or right turn. Then we can just use the absolute value of the
         * difference to give us the degree of turn (straight, sharp, left, or right).
         * @param prevBearing A double in [0, 360.0]
         * @param currBearing A double in [0, 360.0]
         * @return the Navigation Direction type
         */
        private static int getDirection(double prevBearing, double currBearing) {
            double absDiff = Math.abs(currBearing - prevBearing);
            if (numInRange(absDiff, 0.0, 15.0)) {
                return NavigationDirection.STRAIGHT;

            }
            if ((currBearing > prevBearing && absDiff < 180.0)
                    || (currBearing < prevBearing && absDiff > 180.0)) {
                // we're going right
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    // bearmaps.proj2c.example of high abs diff is prev = 355 and curr = 2
                    return NavigationDirection.SLIGHT_RIGHT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.RIGHT;
                } else {
                    return NavigationDirection.SHARP_RIGHT;
                }
            } else {
                // we're going left
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    return NavigationDirection.SLIGHT_LEFT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.LEFT;
                } else {
                    return NavigationDirection.SHARP_LEFT;
                }
            }
        }


        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }

        /**
         * Returns the initial bearing (angle) between vertices v and w in degrees.
         * The initial bearing is the angle that, if followed in a straight line
         * along a great-circle arc from the starting point, would take you to the
         * end point.
         * Assumes the lon/lat methods are implemented properly.
         * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
         * @param lonV  The longitude of the first vertex.
         * @param latV  The latitude of the first vertex.
         * @param lonW  The longitude of the second vertex.
         * @param latW  The latitude of the second vertex.
         * @return The initial bearing between the vertices.
         */
        public static double bearing(double lonV, double lonW, double latV, double latW) {
            double phi1 = Math.toRadians(latV);
            double phi2 = Math.toRadians(latW);
            double lambda1 = Math.toRadians(lonV);
            double lambda2 = Math.toRadians(lonW);

            double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
            double x = Math.cos(phi1) * Math.sin(phi2);
            x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
            return Math.toDegrees(Math.atan2(y, x));
        }
    }
}
