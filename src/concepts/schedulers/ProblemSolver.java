package concepts.schedulers;

import concepts.data.Task;
import concepts.data.TaskList;
import concepts.setup.ProblemDataCreator;
import concepts.setup.ProblemDataDto;
import ilog.concert.IloException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemSolver {
    public OutputDto solveProblem(String problemName) throws FileNotFoundException, IloException {
        TaskScheduler taskScheduler = new HeuristicSolution();
        ProblemDataCreator problemDataCreator = new ProblemDataCreator();

        ProblemDataDto data = problemDataCreator.createDataSetFromName(problemName);
        List<Task> taskDataList = data.taskList;

        List<Integer> initialSchedule = taskDataList.stream().map(task -> task.id).collect(Collectors.toList());
        int[][] initialSchedules = {initialSchedule.stream().mapToInt((Integer val) -> val).toArray(), {0}};

        TaskList taskList = new TaskList(taskDataList);


        OutputDto solutionOutput = taskScheduler.solve(taskList, initialSchedules);
        return new OutputDto(solutionOutput, data.firstCost);

    }
}
