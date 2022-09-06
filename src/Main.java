import concepts.LinearProgramSolution;
import concepts.MixedIntegerProgram;
import concepts.SimpleLinearProgram;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Main {

    public static void main(String[] args) throws IloException {
        LinearProgramSolution simple = new MixedIntegerProgram();
        simple.solve();
    }
}

