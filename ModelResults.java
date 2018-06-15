import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class ModelResults {

    private Integer numPredictiveVars = null;
    private SortedMap<Double, Integer> myMapOfFreqByRMSError = null;

    public ModelResults(
            Integer numPredictiveVars_input
    ) {
        this.numPredictiveVars = numPredictiveVars_input;
        myMapOfFreqByRMSError = new TreeMap<>();
    }

    public Integer getNumPredictiveVars() {
        return numPredictiveVars;
    }

    public void addRMSError(Double myRMSError) {
        if(myRMSError==null) {
            return;
        }

        Integer myFreq = this.myMapOfFreqByRMSError.get(myRMSError);
        if(myFreq==null) {
            this.myMapOfFreqByRMSError.put(myRMSError, 1);
        } else {
            this.myMapOfFreqByRMSError.put(myRMSError, myFreq+1);
        }

    }

    public int getNumRMSError() {
        if(this.myMapOfFreqByRMSError ==null) {
            return 0;
        }

        int numRMSError = 0;
        for(Double myRMSError : this.myMapOfFreqByRMSError.keySet()) {
            Integer myFreq = this.myMapOfFreqByRMSError.get(myRMSError);
            numRMSError += myFreq;
        }

        return numRMSError;
    }

    public int getNumRMSError_excludingBest() {
        return this.getNumRMSError()-1;
    }

    public double getAvgRMSError() {
        double myAvg = 0.0;
        for(Double myRMSError : this.myMapOfFreqByRMSError.keySet()) {
            Integer myFreq = this.myMapOfFreqByRMSError.get(myRMSError);
            myAvg += myFreq*myRMSError;
        }

        myAvg = myAvg/(this.getNumRMSError()+0.0000001);

        return myAvg;
    }

    public double getAvgRMSError_excludingBest() {
        double myAvg = 0.0;
        int count=0;
        for(Double myRMSError : this.myMapOfFreqByRMSError.keySet()) {
            count++;
            if(count==1) {
                // skip the first one...which is the smallest RMS error
                continue;
            }
            Integer myFreq = this.myMapOfFreqByRMSError.get(myRMSError);
            myAvg += myFreq*myRMSError;
        }

        myAvg = myAvg/(this.getNumRMSError_excludingBest()+0.0000001);

        return myAvg;
    }

    public double getVarRMSError() {
        double myAvg = this.getAvgRMSError();
        double myVar = 0.0;
        for(Double myRMSError : this.myMapOfFreqByRMSError.keySet()) {
            Integer myFreq = this.myMapOfFreqByRMSError.get(myRMSError);
            double myDiff = myFreq*(myRMSError-myAvg);
            myVar += myDiff*myDiff;
        }

        myVar = myVar/(this.getNumRMSError()+0.0000001);

        return myVar;
    }

    public double getVarRMSError_excludingBest() {
        double myAvg = this.getAvgRMSError_excludingBest();
        double myVar = 0.0;
        int count=0;
        for(Double myRMSError : this.myMapOfFreqByRMSError.keySet()) {
            count++;
            if(count==1) {
                // skip the first one...which is the smallest RMS error
                continue;
            }
            Integer myFreq = this.myMapOfFreqByRMSError.get(myRMSError);
            double myDiff = myFreq*(myRMSError-myAvg);
            myVar += myDiff*myDiff;
        }

        myVar = myVar/(this.getNumRMSError_excludingBest()+0.0000001);

        return myVar;
    }

    public double getStdDevRMSError() {
        double myVar = this.getVarRMSError();

        myVar = Math.sqrt(myVar);

        return myVar;
    }

    public double getStdDevRMSError_excludingBest() {
        double myVar = this.getVarRMSError_excludingBest();

        myVar = Math.sqrt(myVar);

        return myVar;
    }


}
