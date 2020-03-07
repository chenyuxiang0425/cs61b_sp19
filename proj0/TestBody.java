/**
 *  Tests calcForceExertedBy
 */
public class TestBody {
    /**
     *  Tests calcForceExertedBy.
     */
    public static void main(String[] args) {
        checkBody();
    }
        /**
         *  Checks whether or not two Doubles are equal and prints the result.
         * @param expected    Expected double
         * @param actual      Double received
         * @param label       Label for the "test" case
         * @param eps         Tolerance of the double comparison
         */

     private static void checkEquals(double actual,double expected,String label, double eps) {
         if (Double.isNaN(actual) || Double.isInfinite(actual)) {
             System.out.println("FAIL: " + label + "Pairwise Force expected: " + expected + " and you gave: " + actual);
         } else if (Math.abs(expected - actual) <= eps * Math.max(expected , actual)) {
             System.out.println("PASS: " + label + "Pairwise Force expected: " + expected + " and you gave: " + actual);
         } else {
             System.out.println("FAIL: " + label + "Pairwise Force expected: " + expected + " and you gave: " + actual);
         }
     }

    /**
     * Check the Body class to make sure calcDistance works.
     */
    private static void checkBody() {
         System.out.println("Checking calcForce...");

         // Body(double xP, double yP, double xV,double yV, double m, String img)
         Body b1 = new Body(1.0,1.0,3.0,5.0,1.0,"jupiter.gif");
         Body b2 = new Body(2.0,1.0,3.0,5.0,1.0,"jupiter.gif");
         checkEquals(b1.calcForceExertedBy(b2),6.67e-11, "calcForceExertedBy():",0.01);
     }
}
