package concepts;

import ilog.concert.IloException;

import java.util.Arrays;
import java.util.List;

public class HeuristicSolution implements TaskScheduler{
    @Override
    public void solve() throws IloException {
        LinearProgramSolution solver = new MixedIntegerProgram();
        TaskList taskList = new TaskList(10);
        int[][] initialSchedules = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                {0, 1, 2, 3},
                {8, 9},
                {4, 5, 6, 7}
        };
        ScheduleList schedules = new ScheduleList(initialSchedules);

        for(int i = 0; i < 2; i++){
            schedules.mutateRandomly(taskList.getNumTasks());
        }

        System.out.println(Arrays.deepToString(schedules.getSchedules()));
        for(int i = 0; i < 30; i++) {
            System.out.println("Solution " + i);
            List<Integer> solutions = solver.solve(taskList, schedules);
            int mostCostly = schedules.getMostCostly(solutions);
            schedules.mutateOnSchedule(mostCostly, taskList);

        }
        schedules.getAllFeasibleSchedules(taskList);
        solver.solve(taskList, schedules);
    }
}
