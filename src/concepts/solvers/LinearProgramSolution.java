package concepts.solvers;

import concepts.data.ScheduleList;
import concepts.schedulers.SolutionDto;
import concepts.data.TaskList;
import ilog.concert.IloException;

public interface LinearProgramSolution {
    SolutionDto solve(TaskList taskList, ScheduleList schedules) throws IloException;
}
