package CharlieNetwork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rosa.charles on 1/25/16.
 */
public class LoadLeg implements java.util.Comparator<LoadLeg>, java.lang.Comparable<LoadLeg> {

    private String originLocation = null;
    private String originShift = null;
    private String destLocation = null;
    private String destShift = null;
    private String mode = null;
    private Integer myLoadLegID = null;

    public static int loadLegCounter = 0;

    private Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight> myODs = null;

    public LoadLeg(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            String mode_input
    ) {
        this.originLocation = originLocation_input;
        this.originShift = originShift_input;
        this.destLocation = destLocation_input;
        this.destShift = destShift_input;
        this.mode = mode_input;

        this.myODs = new HashMap<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>();

        loadLegCounter++;

        this.myLoadLegID = loadLegCounter;
    }

    public void emptyMapOfODs() {
        this.myODs.clear();
    }

    public Integer getMyLoadLegID() {
        return myLoadLegID;
    }

    public void addOD(O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight myCube_Weight) {
        this.myODs.put(myO_OShift_Dest_Service__isEarly_cube_weight, myCube_Weight);
    }

    public int getUniqueODCount() {
        return this.myODs.size();
    }

    public int getShipmentCount() {
        int totalShipmentCount = 0;
        for(O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight : this.myODs.keySet()) {
            totalShipmentCount += myO_OShift_Dest_Service__isEarly_cube_weight.getShipmentCount();
        }

        return totalShipmentCount;
    }

    public double getTotalCube() {
        double totalCube = 0;
        for(O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight : this.myODs.keySet()) {
            Cube_Weight myCube_Weight = this.myODs.get(myO_OShift_Dest_Service__isEarly_cube_weight);
            totalCube += myCube_Weight.getMyCube();
        }

        return totalCube;
    }

    public double getTotalWeight() {
        double totalWeight = 0;
        for(O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight : this.myODs.keySet()) {
            Cube_Weight myCube_Weight = this.myODs.get(myO_OShift_Dest_Service__isEarly_cube_weight);
            totalWeight += myCube_Weight.getMyWeight();
        }

        return totalWeight;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public String getOriginShift() {
        return originShift;
    }

    public String getDestLocation() {
        return destLocation;
    }

    public String getDestShift() {
        return destShift;
    }

    public String getMode() {
        return mode;
    }

    public static String getUniqueIdentifier(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            String mode_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(originLocation_input+",");
        buffer.append(originShift_input+",");
        buffer.append(destLocation_input+",");
        buffer.append(destShift_input+",");
        buffer.append(mode_input+",");
        return buffer.toString();
    }

    public int compare(LoadLeg o1, LoadLeg o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(LoadLeg obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        return LoadLeg.getUniqueIdentifier(
                this.getOriginLocation(),
                this.getOriginShift(),
                this.getDestLocation(),
                this.getDestShift(),
                this.getMode()
        );
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(LoadLeg obj) {

        return this.toString().compareTo(obj.toString());

    }



}
