import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class blackWhiteChess {
    private int cols; //行
    private int rows; //列
    private int[][] grid;
    private int checkPoint;

    public blackWhiteChess() {
    }

    public blackWhiteChess(int[][] grid) {
        cols = grid.length;
        rows = grid[0].length;
        this.grid = grid;
        checkPoint = 0;  // 1则黑棋下，旁边要有2；0则白棋下，旁边要有1
    }

    public int checkPoint() {
        checkPoint = checkPoint + 1;
        return (checkPoint % 2);
    }

    public List check() {
        List res = new ArrayList(2);
        //TODO

        /*现在有了三个方法：
        1.判断所选的position是否为0
        2.判断 mypostion 是否在 other 附近
        3.判断 mypostion 是否在 other 横竖撇捺位置
         */
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                int[] position = new int[]{col,row};
                if (validPlace(position) && ifNearOtherChess(position) && whetherOtherSide(position)) {
                    res.add(new int[]{col,row});
                }
            }
        }
        return res;
    }

    private boolean validPlace(int[] position) {
        int col = position[0];
        int row = position[1];
        return grid[col][row] == 0;
    }

    // 判断 mypostion 四周是否又有与其不同的值
    public boolean ifNearOtherChess(int[] position) {
        //other 是一个x,y
        int pcol = position[0];
        int prow = position[1];
        for (int col = -1; col <= 1; col +=1) {
            for (int row = -1; row <= 1; row +=1) {
                if (col == 0 && row == 0) {
                    continue;
                }
                int newCol = pcol + col;
                int newRow = prow + row;
                if (newCol >= 0 && newCol <cols && newRow>=0 && newRow <rows) {
                    int surroundNum = grid[newCol][newRow];
                    if (surroundNum != 0 && surroundNum == (checkPoint + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 判断  棋盘中 横竖撇捺方向 是否有 与 poistion 相同的值
    public boolean whetherOtherSide(int[] position) {
        int pcol = position[0];
        int prow = position[1];
        if (grid[pcol][prow] != 0) {
            return false;
        }

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                if (col == pcol || row == prow || (col - pcol) == (row - prow) || (col - pcol) == -(row - prow)) {
                    int otherNum = grid[col][row];
                    // checkpoint 为 0 时，白棋下，要 = 2，checkpoint 为1时，要=1，黑棋下
                    if (checkPoint == 0) {
                        if (otherNum != 0 && otherNum == (checkPoint + 2)) {
                            return true;
                        }
                    } else {
                        if (otherNum != 0 && otherNum == checkPoint) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    }
