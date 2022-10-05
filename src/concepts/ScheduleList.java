package concepts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public int getMostCostly(List<Integer> scheduleIndices){
        int maxVal = 0;
        int maxIndex = -1;
        System.out.println(scheduleIndices);
        return scheduleIndices.stream()
                .max(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(scheduleCosts[o1], scheduleCosts[o2]);
                    }
                }).orElse(0);

    }

    public void mutateRandomly(int taskNum){
        int totalOptions = (int)Math.pow(2, taskNum);
        int mutatedOptions = (int)(totalOptions * .02);

        List<int[]> mutations = new ArrayList<>();
        for(int i = 0; i < 40; i++){
            List<Integer> mutation = new ArrayList<>();
            for(int j = 0; j < taskNum; j++){
                if(Math.random() > .3){
                    mutation.add(j);
                }
            }
            mutations.add(mutation.stream().mapToInt((Integer val) -> val).toArray());


        }

        List<int[]> newSchedules = new ArrayList<>(Arrays.asList(schedules));
        newSchedules.addAll(mutations);
        schedules = newSchedules.toArray(schedules);
    }

    public String mutateOnSchedule(int scheduleIndex, TaskList taskList){
        int[] includedTasks = schedules[scheduleIndex];
        int[] excludedTasks = taskList.getExcludedList(Arrays.stream(includedTasks).boxed().collect(Collectors.toList()));

        List<int[]> mutations = new ArrayList<>();
        for(int i = 0; i < includedTasks.length; i++){
            for(int j = 0; j < excludedTasks.length; j++){
                int[] schedule = includedTasks.clone();
                schedule[i] = excludedTasks[j];
                mutations.add(Arrays.stream(schedule).sorted().toArray());
            }
        }

//        System.out.println("Task Lists");
//        System.out.println(Arrays.toString(includedTasks));
//        System.out.println(Arrays.toString(excludedTasks));
        List<int[]> newSchedules = new ArrayList<>(Arrays.asList(this.schedules));
        //System.out.println("Mutations");
        //mutations.forEach(schedule -> System.out.println(Arrays.toString(schedule)));
        //System.out.println(mutations.getClass() + " " + newSchedules.getClass());
        newSchedules.addAll(mutations);
        schedules = newSchedules.toArray(schedules);

        //System.out.println(Arrays.deepToString(schedules));
        //Mutation Key
        return Arrays.toString(includedTasks);
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
        this.scheduleCosts = scheduleCosts;
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
        //System.out.println(Arrays.deepToString(allSchedules));
        schedules = allSchedules;
    }
}