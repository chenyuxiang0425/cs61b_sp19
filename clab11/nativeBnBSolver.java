import java.util.ArrayList;
import java.util.List;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class nativeBnBSolver {
    private List<Bear> bears;
    private List<Bed> beds;

    public nativeBnBSolver(List<Bear> bears, List<Bed> beds) {
        this.bears = bears;
        this.beds = beds;
    }

    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        List<Pair<Bear, Bed>> pairList = sortBearAndBed();
        List<Bear> bearArrayList = new ArrayList<>();
        for (Pair<Bear, Bed> bearBedPair : pairList) {
            bearArrayList.add(bearBedPair.first());
        }
        return bearArrayList;
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        List<Pair<Bear, Bed>> pairList = sortBearAndBed();
        List<Bed> bedArrayList = new ArrayList<>();
        for (Pair<Bear, Bed> bearBedPair : pairList) {
            bedArrayList.add(bearBedPair.second());
        }
        return bedArrayList;
    }

    private List<Pair<Bear, Bed>> sortBearAndBed() {
        List<Pair<Bear, Bed>> pairList = new ArrayList<>();
        for (Bear bear : this.bears) {
            for (Bed bed : this.beds) {
                if (bear.compareTo(bed) == 0) {
                    Pair<Bear, Bed> bearBedPair = new Pair<>(bear, bed);
                    pairList.add(bearBedPair);
                }
            }
        }
        return pairList;
    }

}
