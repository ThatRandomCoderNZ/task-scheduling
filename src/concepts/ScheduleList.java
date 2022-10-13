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

    private class ScheduleCostComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(scheduleCosts[o1], scheduleCosts[o2]);
        }
    }

    private class BackwardsScheduleCostComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(scheduleCosts[o2], scheduleCosts[o1]);
        }
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

    public int getCostOf(int scheduleIndex){
        return scheduleCosts[scheduleIndex];
    }

    public void purgeCostlySchedules(int upperBound){
        List<int[]> schedulesToKeep = new ArrayList<>();
        //System.out.println("Upper Bound: " + upperBound);
        for(int i = 0; i < schedules.length; i++){
            if(scheduleCosts[i] <= upperBound){
                //System.out.println("Adding: " + Arrays.toString(schedules[i]) + " Cost: "+ scheduleCosts[i]);
                schedulesToKeep.add(schedules[i]);
            }
        }

        //System.out.println(this.scheduleCount());
        this.schedules = schedulesToKeep.toArray(new int[0][]);
        //System.out.println(this.scheduleCount());
    }

    public void smartMutate(int costlyScheduleIndex, int cheapScheduleIndex){
        if(costlyScheduleIndex == cheapScheduleIndex){
            return;
        }

        List<Integer> worstSchedule = Arrays.stream(schedules[costlyScheduleIndex])
                .boxed()
                .collect(Collectors.toList());
        List<Integer> bestSchedule = Arrays.stream(schedules[cheapScheduleIndex]).boxed().collect(Collectors.toList());

        worstSchedule.sort(new BackwardsScheduleCostComparator());
        bestSchedule.sort(new ScheduleCostComparator());

        List<int[]> schedules = new ArrayList<>();

        int maxBestDepth = Integer.min(12, bestSchedule.size());
        int maxWorstDepth = Integer.min(12, worstSchedule.size());

        for(int i = 0; i < maxWorstDepth; i++){
            for(int j = 0; j < maxBestDepth; j++) {
                List<Integer> schedule = new ArrayList<>(worstSchedule);
                schedule.remove(i);
                schedule.add(bestSchedule.get(j));
                schedules.add(schedule.stream().sorted().mapToInt((Integer val) -> val).toArray());
            }
        }

        for(int i = 0; i < maxBestDepth; i++){
            for(int j = 0; j < maxWorstDepth; j++) {
                List<Integer> schedule = new ArrayList<>(bestSchedule);
                schedule.remove(i);
                schedule.add(worstSchedule.get(j));
                schedules.add(schedule.stream().sorted().mapToInt((Integer val) -> val).toArray());
                //System.out.println(Arrays.toString(schedule.toArray()));
            }
        }

        List<int[]> newSchedules = new ArrayList<>(Arrays.asList(this.schedules));
        newSchedules.addAll(schedules);
        this.schedules = newSchedules.toArray(this.schedules);

    }

    public int scheduleCount(){
        return schedules.length;
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
                if(bestTasks.length > 0 && bestTasks[0] != includedTasks[0]) {
                    for (int bestTask : bestTasks) {
                        if (Math.random() * 10 > 2) {
                            schedule.add(bestTask);
                        }
                    }
                }
                //System.out.println(Arrays.toString(schedule.stream().sorted().mapToInt((Integer val) -> val).toArray()));
                if (Math.random() * 10 > 5) {
                    mutations.add(schedule.stream().sorted().mapToInt((Integer val) -> val).toArray());
                }
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
        //System.out.println(schedules.length);
        for(int i = 0; i < schedules.length; i++){

            List<AbstractMap.SimpleImmutableEntry<Integer, Integer>> communicationDelays = new ArrayList<>();
            for(int j = 0; j < schedules[i].length; j++){
                AbstractMap.SimpleImmutableEntry<Integer, Integer> taskComDelay = new AbstractMap.SimpleImmutableEntry<>(j,tasks.getComDelay(schedules[i][j]));
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
            //System.out.println("schedule: " + Arrays.toString(schedules[i]) + " delays: " + communicationDelays );
            for(int j = 0; j < communicationDelays.size(); j++){
                int comDelay = Math.max(communicationDelays.get(j).getValue() - cost, 0);
                totalComDelay += comDelay;
                cost += tasks.getTaskCost(schedules[i][communicationDelays.get(j).getKey()]) + comDelay;
                //System.out.println("task: " + j + " com delay: " + comDelay + " cost: " + cost);
            }
            //System.out.println("schedule: " + Arrays.toString(schedules[i]) + " cost: "  +cost);
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
