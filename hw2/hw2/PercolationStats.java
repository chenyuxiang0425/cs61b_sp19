package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.Stopwatch;
import edu.princeton.cs.introcs.StdOut;

public class PercolationStats {
    private double[] fractions;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        Percolation percolation = pf.make(N);
        fractions = new double[N * N];
        for (int i = 0; i <= T; i++) {
            while (!percolation.percolates()) {
                int random  = StdRandom.uniform(N * N);
                int row = random / N;
                int col = random % N;
                percolation.open(row, col);
            }
            int numOfOpen = percolation.numberOfOpenSites();
            double ratio = (double) numOfOpen / (N * N);
            fractions[i] = ratio;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(fractions.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(fractions.length);
    }

    public static void main(String[] args) {
        int N = 400;
        int T = 2000;
        Stopwatch sw = new Stopwatch();
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(N, T, pf);
        StdOut.printf("N_%d, T_%d, cost time: %.2f", N, T, sw.elapsedTime());
    }
}