package kelly.maze;

/**
 *Listens to maze maker events
 */
public interface MazeMakerEventListener {
    /**
     * Update method to be implemented based on the event
     * @param e
     */
    void update(MazeMakerEvent e);
}
