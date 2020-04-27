package kelly.maze;

import java.util.*;

/**
 *Walker that solves the maze
 */
public class MazeWalker {
    private MazeCell endCell;
    private MazeCell startCell;
    private MazeCellGrid grid;
    private Stack<MazeCell> path;
    private HashSet<MazeCell> visitedCells;
    private long delay;
    private Set<MazeWalkerEventListener> listeners;

    /**
     *Constructs the walker in the given grid from (0, 0) to the finishing cell
     * @param grid Grid where the walker is to solve the maze
     * @param endCell Finishing cell where the walker ends its path
     * @param delay Delay to make the walker's movements visible
     */
    public MazeWalker(MazeCellGrid grid, MazeCell startCell, MazeCell endCell, long delay) {
        this.grid = grid;
        this.startCell = startCell;
        this.endCell = endCell;
        this.delay = delay;
        listeners = new HashSet<>();
        path = new Stack<>();
        visitedCells = new HashSet<>();
    }

    public MazeCell getEndCell() {
        return endCell;
    }

    public MazeCell getStartCell() {
        return startCell;
    }

    /**
     *Adds a listener to the set of maze walker event listeners
     * @param l Maze walker event listener
     */
    public void addListener(MazeWalkerEventListener l) {
        listeners.add(l);
    }

    /**
     *Publishes maze walker events to each of the maze walker event listeners in the set
     * @param t Type of event published
     */
    private void publishEvent(MazeWalkerEvent.Type t) {
        MazeWalkerEvent e = new MazeWalkerEvent(t);
        for(MazeWalkerEventListener l : listeners) {
            l.update(e);
        }
    }

    /**
     *Checks whether the walker has started solving the maze
     * @return Boolean whether the walker has started
     */
    public boolean hasStarted() {
        return path != null && !path.isEmpty();
    }

    /**
     *Gets the path of the walker
     * @return Stack of maze cells of the walker's path
     */
    public Stack<MazeCell> getPath() {
        return path;
    }

    /**
     * Gets the head of the walker path
     * @return The maze cell where the head is
     */
    public MazeCell getHead() {
        return path.peek();
    }

    /**
     * Finds the connected cells that have not been visited
     * @param cell The cell which is being checked
     * @return Return a list of Maze Cells that are good
     */
    private List<MazeCell> findGoodCells(MazeCell cell) throws InterruptedException {
        List<MazeCell> result = new ArrayList<>();
        for(MazeDirection dir : cell.getOpenings()) {
            MazeCell neighbor = grid.getNeighbor(cell, dir);
            if(!visitedCells.contains(neighbor)) {
                result.add(neighbor);
            }
        }
        if(result.isEmpty()) {
            publishEvent(MazeWalkerEvent.Type.WALL);
            Thread.sleep(delay);
        }
        return result;
    }

    public boolean solve() throws InterruptedException {
        return solveFrom(startCell);
    }

    /**
     * Solves the maze from the given cell
     * @param cell The starting cell to solve from
     * @return Boolean whether the walker has reached the end
     * @throws InterruptedException
     */
    public boolean solveFrom(MazeCell cell) throws InterruptedException {
        path.add(cell);
        visitedCells.add(cell);

        publishEvent(MazeWalkerEvent.Type.FORWARD);
        Thread.sleep(delay);

        if(cell.equals(endCell)) {
            publishEvent(MazeWalkerEvent.Type.FINISH);
            return true;
        }

        List<MazeCell> neighbors = findGoodCells(cell);
        for(MazeCell c : neighbors) {
            if(solveFrom(c)) {
                return true;
            }
            publishEvent(MazeWalkerEvent.Type.BACKWARD);
            Thread.sleep(delay);

            path.pop();
        }
        return false;
    }
}
