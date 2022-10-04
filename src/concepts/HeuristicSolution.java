package concepts;

import ilog.concert.IloException;

public class HeuristicSolution implements TaskScheduler{
    @Override
    public void solve() throws IloException {
        LinearProgramSolution solver = new MixedIntegerProgram();
        TaskList taskList = new TaskList(10);
        solver.solve(taskList);
    }
}
