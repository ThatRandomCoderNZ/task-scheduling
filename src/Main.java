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
                //Initial Problem Set
                "LL1", "LL2", "LL3", "LL4", "LL5", "LM1", "LM2", "LM3", "LM4", "LM5", "LH1", "LH2", "LH3", "LH4","LH5",
                "ML1", "ML2", "ML3", "ML4", "ML5", "MM1", "MM2", "MM3", "MM4", "MM5", "MH1", "MH2", "MH3", "MH4", "MH5",
                "HL1", "HL2", "HL3", "HL4", "HL5", "HM1", "HM2", "HM3", "HM4", "HM5", "HH1", "HH2", "HH3", "HH4", "HH5",

                ////Extra Problem Set
                "E20_1", "E20_2", "E20_3", "E20_4", "E20_5", "E30_1", "E30_2", "E30_3","E30_4", "E30_5",
                "E40_1", "E40_2", "E40_3", "E40_4", "E40_5", "E50_1", "E50_2", "E50_3", "E50_4", "E50_5",
                "H20_1", "H20_2", "H20_3", "H20_4", "H20_5", "H30_1", "H30_2", "H30_3", "H30_4", "H30_5",
                "H40_1", "H40_2", "H40_3", "H40_4", "H40_5", "H50_1", "H50_2", "H50_3", "H50_4", "H50_5",

        };

        List<String[]> outputData = new ArrayList<>();
        outputData.add(new String[] {"Problem Set Name", "Time to Complete (ms)", "cMax", "Total Columns", "Total Iterations", "Columns Generated", "Improvements Made"});
        for(String problem: problemSet) {
            long initialTimestamp = System.nanoTime();

            OutputDto best = solver.solveProblem(problem);

            long finalTimestamp = System.nanoTime();
            long executionTime = finalTimestamp - initialTimestamp;
            long executionTimeMilliseconds = executionTime / 1000000;

            System.out.println("Solved Problem: " + problem + " in " + executionTimeMilliseconds + " ms and found the best solution: " + best.cMax );
            outputData.add(new String[]{
                    problem,
                    executionTimeMilliseconds + "",
                    best.cMax + "",
                    best.columnCounts.get(best.columnCounts.size() - 1) + "",
                    best.columnCounts.size() + "",
                    best.columnCounts.stream().map(String::valueOf).collect(Collectors.joining("=>")),
                    best.improvements.stream().map(String::valueOf).collect(Collectors.joining("=>")),
            });
        }

        createCsvFile(outputData);


    }

    public static void createCsvFile(List<String[]> dataLines) throws IOException {
        File csvOutputFile = new File("output-with-column-size-and-iterations.csv");
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

