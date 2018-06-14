import java.util.ArrayList;
import java.util.List;

public class ModelResults {

    private Integer numPredictiveVars = null;
    private List<Double> myListOfRMSError = null;

    public ModelResults(
            Integer numPredictiveVars_input
    ) {
        this.numPredictiveVars = numPredictiveVars_input;
        myListOfRMSError = new ArrayList<Double>();
    }

    public Integer getNumPredictiveVars() {
        return numPredictiveVars;
    }

    public void addRMSError(Double myRMSError) {
        if(myRMSError==null) {
            return;
        }

        myListOfRMSError.add(myRMSError);
    }

    public int getNumRMSError() {
        if(this.myListOfRMSError==null) {
            return 0;
        }
        return this.myListOfRMSError.size();
    }

    public double getAvgRMSError() {
        double myAvg = 0.0;
        for(Double myRMS : this.myListOfRMSError) {
            myAvg += myRMS;
        }

        myAvg = myAvg/(this.getNumRMSError()+0.0000001);

        return myAvg;
    }

    public double getVarRMSError() {
        double myAvg = this.getAvgRMSError();
        double myVar = 0.0;
        for(Double myRMS : this.myListOfRMSError) {
            double myDiff = myRMS-myAvg;
            myVar += myDiff*myDiff;
        }

        myVar = myVar/(this.getNumRMSError()+0.0000001);

        return myVar;
    }

    public double getStdDevRMSError() {
        double myVar = this.getVarRMSError();

        myVar = Math.sqrt(myVar);

        return myVar;
    }



}
