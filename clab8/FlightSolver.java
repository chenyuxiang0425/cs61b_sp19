import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {
    int maxPassengers = 0;

    public FlightSolver(ArrayList<Flight> flights) {
        Comparator<Flight> startTimeCmp = (Flight o1, Flight o2) -> (o1.startTime - o2.startTime);
        Comparator<Flight> endTimeCmp = (Flight o1, Flight o2) -> (o1.endTime - o2.endTime);
        // public PriorityQueue(Comparator<? super E> comparator);
        PriorityQueue<Flight> startMinPQ = new PriorityQueue<>(startTimeCmp);
        PriorityQueue<Flight> endMinPQ = new PriorityQueue<>(endTimeCmp);

        startMinPQ.addAll(flights);
        endMinPQ.addAll(flights);

        int nowPassengers = 0;  //To store current timestamp passenger number
        while (startMinPQ.peek() != null) {
            //compare the smallest startTime with the smallest endTime
            if (startMinPQ.peek().startTime <= endMinPQ.peek().endTime) {
                nowPassengers += startMinPQ.poll().passengers;
                if (nowPassengers > maxPassengers) maxPassengers = nowPassengers;
            } else {
                nowPassengers -= endMinPQ.poll().passengers;
            }
        }
    }

    public int solve() {
        return maxPassengers;
    }

}
