package ppp.chapter8;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在解决器中找不到解答
 */
public class PuzzleSolver <P, M> extends ConcurrentPuzzleSolver<P, M> {


    public PuzzleSolver(Puzzle<P, M> puzzle) {
        super(puzzle);
    }

    private final AtomicInteger taskCount = new AtomicInteger(0);

    @Override
    protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
        return new CoutingSolverTask(p, m, n);
    }

    class CoutingSolverTask extends SolverTask {

        CoutingSolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0)
                    solution.setValue(null);
            }
        }
    }
}
