package kelly.maze;

public enum MazeDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public MazeDirection opposite() {
        switch(this) {
            case NORTH :
                return SOUTH;
            case EAST :
                return WEST;
            case SOUTH :
                return NORTH;
            case WEST :
                return EAST;
        }
        return null;
    }
}
