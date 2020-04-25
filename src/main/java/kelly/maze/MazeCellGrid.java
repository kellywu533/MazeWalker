package kelly.maze;

import java.util.*;

/**
 *Grid that stores each individual maze cell
 */
public class MazeCellGrid {
    private static final Random random = new Random();
    private int rows;
    private int columns;
    private MazeCell[] grid;
    Set<MazeCell> freeCells;

    /**
     *Constructs the maze cell grid with the given amount of rows and columns
     * @param rows Rows of the grid
     * @param columns Columns of the grid
     */
    public MazeCellGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        reset();
    }

    public void reset() {
        grid = new MazeCell[rows*columns];
        freeCells = new HashSet<>();
        for(int r=0; r<rows; r++) {
            for(int c=0; c<columns; c++) {
                MazeCell cell = new MazeCell(r, c);
                grid[translateToIndex(r, c)] = cell;
                freeCells.add(cell);
            }
        }
    }

    /**
     *Translates the row and column of a specific maze cell to the index where the maze cell is stored in an array
     * @param row Row of the maze cell
     * @param column Column of the maze cell
     * @return Integer of the index of the maze cell
     */
    private int translateToIndex(int row, int column) {
        return columns * row + column;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     *Returns a random element from a collection
     * @param collection Collection to get a random element from
     * @param <T> Type of the elements stored in the collection
     * @return Random element of the collection
     */
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

    /**
     *Gets a random maze cell to start building the maze
     * @return Random maze cell to start the maze
     */
    public MazeCell getStart() {
        return randomElementFrom(freeCells);
    }

    /**
     *Gets all the cells stored in the grid
     * @return Array of maze cells in the grid
     */
    public MazeCell[] getCells() {
        return grid;
    }

    /**
     *Gets the cell at a specific row and column
     * @param row Row of the maze cell
     * @param column Column of the maze cell
     * @return Maze cell at the row and column
     */
    public MazeCell cellAt(int row, int column) {
        return grid[translateToIndex(row, column)];
    }

    /**
     *Gets the neighboring cell of a given cell at a specific direction
     * @param cell Cell to get the neighbor
     * @param direction Direction of the neighboring cell
     * @return Maze cell that neighbors the original cell
     */
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

    /**
     *Breaks the wall of a maze cell at a specific direction and the neighbor's wall at the opposite direction
     * @param cell Cell which is to be broken
     * @param direction Direction to break the wall
     * @return Maze cell neighboring the original cell at the direction specified
     */
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

    /**
     *Gets an array list of connected or unconnected directions of a specific cell
     * @param cell Cell to check the directions of
     * @param connected Check for either connected or unconnected directions
     * @return Array list of directions of the specified connection
     */
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

    /**
     *Gets a random cell that is unconnected and adjacent to a cell with a broken wall
     * @return Maze cell that is unconnected and adjacent to an existing path
     */
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
