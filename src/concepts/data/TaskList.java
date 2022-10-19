package concepts.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {
    private List<Task> tasks;

    public TaskList(){
        tasks = new ArrayList<>();
    }

    public TaskList(int listSize){
        tasks = new ArrayList<>();
        for(int i = 0; i < listSize; i++){
            int taskProcCost = (int)(Math.random() * 1000);
            int taskComCost = (int)(Math.random() * 20);
            Task task = new Task(i, taskProcCost, taskComCost);
            this.tasks.add(task);
        }
    }

    public int[] getExcludedList(List<Integer> selected){
        return tasks.stream()
                .map(task -> task.id)
                .filter(id -> !selected.contains(id))
                .mapToInt((Integer val) -> val)
                .toArray();
    }

    private boolean contains(int id){
        return tasks.stream().map(task -> task.id).collect(Collectors.toList()).contains(id);
    }

    public int getTaskCost(int index){
        return tasks.get(index).procCost;
    }

    public int getComDelay(int index){
        return tasks.get(index).comCost;
    }

    public int getNumTasks(){
        return this.tasks.size();
    }

    public TaskList(List<Task> tasks){
        this.tasks = tasks;
    }
}
