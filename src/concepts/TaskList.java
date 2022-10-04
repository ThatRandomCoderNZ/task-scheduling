package concepts;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList(){
        tasks = new ArrayList<>();
    }

    public TaskList(int listSize){
        tasks = new ArrayList<>();
        for(int i = 0; i < listSize; i++){
            int taskProcCost = (int)(Math.random() * 20);
            int taskComCost = (int)(Math.random() * 20);
            Task task = new Task(i, taskProcCost, taskComCost);
            this.tasks.add(task);
        }
    }

    public int getTaskCost(int index){
        return tasks.get(index).procCost;
    }

    public int getNumTasks(){
        return this.tasks.size();
    }

    public TaskList(List<Task> tasks){
        this.tasks = tasks;
    }
}
