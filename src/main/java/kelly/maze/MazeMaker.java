package kelly.maze;

import java.util.ArrayList;
import java.util.Random;

public class MazeMaker {
    private MazeCellGrid grid;
    private boolean isFinished;
    private static final long delay = 100;
    private Random random = new Random();
    private ArrayList<MazeMakerEventListener> listeners = null;

    public MazeMaker(MazeCellGrid grid) {
        listeners = new ArrayList<>();
        this.grid = grid;
    }

    public void addListener(MazeMakerEventListener l) {
        this.listeners.add(l);
    }

    private void publishEvent() {
        MazeMakerEvent e = new MazeMakerEvent();
        for(MazeMakerEventListener l : listeners) {
            l.update(e);
        }
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
        publishEvent();
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
            publishEvent();
            Thread.sleep(delay);
        } while (cell != null);
    }
}
