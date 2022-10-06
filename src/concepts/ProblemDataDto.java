package concepts;

import java.util.List;

public class ProblemDataDto {
    public List<Task> taskList;
    public int firstCost;

    public ProblemDataDto(){}

    public ProblemDataDto(List<Task> taskList, int firstCost){
        this.taskList = taskList;
        this.firstCost = firstCost;
    }
}
