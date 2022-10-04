package concepts;

import ilog.concert.IloException;

public interface LinearProgramSolution {
    void solve(TaskList taskList) throws IloException;
}
