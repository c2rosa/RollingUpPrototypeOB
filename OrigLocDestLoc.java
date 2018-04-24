import com.con_way.network.model.Facility;
import com.con_way.network.model.LoadPath;
import com.con_way.network.model.Operation;

import java.util.*;

/**
 * Created by rosa.charles on 1/17/2017.
 */
public class OrigLocDestLoc implements java.util.Comparator<OrigLocDestLoc>, java.lang.Comparable<OrigLocDestLoc> {

    public static double epsilon = 0.0001;

    private com.con_way.network.model.LoadPath loadPath_default = null;

    private Set<com.con_way.network.model.LoadPath> loadPath_All = null;

    private Data myData = null;

    private com.con_way.network.model.Facility orig = null;
    private com.con_way.network.model.Facility dest = null;

    private String originLocation = null;
    private String destinationLocation = null;

    private Operation myOp_origin = null;
    private Operation myOp_destination = null;

    private boolean isHSS = false;
    private Set<Double> mySetOfMiles = null;

    private Set<Double> mySetOfBackupMiles = null;

    private String stringRepresentationOfOrigLocDestLoc = null;

    private Integer numPup_model = 0;
    private Integer numPup_actual = 0;

    private Integer numHSS_model = 0;
    private Integer numHSS_actual = 0;

    private Map<OrigLocOrigShiftDestLocDestShift_mode, Double> myNumPupByOrigLocOrigShiftDestLocDestShift_mode = null;
    private Map<OrigLocOrigShiftDestLocDestShift_mode, Double> myNumHSSVanByOrigLocOrigShiftDestLocDestShift_mode = null;
    private Map<OrigLocOrigShiftDestLocDestShift_mode, Double> myCostByOrigLocOrigShiftDestLocDestShift_mode = null;

    public OrigLocDestLoc(
            Data myData_input,
            String originLocation_input,
            String destinationLocation_input
    ) throws Exception {
        this.myData = myData_input;
        this.originLocation = originLocation_input;
        this.destinationLocation = destinationLocation_input;

        this.myNumPupByOrigLocOrigShiftDestLocDestShift_mode = new HashMap<OrigLocOrigShiftDestLocDestShift_mode, Double>();
        this.myNumHSSVanByOrigLocOrigShiftDestLocDestShift_mode = new HashMap<OrigLocOrigShiftDestLocDestShift_mode, Double>();
        this.myCostByOrigLocOrigShiftDestLocDestShift_mode = new HashMap<OrigLocOrigShiftDestLocDestShift_mode, Double>();

        this.myOp_origin = Operation.O;
        this.myOp_destination = Operation.I;

        this.loadPath_All = new HashSet<com.con_way.network.model.LoadPath>();

    }

    public int getDefaultServiceLevel() {
        int defaultServiceLevel = this.getLoadPath_default().getCycle();
        return defaultServiceLevel;
    }

    public LoadPath getLoadPath_default() {
        return loadPath_default;
    }

    public void setLoadPath_default(LoadPath loadPath_default) {
        this.loadPath_default = loadPath_default;

        this.addLoadPath(loadPath_default);
    }

    public Facility getOrig() {
        return orig;
    }

    public void setOrig(Facility orig) throws Exception {
        this.orig = orig;

        Operation myOrigOperation = this.orig.getOperation();
        if(!myOrigOperation.equals(this.myOp_origin)) {
            throw new Exception("this.orig="+this.orig+" but this.myOp_origin="+this.myOp_origin);
        }

        String myOrigSic = this.orig.getSic();
        if(!myOrigSic.equals(this.originLocation)) {
            throw new Exception("myOrigSic="+myOrigSic+" but this.originLocation="+this.originLocation);
        }

    }

    public Facility getDest() {
        return dest;
    }

    public void setDest(Facility dest) throws Exception {
        this.dest = dest;

        Operation myDestOperation = this.dest.getOperation();
        if(!myDestOperation.equals(this.myOp_destination)) {
            throw new Exception("this.dest="+this.dest+" but this.myOp_destination="+this.myOp_destination);
        }

        String myDestSic = this.dest.getSic();
        if(!myDestSic.equals(this.destinationLocation)) {
            throw new Exception("myDestSic="+myDestSic+" but this.destinationLocation="+this.destinationLocation);
        }

    }


    public void addLoadPath(com.con_way.network.model.LoadPath myLoadPath) {
        if(myLoadPath==null) {
            return;
        }

        this.loadPath_All.add(myLoadPath);
    }

    public Set<LoadPath> getLoadPath_All() {
        return loadPath_All;
    }

