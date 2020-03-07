import javax.xml.crypto.dsig.spec.XPathType;
import java.nio.file.LinkPermission;

public class RabinKarpAlgorithm {


    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        if (pattern.length() > input.length()) return -1;
        String patternLengthInput = input.substring(0,pattern.length());

        RollingString rollingString = new RollingString(patternLengthInput,pattern.length());
        RollingString patternString = new RollingString(pattern,pattern.length());
        int ideaHashCode = patternString.hashCode();

        for (int i = pattern.length(); i <= input.length(); i++) {
            if (rollingString.hashCode() == ideaHashCode) {
                return 1;
            } else {
                // the last circulation only for comparing hashcode
                if (i != input.length()) rollingString.addChar(input.charAt(i));
            }
        }
        return -1;
    }
}
