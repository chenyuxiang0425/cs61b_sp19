package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;

import static byow.Core.WorldGenerator.createWorld;

public class Engine {
    TERenderer ter = new TERenderer();
    //TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private static final int TILE_SIZE = 16;

    private Position avatarPos;
    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    private StringBuilder record = new StringBuilder(); // to deal with load function

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource source = new KeyboardInputSource();
        boolean gameover = false;
        ter.initialize(WIDTH, HEIGHT);
        drawStartMenu();
        while (!gameover) {
            if (record.length() > 0) {
                ter.renderFrame(world);
                tileInfo(new Position((int) StdDraw.mouseX(), (int) StdDraw.mouseY()));
            }
            if (StdDraw.hasNextKeyTyped()) {
                char action = source.getNextKey();
                takeAction(source,action);
            }
        }

    }

    private void tileInfo(Position position) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1,HEIGHT-1, world[position.x][position.y].description());
        StdDraw.show();
    }

    private void drawStartMenu() {
        StdDraw.clear(StdDraw.BLACK);
        // Draw title.
        Font font = new Font("Monaco",Font.BOLD,60);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH/2,HEIGHT * 3 / 4,"CS61B: Project 3");
        // Draw menu options.
        font = new Font("Monaco",Font.BOLD,30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 5 / 10, "New World (N)");
        StdDraw.text(WIDTH / 2, HEIGHT * 4 / 10, "Load World (L)");
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 10, "Quit (Q)");
        // Reset font size to TeRenderer's default size.
        font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.show();
    }


        /**
         * Method used for autograding and testing your code. The input string will be a series
         * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
         * behave exactly as if the user typed these characters into the engine using
         * interactWithKeyboard.
         * <p>
         * Recall that strings ending in ":q" should cause the game to quite save. For example,
         * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
         * 7 commands (n123sss) and then quit and save. If we then do
         * interactWithInputString("l"), we should be back in the exact same state.
         * <p>
         * In other words, both of these calls:
         * - interactWithInputString("n123sss:q")
         * - interactWithInputString("lww")
         * <p>
         * should yield the exact same world state as:
         * - interactWithInputString("n123sssww")
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
        StringInputSource source = new StringInputSource(input);
        while (source.possibleNextInput()) {
            char action = source.getNextKey();
            takeAction(source, action);
        }
        TETile[][] finalWorldFrame = world;
        return finalWorldFrame;
    }

    // Take the action based on input source type.
    private void takeAction(InputSource inputSource, char action) {
        record.append(action);
        //Press N to create a new world
        if (action == 'N') {
            long seed = inputSeed(inputSource);
            record.append(seed);
            record.append('S'); // record : N XXXXX S
            Random random = new Random(seed);
            avatarPos = createWorld(world, random);
            // If interacts with string, do not render.
            if (inputSource.getClass().equals(KeyboardInputSource.class)) {
                ter.renderFrame(world);
            }
        } else if (action == ':') {
            char nextAction = inputSource.getNextKey();
            if (nextAction == 'Q') {
                record.deleteCharAt(record.length()-1);
                save(record.toString());
                if (inputSource.getClass().equals(KeyboardInputSource.class)) {
                    System.exit(0);
                }
            }
        } else if (action == 'L') {
            record.deleteCharAt(record.length() - 1); // Remove 'L' from the record.
            String savedRecord = load();
            if (savedRecord.equals("")) {
                System.exit(0);
            } else {
                world = interactWithInputString(savedRecord);
                if (inputSource.getClass().equals(KeyboardInputSource.class)) {
                    ter.renderFrame(world);
                }
            }
        } else if (action == 'W') { // Move avatar upwards if there is no wall.
            resetAvatar(inputSource,avatarPos.x,avatarPos.y + 1);
        } else if (action == 'S') {
            resetAvatar(inputSource,avatarPos.x,avatarPos.y - 1);
        } else if (action == 'A') {
            resetAvatar(inputSource,avatarPos.x - 1,avatarPos.y);
        } else if (action == 'D') {
            resetAvatar(inputSource,avatarPos.x + 1,avatarPos.y);
        }
    }

    private void resetAvatar(InputSource inputSource ,int x,int y) {
        if (world[x][y].equals(Tileset.FLOOR)) {
            world[x][y] = Tileset.AVATAR; // Update world.
            world[avatarPos.x][avatarPos.y] = Tileset.FLOOR;
            avatarPos = new Position(x, y); // Update avatar's position.
            if (inputSource.getClass().equals(KeyboardInputSource.class)) {
                ter.renderFrame(world);
            }
        }
    }


    private long inputSeed(InputSource inputSource) {
        //TODO
        // Display the typing interface.
        if (inputSource.getClass().equals(KeyboardInputSource.class)) {
            drawSeed("");
        }

        StringBuilder seedRecord = new StringBuilder();
        long seed = 0L;
        while (inputSource.possibleNextInput()) {
            char next = inputSource.getNextKey();
            if (next != 'S') {
                seed = seed * 10 + Character.getNumericValue(next);
                seedRecord.append(seed);
                if (inputSource.getClass().equals(KeyboardInputSource.class)) {
                    drawSeed(seedRecord.toString());
                    StdDraw.show();
                }
            } else {
                break;
            }
        }
        return seed;
    }

    // Display the seed typed by the player when interact with keyboard.
    private void drawSeed(String s) {
        StdDraw.clear(StdDraw.BLACK);

        Font font = new Font("Monaco",Font.BOLD,30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH/2,HEIGHT * 6 / 10,"Please type a seed, press 'S' to confirm");

        StdDraw.text(WIDTH / 2, HEIGHT * 5 / 10, s);
        font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.show();

    }


    private static void save(String record) {
        File f = new File("./save_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(record);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static String load() {
        File f = new File("./save_data.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found.");
                System.exit(0);
            }
        }
        // In the case no file has been saved yet, we return an empty string.
        return "";
    }
}