    public LoadPath getLoadPath() throws Exception {
        if(this.loadPath_All == null) {
            return null;
        }

        if(this.loadPath_All.size()==1) {
            for(LoadPath myLoadPath : this.loadPath_All) {
                return myLoadPath;
            }
        }

        if(1==1) {
            throw new Exception("OD="+this+" has more than one loadpath and the size is "+this.loadPath_All.size());
        }

        return null;
    }

    public Operation getMyOp_origin() {
        return myOp_origin;
    }

    public void setMyOp_origin(Operation myOp_origin) {
        this.myOp_origin = myOp_origin;
    }

    public Operation getMyOp_destination() {
        return myOp_destination;
    }

    public void setMyOp_destination(Operation myOp_destination) {
        this.myOp_destination = myOp_destination;
    }

    public boolean isHSS() {
        return isHSS;
    }

    public void setHSS(boolean HSS) {
        isHSS = HSS;
    }

    public Double getMyMiles(boolean ignoreZeros) {
        if(this.mySetOfMiles==null || this.mySetOfMiles.size()==0) {
            return this.getMyBackupMiles(ignoreZeros);
        }

        double myAvg = LHHssReportBuilder.getAverage(new ArrayList(this.mySetOfMiles),ignoreZeros);

        if(myAvg<LHHssReportBuilder.epsilon) {
            return this.getMyBackupMiles(ignoreZeros);
        }

        return myAvg;
    }

    public void addMiles(Double myMiles) {
        if(this.mySetOfMiles == null) {
            this.mySetOfMiles = new HashSet<Double>();
        }
        this.mySetOfMiles.add(myMiles);
    }

    public void addBackupMiles(Double myMiles) {
        if(this.mySetOfBackupMiles == null) {
            this.mySetOfBackupMiles = new HashSet<Double>();
        }
        this.mySetOfBackupMiles.add(myMiles);

    }

    public Double getMyBackupMiles(boolean ignoreZeros) {
        if(this.mySetOfBackupMiles==null || this.mySetOfBackupMiles.size()==0) {
            return 0.0;
        }

        double myAvg = LHHssReportBuilder.getAverage(new ArrayList(this.mySetOfBackupMiles),ignoreZeros);

        return myAvg;
    }

    public Integer getNumPup_model() {
        return numPup_model;
    }

    public void setNumPup_model(Integer numPup_model) {
        this.numPup_model = numPup_model;
    }

    public Integer getNumPup_actual() {
        return numPup_actual;
    }

    public void setNumPup_actual(Integer numPup_actual) {
        this.numPup_actual = numPup_actual;
    }

    public Integer getNumPup_excess() {
        int numPup_excess = this.getNumPup_actual()-this.getNumPup_model();
        return numPup_excess;
    }

    public Integer getNumHSSVans_excess() {
        int numNumHSSVans_excess = this.getNumHSS_actual()-this.getNumHSS_model();
        return numNumHSSVans_excess;
    }

    public Integer getNumHSS_model() {
        return numHSS_model;
    }

    public void setNumHSS_model(Integer numHSS_model) {
        this.numHSS_model = numHSS_model;
    }

    public Integer getNumHSS_actual() {
        return numHSS_actual;
    }

    public void setNumHSS_actual(Integer numHSS_actual) {
        this.numHSS_actual = numHSS_actual;
    }

    private boolean doesWorkForThisObject(OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode) {
        if(myOrigLocOrigShiftDestLocDestShift_mode == null) {
            return false;
        }

        if(!this.originLocation.equals(myOrigLocOrigShiftDestLocDestShift_mode.getOriginLocation())) {
            return false;
        }

        if(!this.destinationLocation.equals(myOrigLocOrigShiftDestLocDestShift_mode.getDestLocation())) {
            return false;
        }

        return true;

    }

    public Double getAverageCostPerPup() {
        double totalCost = 0.0;
        double totalPupCount = 0.0;

        boolean anyLH = false;

        for(OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode : this.myNumPupByOrigLocOrigShiftDestLocDestShift_mode.keySet()) {
            Double myPupNum = this.myNumPupByOrigLocOrigShiftDestLocDestShift_mode.get(myOrigLocOrigShiftDestLocDestShift_mode);
            if(myPupNum == null) {
                continue;
            }

            if(!myOrigLocOrigShiftDestLocDestShift_mode.isLineHaul()) {
                continue;
            }

            anyLH = true;

            totalPupCount += myPupNum;

            Double myCost = this.myCostByOrigLocOrigShiftDestLocDestShift_mode.get(myOrigLocOrigShiftDestLocDestShift_mode);
            if(myCost == null) {
                continue;
            }

            totalCost += myCost;
        }

        if(!anyLH) {
            return 0.0;
        }

        if(totalPupCount<OrigLocDestLoc.epsilon) {
            return 0.0;
        }

        double costPerPup = totalCost/(totalPupCount+OrigLocDestLoc.epsilon);
        return costPerPup;
    }

