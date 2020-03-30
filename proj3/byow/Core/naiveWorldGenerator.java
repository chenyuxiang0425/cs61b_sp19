package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;


public class naiveWorldGenerator {


    public static void createWorld(TETile[][] tiles, Random random) {
        fillWithNothing(tiles);
        ArrayList<Room> rooms = createRoom(tiles, random);
        for (int i =0; i < rooms.size() -1; i++ ) {
            int j = i + 1;
            createPath(tiles,rooms.get(i), rooms.get(j), random);
        }

    }

    private static ArrayList<Room> createRoom(TETile[][] tiles, Random random) {
        ArrayList<Room> rooms = new ArrayList<>();
        int times = RandomUtils.uniform(random,5,tiles[0].length + tiles.length);
        for (int i = 0; i < times; i++) {
            Room room = createRoomInRandomPlace(tiles,random);
            if (room != null) rooms.add(room);
        }
        return rooms;
    }


    private static Room createRoomInRandomPlace(TETile[][] tiles, Random random) {
        int height = tiles[0].length;
        int width = tiles.length;
        int roomHeight = RandomUtils.uniform(random,2,height/10);
        int roomWidth = RandomUtils.uniform(random,2,width/10);
        int x = random.nextInt(width-roomWidth);
        int y = random.nextInt(height-roomHeight);
        Position position = new Position(x,y);
        Room room = new Room(position,roomWidth,roomHeight);

        for (int i = room.position.x; i < room.position.x + room.getWidth(); i ++) {
            for (int j = room.position.y; j < room.position.y + room.getHeight(); j ++) {
                if (!tiles[i][j].equals(Tileset.NOTHING)
                        || tiles[i+1][j].equals(Tileset.FLOOR)
                        || tiles[i-1][j].equals(Tileset.FLOOR)
                        || tiles[i][j+1].equals(Tileset.FLOOR)
                        || tiles[i][j-1].equals(Tileset.FLOOR)){
                    return null;
                }
            }
        }
        for (int i = room.position.x; i < room.position.x + room.getWidth(); i ++) {
            for (int j = room.position.y; j < room.position.y + room.getHeight(); j ++) {
                tiles[i][j] = Tileset.FLOOR;
            }
        }
        return room;
    }

    private static void createPath(TETile[][] tiles,Room room1,Room room2, Random random) {
        Position room1Position = room1.position;
        Position room2Position = room2.position;
        if (room2Position.x > room1Position.x) {
            for (int i = room1Position.x; i < room2Position.x; i += 1) {
                tiles[i][room1Position.y] = Tileset.FLOOR;
            }
        }
        if (room1Position.x > room2Position.x) {
            for (int i = room2Position.x; i < room1Position.x; i += 1) {
                tiles[i][room1Position.y] = Tileset.FLOOR;
            }
        }

        if (room2Position.y > room1Position.y) {
            for (int i = room1Position.y; i < room2Position.y; i += 1) {
                tiles[room1Position.x][i] = Tileset.FLOOR;
            }
        }
        if (room1Position.x > room2Position.x) {
            for (int i = room2Position.x; i < room1Position.x; i += 1) {
                tiles[room1Position.x][i] = Tileset.FLOOR;
            }
        }

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

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        int WIDTH = 50;
        int HEIGHT = 50;
        ter.initialize(WIDTH,HEIGHT);
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        Random random = new Random(50);
        createWorld(tiles,random);
        ter.renderFrame(tiles);
    }
}
