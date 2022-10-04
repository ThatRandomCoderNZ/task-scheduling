package concepts;

import ilog.concert.IloException;

public class HeuristicSolution implements TaskScheduler{
    @Override
    public void solve() throws IloException {
        LinearProgramSolution solver = new MixedIntegerProgram();
        TaskList taskList = new TaskList(10);
        int[][] initialSchedules = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {8, 9},
        };
        ScheduleList schedules = new ScheduleList(initialSchedules);
        solver.solve(taskList, schedules);
    }
}
