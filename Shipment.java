/**
 * Created by rosa.charles on 2/27/2018.
 */
public class Shipment {

    private String myShptID = null;
    private Double myWeight = null;
    private Double mySicMiles = null;
    private Double myCustRev = null;
    private Double myWeightedClass = null;
    private Boolean isAMC = null;

    public Shipment(
            String myShptID_input,
            Double myWeight_input,
            Double mySicMiles_input,
            Double myCustRev_input,
            Double myWeightedClass_input,
            Boolean isAMC_input
    ) {
        this.myShptID = myShptID_input;
        this.myWeight = myWeight_input;
        this.mySicMiles = mySicMiles_input;
        this.myCustRev = myCustRev_input;
        this.myWeightedClass = myWeightedClass_input;
        this.isAMC = isAMC_input;
    }

    public String getMyShptID() {
        return myShptID;
    }

    public Double getMyWeight() {
        return myWeight;
    }

    public Double getMySicMiles() {
        return mySicMiles;
    }

    public Double getMyCustRev() {
        return myCustRev;
    }

    public Double getMyWeightedClass() {
        return myWeightedClass;
    }

    public Boolean getAMC() {
        return isAMC;
    }


}
