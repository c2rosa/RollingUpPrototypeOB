package CharlieNetwork;

/**
 * Created by rosa.charles on 10/6/15.
 */
public class O_OShift_Dest_Service__isEarly_cube_weight implements java.util.Comparator<O_OShift_Dest_Service__isEarly_cube_weight>, java.lang.Comparable<O_OShift_Dest_Service__isEarly_cube_weight> {

    private String originLocation = null;
    private String originShift = null;
    private String destLocation = null;
    private String destShift = null;
    private Boolean isEarly = null;
    private Integer serviceLevel = null;
    private Integer certificationLevel = null;

    private Double myCube = null;
    private Double myWeight = null;

    private int shipmentCount = 0;

    public O_OShift_Dest_Service__isEarly_cube_weight(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            Boolean isEarly_input,
            Integer serviceLevel_input,
            Integer certificationLevel_input
    ) {
        this.originLocation = originLocation_input;
        this.originShift = originShift_input;
        this.destLocation = destLocation_input;
        this.destShift = destShift_input;
        this.isEarly = isEarly_input;
        this.serviceLevel = serviceLevel_input;
        this.certificationLevel = certificationLevel_input;
    }

    public int getShipmentCount() {
        return shipmentCount;
    }

    public void setShipmentCount(int shipmentCount) {
        this.shipmentCount = shipmentCount;
    }

    public Integer getCertificationLevel() {
        return certificationLevel;
    }

    public Double getMyCube() {
        return myCube;
    }

    public void setMyCube(Double myCube) {
        this.myCube = myCube;
    }

    public Double getMyWeight() {
        return myWeight;
    }

    public void setMyWeight(Double myWeight) {
        this.myWeight = myWeight;
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

    public Boolean getIsEarly() {
        return isEarly;
    }

    public Integer getServiceLevel() {
        return serviceLevel;
    }

    public static String getUniqueIdentifier(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            Boolean isEarly_input,
            Integer serviceLevel_input,
            Integer certificationLevel_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(originLocation_input+",");
        buffer.append(originShift_input+",");
        buffer.append(destLocation_input+",");
        buffer.append(destShift_input+",");
        buffer.append(isEarly_input+",");
        buffer.append(serviceLevel_input+",");
        buffer.append(certificationLevel_input+",");
        return buffer.toString();
    }

    public int compare(O_OShift_Dest_Service__isEarly_cube_weight o1, O_OShift_Dest_Service__isEarly_cube_weight o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(O_OShift_Dest_Service__isEarly_cube_weight obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        return O_OShift_Dest_Service__isEarly_cube_weight.getUniqueIdentifier(
                this.getOriginLocation(),
                this.getOriginShift(),
                this.getDestLocation(),
                this.getDestShift(),
                this.getIsEarly(),
                this.getServiceLevel(),
                this.getCertificationLevel()
        );
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(O_OShift_Dest_Service__isEarly_cube_weight obj) {

        return this.toString().compareTo(obj.toString());

    }



}
