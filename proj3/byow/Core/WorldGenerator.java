package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.*;

public class WorldGenerator {

    public static Position createWorld(TETile[][] tiles, Random random) {
        fillWithWall(tiles); // Fill whole world with wall tiles.
        Queue<Room> rooms = placeRoomsToWorld(tiles, random); // Carve rooms at random place.
        connectRoomsInWorld(tiles, rooms, random); // Connect rooms with hallways has no walls.
        removeRedundantWalls(tiles); // Cleverly make hallways have walls.
        return addAvatar(tiles, rooms, random); // Add an avatar to the world.
    }

    private static Position addAvatar(TETile[][] tiles, Queue<Room> rooms, Random random) {
        List<Room> birthRooms = new ArrayList<>(rooms);
        int birthRoomIndex = random.nextInt(birthRooms.size());
        Room birthRoom = birthRooms.get(birthRoomIndex);
        int avatarX = birthRoom.position.x + 1 + random.nextInt(birthRoom.width - 2);
        int avatarY = birthRoom.position.y + 1 + random.nextInt(birthRoom.height - 2);
        tiles[avatarX][avatarY] = Tileset.AVATAR;
        return new Position(avatarX,avatarY);
    }

    private static void removeRedundantWalls(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                int floorNum = 0;
                if (tiles[x][y].equals(Tileset.WALL)) {
                    // up,down,right,left
                    if (y < height - 1 && tiles[x][y + 1].equals(Tileset.FLOOR)) floorNum += 1;
                    if (y > 0 && tiles[x][y - 1].equals(Tileset.FLOOR)) floorNum += 1;
                    if (x < width - 1 && tiles[x + 1][y].equals(Tileset.FLOOR)) floorNum += 1;
                    if (x > 0 && tiles[x - 1][y].equals(Tileset.FLOOR)) floorNum += 1;
                    // No floor nearby
                    if (floorNum == 0) tiles[x][y] = Tileset.NOTHING;
                }
            }
        }
    }

    private static void connectRoomsInWorld(TETile[][] tiles, Queue<Room> rooms, Random random) {
        // If set toBeConnected = rooms directly, rooms will also be altered after following loop.
        Queue<Room> toBeConnected = new ArrayDeque<>(rooms);
        // Make sure that all rooms are connected.
        while (toBeConnected.size() > 1) {
            Room roomA = toBeConnected.poll();
            Room roomB = toBeConnected.poll();
            // Calculate center position of each room.
            Position posA = roomA.position;
            int xACenter = posA.x + roomA.width / 2;
            int yACenter = posA.y + roomA.height / 2;
            Position roomACenter = new Position(xACenter, yACenter);

            Position posB = roomB.position;
            int xBCenter = posB.x + roomB.width / 2;
            int yBCenter = posB.y + roomB.height / 2;
            Position roomBCenter = new Position(xBCenter, yBCenter);

            // Choose left room's x position as horizontal hallway's start x position.
            // Choose lower room's y position as horizontal hallway's start y position.
            int horzStartX = Math.min(roomACenter.x, roomBCenter.x);
            int horzEndX = Math.max(roomACenter.x, roomBCenter.x);
            int horzLength = horzEndX - horzStartX;
            int horzStartY = Math.min(roomACenter.y, roomBCenter.y);
            Position horzHallwayPos = new Position(horzStartX, horzStartY);

            // Choose lower room's y position as vertical hallway's start y position.
            // Choose higher room's x position as vertical hallway's start x position.
            int vertStartY = Math.min(roomACenter.y, roomBCenter.y);
            int vertEndY = Math.max(roomACenter.y, roomBCenter.y);
            int vertLength = vertEndY - vertStartY;
            int vertStartX = Math.min(roomACenter.x, roomBCenter.x);
            Position vertHallwayPos = new Position(vertStartX, vertStartY);

            // Build hallways only using floor tiles.
            addTileRowToWorld(tiles, horzHallwayPos, horzLength, Tileset.FLOOR);
            addTileColToWorld(tiles, vertHallwayPos, vertLength, Tileset.FLOOR);

            // Pick a random room from two polled rooms, reinsert it into queue,
            // thus guaranteeing already connected rooms can be connected to other rooms.
            int i = random.nextInt(2);
            if (i == 0) toBeConnected.offer(roomA);
            if (i == 1) toBeConnected.offer(roomB);
        }
    }



    // Place random number, random size, random position rooms to the world,
    // and return a queue of rooms prepared for connection.
    private static Queue<Room> placeRoomsToWorld(TETile[][] tiles, Random random) {
        Queue<Room> rooms = new ArrayDeque<>();
        Queue<BPSpace> bpSpaceQueue = new ArrayDeque<>(); // used for BPS
        List<BPSpace> bpSpaceList = new ArrayList<>(); // used for record the space

        BPSpace root = new BPSpace(new Position(0, 0), tiles.length, tiles[0].length - 1);
        bpSpaceQueue.offer(root);
        bpSpaceList.add(root);

        // BPS
        int num = 15 + random.nextInt(10); // A suitable time for partition
        while (num > 0 && !bpSpaceQueue.isEmpty()) {
            BPSpace toPartition = bpSpaceQueue.poll();
            if (toPartition.partition(random)) {
                // record the space
                bpSpaceList.add(toPartition.leftChild);
                bpSpaceList.add(toPartition.rightChild);
                // partition every child.
                bpSpaceQueue.offer(toPartition.leftChild);
                bpSpaceQueue.offer(toPartition.rightChild);
            }
            num -=1;
        }
        // add rooms in space
        root.setRoom(random);
        // record rooms
        for (BPSpace space: bpSpaceList) {
            if (space.room != null) {
                addRoomToWorld(tiles ,space.room);
                rooms.offer(space.room);
            }
        }
        return rooms;
    }

    private static void addRoomToWorld(TETile[][] tiles, Room room) {
        Position position = room.position; // bottom left
        int x = position.x;
        int y = position.y;
        int roomWidth= room.width;
        int roomHeight= room.height;
        // place wall
        addRoomWallToWorld(tiles,room);
        // the room except the wall
        for (int i = 1; i < roomHeight - 1; i++) {
            Position p = new Position(x + 1,y + i);
            addTileRowToWorld(tiles,p,roomWidth - 2,Tileset.FLOOR);
        }

    }

    private static void addRoomWallToWorld(TETile[][] tiles, Room room) {
        Position position = room.position; // bottom left
        int x = position.x;
        int y = position.y;
        int roomWidth= room.width;
        int roomHeight= room.height;
        //bottom
        addTileRowToWorld(tiles,position,roomWidth,Tileset.WALL);
        //left
        addTileColToWorld(tiles,position,roomHeight,Tileset.WALL);
        //up
        Position upLeft = new Position(x, y + roomHeight - 1);
        addTileRowToWorld(tiles,upLeft,roomWidth,Tileset.WALL);
        //right
        Position bottomRight = new Position(x + roomWidth - 1,y);
        addTileColToWorld(tiles,bottomRight,roomHeight,Tileset.WALL);
    }

        // Add a row of specific tile with specific length to the world, from left to right.
    private static void addTileRowToWorld(TETile[][] tiles,Position position,int length, TETile tile) {
        int x = position.x;
        int y = position.y;
        for (int i = 0; i < length; i++) {
            tiles[x+i][y] = tile;
        }
    }
    // Add a col of specific tile with specific length to the world, from bottom to up.
    private static void addTileColToWorld(TETile[][] tiles,Position position,int length, TETile tile) {
        int x = position.x;
        int y = position.y;
        for (int i = 0; i < length; i++) {
            tiles[x][y+i] = tile;
        }
    }


    private static void fillWithWall(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.WALL;
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        int WIDTH = 50;
        int HEIGHT = 50;
        ter.initialize(WIDTH,HEIGHT);
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        Random random = new Random(153314241);
        createWorld(tiles,random);
        ter.renderFrame(tiles);
    }


}
