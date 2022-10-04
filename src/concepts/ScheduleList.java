package concepts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleList {
    private int[][] schedules;
    private int[] scheduleCosts;

    public ScheduleList(){}

    public ScheduleList(int[][] schedules){
        this.schedules = schedules;
    }

    //Has a nasty side effect with invocation order dependency
    public int[][] getSchedules() {
        return schedules;
    }

    public int[] getAllScheduleCosts(TaskList tasks){
        int[] scheduleCosts = new int[schedules.length];
        for(int i = 0; i < schedules.length; i++){
            int cost = 0;
            for(int j = 0; j < schedules[i].length; j++){
                cost += tasks.getTaskCost(schedules[i][j]);
            }
            scheduleCosts[i] = cost;
        }
        return scheduleCosts;
    }

    public void getAllFeasibleSchedules(TaskList taskList){
        int totalOptions = (int)Math.pow(2, taskList.getNumTasks());
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
        System.out.println(Arrays.deepToString(allSchedules));
        schedules = allSchedules;
    }
}
