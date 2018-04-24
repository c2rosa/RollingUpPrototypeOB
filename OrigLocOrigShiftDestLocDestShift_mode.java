/**
 * Created by rosa.charles on 12/15/14.
 */
public class OrigLocOrigShiftDestLocDestShift_mode implements java.util.Comparator<OrigLocOrigShiftDestLocDestShift_mode>, java.lang.Comparable<OrigLocOrigShiftDestLocDestShift_mode> {

    private String originLocation = null;
    private String originShift = null;
    private String viaLocation = null;
    private String destLocation = null;
    private String destShift = null;
    private String mode = null;

    public OrigLocOrigShiftDestLocDestShift_mode(
            String originLocation_input,
            String originShift_input,
            String viaLocation_input,
            String destLocation_input,
            String destShift_input,
            String mode_input
    ) {
        this.originLocation = originLocation_input;
        this.originShift = originShift_input;
        this.viaLocation = viaLocation_input;
        this.destLocation = destLocation_input;
        this.destShift = destShift_input;
        this.mode = mode_input;
    }

    public boolean hasVia() {
        if(this.viaLocation==null) {
            return false;
        } else  {
            if(!this.viaLocation.equals("null")) {
                return true;
            } else {
                return false;
            }
        }

    }

    public boolean isLineHaul() {
        if(this.mode != null) {
            if(this.mode.equals(LHHssReportBuilder.L)) {
                return true;
            }
        }

        return false;
    }

    public OrigLocDestLoc getOrigLocDestLoc(Data myData) throws Exception {
        OrigLocDestLoc myOrigLocDestLoc = myData.getOrigLocDestLoc(this.getOriginLocation(), this.getDestLocation(), true);

        return myOrigLocDestLoc;
    }

    public String getViaLocation() {
        return viaLocation;
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
            String viaLocation_input,
            String destLocation_input,
            String destShift_input,
            String mode_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(originLocation_input+",");
        buffer.append(originShift_input+",");
        buffer.append(viaLocation_input+",");
        buffer.append(destLocation_input+",");
        buffer.append(destShift_input+",");
        buffer.append(mode_input);
        return buffer.toString();
    }

    public int compare(OrigLocOrigShiftDestLocDestShift_mode o1, OrigLocOrigShiftDestLocDestShift_mode o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(OrigLocOrigShiftDestLocDestShift_mode obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        return OrigLocOrigShiftDestLocDestShift_mode.getUniqueIdentifier(this.getOriginLocation(),this.getOriginShift(),this.getViaLocation(),this.getDestLocation(),this.getDestShift(),this.getMode());
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(OrigLocOrigShiftDestLocDestShift_mode obj) {

        return this.toString().compareTo(obj.toString());

    }

}
