package concepts.schedulers;

import concepts.data.TaskList;
import ilog.concert.IloException;

public interface TaskScheduler {
    public OutputDto solve(TaskList taskList, int[][] initialScheduler) throws IloException;
}
