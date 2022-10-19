package concepts.setup;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProblemInputHandler {

    public static final String PROBLEM_FILE_DIR = "problem-input";
    public static final String FILE_DIR_SEPARATOR = "/";
    public static final String FILE_NAME_SEPARATOR = "_";
    public static final String CSV_DELIMITER = ",";
    public static final String TASK_DATA = "TD";
    public static final String COM_DATA = "CDD";
    public static final String CSV_EXT = ".csv";

    public List<List<String>> getData(String fileName, boolean isTaskData) throws FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(
                createFilePath(fileName, isTaskData)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(CSV_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    public String createFilePath(String fileName, boolean isTaskData){
        String dataType = isTaskData ? TASK_DATA : COM_DATA;
        return "" + PROBLEM_FILE_DIR + FILE_DIR_SEPARATOR + dataType + FILE_NAME_SEPARATOR + fileName + CSV_EXT;
    }

    public String createFilePath(String fileName){
        return createFilePath(fileName, true);
    }

    public void getComData(){

    }

}
