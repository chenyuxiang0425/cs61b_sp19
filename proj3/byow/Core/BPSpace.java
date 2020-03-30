package byow.Core;
/**
 * Binary Partition Space.
 * @source https://github.com/zangsy/cs61b_sp19/blob/master/proj3/byow/Core/BPSpace.java
 */
import java.util.Random;

public class BPSpace {
    private static final int MIN_SIZE = 10;

    private int width;
    private int height;
    Position pos; // Bottom left position.
    BPSpace leftChild;
    BPSpace rightChild;
    Room room;

    public BPSpace(Position pos,int width,int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public boolean partition(Random random) {
        if (leftChild != null) {
            return false;
        }
        // direction is determined by width and height
        boolean horizontal;
        if (width < height) {
            horizontal = true;
        } else if (width > height) {
            horizontal = false;
        } else {
            horizontal = random.nextBoolean();
        }

        // Make sure that both left(bottom) and right(top) child can have MIN_SIZE.
        int currsize = (horizontal ? height : width) - MIN_SIZE;
        if (currsize <= MIN_SIZE) {
            return false;
        }

        // split in random size
        int split = random.nextInt(currsize);
        if (split < MIN_SIZE) {
            split = MIN_SIZE;
        }

        if (horizontal) {
            //bottom
            leftChild = new BPSpace(pos ,width, split);
            //upper
            Position rightPosition = new Position(pos.x,pos.y + split);
            rightChild = new BPSpace(rightPosition ,width, height - split);
        } else {
            // left
            leftChild = new BPSpace(pos ,split, height);
            // right
            Position rightPosition = new Position(pos.x + split,pos.y);
            rightChild = new BPSpace(rightPosition ,width - split,height);
        }
        return true;
    }

    public void setRoom(Random random) {
        // If current area has child areas, then we cannot build room in it,
        // instead, go to check its children.
        if (leftChild != null) {
            leftChild.setRoom(random);
            rightChild.setRoom(random);
        } else {
            int offsetX = width <= MIN_SIZE ? 0: random.nextInt(width - MIN_SIZE);
            int offsetY = height <= MIN_SIZE ? 0: random.nextInt(height - MIN_SIZE);
            int roomWidth = Math.max(MIN_SIZE, random.nextInt(width - offsetX));
            int roomHeight = Math.max(MIN_SIZE, random.nextInt(height - offsetY));
            Position position = new Position(pos.x+offsetX,pos.y+offsetY);
            room = new Room(position,roomWidth,roomHeight);
        }
    }
}
