package concepts;

import ilog.concert.IloException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemSolver {
    public double solveProblem(String problemName) throws FileNotFoundException, IloException {
        TaskScheduler taskScheduler = new HeuristicSolution();
        ProblemDataCreator problemDataCreator = new ProblemDataCreator();

        ProblemDataDto data = problemDataCreator.createDataSetFromName(problemName);
        List<Task> taskDataList = data.taskList;

        List<Integer> initialSchedule = taskDataList.stream().map(task -> task.id).collect(Collectors.toList());
        int[][] initialSchedules = {initialSchedule.stream().mapToInt((Integer val) -> val).toArray(), {0}};

        TaskList taskList = new TaskList(taskDataList);


        return taskScheduler.solve(taskList, initialSchedules) + data.firstCost;

    }
}
