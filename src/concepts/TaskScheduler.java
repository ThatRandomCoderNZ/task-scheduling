package concepts;

import ilog.concert.IloException;

public interface TaskScheduler {
    public void solve(TaskList taskList, int[][] initialScheduler) throws IloException;
}
