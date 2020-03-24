package bearmaps.proj2c;

import bearmaps.lab9.MyTrieSet;
import bearmaps.lab9.TrieSet61B;
import bearmaps.proj2ab.KDTree;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;


import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private List<Point> points;
    private List<Node> myNodes;
    private Map<Point,Node> pointToNodeMap;
    private TrieSet61B myTriesSet;
    private Map<String, List<Node>> nametoNode;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        // List<Node> nodes = this.getNodes();

        // Project Part II
        List<Node> nodes = this.getNodes();
        points = new ArrayList<>();
        myNodes = new ArrayList<>();
        pointToNodeMap = new HashMap<>();
        // Project Part III
        myTriesSet = new MyTrieSet();
        nametoNode = new HashMap<>();

        for (Node node: nodes) {
            // Project Part II
            double lat = node.lat();
            double lon = node.lon();
            Point point = new Point(lat,lon);
            this.points.add(point);
            myNodes.add(node);
            pointToNodeMap.put(point,node);
            // Project Part III
            if (node.name() != null) {
                String cleanName = cleanString(node.name());
                myTriesSet.add(cleanName);
                if (!nametoNode.containsKey(cleanName)) nametoNode.put(cleanName,new LinkedList<>());
                nametoNode.get(cleanName).add(node);
            }
        }
    }

    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        PointSet pointSet = new KDTree(points);
        Point point = pointSet.nearest(lat,lon);
        Node node = pointToNodeMap.get(point);
        return node.id();
    }

    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanName = cleanString(prefix);
        List<String> lowercaseStringList =myTriesSet.keysWithPrefix(cleanName);
        List<String> stringList = new LinkedList<>();

        for (String lowercaseString : lowercaseStringList) {
            List<Node> currNodeList = nametoNode.get(lowercaseString);
            for (Node currnode : currNodeList) {
                if (!stringList.contains(currnode.name())) stringList.add(currnode.name());
            }
        }
        return stringList;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> mapList = new LinkedList<>();
        String cleanName = cleanString(locationName);

        if (nametoNode.containsKey(cleanName)) {
            List<Node> nodeList = nametoNode.get(cleanName);
            for (Node node : nodeList) {
                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("lat", node.lat());
                stringObjectMap.put("lon", node.lon());
                stringObjectMap.put("name", node.name());
                stringObjectMap.put("id", node.id());
                mapList.add(stringObjectMap);
            }
        }
        return mapList;
    }

    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
