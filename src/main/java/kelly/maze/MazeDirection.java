package kelly.maze;

/**
 *Enum of maze directions of a maze cell
 */
public enum MazeDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    /**
     *Gets the opposite direction given a direction
     * @return Opposite direction
     */
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
