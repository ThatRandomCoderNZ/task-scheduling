package concepts;

import java.util.Comparator;

public class JobSorter implements Comparator<Job> {
    @Override
    public int compare(Job o1, Job o2) {
        float relativeWeightFirstJob = (float)o1.processingTime / o1.weight;
        float relativeWeightSecondJob = (float)o2.processingTime / o2.weight;

        int comparisonValue = 0;
        if(relativeWeightFirstJob < relativeWeightSecondJob){
            comparisonValue = -1;
        }else if(relativeWeightFirstJob > relativeWeightSecondJob){
            comparisonValue = 1;
        }
        return comparisonValue;
    }
}
