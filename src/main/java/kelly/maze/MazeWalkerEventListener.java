package kelly.maze;

/**
 *Listens to the maze walker events
 */
public interface MazeWalkerEventListener {
    /**
     * Update method to be implemented based on the event
     * @param e Type of event
     */
    void update(MazeWalkerEvent e);
}
