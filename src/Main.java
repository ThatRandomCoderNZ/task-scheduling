import concepts.*;
import ilog.concert.IloException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IloException, IOException {
        ProblemSolver solver = new ProblemSolver();
        String[] problemSet = {
                "LL1", "LL2", "LL3", "LL4", "LL5", "LM1", "LM2", "LM3", "LM4", "LM5", "LH1", "LH2", "LH3", "LH4", "LH5",
                "ML1", "ML2", "ML3", "ML4", "ML5", "MM1", "MM2", "MM3", "MM4", "MM5", "MH1", "MH2", "MH3", "MH4", "MH5",
                "HL1", "HL2", "HL3", "HL4", "HL5", "HM1", "HM2", "HM3", "HM4", "HM5", "HH1", "HH2", "HH3", "HH4", "HH5",
        };

        List<String[]> outputData = new ArrayList<>();
        outputData.add(new String[] {"Problem Set Name", "Time to Complete (ms)", "cMax"});
        for(String problem: problemSet) {
            long initialTimestamp = System.nanoTime();

            double best = solver.solveProblem(problem);

            long finalTimestamp = System.nanoTime();
            long executionTime = finalTimestamp - initialTimestamp;
            long executionTimeMilliseconds = executionTime / 1000000;

            outputData.add(new String[]{ problem, executionTimeMilliseconds + "", best + ""});
        }

        createCsvFile(outputData);


    }

    public static void createCsvFile(List<String[]> dataLines) throws IOException {
        File csvOutputFile = new File("output.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(Main::convertToCSV)
                    .forEach(pw::println);
        }
    }

    public static String convertToCSV(String[] data) {
        return String.join(",", data);
    }
}

