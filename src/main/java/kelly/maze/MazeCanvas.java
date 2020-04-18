package kelly.maze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class MazeCanvas extends Canvas {
    private int scale;
    private MazeCellGrid grid;
    private MazeWalker walker;
    private BufferedImage walkerHead = null;

    /**
     * Constructs the maze canvas to be drawn on the scree
     * @param grid The grid where the maze is to be constructed
     * @param walker The solution walker of the maze
     * @param scale The scale or size of each unit of the grid
     */
    public MazeCanvas(MazeCellGrid grid, MazeWalker walker, int scale) {
        this.scale = scale;
        this.grid = grid;
        this.walker = walker;
    }

    /**
     * Clears the maze
     */
    public void clearMaze() {
        this.maze = null;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(scale * grid.getColumns(), scale * grid.getRows());
    }

    /**
     * Erases the walls of the maze
     * @param g Graphics of the canvas
     * @param cell The cell that is being erased
     */
    private void eraseWall(Graphics g, MazeCell cell) {
        g.setColor(Color.WHITE);
        boolean isConnected = false;
        if(cell.isBroken(MazeDirection.NORTH)) {
            isConnected = true;
            int y = cell.getRow() * scale;
            int x = cell.getColumn() * scale;
            g.drawLine(x + 1, y, x + scale - 1, y);
        }
        if(cell.isBroken(MazeDirection.WEST)) {
            isConnected = true;
            int y = cell.getRow() * scale;
            int x = cell.getColumn() * scale;
            g.drawLine(x, y + 1, x, y + scale - 1);
        }
        if(cell.isBroken(MazeDirection.EAST)) {
            isConnected = true;
            int y = cell.getRow() * scale;
            int x = (cell.getColumn() + 1 ) * scale;
            g.drawLine(x, y + 1, x, y + scale - 1);
        }
        if(cell.isBroken(MazeDirection.SOUTH)) {
            isConnected = true;
            int y = (cell.getRow() + 1) * scale;
            int x = cell.getColumn() * scale;
            g.drawLine(x + 1, y, x + scale - 1, y);
        }

        if(isConnected) {
            int x = cell.getColumn() * scale + 1;
            int y = cell.getRow() * scale + 1;
            g.fillRect(x, y, scale -2, scale -2);
        }
    }

    /**
     *Draws the walker solution to the maze
     * @param g Graphics of the canvas
     * @param c1 The starting cell to connect the path
     * @param c2 The ending cell to connect the path
     */
    private void drawMazePath(Graphics g, MazeCell c1, MazeCell c2) {
        int x1 = scale*c1.getColumn() + scale/2;
        int x2 = scale*c2.getColumn() + scale/2;
        int y1 = scale*c1.getRow() + scale/2;
        int y2 = scale*c2.getRow() + scale/2;
        g.drawLine(x1, y1, x2, y2);
    }

    /**
     * Draws the image of the maze walker head.
     * @param offset Distance from the walls of the maze
     * @return Buffered image of the walker head
     */
    private BufferedImage drawWalkerHeadImage(int offset) {
        if(walkerHead == null) {
            int w = scale - (offset * 2);
            walkerHead = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
            Graphics g = walkerHead.getGraphics();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints. VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            g.setColor(Color.RED);
            g.fillOval(0, 0, w, w);
        }
        return walkerHead;
    }

    /**
     * Draws the head image on the canvas
     * @param g Graphics of the canvas
     */
    private void drawWalkerHead(Graphics g) {
        int offset = 4;
        MazeCell head = walker.getHead();
        int x = scale * head.getColumn() + offset;
        int y = scale * head.getRow() + offset;
        g.drawImage(drawWalkerHeadImage(offset), x, y, this);
    }

    /**
     * Draws the maze on the grid
     * @return Buffered Image returned to be drawn
     */
    private BufferedImage drawMaze() {
        int width = scale * grid.getColumns();
        int height = scale * grid.getRows();
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffer.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints. VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        g.setColor(Color.BLACK);

        for(int r=0; r<=grid.getRows(); r++) {
            int y = scale * r;
            g.drawLine(0, y, width, y);
        }

        for(int c=0; c<=grid.getColumns(); c++) {
            int x = scale * c;
            g.drawLine(x, 0, x, height);
        }

        for (MazeCell cell : grid.getCells()) {
            eraseWall(g, cell);
        }
        return buffer;
    }

    /**
     * Draws the solution or maze walker path on the grid
     * @param g Graphics of the canvas
     */
    private void drawSolution(Graphics g) {
        Stack<MazeCell> path = walker.getPath();

        g.setColor(Color.BLUE);
        for(int i=0; i<path.size() - 1; i++) {
            drawMazePath(g, path.get(i), path.get(i + 1));
        }
    }

    /**
     * Overrides the paint method to draw the maze and the walker
     */
    private BufferedImage maze = null;
    @Override
    public void paint(Graphics g) {
        if(maze == null) {
            maze = drawMaze();
        }
        g.drawImage(maze, 0, 0, this);

        if(walker.hasStarted()) {
            drawSolution(g);
            drawWalkerHead(g);
        }
    }
}
