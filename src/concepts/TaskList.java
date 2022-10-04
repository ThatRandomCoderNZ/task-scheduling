package concepts;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList(){
        tasks = new ArrayList<>();
    }

    public TaskList(int listSize){
        tasks = new ArrayList<>();
        for(int i = 0; i < listSize; i++){
            int taskProcCost = (int)(Math.random() * 20);
            int taskComCost = (int)(Math.random() * 20);
            Task task = new Task(i, taskProcCost, taskComCost);
            this.tasks.add(task);
        }
    }

    public int[] getAllScheduleCosts(){
        int[][] schedules = getAllFeasibleSchedules();
        int[] scheduleCosts = new int[schedules.length];
        for(int i = 0; i < schedules.length; i++){
            int cost = 0;
            for(int j = 0; j < schedules[i].length; j++){
                cost += tasks.get(schedules[i][j]).procCost;
            }
            scheduleCosts[i] = cost;
        }
        return scheduleCosts;
    }

    public int[][] getAllFeasibleSchedules(){
        int totalOptions = (int)Math.pow(2, getNumTasks());
        int[][] allSchedules = new int[totalOptions - 1][];
        for(int i = 1; i < totalOptions; i++){
            List<Integer> currentSchedule = new ArrayList<>();
            String binary = Integer.toBinaryString(i);
            for(int j = binary.length() - 1; j >= 0; j--){
                if(binary.charAt(j) == '1'){
                    currentSchedule.add(binary.length() - 1 - j);
                }
            }
            allSchedules[i - 1] = currentSchedule.stream().mapToInt((Integer val)->val).toArray();
        }
        return allSchedules;
    }

    public int getNumTasks(){
        return this.tasks.size();
    }

    public TaskList(List<Task> tasks){
        this.tasks = tasks;
    }
}
