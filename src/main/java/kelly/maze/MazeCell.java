package kelly.maze;

import java.util.ArrayList;
import java.util.List;

public class MazeCell {
    private int row;
    private int column;
    private boolean[] walls = new boolean[MazeDirection.values().length];

    public MazeCell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isConnected() {
        for(boolean isOpen : walls) {
            if (isOpen) {
                return true;
            }
        }
        return false;
    }

    public List<MazeDirection> getOpenings() {
        List<MazeDirection> goodDirections = new ArrayList<>();
        for(MazeDirection direction : MazeDirection.values()) {
            if(isBroken(direction)) {
                goodDirections.add(direction);
            }
        }
        return goodDirections;
    }

    public boolean isBroken(MazeDirection direction) {
        return walls[direction.ordinal()];
    }

    public void breakWall(MazeDirection direction) {
        walls[direction.ordinal()] = true;
    }
}
