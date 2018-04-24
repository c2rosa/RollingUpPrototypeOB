import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rosa.charles on 8/17/16.
 */
public class ModelRunID_Sic_driverUsageCounts implements java.util.Comparator<ModelRunID_Sic_driverUsageCounts>, java.lang.Comparable<ModelRunID_Sic_driverUsageCounts> {

    private Integer myModelRunID = null;
    private String mySic = null;

    private Integer myReportedAvailableDrivers_isDriversLimited = null;
    private Integer myReportedAvailableDrivers_isDriversUnLimited = null;

    private Integer myExcessDrivers_isDriversLimited = null;
    private Integer myExcessDrivers_isDriversUnLimited = null;

    private Integer myDriversUsed_isDriversLimited = null;
    private Integer myDriversUsed_isDriversUnLimited = null;

    private String myModelGroupID = null;
    private Date myDateForThisRunID = null;

    public static String OBShift = new String("O");
    public static String FACShift = new String("F");

    public ModelRunID_Sic_driverUsageCounts(
            Integer myModelRunID_input,
            String mySic_input
    ) {
        this.myModelRunID = myModelRunID_input;
        this.mySic = mySic_input;


    }

    public Integer getMyReportedAvailableDrivers_isDriversLimited() {
        return myReportedAvailableDrivers_isDriversLimited;
    }

    public void setMyReportedAvailableDrivers_isDriversLimited(Integer myReportedAvailableDrivers_isDriversLimited) {
        this.myReportedAvailableDrivers_isDriversLimited = myReportedAvailableDrivers_isDriversLimited;
    }

    public Integer getMyReportedAvailableDrivers_isDriversUnLimited() {
        return myReportedAvailableDrivers_isDriversUnLimited;
    }

    public void setMyReportedAvailableDrivers_isDriversUnLimited(Integer myReportedAvailableDrivers_isDriversUnLimited) {
        this.myReportedAvailableDrivers_isDriversUnLimited = myReportedAvailableDrivers_isDriversUnLimited;
    }

    public Integer getMyExcessDrivers_isDriversLimited() {
        return myExcessDrivers_isDriversLimited;
    }

    public void setMyExcessDrivers_isDriversLimited(Integer myExcessDrivers_isDriversLimited) {
        this.myExcessDrivers_isDriversLimited = myExcessDrivers_isDriversLimited;
    }

    public Integer getMyExcessDrivers_isDriversUnLimited() {
        return myExcessDrivers_isDriversUnLimited;
    }

    public void setMyExcessDrivers_isDriversUnLimited(Integer myExcessDrivers_isDriversUnLimited) {
        this.myExcessDrivers_isDriversUnLimited = myExcessDrivers_isDriversUnLimited;
    }

    public Integer getMyDriversUsed_isDriversLimited() {
        return myDriversUsed_isDriversLimited;
    }

    public void setMyDriversUsed_isDriversLimited(Integer myDriversUsed_isDriversLimited) {
        this.myDriversUsed_isDriversLimited = myDriversUsed_isDriversLimited;
    }

    public Integer getMyDriversUsed_isDriversUnLimited() {
        return myDriversUsed_isDriversUnLimited;
    }

    public void setMyDriversUsed_isDriversUnLimited(Integer myDriversUsed_isDriversUnLimited) {
        this.myDriversUsed_isDriversUnLimited = myDriversUsed_isDriversUnLimited;
    }

    public String getMyModelGroupID() {
        return myModelGroupID;
    }

    public void setMyModelGroupID(String myModelGroupID) {
        this.myModelGroupID = myModelGroupID;
    }

    public Date getMyDateForThisRunID() {
        return myDateForThisRunID;
    }

    public void setMyDateForThisRunID(Date myDateForThisRunID) {
        this.myDateForThisRunID = myDateForThisRunID;
    }

    public Integer getMyModelRunID() {
        return myModelRunID;
    }

    public String getMySic() {
        return mySic;
    }

    public static String getUniqueIdentifier(
            Integer myModelRunID_input,
            String mySic_input
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(myModelRunID_input+",");
        buffer.append(mySic_input);
        return buffer.toString();
    }

    public static String myHeader = new String("isDriversLimited, modelRunID, ModelGroupID, Date, sic, myReportedAvailableDrivers, myExcessDrivers, myDriversUsed");

    public String getStringOutputFileFile_isDriversLimited() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(true+",");
        buffer.append(this.getMyModelRunID()+",");
        buffer.append(this.getMyModelGroupID()+",");
        buffer.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");
        buffer.append(this.getMySic()+",");
        buffer.append(this.getMyReportedAvailableDrivers_isDriversLimited()+",");
        buffer.append(this.getMyExcessDrivers_isDriversLimited()+",");
        buffer.append(this.getMyDriversUsed_isDriversLimited()+",");

        return buffer.toString();
    }

    public String getStringOutputFileFile_isDriversUnlimited() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(false+",");
        buffer.append(this.getMyModelRunID()+",");
        buffer.append(this.getMyModelGroupID()+",");
        buffer.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");
        buffer.append(this.getMySic()+",");
        buffer.append(this.getMyReportedAvailableDrivers_isDriversUnLimited()+",");
        buffer.append(this.getMyExcessDrivers_isDriversUnLimited()+",");
        buffer.append(this.getMyDriversUsed_isDriversUnLimited()+",");

        return buffer.toString();

    }


    public int compare(ModelRunID_Sic_driverUsageCounts o1, ModelRunID_Sic_driverUsageCounts o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(ModelRunID_Sic_driverUsageCounts obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        return ModelRunID_Sic_driverUsageCounts.getUniqueIdentifier(this.getMyModelRunID(), this.getMySic());
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(ModelRunID_Sic_driverUsageCounts obj) {

        return this.toString().compareTo(obj.toString());

    }




}
