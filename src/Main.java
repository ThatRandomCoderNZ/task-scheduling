import concepts.*;
import ilog.concert.IloException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IloException, FileNotFoundException {
        TaskScheduler taskScheduler = new HeuristicSolution();
        long initialTimestamp = System.nanoTime();

        System.out.println(Math.random() * 10);

        ProblemInputHandler inputHandler = new ProblemInputHandler();
        String fileName = "MM5";
        List<List<String>> taskData = inputHandler.getData(fileName, true);
        List<List<String>> comData = inputHandler.getData(fileName, false);

        List<Integer> taskCosts = new ArrayList<>();
        for(List<String> record: taskData){
            for(String data: record){
                int cost = Integer.parseInt(data.trim().replaceAll("\\s+", " ").split(" ")[1]);
                taskCosts.add(cost);
            }
        }
        int firstTaskCost = taskCosts.get(0);
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
        int[][] initialSchedules = {initialSchedule.stream().mapToInt((Integer val) -> val).toArray(), {0}, {1,2,3,4}, {2, 3}, {9}};

        TaskList taskList = new TaskList(taskDataList);

//        TaskList taskList = new TaskList(20);
//        int[][] initialSchedules = {{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19}, {0}};

//        int[][] initialSchedules = {
//                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
//                {0, 1, 2, 3},
//                {8, 9},
//                {4, 5, 6, 7}
//        };

        double best = taskScheduler.solve(taskList, initialSchedules);

        long finalTimestamp = System.nanoTime();

        long executionTime = finalTimestamp - initialTimestamp;

        long executionTimeMilliseconds = executionTime / 1000000;

        System.out.println("Found " + best + " in " + executionTimeMilliseconds + " milliseconds");
    }
}

