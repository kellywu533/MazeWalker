package kelly.maze;

import java.util.*;

public class MazeWalker {
    private MazeCell finish;
    private MazeCellGrid grid;
    private Stack<MazeCell> path;
    private HashSet<MazeCell> visitedCells;
    private boolean finished = false;
    private long delay;
    private Set<MazeWalkerEventListener> listeners = null;

    public MazeWalker(MazeCellGrid grid, MazeCell finish, long delay) {
        this.grid = grid;
        this.finish = finish;
        path = new Stack<>();
        visitedCells = new HashSet<>();
        this.delay = delay;
        listeners = new HashSet<>();
    }

    public void addListener(MazeWalkerEventListener l) {
        listeners.add(l);
    }

    private void publishEvent(MazeWalkerEvent.Type t) {
        MazeWalkerEvent e = new MazeWalkerEvent(t);
        for(MazeWalkerEventListener l : listeners) {
            l.update(e);
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean hasStarted() {
        return !path.isEmpty();
    }

    /**
     *
     * @return
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

        if(cell.equals(finish)) {
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
