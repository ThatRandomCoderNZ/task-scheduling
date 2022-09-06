package concepts;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;

public class MixedIntegerProgram implements LinearProgramSolution{


    public void solve() throws IloException {
        IloCplex cplex = new IloCplex();

        int numOfMachines = 3;
        int numOfJobs = 3;

        //Task 1 - cost 1, Task 2 - cost 2, Task 3 - cost 3
        int[][] allFeasibleSchedules = {
                {1},{1,2},{1,2,3},{2,3},{3},{3,2},{2},{3,1}
        };
        int[] costOfSchedule = {1,3,7,6,4,6,2,5};
        int[][] jobIsCovered = {
                {1, 1, 1, 0, 0, 0, 0, 1},
                {0, 1, 1, 1, 0, 1, 1, 0},
                {0, 0, 1, 1, 1, 1, 0, 1}
        };

        IloNumVar[][] scheduleIsUsed = new IloNumVar[numOfMachines][];
        for(int i = 0; i < numOfMachines; i++){
            scheduleIsUsed[i] = cplex.boolVarArray(costOfSchedule.length);
        }

        IloNumExpr[] objFunc = new IloNumExpr[numOfMachines];
        for(int index = 0; index < numOfMachines; index++){
            IloNumExpr expr = cplex.numExpr();
            objFunc[index] = cplex.sum(expr, cplex.scalProd(costOfSchedule, scheduleIsUsed[index]));
        }

        cplex.addMinimize(cplex.max(objFunc));

        for(int i = 0; i < numOfJobs; i++){
            IloNumExpr jobExecutedOnceConstraint = cplex.numExpr();
            for(int j = 0; j < numOfMachines; j++) {
                jobExecutedOnceConstraint = cplex.sum(jobExecutedOnceConstraint, cplex.scalProd(scheduleIsUsed[j], jobIsCovered[i]));
            }
            cplex.addEq(jobExecutedOnceConstraint, 1);
        }

        IloNumExpr totalMachines = cplex.numExpr();
        for(int i = 0; i < numOfMachines; i++) {
            totalMachines = cplex.sum(totalMachines, cplex.sum(scheduleIsUsed[i]));
        }
        cplex.addLe(totalMachines, numOfMachines);

        System.out.println(cplex.getModel());
        cplex.exportModel("test.lp");

        if(cplex.solve()){
            System.out.println("Solution Status: " + cplex.getStatus());
            System.out.println();
            System.out.println("Minimum Completion Time = " + cplex.getObjValue());
            System.out.println();
            System.out.println("Optimal Values = ");
            System.out.println("            s1  s2  s3  s4  s5  s6  s7  s8");
            for(int i = 0; i < numOfMachines; i++){
                double[] values = cplex.getValues(scheduleIsUsed[i]);
                System.out.print("Machine " + i + ": ");
                for (double value : values) {
                    System.out.print(Math.abs(value) + " ");
                }
                System.out.println();
            }
//

        }
    }
}
