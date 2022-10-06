package concepts;

import java.util.*;
import java.util.stream.Collectors;

public class ScheduleList {
    private int[][] schedules;
    private int[] scheduleCosts;

    private int[] scheduleGap;
    public ScheduleList(){}

    public ScheduleList(int[][] schedules){
        this.schedules = schedules;
    }

    //Has a nasty side effect with invocation order dependency
    public int[][] getSchedules() {
        return schedules;
    }

    public int getScheduleGap(int index){
        return this.scheduleGap[index];
    }

    public int getMostCostly(List<Integer> scheduleIndices){
        int maxVal = 0;
        int maxIndex = -1;
        return scheduleIndices.stream()
                .max(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(scheduleCosts[o1], scheduleCosts[o2]);
                    }
                }).orElse(0);

    }

    public int getLeastCostly(List<Integer> scheduleIndices){
        int maxVal = 0;
        int maxIndex = -1;
        return scheduleIndices.stream()
                .min(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(scheduleCosts[o1], scheduleCosts[o2]);
                    }
                }).orElse(0);

    }

    public int getSecondLeastCostly(List<Integer> scheduleIndices){
        int maxVal = 0;
        int maxIndex = -1;
        List<Integer> sortedIndices = scheduleIndices.stream().sorted(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare( scheduleCosts[o1], scheduleCosts[o2]);
            }
        }).collect(Collectors.toList());

        if(sortedIndices.size() == 0){
            return 0;
        }

        return sortedIndices.size() > 1 ? sortedIndices.get(1): sortedIndices.get(0);

    }

    public void mutateRandomly(int taskNum){
        int totalOptions = (int)Math.pow(2, taskNum);
        int mutatedOptions = (int)(totalOptions * .02);

        List<int[]> mutations = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            List<Integer> mutation = new ArrayList<>();
            for(int j = 0; j < taskNum; j++){
                if(Math.random() > .7){
                    mutation.add(j);
                }
            }
            mutations.add(mutation.stream().mapToInt((Integer val) -> val).toArray());


        }

        List<int[]> newSchedules = new ArrayList<>(Arrays.asList(schedules));
        newSchedules.addAll(mutations);
        schedules = newSchedules.toArray(schedules);
    }

    private int pickRandomFromRange(int rangeLimit){
        boolean picked = false;
        int indexCounter = 0;
        while(!picked){
            indexCounter++;
            if(indexCounter >= rangeLimit){
                indexCounter = 0;
            }
            picked = (Math.random() * 10 > 2);
        }
        return indexCounter;
    }

    public void mutateOnSchedule(int costlyScheduleIndex, int cheapScheduleIndex, TaskList taskList){
        int[] includedTasks = schedules[costlyScheduleIndex];
        int[] bestTasks = schedules[cheapScheduleIndex];
        int[] excludedTasks = taskList.getExcludedList(Arrays.stream(includedTasks).boxed().collect(Collectors.toList()));

        List<int[]> mutations = new ArrayList<>();
        for (int includedTask : includedTasks) {
            Set<Integer> schedule = new HashSet<>();
            schedule.add(includedTask);
            if (excludedTasks.length > 0) {
                schedule.add(excludedTasks[pickRandomFromRange(excludedTasks.length)]);
            }
            List<Integer> scheduleToAdd = new ArrayList<>(schedule);
            mutations.add(scheduleToAdd.stream().sorted().mapToInt((Integer val) -> val).toArray());
        }

        for(int i = 0; i < includedTasks.length; i++){
            for(int k = i; k >= 0; k--){
                List<Integer> schedule = new ArrayList<>();
                for(int b = k; b <= i; b ++) {
                    schedule.add(includedTasks[b]);
                }
                if(bestTasks[0] != includedTasks[0]) {
                    for (int bestTask : bestTasks) {
                        if (Math.random() * 10 > 7) {
                            schedule.add(bestTask);
                        }
                    }
                }
                mutations.add(schedule.stream().sorted().mapToInt((Integer val) -> val).toArray());
            }

        }

        List<int[]> newSchedules = new ArrayList<>(Arrays.asList(this.schedules));
        newSchedules.addAll(mutations);
        schedules = newSchedules.toArray(schedules);

        //System.out.println(Arrays.deepToString(schedules));
        //Mutation Key
    }

    public int[] getAllScheduleCosts(TaskList tasks){
        int[] scheduleCosts = new int[schedules.length];
        int[] scheduleGaps = new int[schedules.length];
        for(int i = 0; i < schedules.length; i++){

            List<AbstractMap.SimpleImmutableEntry<Integer, Integer>> communicationDelays = new ArrayList<>();
            for(int j = 0; j < schedules[i].length; j++){
                AbstractMap.SimpleImmutableEntry<Integer, Integer> taskComDelay = new AbstractMap.SimpleImmutableEntry<>(j,tasks.getComDelay(j));
                communicationDelays.add(taskComDelay);
            }
            communicationDelays.sort(new Comparator<AbstractMap.SimpleImmutableEntry<Integer, Integer>>() {
                @Override
                public int compare(AbstractMap.SimpleImmutableEntry<Integer, Integer> o1, AbstractMap.SimpleImmutableEntry<Integer, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

            int cost = 0;
            int totalComDelay = 0;
            for(int j = 0; j < communicationDelays.size(); j++){
                int comDelay = Math.max(communicationDelays.get(j).getValue() - cost, 0);
                totalComDelay += comDelay;
                cost += tasks.getTaskCost(schedules[i][j]) + comDelay;
            }
            scheduleCosts[i] = cost;
            scheduleGaps[i] = totalComDelay;
        }
        this.scheduleCosts = scheduleCosts;
        this.scheduleGap = scheduleGaps;
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

    public void printAllGaps(){
        Arrays.stream(this.scheduleGap).forEach(System.out::println);
    }
}
