package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;  // check situation
    private double solutionWeight;  // the distance result from start to target
    private int numStatesExplored;  // how many number has been explored
    private LinkedList<Vertex> solution;  // List of solution from start to target
    private Map<Vertex,Vertex> edgeToMap;  //edge to: target -> from
    private double timeSpent;
    private Map<Vertex,Double> distToMap;  // vertex distance from start
    private ArrayHeapMinPQ<Vertex> PQ;
    private Map<Vertex, Double> heuristicMap;  // A* Map

    public AStarSolver(AStarGraph<Vertex> G, Vertex start, Vertex goal, double timeout) {
        PQ = new ArrayHeapMinPQ<>();
        double heuristicStart = G.estimatedDistanceToGoal(start,goal);
        distToMap = new HashMap<>();
        heuristicMap = new HashMap<>();
        edgeToMap = new HashMap<>();
        solution = new LinkedList<>();

        Stopwatch sw = new Stopwatch();
        PQ.add(start,0.0);  //initial PQ
        distToMap.put(start,0.0);
        heuristicMap.put(start,heuristicStart);

        while (PQ.size() != 0) {
            Vertex p = PQ.getSmallest();
            if (p.equals(goal)) {
                solutionWeight = distToMap.get(p);
                // deal with solution list
                solution.add(p); // last node
                while (!p.equals(start)) {
                    solution.addFirst(edgeToMap.get(p));
                    p = edgeToMap.get(p);
                }
                outcome = SolverOutcome.SOLVED;
                timeSpent = sw.elapsedTime();
                return;
            }
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                solution.clear();
                solutionWeight = 0.0;
                timeSpent = sw.elapsedTime();
                return;
            }

            PQ.removeSmallest();
            numStatesExplored += 1;
            // relax all the neighbor of PQ's smallest item
            List<WeightedEdge<Vertex>> pNeighbors = G.neighbors(p);
            for (WeightedEdge<Vertex> neighbor : pNeighbors) {
                relax(neighbor,G,goal);
            }
        }

        // if PQ is null and not have result
        outcome = SolverOutcome.UNSOLVABLE;
        solution.clear();
        solutionWeight = 0.0;
        timeSpent = sw.elapsedTime();
    }

    private void relax(WeightedEdge<Vertex> e,AStarGraph<Vertex> input,Vertex goal) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();
        // put the unreached vertex distance of infinity
        if (!distToMap.containsKey(q)) {
            distToMap.put(q,Double.POSITIVE_INFINITY);
        }
        // put the unreached vertex heuristic distance
        if (!heuristicMap.containsKey(q)) {
            heuristicMap.put(q,input.estimatedDistanceToGoal(q,goal));
        }
        double dist = distToMap.get(p) + w; //actual distance
        if (dist < distToMap.get(q)) {
            edgeToMap.put(q,p); //
            distToMap.replace(q,dist); // before is infinity
            if (PQ.contains(q)) {
                PQ.changePriority(q,distToMap.get(q) + heuristicMap.get(q));
            } else {
                PQ.add(q,distToMap.get(q) + heuristicMap.get(q));
            }
        }
    }


    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return timeSpent;
    }
}
