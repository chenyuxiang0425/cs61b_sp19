package byow.Core;

public class StringInputSource implements InputSource {
    private int index;
    private String input;

    public StringInputSource(String s) {
        input = s;
        index = 0;
    }

    @Override
    public char getNextKey() {
        char returnChar = Character.toUpperCase(input.charAt(index));
        index += 1;
        return returnChar;
    }

    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }
}
