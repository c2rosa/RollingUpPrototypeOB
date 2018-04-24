import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by rosa.charles on 8/4/15.
 */
public class Trailer implements java.util.Comparator<Trailer>, java.lang.Comparable<Trailer> {

    private Integer myID = null;

    private SortedMap<Integer, LocationShiftTrailerActionDate> myMapOfSequenceOfLocationTimeTrailerActionDate = null;

    private String stringRepresentationOfTrailer = null;

    private String obLocation = null;
    private String trailerPrefix = null;
    private String trailerSuffix = null;
    private String loadStatus = null;
    private String loadToLocation = null;
    private String loadToShift = null;
    private String loadToDay = null;
    private Boolean isLH = null;

    public Trailer(Integer myID_input) {
        this.myID = myID_input;

        this.myMapOfSequenceOfLocationTimeTrailerActionDate = new TreeMap<Integer,LocationShiftTrailerActionDate> ();
    }

    public String getDescriptorString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getMyID()+",");
        buffer.append(this.getTrailerPrefix()+",");
        buffer.append(this.getTrailerSuffix()+",");
        buffer.append(this.getObLocation()+",");
        buffer.append(this.getLoadStatus()+",");
        buffer.append(this.getLoadToLocation()+",");
        buffer.append(this.getLoadToShift()+",");
        buffer.append(this.getLoadToDay()+",");
        buffer.append(this.getIsLH()+",");

        return buffer.toString();
    }

    public Boolean getIsLH() {
        return isLH;
    }

    public void setIsLH(Boolean isLH) {
        this.isLH = isLH;
    }

    public String getObLocation() {
        return obLocation;
    }

    public void setObLocation(String obLocation) {
        this.obLocation = obLocation;
    }

    public String getTrailerPrefix() {
        return trailerPrefix;
    }

    public void setTrailerPrefix(String trailerPrefix) {
        this.trailerPrefix = trailerPrefix;
    }

    public String getTrailerSuffix() {
        return trailerSuffix;
    }

    public void setTrailerSuffix(String trailerSuffix) {
        this.trailerSuffix = trailerSuffix;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getLoadToLocation() {
        return loadToLocation;
    }

    public void setLoadToLocation(String loadToLocation) {
        this.loadToLocation = loadToLocation;
    }

    public String getLoadToShift() {
        return loadToShift;
    }

    public void setLoadToShift(String loadToShift) {
        this.loadToShift = loadToShift;
    }

    public String getLoadToDay() {
        return loadToDay;
    }

    public void setLoadToDay(String loadToDay) {
        this.loadToDay = loadToDay;
    }

    public static String getUniqueIdentifier(
            Integer myID_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(myID_input);
        return buffer.toString();
    }

    public void addLocationShiftTrailerActionDate(Integer mySeq, LocationShiftTrailerActionDate myLocationShiftTrailerActionDate) {
        if(myLocationShiftTrailerActionDate==null || mySeq==null) {
            return;
        }
        this.myMapOfSequenceOfLocationTimeTrailerActionDate.put(mySeq, myLocationShiftTrailerActionDate);
    }

    public LocationShiftTrailerActionDate doesThisHoldOffOBOnThisDateAndFromThisRegion(Date myDate, String myRegion, LHHssReportBuilder myLHHssReportBuilder) {
        for(Integer mySeq : this.myMapOfSequenceOfLocationTimeTrailerActionDate.keySet()) {
            LocationShiftTrailerActionDate myLocationShiftTrailerActionDate = this.myMapOfSequenceOfLocationTimeTrailerActionDate.get(mySeq);
            if(myLocationShiftTrailerActionDate.isThisHoldOffOBOnThisDateAndFromThisRegion(myDate, myRegion, myLHHssReportBuilder)) {
                return myLocationShiftTrailerActionDate;
            }
        }

        return null;
    }

    public Integer getMyID() {
        return myID;
    }

    public int compare(Trailer o1, Trailer o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(Trailer obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        if(this.stringRepresentationOfTrailer != null) {
            return this.stringRepresentationOfTrailer;
        }

        this.stringRepresentationOfTrailer = Trailer.getUniqueIdentifier(this.getMyID());

        return this.stringRepresentationOfTrailer;
    }


    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(Trailer obj) {

        if(this.equals(obj)) {
            return 0;
        }

        return this.toString().compareTo(obj.toString());

    }

}
