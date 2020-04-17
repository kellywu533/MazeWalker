package kelly.maze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class MazeCanvas extends Canvas {
    private int scale;
    MazeCellGrid grid;
    MazeWalker walker;
    BufferedImage walkerHead = null;

    public MazeCanvas(MazeCellGrid grid, MazeWalker walker, int scale) {
        this.scale = scale;
        this.grid = grid;
        this.walker = walker;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(scale * grid.getColumns(), scale * grid.getRows());
    }

    private void eraseWall(Graphics g, MazeCell cell) {
        g.setColor(getBackground());
        if(cell.isBroken(MazeDirection.NORTH)) {
            int y = cell.getRow() * scale;
            int x = cell.getColumn() * scale;
            g.drawLine(x + 1, y, x + scale - 1, y);
        }
        if(cell.isBroken(MazeDirection.WEST)) {
            int y = cell.getRow() * scale;
            int x = cell.getColumn() * scale;
            g.drawLine(x, y + 1, x, y + scale - 1);
        }
        if(cell.isBroken(MazeDirection.EAST)) {
            int y = cell.getRow() * scale;
            int x = (cell.getColumn() + 1 ) * scale;
            g.drawLine(x, y + 1, x, y + scale - 1);
        }
        if(cell.isBroken(MazeDirection.SOUTH)) {
            int y = (cell.getRow() + 1) * scale;
            int x = cell.getColumn() * scale;
            g.drawLine(x + 1, y, x + scale - 1, y);
        }
    }

    private void drawMazePath(Graphics g, MazeCell c1, MazeCell c2) {
        int x1 = scale*c1.getColumn() + scale/2;
        int x2 = scale*c2.getColumn() + scale/2;
        int y1 = scale*c1.getRow() + scale/2;
        int y2 = scale*c2.getRow() + scale/2;
        g.drawLine(x1, y1, x2, y2);
    }

    /**
     * Draws the image of the maze walker head.
     * @param offset distance from the walls of the maze
     * @return buffered image of the walker head
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
     * @param g graphics of the canvas
     */
    private void drawWalkerHead(Graphics g) {
        int offset = 4;
        MazeCell head = walker.getHead();
        int x = scale * head.getColumn() + offset;
        int y = scale * head.getRow() + offset;
        g.drawImage(drawWalkerHeadImage(offset), x, y, this);
    }

    @Override
    public void paint(Graphics g) {
        int width = scale * grid.getColumns();
        int height = scale * grid.getRows();
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

        Stack<MazeCell> path = walker.getPath();

        g.setColor(Color.BLUE);
        for(int i=0; i<path.size() - 1; i++) {
            drawMazePath(g, path.get(i), path.get(i + 1));
        }

        drawWalkerHead(g);
    }
}
