package concepts;

public class Job {
    public int weight = 1;
    public int processingTime;

    public Job(int processingTime){
        this.processingTime = processingTime;
    }

    public Job(int processingTime, int weight){
        this.processingTime = processingTime;
        this.weight = weight;
    }
}
