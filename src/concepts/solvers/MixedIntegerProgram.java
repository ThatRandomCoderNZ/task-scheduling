package concepts.solvers;

import concepts.data.ScheduleList;
import concepts.schedulers.SolutionDto;
import concepts.data.TaskList;
import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MixedIntegerProgram implements LinearProgramSolution {



    public SolutionDto solve(TaskList taskList, ScheduleList schedules) throws IloException {
        IloCplex cplex = new IloCplex();

        int numOfMachines = 4;

        int numOfJobs = taskList.getNumTasks();
        int[][] allFeasibleSchedules = schedules.getSchedules();
        //System.out.println(allFeasibleSchedules.length);

        int[] costOfSchedule = schedules.getAllScheduleCosts(taskList);

        //Task 1 - cost 1, Task 2 - cost 2, Task 3 - cost 3
        //int[][] allFeasibleSchedules = {
        //        {1},{1,2},{1,2,3},{2,3},{3},{3,2},{2},{3,1}
        //};                    //1 2 3 4 5 6 7 8
        //int[] costOfSchedule = {1,3,7,6,4,6,2,5};

        int[][] jobIsCovered = new int[taskList.getNumTasks()][];
        for(int i = 0; i < taskList.getNumTasks(); i++){
            int[] cover = new int[allFeasibleSchedules.length];
            for(int j = 0; j < allFeasibleSchedules.length; j++){
                List<Integer> schedule = Arrays.stream(allFeasibleSchedules[j]).boxed().collect(Collectors.toList());
                cover[j] = (schedule.contains(i)) ? 1:0;
            }
            jobIsCovered[i] = cover;
        }


//        int[][] jobIsCovered = {
//                //  2  3  4  5  6  7  8
//                {1, 1, 1, 0, 0, 0, 0, 1},
//                {0, 1, 1, 1, 0, 1, 1, 0},
//                {0, 0, 1, 1, 1, 1, 0, 1}
//        };



        IloNumVar[][] scheduleIsUsed = new IloNumVar[numOfMachines][];
        for(int i = 0; i < numOfMachines; i++){
            scheduleIsUsed[i] = cplex.boolVarArray(costOfSchedule.length);
        }

        IloNumVar[] z = new IloNumVar[1];
        z[0] = cplex.numVar(0, 10000);


        IloNumExpr[] objFunc = new IloNumExpr[numOfMachines];
        for(int index = 0; index < numOfMachines; index++){
            IloNumExpr expr = cplex.numExpr();
            objFunc[index] = cplex.sum(expr, cplex.scalProd(costOfSchedule, scheduleIsUsed[index]));
        }

        cplex.addMinimize(cplex.sum(z));


        IloRange[] constraints = new IloRange[numOfJobs];
        for(int i = 0; i < numOfJobs; i++){
            IloNumExpr jobExecutedOnceConstraint = cplex.numExpr();
            for(int j = 0; j < numOfMachines; j++) {
                jobExecutedOnceConstraint = cplex.sum(jobExecutedOnceConstraint, cplex.scalProd(scheduleIsUsed[j], jobIsCovered[i]));
            }
            IloRange range = cplex.addEq(jobExecutedOnceConstraint, 1);
            constraints[i] = range;
        }

        for(int i = 0; i < allFeasibleSchedules.length; i++){
            IloNumExpr scheduleExecutedOnce = cplex.numExpr();
            for(int j = 0; j < numOfMachines; j++) {
                scheduleExecutedOnce = cplex.sum(scheduleExecutedOnce, cplex.min(cplex.prod(scheduleIsUsed[j][i], 1000), 1));
            }
            cplex.addLe(scheduleExecutedOnce, 1);
        }

        IloRange[] zConstraints = new IloRange[numOfMachines * allFeasibleSchedules.length];
        int index = 0;
        for(int m = 0; m < numOfMachines; m++){
            for(int s = 0; s < allFeasibleSchedules.length; s++){
                IloNumExpr test = cplex.prod(scheduleIsUsed[m][s], costOfSchedule[s]);
                IloRange range = (IloRange) cplex.addLe(test, z[0]);
                zConstraints[index] = range;
                index++;
            }
        }

        IloRange[] machineConstraints = new IloRange[numOfMachines];
        IloNumExpr totalMachines = cplex.numExpr();
        for(int i = 0; i < numOfMachines; i++) {
            totalMachines = cplex.sum(totalMachines, cplex.sum(scheduleIsUsed[i]));
        }
        cplex.addLe(totalMachines, numOfMachines);

        //cplex.exportModel("test.lp");

        List<Integer> solutions = new ArrayList<>();
        SolutionDto solution = new SolutionDto();
        solution.minCost = Double.MAX_VALUE;
        cplex.setOut(null);
        if(cplex.solve()){
            solution.minCost = cplex.getObjValue();
            for(int i = 0; i < numOfMachines; i++){
                double[] values = cplex.getValues(scheduleIsUsed[i]);
                for (int j = 0; j < scheduleIsUsed[i].length; j++) {
                    if(cplex.getValue(scheduleIsUsed[i][j]) == 1.0){
                        solutions.add(j);
                        System.out.println("Machine: " + i  + " Schedule: " + j + " Cost: " + costOfSchedule[j]);
                        System.out.print("Job Covering: ");
                        for(int a = 0; a < jobIsCovered.length; a++){
                            System.out.print("Job " + a + ": " + jobIsCovered[a][j] + " ");
                        }
                        System.out.println();
                    }
                }
                System.out.println();
            }
            cplex.end();
        }

        solution.solution = solutions;
        return solution;
    }
}
