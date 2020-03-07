import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hug.
 */
public class Experiments {
    public static void experiment1() {
        BST<Integer> bst = new BST<>();
        Random r = new Random();
        List<Double> avgDepthMy = new ArrayList<>();
        List<Integer> xValues = new ArrayList<>();
        List<Double> avgDepthOpt = new ArrayList<>();

        for (int i = 0; i <= 5000; i++) {
            int x = r.nextInt(5000);
            if (bst.contains(x)) {
                continue;
            }

            bst.add(x);
            double thisY = bst.averageDepth();
            xValues.add(i);
            avgDepthMy.add(thisY);
            avgDepthOpt.add(ExperimentHelper.optimalAverageDepth(x));

        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart.addSeries("myBST", xValues, avgDepthMy);
        chart.addSeries("optBST", xValues, avgDepthOpt);

        new SwingWrapper(chart).displayChart();

    }

    public static void experiment2() {
        BST<Integer> bst = new BST<>();
        Random r = new Random();
        List<Double> avgDepthMy = new ArrayList<>();
        List<Integer> maxDepthMy = new ArrayList<>();
        List<Integer> xValues = new ArrayList<>();

        //a bst tree of 2000 items
        for (int i = 0; i <= 2000; i++) {
            int x = r.nextInt(5000);
            if (bst.contains(x)) {
                continue;
            }
            bst.add(x);
        }

        for (int i = 0; i <=1000000; i++) {
            double avgDepths = bst.averageDepth();
            int maxDepth = bst.Depth();
            ExperimentHelper.insertAndDeleteSuccessor(bst);
            xValues.add(i);
            avgDepthMy.add(avgDepths);
            maxDepthMy.add(maxDepth);
    }
        XYChart chart1 = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        XYChart chart2 = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart1.addSeries("avgDepth", xValues, avgDepthMy);
        chart2.addSeries("maxDepth", xValues, maxDepthMy);
        new SwingWrapper(chart1).displayChart();
        new SwingWrapper(chart2).displayChart();
    }

    public static void experiment3() {
        BST<Integer> bst = new BST<>();
        Random r = new Random();
        List<Double> avgDepthMy = new ArrayList<>();
        List<Integer> maxDepthMy = new ArrayList<>();
        List<Integer> xValues = new ArrayList<>();

        //a bst tree of 2000 items
        for (int i = 0; i <= 2000; i++) {
            int x = r.nextInt(5000);
            if (bst.contains(x)) {
                continue;
            }
            bst.add(x);
        }

        for (int i = 0; i <=1000000; i++) {
            double avgDepths = bst.averageDepth();
            int maxDepth = bst.Depth();
            ExperimentHelper.insertAndDeleteRandom(bst);
            xValues.add(i);
            avgDepthMy.add(avgDepths);
            maxDepthMy.add(maxDepth);
        }
        XYChart chart1 = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        XYChart chart2 = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart1.addSeries("avgDepth", xValues, avgDepthMy);
        chart2.addSeries("maxDepth", xValues, maxDepthMy);
        new SwingWrapper(chart1).displayChart();
        new SwingWrapper(chart2).displayChart();
    }

    public static void main(String[] args) {
        experiment1();
        experiment2();
        experiment3();
    }
}