    public Double getAverageCostPerHSSVan() {
        double totalCost = 0.0;
        double totalHSSVanCount = 0.0;

        boolean anyHSS = false;

        for(OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode : this.myNumHSSVanByOrigLocOrigShiftDestLocDestShift_mode.keySet()) {
            Double myHSSVanNum = this.myNumHSSVanByOrigLocOrigShiftDestLocDestShift_mode.get(myOrigLocOrigShiftDestLocDestShift_mode);
            if(myHSSVanNum == null) {
                continue;
            }

            if(myOrigLocOrigShiftDestLocDestShift_mode.isLineHaul()) {
                continue;
            }

            anyHSS = true;

            totalHSSVanCount += myHSSVanNum;

            Double myCost = this.myCostByOrigLocOrigShiftDestLocDestShift_mode.get(myOrigLocOrigShiftDestLocDestShift_mode);
            if(myCost == null) {
                continue;
            }

            totalCost += myCost;
        }

        if(!anyHSS) {
            return 0.0;
        }

        if(totalHSSVanCount<OrigLocDestLoc.epsilon) {
            return 0.0;
        }

        double costPerHSSVan = totalCost/(totalHSSVanCount+OrigLocDestLoc.epsilon);
        return costPerHSSVan;
    }

    public void addNumPup(OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode, Double numPup) {
        if(!this.doesWorkForThisObject(myOrigLocOrigShiftDestLocDestShift_mode)) {
            return;
        }

        Double currentNumPup = this.myNumPupByOrigLocOrigShiftDestLocDestShift_mode.get(myOrigLocOrigShiftDestLocDestShift_mode);
        if(currentNumPup == null) {
            this.myNumPupByOrigLocOrigShiftDestLocDestShift_mode.put(myOrigLocOrigShiftDestLocDestShift_mode, numPup);
        } else {
            this.myNumPupByOrigLocOrigShiftDestLocDestShift_mode.put(myOrigLocOrigShiftDestLocDestShift_mode, currentNumPup+numPup);
        }
    }

    public void addNumHSSVan(OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode, Double numHSSVan) {
        if(!this.doesWorkForThisObject(myOrigLocOrigShiftDestLocDestShift_mode)) {
            return;
        }

        Double currentNumHSSVan = this.myNumHSSVanByOrigLocOrigShiftDestLocDestShift_mode.get(myOrigLocOrigShiftDestLocDestShift_mode);
        if(currentNumHSSVan == null) {
            this.myNumHSSVanByOrigLocOrigShiftDestLocDestShift_mode.put(myOrigLocOrigShiftDestLocDestShift_mode, numHSSVan);
        } else {
            this.myNumHSSVanByOrigLocOrigShiftDestLocDestShift_mode.put(myOrigLocOrigShiftDestLocDestShift_mode, currentNumHSSVan+numHSSVan);
        }
    }

    public void addCost(OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode, Double myCost) {
        if(!this.doesWorkForThisObject(myOrigLocOrigShiftDestLocDestShift_mode)) {
            return;
        }

        Double currentCost = this.myCostByOrigLocOrigShiftDestLocDestShift_mode.get(myOrigLocOrigShiftDestLocDestShift_mode);
        if(currentCost == null) {
            this.myCostByOrigLocOrigShiftDestLocDestShift_mode.put(myOrigLocOrigShiftDestLocDestShift_mode, myCost);
        } else {
            this.myCostByOrigLocOrigShiftDestLocDestShift_mode.put(myOrigLocOrigShiftDestLocDestShift_mode, currentCost+myCost);
        }
    }

    public String getLocation_origin() {
        return this.originLocation;
    }

    public String getLocation_destination() {
        return this.destinationLocation;
    }

    public static String getUniqueIdentifier(
            String myOriginLocation_input,
            String myDestinationLocation_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(myOriginLocation_input+"_");
        buffer.append(myDestinationLocation_input);
        return buffer.toString();
    }


    public int compare(OrigLocDestLoc o1, OrigLocDestLoc o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(OrigLocDestLoc obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        if(this.stringRepresentationOfOrigLocDestLoc != null) {
            return this.stringRepresentationOfOrigLocDestLoc;
        }

        this.stringRepresentationOfOrigLocDestLoc = OrigLocDestLoc.getUniqueIdentifier(this.getLocation_origin(), this.getLocation_destination());

        return this.stringRepresentationOfOrigLocDestLoc;
    }


    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(OrigLocDestLoc obj) {

        if(this.equals(obj)) {
            return 0;
        }

        return this.toString().compareTo(obj.toString());

    }



}
