import concepts.*;
import ilog.concert.IloException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IloException, FileNotFoundException {
        TaskScheduler taskScheduler = new HeuristicSolution();

        ProblemInputHandler inputHandler = new ProblemInputHandler();
        String fileName = "LL1";
        List<List<String>> taskData = inputHandler.getData(fileName, true);
        List<List<String>> comData = inputHandler.getData(fileName, false);

        List<Integer> taskCosts = new ArrayList<>();
        for(List<String> record: taskData){
            for(String data: record){
                int cost = Integer.parseInt(data.trim().replaceAll("\\s+", " ").split(" ")[1]);
                taskCosts.add(cost);
            }
        }
        taskCosts.remove(0);
        List<Integer> comCosts = new ArrayList<>();
        for(List<String> record: comData){
            for(String recordData: record){
                String[] data = recordData.trim().replaceAll("\\s+", " ").split(" ");
                comCosts.add(Integer.parseInt(data[2]));
            }
        }

        List<Task> taskDataList = new ArrayList<>();
        for(int i = 0; i < taskCosts.size(); i++){
            Task task = new Task(i, taskCosts.get(i), comCosts.get(i));
            taskDataList.add(task);
        }

        List<Integer> initialSchedule = taskDataList.stream().map(task -> task.id).collect(Collectors.toList());
        int[][] initialSchedules = {initialSchedule.stream().mapToInt((Integer val) -> val).toArray()};

        TaskList taskList = new TaskList(taskDataList);
//        int[][] initialSchedules = {
//                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
//                {0, 1, 2, 3},
//                {8, 9},
//                {4, 5, 6, 7}
//        };
//
       taskScheduler.solve(taskList, initialSchedules);
    }
}

