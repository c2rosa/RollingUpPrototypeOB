public class MeanVarStdDev_Metric {

    private Double mean = null;
    private Double variance = null;
    private Double StdDev = null;
    private String nameOfMetric = null;

    public MeanVarStdDev_Metric(
            Double mean_input,
            Double variance_input,
            Double StdDev_input,
            String nameOfMetric_input
    ) {
        this.mean = mean_input;
        this.variance = variance_input;
        this.StdDev = StdDev_input;
        this.nameOfMetric = nameOfMetric_input;
    }

    public Double getMean() {
        return mean;
    }

    public Double getVariance() {
        return variance;
    }

    public Double getStdDev() {
        return StdDev;
    }

    public String getNameOfMetric() {
        return nameOfMetric;
    }


}
