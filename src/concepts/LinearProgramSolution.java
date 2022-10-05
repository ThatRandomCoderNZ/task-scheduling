package concepts;

import ilog.concert.IloException;

import java.util.List;

public interface LinearProgramSolution {
    List<Integer> solve(TaskList taskList, ScheduleList schedules) throws IloException;
}
