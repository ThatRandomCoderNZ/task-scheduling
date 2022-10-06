package concepts;

import ilog.concert.IloException;

public interface TaskScheduler {
    public double solve(TaskList taskList, int[][] initialScheduler) throws IloException;
}
