/**
 * Created by rosa.charles on 9/29/14.
 */
public class OrigLocationViaLocationDestLocation implements java.util.Comparator<OrigLocationViaLocationDestLocation>, java.lang.Comparable<OrigLocationViaLocationDestLocation> {

    String myOBLocation = null;
    String myViaLocation = null;
    String myFACLocation = null;

    public OrigLocationViaLocationDestLocation(
            String myOBLocation_input,
            String myViaLocation_input,
            String myFACLocation_input
    ) {
        this.myOBLocation = myOBLocation_input;
        this.myViaLocation = myViaLocation_input;
        this.myFACLocation = myFACLocation_input;
    }

    public String getMyOBLocation() {
        return myOBLocation;
    }

    public String getMyViaLocation() {
        return myViaLocation;
    }

    public String getMyFACLocation() {
        return myFACLocation;
    }

    public static String getUniqueIdentifier(
            String myOBLocation_input,
            String myViaLocation_input,
            String myFACLocation_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(myOBLocation_input+",");
        buffer.append(myViaLocation_input+",");
        buffer.append(myFACLocation_input);
        return buffer.toString();
    }

    public int compare(OrigLocationViaLocationDestLocation o1, OrigLocationViaLocationDestLocation o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(OrigLocationViaLocationDestLocation obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        return OrigLocationViaLocationDestLocation.getUniqueIdentifier(this.getMyOBLocation(), this.getMyViaLocation(), this.getMyFACLocation());
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(OrigLocationViaLocationDestLocation obj) {

        return this.toString().compareTo(obj.toString());

    }



}
