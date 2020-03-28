package byow.Core;

public class Room {
    Position position;
    int width;
    int height;

    public Room(Position position, int width,int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Position getPosition() {
        return position;
    }
}
