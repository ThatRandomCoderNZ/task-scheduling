package concepts;

import ilog.concert.IloException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeuristicSolution implements TaskScheduler{
    @Override
    public OutputDto solve(TaskList taskList, int[][] initialSchedules) throws IloException {
        LinearProgramSolution solver = new MixedIntegerProgram();

        ScheduleList schedules = new ScheduleList(initialSchedules);

        for(int i = 0; i < 2; i++){
            schedules.mutateRandomly(taskList.getNumTasks());
        }
        double currentBest = Double.MAX_VALUE;

        int randomShakeLimit = 1;
        int randomShakeCounter = 0;
        boolean stuck = false;
        int solutionCounter = 0;
        double lastCost = 0;
        int columns = 0;
        List<Integer> columnCounts = new ArrayList<>();
        List<Double> improvements = new ArrayList<>();
        columnCounts.add(schedules.scheduleCount());
        while(!stuck) {
            System.out.println("Columns Generated: " + (schedules.scheduleCount() - columns));
            SolutionDto solution = solver.solve(taskList, schedules);
            //System.out.println("Solution " + solutionCounter + " cost: " + solution.minCost);
            columns = schedules.scheduleCount();
            double currentCost = solution.minCost;
            improvements.add(lastCost - currentCost);
            System.out.println("Current Columns: " + columns + " Current Cost: " + currentCost + " Improvement: " + (lastCost - currentCost));
            lastCost = currentCost;
            int mostCostly = schedules.getMostCostly(solution.solution);
            int mostCostlyValue = schedules.getCostOf(mostCostly);
            int leastCostly = schedules.getLeastCostly(solution.solution);
            int secondLeastCostly = schedules.getSecondLeastCostly(solution.solution);

            //System.out.println(Math.round(currentBest)  + "," + Math.round(solution.minCost));

            if(Math.round(currentBest) - Math.round(solution.minCost) == 0){
                if(randomShakeCounter < randomShakeLimit) {
                    schedules.mutateRandomly(taskList.getNumTasks());
                    schedules.mutateOnSchedule(mostCostly, leastCostly, taskList);
                    //schedules.mutateOnSchedule(mostCostly, secondLeastCostly, taskList);
                    randomShakeCounter += 1;
                }else{
                    stuck = true;
                }
            }else{
                randomShakeCounter = 0;
                schedules.smartMutate(mostCostly, leastCostly);
                //schedules.mutateOnSchedule(mostCostly, leastCostly, taskList);
            }

            if(currentBest > Math.round(solution.minCost)){
                currentBest = Math.round(solution.minCost);
            }
            solutionCounter += 1;
            //System.out.println("Before: " + schedules.scheduleCount());
            schedules.getAllScheduleCosts(taskList);
            schedules.purgeCostlySchedules((int) Math.ceil(solution.minCost));
            //System.out.println("After: " + schedules.scheduleCount());
            columnCounts.add(schedules.scheduleCount());
        }

        //schedules.getAllFeasibleSchedules(taskList);
//        schedules.getAllScheduleCosts(taskList);
//        schedules.printAllGaps();
        //solver.solve(taskList, schedules);
        return new OutputDto(currentBest, columnCounts, improvements);
    }
}
