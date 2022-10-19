package concepts.schedulers;

import java.util.List;

public class SolutionDto {
    public List<Integer> solution;
    public double minCost;

    public SolutionDto(List<Integer> solution, double minCost){
        this.solution = solution;
        this.minCost = minCost;
    }

    public SolutionDto() {

    }
}
