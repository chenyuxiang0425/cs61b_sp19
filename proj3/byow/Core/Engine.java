package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

import static byow.Core.WorldGenerator.createWorld;

public class Engine {
    //TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    private TETile[][] world = new TETile[WIDTH][HEIGHT];

    //private StringBuilder record = new StringBuilder();

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // DONE: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        StringInputSource inputSource = new StringInputSource(input);
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == 'N') {
                long seed = inputSeed(inputSource);
                Random random = new Random(seed);
                createWorld(world,random);
            }
        }
        return world;
    }

    public static void main(String[] args) {
        //TODO
        TERenderer ter = new TERenderer();
        Engine engine = new Engine();
        ter.initialize(WIDTH,HEIGHT);
        TETile[][] world = engine.interactWithInputString("ANAAASAaaaa");
        ter.renderFrame(world);

    }

    // Take the action based on input source type.
    private void getAction(InputSource inputSource, char action) {
        //TODO

        //record.append(action);
        //Press N to create a new world
        if (action == 'N') {
            long seed = inputSeed(inputSource);
            Random random = new Random(seed);
            createWorld(world,random);
        }
    }

    private long inputSeed(InputSource inputSource) {
        //TODO
        // Display the typing interface.
        long seed = 0L;
        StringBuilder seedRecord = new StringBuilder();
        while (inputSource.possibleNextInput()) {
            char next = inputSource.getNextKey();
            if (next != 'S') {
                seed = seed * 10  + Character.getNumericValue(next);
                seedRecord.append(next);
            } else  {
                break;
            }
        }
        return seed;
    }
}
