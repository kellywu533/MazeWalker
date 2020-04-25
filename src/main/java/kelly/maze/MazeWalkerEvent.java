package kelly.maze;

/**
 *Event of the maze walker
 */
public class MazeWalkerEvent {
    /**
     *Types of events of the maze walker
     */
    public enum Type {
        FORWARD
        , BACKWARD
        , WALL
        , FINISH;
    }
    private Type eventType;

    /**
     *Constructs a new event of a specified type
     * @param t Type of event that is shared with the class
     */
    public MazeWalkerEvent(Type t) {
        eventType = t;
    }

    /**
     *Gets the event type
     * @return Type of event
     */
    public Type getEventType() {
        return eventType;
    }
}
