package kelly.maze;

import javax.swing.*;
import java.awt.*;

public class App {
    private static final int ROWS = 20;
    private static final int COLUMNS = 20;
    private static final int SCALE = 30;
    private static final long MAKER_DELAY = 20;
    private static final long WALKER_DELAY = 100;

    public static Thread maker(final MazeCanvas c, final MazeMaker m, final long delay) {
        Thread t = new Thread() {
            @Override
            public void run() {
                while(!m.isFinished()) {
                    c.clearMaze();
                    c.repaint();
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                c.clearMaze();
                c.repaint();
            }
        };
        return t;
    }

    public static Thread walker(final Canvas c, final MazeWalker w, final long delay) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(!w.isFinished()) {
                    c.repaint();

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                c.repaint();
            }
        };
        return new Thread(r);
    }

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

        Thread t = maker(canvas, maker, MAKER_DELAY);
        t.start();
        maker.makeMaze();
        t.join();

        t = walker(canvas, walker, WALKER_DELAY);
        t.start();
        walker.solveFrom(start);
    }

    public static void main(String[] args) throws InterruptedException {
        start();
    }
}
