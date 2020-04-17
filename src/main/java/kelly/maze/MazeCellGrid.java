package kelly.maze;

import java.util.ArrayList;
import java.util.Random;

public class MazeCellGrid {
    private int rows;
    private int columns;
    private Random random = new Random();
    private MazeCell[] grid;

    public MazeCellGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        int cells = rows * columns;
        grid = new MazeCell[cells];
        for(int r=0; r<rows; r++) {
            for(int c=0; c<columns; c++) {
                grid[translateToIndex(r, c)] = new MazeCell(r, c);
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

    public MazeCell getStart() {
        MazeCell cell = grid[random.nextInt(grid.length)];
        return cell;
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
        assert direction.opposite() != null;
        neighbor.breakWall(direction.opposite());
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
        for(MazeCell c : grid) {
            if(!c.isConnected()) {
                ArrayList<MazeDirection> directions = goodDirections(c, true);
                if(!directions.isEmpty()) {
                    MazeDirection direction = directions.get(random.nextInt(directions.size()));
                    breakWall(c, direction);
                    return c;
                }
            }
        }
        return null;
    }
}
