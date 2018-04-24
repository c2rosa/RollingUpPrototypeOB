import java.util.Date;

/**
 * Created by rosa.charles on 8/4/15.
 */
public class LocationShiftTrailerActionDate {

    private String myLocation = null;
    private String myShift = null;
    private String myTrailerAction = null;
    private Date myDate = null;

    public LocationShiftTrailerActionDate(
            String myLocation_input,
            String myShift_input,
            String myTrailerAction_input,
            Date myDate_input
    ) {
        this.myLocation = myLocation_input;
        this.myShift = myShift_input;
        this.myTrailerAction = myTrailerAction_input;
        this.myDate = myDate_input;
    }

    public boolean isThisHoldOffOBOnThisDateAndFromThisRegion(Date myDate_input, String myRegion, LHHssReportBuilder myLHHssReportBuilder) {

        String theRegionOfThisLocation = myLHHssReportBuilder.myMapOfRegionBySic.get(this.myLocation);

        if(theRegionOfThisLocation != null) {
            if(!theRegionOfThisLocation.equals(myRegion)) {
                return false;
            }
        }

        if(myShift.equals("O")) {
            if(this.myTrailerAction.equals("HLD")) {
                if(this.myDate.equals(myDate_input)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getMyLocation() {
        return myLocation;
    }

    public String getMyShift() {
        return myShift;
    }

    public String getMyTrailerAction() {
        return myTrailerAction;
    }

    public Date getMyDate() {
        return myDate;
    }


}
