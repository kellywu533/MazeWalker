package kelly.maze;

public class MazeWalkerEvent {
    public enum Type {
        FORWARD
        , BACKWARD
        , WALL
        , FINISH;
    }
    private Type eventType;

    public MazeWalkerEvent(Type t) {
        eventType = t;
    }

    public Type getEventType() {
        return eventType;
    }
}
