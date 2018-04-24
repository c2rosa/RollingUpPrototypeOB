import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rosa.charles on 7/22/15.
 */
public class OriginLocationDestinationLocation implements java.util.Comparator<OriginLocationDestinationLocation>, java.lang.Comparable<OriginLocationDestinationLocation> {

    private String originLocation = null;
    private String originShift = null;
    private String destinationLocation = null;
    private String destinationShift = null;
    private String myMode = null;

    private String stringRepresentationOfOriginLocationDestinationLocation = null;

    private Integer trailersPerTractor = null;

    public OriginLocationDestinationLocation(
            String originLocation_input,
            String originShift_input,
            String destinationLocation_input,
            String destinationShift_input,
            String myMode_input
    ) throws Exception {
        this.originLocation = originLocation_input;
        this.originShift = originShift_input;
        this.destinationLocation = destinationLocation_input;
        this.destinationShift = destinationShift_input;
        this.myMode = myMode_input;
    }

    public String getMyMode() {
        return myMode;
    }

    public String getLocation_origin() {
        return this.originLocation;
    }

    public String getShift_origin() {
        return this.originShift;
    }

    public String getLocation_destination() {
        return this.destinationLocation;
    }

    public String getShift_destination() {
        return this.destinationShift;
    }


    public int compare(OriginLocationDestinationLocation o1, OriginLocationDestinationLocation o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(OriginLocationDestinationLocation obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        if(this.stringRepresentationOfOriginLocationDestinationLocation != null) {
            return this.stringRepresentationOfOriginLocationDestinationLocation;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getLocation_origin()+"_");
        buffer.append(this.getShift_origin()+"_");
        buffer.append(this.getLocation_destination()+"_");
        buffer.append(this.getShift_destination()+"_");
        buffer.append(this.getMyMode()+"_");

        this.stringRepresentationOfOriginLocationDestinationLocation = buffer.toString();

        return this.stringRepresentationOfOriginLocationDestinationLocation;
    }


    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(OriginLocationDestinationLocation obj) {

        if(this.equals(obj)) {
            return 0;
        }

        return this.toString().compareTo(obj.toString());

    }

}

