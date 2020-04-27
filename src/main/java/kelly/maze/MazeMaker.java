package kelly.maze;

import java.util.ArrayList;
import java.util.Random;

/**
 *Builds the maze
 */
public class MazeMaker {
    private MazeCellGrid grid;
    private static final long delay = 5;
    private Random random = new Random();
    private ArrayList<MazeMakerEventListener> listeners;

    /**
     *Constucts a maze maker that creates a new array list of listeners
     * @param grid Grid where the maze is to be created
     */
    public MazeMaker(MazeCellGrid grid) {
        listeners = new ArrayList<>();
        this.grid = grid;
    }

    /**
     *Adds a listener to the array list of maze maker listeners
     * @param l Maze maker event listener added to the list of listeners
     */
    public void addListener(MazeMakerEventListener l) {
        this.listeners.add(l);
    }

    /**
     *Publishes a new event to each of the maze maker event listeners
     */
    private void publishEvent() {
        MazeMakerEvent e = new MazeMakerEvent();
        for(MazeMakerEventListener l : listeners) {
            l.update(e);
        }
    }

    /**
     *Chooses a random direction given an array list of directions
     * @param directions Directions of which to be chosen from
     * @return Random maze direction from the list of directions
     */
    public MazeDirection chooseRandomDirection(ArrayList<MazeDirection> directions) {
        if(directions.isEmpty()) {
            return null;
        }
        int size = directions.size();
        return directions.get(random.nextInt(size));
    }

    /**
     *Makes the maze
     * @throws InterruptedException
     */
    public void makeMaze() throws InterruptedException {
        MazeCell cell = grid.getStart();
        makePath(cell);
        cell = grid.getCellAdjacentToPath();
        while(cell != null) {
            makePath(cell);
            cell = grid.getCellAdjacentToPath();
        }
        publishEvent();
    }

    /**
     *Makes a path in the maze starting from a given cell
     * @param cell Cell to start the path from
     * @throws InterruptedException
     */
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
