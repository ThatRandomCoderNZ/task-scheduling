package concepts;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MixedIntegerProgram implements LinearProgramSolution{


    public void solve() throws IloException {
        IloCplex cplex = new IloCplex();

        int numOfMachines = 3;
        int numOfJobs = 3;

        //Task 1 - cost 1, Task 2 - cost 2, Task 3 - cost 3
        int[][] allFeasibleSchedules = {
                {1},{1,2},{1,2,3}//,{2,3},{3},{3,2},{2},{3,1}
        };                    //1 2 3 4 5 6 7 8
        int[] costOfSchedule = {1,3,7};//,6,4,6,2,5};
        int[][] jobIsCovered = {
                //  2  3  4  5  6  7  8
                {1, 1, 1, },//0, 0, 0, 0, 1},
                {0, 1, 1, },//1, 0, 1, 1, 0},
                {0, 0, 1, },//1, 1, 1, 0, 1}
        };



        IloNumVar[][] scheduleIsUsed = new IloNumVar[numOfMachines][];
        for(int i = 0; i < numOfMachines; i++){
            scheduleIsUsed[i] = cplex.boolVarArray(costOfSchedule.length);
        }

        IloNumVar[] z = new IloNumVar[1];
        z[0] = cplex.numVar(0, 20);


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

//        for(int i = 0; i < allFeasibleSchedules.length; i++){
//            IloNumExpr scheduleExecutedOnce = cplex.numExpr();
//            for(int j = 0; j < numOfMachines; j++) {
//                scheduleExecutedOnce = cplex.sum(scheduleExecutedOnce, cplex.min(cplex.prod(scheduleIsUsed[j][i], 100), 1));
//            }
//            cplex.addLe(scheduleExecutedOnce, 1);
//        }

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

            System.out.println("Dual Variables");
            //System.out.println(Arrays.toString(zConstraints));
            //double[] range = cplex.getDuals(zConstraints);
            //System.out.println(Arrays.toString(range));

            System.out.println("Reduced Costs");
            for(int i = 0; i < numOfMachines; i++) {
                System.out.println(Arrays.toString(cplex.getReducedCosts(scheduleIsUsed[i])));
            }

//

        }
    }
}
