package kelly.maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class MazeWalker {
    private MazeCell finish;
    private MazeCellGrid grid;
    private Stack<MazeCell> path;
    private HashSet<MazeCell> visitedCells;
    private boolean finished = false;
    private long delay;

    public MazeWalker(MazeCellGrid grid, MazeCell finish, long delay) {
        this.grid = grid;
        this.finish = finish;
        path = new Stack<>();
        visitedCells = new HashSet<>();
        this.delay = delay;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean hasStarted() {
        return !path.isEmpty();
    }

    public Stack<MazeCell> getPath() {
        return path;
    }

    public MazeCell getHead() {
        return path.peek();
    }

    private List<MazeCell> nextGoodCell(MazeCell cell) {
        List<MazeCell> result = new ArrayList<>();
        for(MazeDirection dir : cell.getOpenings()) {
            MazeCell neighbor = grid.getNeighbor(cell, dir);
            if(!visitedCells.contains(neighbor)) {
                result.add(neighbor);
            }
        }
        return result;
    }

    public boolean solveFrom(MazeCell cell) throws InterruptedException {
        path.push(cell);
        visitedCells.add(cell);

        Thread.sleep(delay);

        if(cell.equals(finish)) {
            return true;
        }

        List<MazeCell> neighbors = nextGoodCell(cell);
        for(MazeCell c : neighbors) {
            if(solveFrom(c)) {
                return true;
            }
            Thread.sleep(delay);

            path.pop();
        }
        return false;
    }
}
