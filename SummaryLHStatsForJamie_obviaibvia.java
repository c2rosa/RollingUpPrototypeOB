import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rosa.charles on 7/22/15.
 */
public class SummaryLHStatsForJamie_obviaibvia implements java.util.Comparator<SummaryLHStatsForJamie_obviaibvia>, java.lang.Comparable<SummaryLHStatsForJamie_obviaibvia>  {

    public static Double hss_multiplier = 205.0/100.0;

    // If this is left at null, this means the aggregation of ALL Shifts
    private String myOutgoingShift = null;
    private List<String> myListOfMode = null;
    private List<Double> myListOfPricePerMile = null;
    private List<Double> myListOfTractorTotals = null;
    private List<Double> myListOfNumTrailersPerTractor = null;
    private List<Double> myListOfMiles_OD = null;
    private List<Double> myListOfMiles_OV = null;
    private List<Double> myListOfMiles_VD = null;
    private List<Double> myListOfNumLadenPups_OD = null;
    private List<Double> myListOfNumLadenPups_OV = null;
    private List<Double> myListOfNumLadenPups_VD = null;
    private List<Double> myListOfNumEmptyPups_OD = null;
    private List<Double> myListOfNumEmptyPups_OV = null;
    private List<Double> myListOfNumEmptyPups_VD = null;
    private List<Double> myListOfCubes_OD = null;
    private List<Double> myListOfCubes_OV = null;
    private List<Double> myListOfCubes_VD = null;
    private List<Double> myListOfWeights_OD = null;
    private List<Double> myListOfWeights_OV = null;
    private List<Double> myListOfWeights_VD = null;

    private List<Integer> myNumTrailerPerTractor = null;

    private Map<String, Integer> mapOfPupCount = null;
    private Map<String, Integer> mapOfVanCount = null;
    private Map<String, Double> mapOfCost = null;

    public SummaryLHStatsForJamie_obviaibvia(String myOutgoingShift_input) {
        this.myOutgoingShift = myOutgoingShift_input;

        this.myListOfMode = new ArrayList<String>();
        this.myListOfPricePerMile = new ArrayList<Double>();
        this.myListOfTractorTotals = new ArrayList<Double>();
        this.myListOfNumTrailersPerTractor = new ArrayList<Double>();
        this.myListOfMiles_OV = new ArrayList<Double>();
        this.myListOfMiles_VD = new ArrayList<Double>();
        this.myListOfMiles_OD = new ArrayList<Double>();
        this.myListOfNumLadenPups_OV = new ArrayList<Double>();
        this.myListOfNumLadenPups_VD = new ArrayList<Double>();
        this.myListOfNumLadenPups_OD = new ArrayList<Double>();
        this.myListOfNumEmptyPups_OV = new ArrayList<Double>();
        this.myListOfNumEmptyPups_VD = new ArrayList<Double>();
        this.myListOfNumEmptyPups_OD = new ArrayList<Double>();
        this.myListOfCubes_OV = new ArrayList<Double>();
        this.myListOfCubes_VD = new ArrayList<Double>();
        this.myListOfCubes_OD = new ArrayList<Double>();
        this.myListOfWeights_OV = new ArrayList<Double>();
        this.myListOfWeights_VD = new ArrayList<Double>();
        this.myListOfWeights_OD = new ArrayList<Double>();

    }

    //public static String myHeader = "Shift, DollarsPerMile, Total Tractors, TU, SU, CF, LF, Empty %, Total Miles, Total Cost";
    public static String myHeader = "Shift, Total Num Shipment Diversions,Total Num Trailer Diversions, Total Tractors, Phantom Tractors, Total Tractor Vias, TU, LoadedPupCount, EmptyPupCount, SU, LF, Empty %, Total Miles, Total Cost";

    public Integer getTotalNumTractors() {
        int totalNumTractors = 0;
        for(Double myNumTractors : this.myListOfTractorTotals) {
            totalNumTractors += myNumTractors;
        }
        return totalNumTractors;
    }

    public Integer getTotalWeightedLadenPups() {
        int totalWeightedLadenPups = 0;
        //double hss_multiplier = 2544.0/1156.0;
        for(int myIndex = 0; myIndex < this.myListOfNumLadenPups_OD.size() ; myIndex++) {
            double myNumLadenPups_OD = this.myListOfNumLadenPups_OD.get(myIndex);
            double myNumLadenPups_OV = this.myListOfNumLadenPups_OV.get(myIndex);
            double myNumLadenPups_VD = this.myListOfNumLadenPups_VD.get(myIndex);
            double myMult = 1.0;
            if(!this.myListOfMode.get(myIndex).equals(LHHssReportBuilder.L)) {
                myMult = SummaryLHStatsForJamie_obviaibvia.hss_multiplier;
            }
            totalWeightedLadenPups += myNumLadenPups_OD*myMult+myNumLadenPups_OV*myMult+myNumLadenPups_VD*myMult;
        }
        return totalWeightedLadenPups;
    }

    public Integer getTotalWeightedEmptyPups() {
        int totalWeightedEmptyPups = 0;
        //double hss_multiplier = 2544.0/1156.0;
        for(int myIndex = 0; myIndex < this.myListOfNumEmptyPups_OD.size() ; myIndex++) {
            double myNumEmptyPups_OD = this.myListOfNumEmptyPups_OD.get(myIndex);
            double myNumEmptyPups_OV = this.myListOfNumEmptyPups_OV.get(myIndex);
            double myNumEmptyPups_VD = this.myListOfNumEmptyPups_VD.get(myIndex);
            double myMult = 1.0;
            if(!this.myListOfMode.get(myIndex).equals(LHHssReportBuilder.L)) {
                myMult = SummaryLHStatsForJamie_obviaibvia.hss_multiplier;
            }
            totalWeightedEmptyPups += myNumEmptyPups_OD*myMult+myNumEmptyPups_OV*myMult+myNumEmptyPups_VD*myMult;
        }
        return totalWeightedEmptyPups;
    }

