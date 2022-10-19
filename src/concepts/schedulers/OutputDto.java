package concepts.schedulers;

import java.util.List;

public class OutputDto {

    public double cMax;
    public List<Integer> columnCounts;

    public List<Double> improvements;

    public OutputDto(double cMax, List<Integer> columnCounts, List<Double> improvements){
        this.cMax = cMax;
        this.columnCounts = columnCounts;
        this.improvements = improvements;
    }

    public OutputDto(OutputDto output, double firstCost){
        this.cMax = output.cMax + firstCost;
        this.columnCounts = output.columnCounts;
        this.improvements = output.improvements;
    }


}
