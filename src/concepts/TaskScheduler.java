package concepts;

import ilog.concert.IloException;

public interface TaskScheduler {
    public OutputDto solve(TaskList taskList, int[][] initialScheduler) throws IloException;
}
