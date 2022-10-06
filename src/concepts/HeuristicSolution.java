package concepts;

import ilog.concert.IloException;

import java.util.Arrays;
import java.util.List;

public class HeuristicSolution implements TaskScheduler{
    @Override
    public void solve(TaskList taskList, int[][] initialSchedules) throws IloException {
        LinearProgramSolution solver = new MixedIntegerProgram();

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
