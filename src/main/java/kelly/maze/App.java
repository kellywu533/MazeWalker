package kelly.maze;

import javax.swing.*;

public class App {
    private static final int ROWS = 10;
    private static final int COLUMNS = 15;
    private static final int SCALE = 30;
    private static final long WALKER_DELAY = 100;

    private MazeCanvas canvas;
    private MazeCellGrid grid;
    private MazeCell start;
    private MazeCell end;
    private MazeMaker maker;
    private MazeWalker walker;

    public void init() {
        grid = new MazeCellGrid(ROWS, COLUMNS);
        canvas = new MazeCanvas(grid, SCALE);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setResizable(false);

        f.add(canvas);
        f.pack();
        f.setVisible(true);

    }

    public void start() throws InterruptedException{
        grid.reset();
        maker = new MazeMaker(grid);
        maker.addListener(canvas);
        start = grid.cellAt((int) (Math.random() * ROWS), (int) (Math.random() * COLUMNS));
        end = grid.cellAt((int) (Math.random() * ROWS), (int) (Math.random() * COLUMNS));
        walker = new MazeWalker(grid, start, end, WALKER_DELAY);
        walker.addListener(canvas);
        canvas.setWalker(walker);

        maker.makeMaze();
        walker.solve();
    }

    public static void main(String[] args) throws InterruptedException {
        App app = new App();
        app.init();
        while(true) {
            app.start();
            Thread.sleep(1000);
        }
    }
}
