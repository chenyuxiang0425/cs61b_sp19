import java.util.ArrayList;
import java.util.List;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class BnBSolver {
    private List<Bear> sortedBears;
    private List<Bed> sortedBearsbeds;

    public BnBSolver(List<Bear> bears, List<Bed> beds) {
        Pair<List<Bear>, List<Bed>> listListPair = quickSort(bears,beds);
        sortedBears = listListPair.first();
        sortedBearsbeds = listListPair.second();
    }

    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        return sortedBears;
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        return sortedBearsbeds;
    }


    private Pair<List<Bear>, List<Bed>> quickSort(List<Bear> bears, List<Bed> beds) {
        if (bears.size() <= 1 || beds.size() <= 1) {
            return new Pair<>(bears,beds);
        }
        Bed BedPivot = getRandomBed(beds);
        Bear BearPivot;

        List<Bed> bedLess = new ArrayList<>();
        List<Bed> bedEqual = new ArrayList<>();
        List<Bed> bedGreater = new ArrayList<>();

        List<Bear> bearLess = new ArrayList<>();
        List<Bear> bearEqual = new ArrayList<>();
        List<Bear> bearGreater = new ArrayList<>();

        partitionBed(bears, BedPivot, bearLess, bearEqual, bearGreater);
        BearPivot = bearEqual.get(0);
        partitionBear(beds, BearPivot, bedLess, bedEqual, bedGreater);

        Pair<List<Bear>, List<Bed>>  lessSorted = quickSort(bearLess,bedLess);
        Pair<List<Bear>, List<Bed>> equalSorted = new Pair<>(bearEqual,bedEqual);
        Pair<List<Bear>, List<Bed>> greaterSorted = quickSort(bearGreater,bedGreater);


        Pair<List<Bear>, List<Bed>> sorted;
        sorted = catenate(lessSorted,equalSorted);
        sorted = catenate(sorted,greaterSorted);
        return sorted;


    }

    private Pair<List<Bear>, List<Bed>> catenate(Pair<List<Bear>, List<Bed>> Asorted, Pair<List<Bear>, List<Bed>> BSorted) {
        List<Bear> catenateBear = new ArrayList<>(Asorted.first());
        List<Bed> catenateBed = new ArrayList<>(Asorted.second());
        catenateBear.addAll(BSorted.first());
        catenateBed.addAll(BSorted.second());
        return new Pair<>(catenateBear,catenateBed);
    }


    private void partitionBed(List<Bear> bears, Bed bedPivot, List<Bear> bearLess, List<Bear> bearEqual, List<Bear> bearGreater) {
        for (Bear bear: bears) {
            if (bear.compareTo(bedPivot) > 0) {
                // bear > bedPivot
                bearGreater.add(bear);
            } else if(bear.compareTo(bedPivot) < 0) {
                bearLess.add(bear);
            } else {
                bearEqual.add(bear);
            }
        }
    }

    private void partitionBear(List<Bed> beds, Bear bearPivot, List<Bed> bedLess, List<Bed> bedEqual, List<Bed> bedGreater) {
        for (Bed bed: beds) {
            if (bed.compareTo(bearPivot) > 0) {
                bedGreater.add(bed);
            } else if(bed.compareTo(bearPivot) < 0) {
                bedLess.add(bed);
            } else {
                bedEqual.add(bed);
            }
        }
    }

    private Bed getRandomBed(List<Bed> beds) {
        int BedIndex = (int) (Math.random()*beds.size());
        Bed bed = null;
        for (Bed b: beds) {
            if (BedIndex == 0) {
                bed = b;
                break;
            }
            BedIndex -= 1;
        }
        return bed;

    }
}
