import concepts.*;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Main {

    public static void main(String[] args) throws IloException {
        TaskScheduler taskScheduler = new HeuristicSolution();

        ProblemInputHandler inputHandler = new ProblemInputHandler();


//        TaskList taskList = new TaskList(10);
//        int[][] initialSchedules = {
//                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
//                {0, 1, 2, 3},
//                {8, 9},
//                {4, 5, 6, 7}
//        };
//
//        taskScheduler.solve(taskList, initialSchedules);
    }
}

