package concepts;

import ilog.concert.IloException;

import java.util.Arrays;
import java.util.List;

public class HeuristicSolution implements TaskScheduler{
    @Override
    public double solve(TaskList taskList, int[][] initialSchedules) throws IloException {
        LinearProgramSolution solver = new MixedIntegerProgram();

        ScheduleList schedules = new ScheduleList(initialSchedules);

        for(int i = 0; i < 2; i++){
            schedules.mutateRandomly(taskList.getNumTasks());
        }
        double currentBest = Double.MAX_VALUE;

        int randomShakeLimit = 5;
        int randomShakeCounter = 0;
        boolean stuck = false;
        int solutionCounter = 0;
        while(!stuck) {
            SolutionDto solution = solver.solve(taskList, schedules);
            //System.out.println("Solution " + solutionCounter + " cost: " + solution.minCost);
            int mostCostly = schedules.getMostCostly(solution.solution);
            int leastCostly = schedules.getLeastCostly(solution.solution);
            int secondLeastCostly = schedules.getSecondLeastCostly(solution.solution);


            if(Math.round(currentBest) == Math.round(solution.minCost)){
                if(randomShakeCounter < randomShakeLimit) {
                    schedules.mutateRandomly(taskList.getNumTasks());
                    schedules.mutateOnSchedule(mostCostly, secondLeastCostly, taskList);
                    randomShakeCounter += 1;
                }else{
                    stuck = true;
                }
            }else{
                randomShakeCounter = 0;
                schedules.mutateOnSchedule(mostCostly, leastCostly, taskList);
            }

            if(currentBest > solution.minCost){
                currentBest = solution.minCost;
            }
            solutionCounter += 1;
        }
        //schedules.getAllFeasibleSchedules(taskList);
//        schedules.getAllScheduleCosts(taskList);
//        schedules.printAllGaps();
        //solver.solve(taskList, schedules);
        return currentBest;
    }
}
