/**
 * Created by rosa.charles on 1/19/2017.
 */
public class TractorCount_TotalCube_TotalWeight {

    private Double totalTractorCount = null;
    private Double totalCube = null;
    private Double totalWeight = null;
    private Integer runID = null;
    private Double totalLadenTrailers = null;
    private Double totalEmptyTrailers = null;

    public TractorCount_TotalCube_TotalWeight(
            Double totalTractorCount_input,
            Double totalCube_input,
            Double totalWeight_input,
            Integer runID_input,
            Double totalLadenTrailers_input,
            Double totalEmptyTrailers_input
    ) {
        this.totalTractorCount = totalTractorCount_input;
        this.totalCube = totalCube_input;
        this.totalWeight = totalWeight_input;
        this.runID = runID_input;
        this.totalLadenTrailers = totalLadenTrailers_input;
        this.totalEmptyTrailers = totalEmptyTrailers_input;
    }

    public Double getTotalLadenTrailers() {
        return totalLadenTrailers;
    }

    public Double getTotalEmptyTrailers() {
        return totalEmptyTrailers;
    }

    public Integer getRunID() {
        return runID;
    }

    public Double getTotalTractorCount() {
        return totalTractorCount;
    }

    public Double getTotalCube() {
        return totalCube;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }


}
