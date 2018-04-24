import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rosa.charles on 1/21/15.
 */
public class OBLocMoveToLocFAC implements java.util.Comparator<OBLocMoveToLocFAC>, java.lang.Comparable<OBLocMoveToLocFAC> {

    String myOBLocation = null;
    String myOBShift = null;
    String myMoveToLocation = null;
    String myMoveToShift = null;
    String myFACLocation = null;

    Set<String> mySetOfOMT = null;

    public OBLocMoveToLocFAC(
            String myOBLocation_input,
            String myOBShift_input,
            String myMoveToLocation_input,
            String myMoveToShift_input,
            String myFACLocation_input
    ) {
        this.myOBLocation = myOBLocation_input;
        this.myOBShift = myOBShift_input;
        this.myMoveToLocation = myMoveToLocation_input;
        this.myMoveToShift = myMoveToShift_input;
        this.myFACLocation = myFACLocation_input;

        this.mySetOfOMT = new HashSet<String>();
    }

    public String printOutString_NetezzaCompatible() {
        StringBuffer myBuffer = new StringBuffer();
        myBuffer.append(this.getMyOBLocation()+",");
        myBuffer.append(this.getMyOBShift()+",");
        myBuffer.append(this.getMyMoveToLocation()+",");
        myBuffer.append(this.getMyMoveToShift()+",");
        myBuffer.append(this.getMyFACLocation());

        return myBuffer.toString();
    }

    public Set<String> getMySetOfOMT() {
        return mySetOfOMT;
    }

    public void addOMT(String myOMTLocation) {
        this.mySetOfOMT.add(myOMTLocation);
    }

    public String getMyOBLocation() {
        return myOBLocation;
    }

    public String getMyOBShift() {
        return myOBShift;
    }

    public String getMyMoveToLocation() {
        return myMoveToLocation;
    }

    public String getMyMoveToShift() {
        return myMoveToShift;
    }

    public String getMyFACLocation() {
        return myFACLocation;
    }

    public static String getUniqueIdentifier(
            String myOBLocation_input,
            String myOBShift_input,
            String myMoveToLocation_input,
            String myMoveToShift_input,
            String myFACLocation_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(myOBLocation_input+",");
        buffer.append(myOBShift_input+",");
        buffer.append(myMoveToLocation_input+",");
        buffer.append(myMoveToShift_input+",");
        buffer.append(myFACLocation_input);
        return buffer.toString();
    }

    public int compare(OBLocMoveToLocFAC o1, OBLocMoveToLocFAC o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(OBLocMoveToLocFAC obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        return OBLocMoveToLocFAC.getUniqueIdentifier(this.getMyOBLocation(), this.getMyOBShift(), this.getMyMoveToLocation(), this.getMyMoveToShift(), this.getMyFACLocation());
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(OBLocMoveToLocFAC obj) {

        return this.toString().compareTo(obj.toString());

    }

}
