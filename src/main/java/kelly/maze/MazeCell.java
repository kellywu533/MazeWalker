package kelly.maze;

import java.util.ArrayList;
import java.util.List;

/**
 *Represents each maze cell on the maze
 */
public class MazeCell {
    private int row;
    private int column;
    private boolean[] walls = new boolean[MazeDirection.values().length];

    /**
     *Constructs a maze cell at a row and column
     * @param row Row of the maze cell
     * @param column Column of the maze cell
     */
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

    /**
     *Overrides the equals method to compare objects in a set
     * @param o Object of comparison
     * @return Boolean depending on if the objects are equivalent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MazeCell mazeCell = (MazeCell) o;

        if (row != mazeCell.row) return false;
        return column == mazeCell.column;
    }

    /**
     *Creates a hash code to randomly store each maze cell in a set
     * @return Integer of the storage location of the maze cell
     */
    @Override
    public int hashCode() {
        int result = column;
        result = 31 * result + row;
        return result;
    }

    /**
     *Checks if the maze cell is connected on any side
     * @return Boolean whether the maze cell is connected
     */
    public boolean isConnected() {
        for(boolean isOpen : walls) {
            if (isOpen) {
                return true;
            }
        }
        return false;
    }

    /**
     *Gets a list of all the openings of a maze cell
     * @return List of the openings
     */
    public List<MazeDirection> getOpenings() {
        List<MazeDirection> goodDirections = new ArrayList<>();
        for(MazeDirection direction : MazeDirection.values()) {
            if(isBroken(direction)) {
                goodDirections.add(direction);
            }
        }
        return goodDirections;
    }

    /**
     * Checks if a specific wall of the maze cell is broken
     * @param direction Direction that is being checked
     * @return Boolean whether the wall of the direction is broken
     */
    public boolean isBroken(MazeDirection direction) {
        return walls[direction.ordinal()];
    }

    /**
     *Breaks the wall of the maze cell in a specific direction
     * @param direction Direction of which wall should be broken
     */
    public void breakWall(MazeDirection direction) {
        walls[direction.ordinal()] = true;
    }
}