    public double getTotalCube() {
        double totalCube = 0.0;
        for(int myIndex = 0; myIndex < this.myListOfCubes_OD.size() ; myIndex++) {
            double myCube_OD = this.myListOfCubes_OD.get(myIndex);
            double myCube_OV = this.myListOfCubes_OV.get(myIndex);
            double myCube_VD = this.myListOfCubes_VD.get(myIndex);
            totalCube += myCube_OD+myCube_OV+myCube_VD;
        }
        return totalCube;
    }

    public double getTotalWeight() {
        double totalWeight = 0.0;
        for(int myIndex = 0; myIndex < this.myListOfWeights_OD.size() ; myIndex++) {
            double myWeight_OD = this.myListOfWeights_OD.get(myIndex);
            double myWeight_OV = this.myListOfWeights_OV.get(myIndex);
            double myWeight_VD = this.myListOfWeights_VD.get(myIndex);
            totalWeight += myWeight_OD+myWeight_OV+myWeight_VD;
        }
        return totalWeight;
    }

    public double getTU() {
        double myTU = (this.getTotalCube()+0.000001)/(this.getTotalWeightedLadenPups()+0.0000001);
        return myTU;
    }

    public double getSU() {
        double mySU = (this.getTotalCube()+0.000001)/(this.getTotalWeightedLadenPups()+this.getTotalWeightedEmptyPups()+0.0000001);
        return mySU;
    }

    public String getMyOutgoingShift() {
        return myOutgoingShift;
    }

    public Double getTotalTractorMiles() {
        double totalTractorMiles = 0.0;
        for(int myIndex = 0; myIndex < this.myListOfMiles_OD.size() ; myIndex++) {
            double numTractors = this.myListOfTractorTotals.get(myIndex);
            double myMiles_OD = this.myListOfMiles_OD.get(myIndex);
            double myMiles_OV = this.myListOfMiles_OV.get(myIndex);
            double myMiles_VD = this.myListOfMiles_VD.get(myIndex);
            totalTractorMiles += numTractors*myMiles_OD+numTractors*myMiles_OV+numTractors*myMiles_VD;
        }
        return totalTractorMiles;
    }

    public void addRecord(
            OriginLocationDestinationLocation myOriginLocationDestinationLocation,
            Double PricePerMile_input,
            Double tractorTotal_input,
            Double numTrailersPerTractor_input,
            Double miles_OD_input,
            Double miles_OV_input,
            Double miles_VD_input,
            Double numLadenPups_OD_input,
            Double numLadenPups_OV_input,
            Double numLadenPups_VD_input,
            Double numEmptyPups_OD_input,
            Double numEmptyPups_OV_input,
            Double numEmptyPups_VD_input,
            Double cube_OD_input,
            Double cube_OV_input,
            Double cube_VD_input,
            Double weight_OD_input,
            Double weight_OV_input,
            Double weight_VD_input
    ) throws Exception {

        if(this.getMyOutgoingShift() != null) {
            if(!this.getMyOutgoingShift().equals(myOriginLocationDestinationLocation.getShift_origin())) {
                return;
            }
        }

        if(myOriginLocationDestinationLocation.getMyMode().equals(LHHssReportBuilder.L)) {
            this.myListOfMode.add(LHHssReportBuilder.L);
        } else if(myOriginLocationDestinationLocation.getMyMode().equals(LHHssReportBuilder.M)) {
            this.myListOfMode.add(LHHssReportBuilder.M);
        } else if(myOriginLocationDestinationLocation.getMyMode().equals(LHHssReportBuilder.S)) {
            this.myListOfMode.add(LHHssReportBuilder.S);
        } else {
            throw new Exception("Bad Mode = "+myOriginLocationDestinationLocation.getMyMode());
        }

        this.myListOfPricePerMile.add(PricePerMile_input);

        this.myListOfTractorTotals.add(tractorTotal_input);

        this.myListOfNumTrailersPerTractor.add(numTrailersPerTractor_input);

        this.myListOfMiles_OD.add(miles_OD_input);
        this.myListOfMiles_OV.add(miles_OV_input);
        this.myListOfMiles_VD.add(miles_VD_input);

        this.myListOfNumLadenPups_OD.add(numLadenPups_OD_input);
        this.myListOfNumLadenPups_OV.add(numLadenPups_OV_input);
        this.myListOfNumLadenPups_VD.add(numLadenPups_VD_input);

        this.myListOfNumEmptyPups_OD.add(numEmptyPups_OD_input);
        this.myListOfNumEmptyPups_OV.add(numEmptyPups_OV_input);
        this.myListOfNumEmptyPups_VD.add(numEmptyPups_VD_input);

        this.myListOfCubes_OD.add(cube_OD_input);
        this.myListOfCubes_OV.add(cube_OV_input);
        this.myListOfCubes_VD.add(cube_VD_input);

        this.myListOfWeights_OD.add(weight_OD_input);
        this.myListOfWeights_OV.add(weight_OV_input);
        this.myListOfWeights_VD.add(weight_VD_input);


    }

    public int compare(SummaryLHStatsForJamie_obviaibvia o1, SummaryLHStatsForJamie_obviaibvia o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(SummaryLHStatsForJamie_obviaibvia obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {

        if(this.getMyOutgoingShift() != null) {
            return this.getMyOutgoingShift();
        } else {
            return "Total";
        }


    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(SummaryLHStatsForJamie_obviaibvia obj) {

        return this.toString().compareTo(obj.toString());

    }

}
