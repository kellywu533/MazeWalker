package kelly.maze;

import java.util.ArrayList;
import java.util.Random;

public class MazeMaker {
    private MazeCellGrid grid;
    public boolean isFinished;
    private static final long delay = 10;
    private Random random = new Random();

    public MazeMaker(MazeCellGrid grid) {
        this.grid = grid;
    }

    public MazeDirection chooseRandomDirection(ArrayList<MazeDirection> directions) {
        if(directions.isEmpty()) {
            return null;
        }
        int size = directions.size();
        return directions.get(random.nextInt(size));
    }

    public void makeMaze() throws InterruptedException {
        MazeCell cell = grid.getStart();
        makePath(cell);
        cell = grid.getCellAdjacentToPath();
        while(cell != null) {
            makePath(cell);
            cell = grid.getCellAdjacentToPath();
        }
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    private void makePath(MazeCell cell) throws InterruptedException {
        do {
            ArrayList<MazeDirection> directions = grid.goodDirections(cell, false);
            MazeDirection direction  = chooseRandomDirection(directions);
            if(direction == null) {
                break;
            }
            cell = grid.breakWall(cell, direction);
            Thread.sleep(delay);
        } while (cell != null);
    }
}
