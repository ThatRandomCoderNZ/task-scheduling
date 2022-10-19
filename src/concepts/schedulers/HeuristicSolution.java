package concepts.schedulers;

import concepts.data.ScheduleList;
import concepts.data.TaskList;
import concepts.solvers.LinearProgramSolution;
import concepts.solvers.MixedIntegerProgram;
import ilog.concert.IloException;

import java.util.ArrayList;
import java.util.List;

public class HeuristicSolution implements TaskScheduler{
    @Override
    public OutputDto solve(TaskList taskList, int[][] initialSchedules) throws IloException {
        LinearProgramSolution solver = new MixedIntegerProgram();

        ScheduleList schedules = new ScheduleList(initialSchedules);

        //Generate a small number of random columns for the solver to work with.
        for(int i = 0; i < 2; i++){
            schedules.mutateRandomly(taskList.getNumTasks());
        }
        double currentBest = Double.MAX_VALUE;

        //set up scheduling parameters
        int randomShakeLimit = 1; // how many times to attempt restart before termination
        int randomShakeCounter = 0;
        boolean stuck = false;
        double lastCost = 0;
        int columns = 0;

        List<Integer> columnCounts = new ArrayList<>();
        List<Double> improvements = new ArrayList<>();
        columnCounts.add(schedules.scheduleCount());
        while(!stuck) {
            System.out.println("Columns Generated: " + (schedules.scheduleCount() - columns));

            SolutionDto solution = solver.solve(taskList, schedules);
            columns = schedules.scheduleCount();
            double currentCost = solution.minCost;
            improvements.add(lastCost - currentCost);
            System.out.println("Current Columns: " + columns + " Current Cost: " + currentCost + " Improvement: " + (lastCost - currentCost));
            lastCost = currentCost;
            int mostCostly = schedules.getMostCostly(solution.solution);
            int leastCostly = schedules.getLeastCostly(solution.solution);



            if(Math.round(currentBest) - Math.round(solution.minCost) == 0){
                if(randomShakeCounter < randomShakeLimit) {
                    schedules.mutateRandomly(taskList.getNumTasks());
                    schedules.mutateOnSchedule(mostCostly, leastCostly, taskList);
                    randomShakeCounter += 1;
                }else{
                    stuck = true;
                }
            }else{
                randomShakeCounter = 0;
                schedules.smartMutate(mostCostly, leastCostly);
            }

            if(currentBest > Math.round(solution.minCost)){
                currentBest = Math.round(solution.minCost);
            }

            schedules.getAllScheduleCosts(taskList);
            schedules.purgeCostlySchedules((int) Math.ceil(solution.minCost));
            columnCounts.add(schedules.scheduleCount());
        }

        return new OutputDto(currentBest, columnCounts, improvements);
    }
}
