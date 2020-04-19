package kelly.maze;

import javax.swing.*;

public class App {
    private static final int ROWS = 20;
    private static final int COLUMNS = 20;
    private static final int SCALE = 30;
    private static final long WALKER_DELAY = 200;

    public static void start() throws InterruptedException {
        MazeCellGrid grid = new MazeCellGrid(ROWS, COLUMNS);
        MazeMaker maker = new MazeMaker(grid);
        MazeCell start = grid.cellAt(0,0);
        MazeCell end = grid.cellAt(ROWS - 1, COLUMNS - 1);
        MazeWalker walker = new MazeWalker(grid, end, WALKER_DELAY);
        MazeCanvas canvas = new MazeCanvas(grid, walker, SCALE);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setResizable(false);

        f.add(canvas);
        f.pack();
        f.setVisible(true);

        maker.addListener(canvas);
        maker.makeMaze();

        walker.addListener(canvas);
        walker.solveFrom(start);
    }

    public static void main(String[] args) throws InterruptedException {
        start();
    }
}
