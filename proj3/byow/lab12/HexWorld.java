package byow.lab12;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);


    private static void addHexagon (int x,int y ,int level,TETile[][] tiles) {
        // x & y is located at top left
        TETile randomTile= randomTile();
        addLowerHalf(x,y,level,tiles,randomTile);
        addUpperHalf(x,y-2*level+1,level,tiles,randomTile);
    }

    private static void addLowerHalf(int x,int y ,int level,TETile[][] tiles,TETile Tile) {
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level + 2 * i; j ++ ) {
                tiles[x -i + j][y - i] = Tile;
            }
        }
    }
    private static void addUpperHalf(int x,int y ,int level,TETile[][] tiles,TETile Tile) {
        TETile randomTile= randomTile();
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level + 2 * i; j ++ ) {
                tiles[x - i + j][y + i] = Tile;
            }
        }
    }

    private static void drawRandomVerticalHexes(int x,int y ,int level,int num,TETile[][] tiles) {
        int deltY = 2 * level;
        int currY = y;
        for (int i = 0; i < num ; i++) {
            addHexagon(x,currY+deltY,level,tiles);
            currY = currY+deltY;
        }
    }

    private static void drawFiveVerticalHexes(int x,int y ,int level,TETile[][] tiles) {
        int deltX = 2 * level - 1;
        int deltY = level;
        drawRandomVerticalHexes(x,y,level,3,tiles);
        drawRandomVerticalHexes(x+deltX,y - deltY,level,4,tiles);
        drawRandomVerticalHexes(x+2*deltX,y-2*deltY,level,5,tiles);
        drawRandomVerticalHexes(x+3*deltX,y-deltY,level,4,tiles);
        drawRandomVerticalHexes(x+4*deltX,y,level,3,tiles);
    }

    private static TETile randomTile() {
        Random dice = new Random();
        int tileNum = dice.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH,HEIGHT);
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        fillWithNothing(tiles);

        drawFiveVerticalHexes(10,10,3,tiles);
        ter.renderFrame(tiles);
    }

    private static void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
}
