package concepts.setup;

import concepts.data.Task;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProblemDataCreator {

    public ProblemDataDto createDataSetFromName(String dataSetName) throws FileNotFoundException {

        ProblemInputHandler inputHandler = new ProblemInputHandler();
        List<List<String>> taskData = inputHandler.getData(dataSetName, true);
        List<List<String>> comData = inputHandler.getData(dataSetName, false);

        List<Integer> taskCosts = new ArrayList<>();
        for(List<String> record: taskData){
            for(String data: record){
                int cost = Integer.parseInt(data.trim().replaceAll("\\s+", " ").split(" ")[1]);
                taskCosts.add(cost);
            }
        }
        int firstTaskCost = taskCosts.get(0);
        taskCosts.remove(0);
        List<Integer> comCosts = new ArrayList<>();
        for(List<String> record: comData){
            for(String recordData: record){
                String[] data = recordData.trim().replaceAll("\\s+", " ").split(" ");
                comCosts.add(Integer.parseInt(data[2]));
            }
        }

        List<Task> taskDataList = new ArrayList<>();
        for(int i = 0; i < taskCosts.size(); i++){
            Task task = new Task(i, taskCosts.get(i), comCosts.get(i));
            taskDataList.add(task);
        }

        return new ProblemDataDto(taskDataList, firstTaskCost);
    }
}
