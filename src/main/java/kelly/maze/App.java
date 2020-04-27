package kelly.maze;

import javax.swing.*;

public class App {
    private static final int ROWS = 20;
    private static final int COLUMNS = 30;
    private static final int SCALE = 30;
    private static final long WALKER_DELAY = 100;

    private MazePane pane;
    private MazeCellGrid grid;

    public void init() {
        grid = new MazeCellGrid(ROWS, COLUMNS);
        pane = new MazePane(grid, SCALE);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setResizable(false);

        f.add(pane);
        f.pack();
        f.setVisible(true);

    }

    public void start() throws InterruptedException{
        grid.reset();
        MazeMaker maker = new MazeMaker(grid);
        maker.addListener(pane);
        MazeCell start = grid.cellAt((int) (Math.random() * ROWS), (int) (Math.random() * COLUMNS));
        MazeCell end = grid.cellAt((int) (Math.random() * ROWS), (int) (Math.random() * COLUMNS));

        MazeWalker walker = new MazeWalker(grid, start, end, WALKER_DELAY);
        walker.addListener(pane);
        pane.setWalker(walker);

        maker.makeMaze();
        pane.update(pane.getGraphics());

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
