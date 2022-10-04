import concepts.*;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Main {

    public static void main(String[] args) throws IloException {
        TaskScheduler taskScheduler = new HeuristicSolution();
        taskScheduler.solve();
    }
}

