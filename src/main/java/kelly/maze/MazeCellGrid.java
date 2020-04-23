package kelly.maze;

import java.util.*;

public class MazeCellGrid {
    private static final Random random = new Random();

    private int rows;
    private int columns;
    private MazeCell[] grid;
    Set<MazeCell> freeCells = new HashSet<>();

    public MazeCellGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        int cells = rows * columns;
        grid = new MazeCell[cells];
        for(int r=0; r<rows; r++) {
            for(int c=0; c<columns; c++) {
                MazeCell cell = new MazeCell(r, c);
                grid[translateToIndex(r, c)] = cell;
                freeCells.add(cell);
            }
        }
    }

    private int translateToIndex(int row, int column) {
        return columns * row + column;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public static <T> T randomElementFrom(Collection<T> collection) {
        if(collection == null || collection.isEmpty()) {
            return null;
        }
        int num = random.nextInt(collection.size());
        for (T t : collection) {
            if (num-- < 1) {
                return t;
            }
        }
        // should never get here
        return null;
    }

    public MazeCell getStart() {
        return randomElementFrom(freeCells);
    }

    public MazeCell[] getCells() {
        return grid;
    }

    public MazeCell cellAt(int row, int column) {
        return grid[translateToIndex(row, column)];
    }

    public MazeCell getNeighbor(MazeCell cell, MazeDirection direction) {
        int row = cell.getRow();
        int column = cell.getColumn();

        switch(direction) {
            case NORTH :
                if(row > 0) {
                    return cellAt(row - 1, column);
                }
                break;
            case EAST :
                if(column < columns - 1) {
                    return cellAt(row, column + 1);
                }
                break;
            case SOUTH :
                if(row < rows - 1) {
                    return cellAt(row + 1, column);
                }
                break;
            case WEST :
                if(column > 0) {
                    return cellAt(row, column - 1);
                }
                break;
        }
        return null;
    }

    public MazeCell breakWall(MazeCell cell, MazeDirection direction) {
        MazeCell neighbor = getNeighbor(cell, direction);
        if (neighbor == null) {
            return null;
        }

        cell.breakWall(direction);
        freeCells.remove(cell);
        assert direction.opposite() != null;
        neighbor.breakWall(direction.opposite());
        freeCells.remove(neighbor);
        return neighbor;
    }

    public ArrayList<MazeDirection> goodDirections(MazeCell cell, boolean connected) {
        ArrayList<MazeDirection> nextDirections = new ArrayList<>();
        for(MazeDirection dir : MazeDirection.values()) {
            MazeCell neighbor = getNeighbor(cell, dir);
            if(neighbor != null && neighbor.isConnected() == connected) {
                nextDirections.add(dir);
            }
        }
        return nextDirections;
    }

    public MazeCell getCellAdjacentToPath() {
        ArrayList<MazeCell> availableCells = new ArrayList<>();
        for(MazeCell c : freeCells) {
            if(!c.isConnected()) {
                if(!goodDirections(c, true).isEmpty()) {
                    availableCells.add(c);
                }
            }
        }
        if(availableCells.isEmpty()) {
            return null;
        }
        MazeCell chosenOne = randomElementFrom(availableCells);
        ArrayList<MazeDirection> directions = goodDirections(chosenOne, true);
        MazeDirection direction = randomElementFrom(directions);
        breakWall(chosenOne, direction);
        return chosenOne;
    }
}
