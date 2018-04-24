import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rosa.charles on 11/24/14.
 */
public class ModelRunID_Sic_excessDriverCounts implements java.util.Comparator<ModelRunID_Sic_excessDriverCounts>, java.lang.Comparable<ModelRunID_Sic_excessDriverCounts> {

    private Integer myModelRunID = null;
    private String mySic = null;

    private Integer myReportedAvailableDrivers = null;

    private Integer myExcessDrivers_new = null;
    private Integer myExcessDrivers_old = null;

    private Integer myExcessDrivers_withVias_withDoubles = null;
    private Integer myExcessDrivers_withNOVias_withDoubles = null;
    private Integer myExcessDrivers_withVias_withNODoubles = null;
    private Integer myExcessDrivers_withNOVias_withNODoubles = null;

    private Map<String, Boolean> myMapOfIsExcessDrivers_new_OutOfShiftString = null;
    private Map<String, Boolean> myMapOfIsExcessDrivers_old_OutOfShiftString = null;

    private String myModelGroupID = null;
    private Date myDateForThisRunID = null;

    public static String OBShift = new String("O");
    public static String FACShift = new String("F");

    public ModelRunID_Sic_excessDriverCounts(
            Integer myModelRunID_input,
            String mySic_input
    ) {
        this.myModelRunID = myModelRunID_input;
        this.mySic = mySic_input;

        myMapOfIsExcessDrivers_new_OutOfShiftString = new HashMap<String, Boolean>();
        myMapOfIsExcessDrivers_old_OutOfShiftString = new HashMap<String, Boolean>();

    }

    public Integer getMyExcessDrivers_withVias_withDoubles() {
        return myExcessDrivers_withVias_withDoubles;
    }

    public void setMyExcessDrivers_withVias_withDoubles(Integer myExcessDrivers_withVias_withDoubles) {
        this.myExcessDrivers_withVias_withDoubles = myExcessDrivers_withVias_withDoubles;
    }

    public Integer getMyExcessDrivers_withNOVias_withDoubles() {
        return myExcessDrivers_withNOVias_withDoubles;
    }

    public void setMyExcessDrivers_withNOVias_withDoubles(Integer myExcessDrivers_withNOVias_withDoubles) {
        this.myExcessDrivers_withNOVias_withDoubles = myExcessDrivers_withNOVias_withDoubles;
    }

    public Integer getMyExcessDrivers_withVias_withNODoubles() {
        return myExcessDrivers_withVias_withNODoubles;
    }

    public void setMyExcessDrivers_withVias_withNODoubles(Integer myExcessDrivers_withVias_withNODoubles) {
        this.myExcessDrivers_withVias_withNODoubles = myExcessDrivers_withVias_withNODoubles;
    }

    public Integer getMyExcessDrivers_withNOVias_withNODoubles() {
        return myExcessDrivers_withNOVias_withNODoubles;
    }

    public void setMyExcessDrivers_withNOVias_withNODoubles(Integer myExcessDrivers_withNOVias_withNODoubles) {
        this.myExcessDrivers_withNOVias_withNODoubles = myExcessDrivers_withNOVias_withNODoubles;
    }

    public Integer getMyReportedAvailableDrivers() {
        return myReportedAvailableDrivers;
    }

    public void setMyReportedAvailableDrivers(Integer myReportedAvailableDrivers) {
        this.myReportedAvailableDrivers = myReportedAvailableDrivers;
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

    public Integer getMyExcessDrivers_new() {
        return myExcessDrivers_new;
    }

    public void setMyExcessDrivers_new(Integer myExcessDrivers_new) {
        this.myExcessDrivers_new = myExcessDrivers_new;
    }

    public void addMyExcessDrivers_new(Integer myExcessDrivers_new) {

        if(this.myExcessDrivers_new == null) {
            this.myExcessDrivers_new = myExcessDrivers_new;
        } else {
            this.myExcessDrivers_new += myExcessDrivers_new;
        }

    }

    public void setShiftAsCause_new(String myShift) {
        boolean isShiftAlright = false;
        if(myShift.equals(ModelRunID_Sic_excessDriverCounts.OBShift)) {
            isShiftAlright = true;
        }

        if(myShift.equals(ModelRunID_Sic_excessDriverCounts.FACShift)) {
            isShiftAlright = true;
        }

        if(!isShiftAlright) {
            return;
        }

        this.myMapOfIsExcessDrivers_new_OutOfShiftString.put(myShift, new Boolean(true));
    }

    public void setShiftAsCause_old(String myShift) {

        boolean isShiftAlright = false;
        if(myShift.equals(ModelRunID_Sic_excessDriverCounts.OBShift)) {
            isShiftAlright = true;
        }

        if(myShift.equals(ModelRunID_Sic_excessDriverCounts.FACShift)) {
            isShiftAlright = true;
        }

        if(!isShiftAlright) {
            return;
        }

        this.myMapOfIsExcessDrivers_old_OutOfShiftString.put(myShift, new Boolean(true));
    }

    public void addMyExcessDrivers_old(Integer myExcessDrivers_old) {

        if(this.myExcessDrivers_old == null) {
            this.myExcessDrivers_old = myExcessDrivers_old;
        } else {
            this.myExcessDrivers_old += myExcessDrivers_old;
        }

    }



    public Integer getMyExcessDrivers_old() {
        return myExcessDrivers_old;
    }

    public void setMyExcessDrivers_old(Integer myExcessDrivers_old) {
        this.myExcessDrivers_old = myExcessDrivers_old;
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

    public static String myHeader = new String("modelRunID, ModelGroupID, Date, sic, myReportedAvailableDrivers, NumDriverOverages, fraction_driverOverage_to_reportedAvailable, isCausedByOB->FAC, isCausedByFAC->IB");

    public String getStringOutputFileFile_new(
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getMyModelRunID()+",");
        buffer.append(this.getMyModelGroupID()+",");
        buffer.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");
        buffer.append(this.getMySic()+",");
        buffer.append(this.getMyReportedAvailableDrivers()+",");
        buffer.append(this.getMyExcessDrivers_new()+",");
        buffer.append((this.getMyExcessDrivers_new()+0.0001)/(this.getMyReportedAvailableDrivers()+0.0001)+",");
        boolean isCausedOB_FAC = false;
        if(this.myMapOfIsExcessDrivers_new_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.OBShift) != null) {
            if(this.myMapOfIsExcessDrivers_new_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.OBShift)) {
                isCausedOB_FAC = true;
            }
        }
        buffer.append(isCausedOB_FAC+",");

        boolean isCausedFAC_IB = false;
        if(this.myMapOfIsExcessDrivers_new_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.FACShift) != null) {
            if(this.myMapOfIsExcessDrivers_new_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.FACShift)) {
                isCausedFAC_IB = true;
            }
        }
        buffer.append(isCausedFAC_IB+",");

        return buffer.toString();
    }

    public String getStringOutputFileFile_old(
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getMyModelRunID()+",");
        buffer.append(this.getMyModelGroupID()+",");
        buffer.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");
        buffer.append(this.getMySic()+",");
        buffer.append(this.getMyReportedAvailableDrivers()+",");
        buffer.append(this.getMyExcessDrivers_old()+",");
        buffer.append((this.getMyExcessDrivers_old()+0.0001)/(this.getMyReportedAvailableDrivers()+0.0001)+",");
        boolean isCausedOB_FAC = false;
        if(this.myMapOfIsExcessDrivers_old_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.OBShift) != null) {
            if(this.myMapOfIsExcessDrivers_old_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.OBShift)) {
                isCausedOB_FAC = true;
            }
        }
        buffer.append(isCausedOB_FAC+",");

        boolean isCausedFAC_IB = false;
        if(this.myMapOfIsExcessDrivers_old_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.FACShift) != null) {
            if(this.myMapOfIsExcessDrivers_old_OutOfShiftString.get(ModelRunID_Sic_excessDriverCounts.FACShift)) {
                isCausedFAC_IB = true;
            }
        }
        buffer.append(isCausedFAC_IB+",");


        return buffer.toString();
    }

    public static String myAggregatedHeader = new String("modelRunID, ModelGroupID, Date, sic, myReportedAvailableDrivers, driverOverages_withVias_withDoubles, driverOverages_withNoVias_withDoubles, driverOverages_withVias_withNoDoubles, driverOverages_withNoVias_withNoDoubles");

    public String getStringOutputFileFile_aggregated(
    ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getMyModelRunID()+",");
        buffer.append(this.getMyModelGroupID()+",");
        buffer.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");
        buffer.append(this.getMySic()+",");
        buffer.append(this.getMyReportedAvailableDrivers()+",");
        if(this.getMyExcessDrivers_withVias_withDoubles() != null) {
            buffer.append(this.getMyExcessDrivers_withVias_withDoubles()+",");
        } else {
            buffer.append(0+",");
        }

        if(this.getMyExcessDrivers_withNOVias_withDoubles() != null) {
            buffer.append(this.getMyExcessDrivers_withNOVias_withDoubles()+",");
        } else {
            buffer.append(0+",");
        }

        if(this.getMyExcessDrivers_withVias_withNODoubles() != null) {
            buffer.append(this.getMyExcessDrivers_withVias_withNODoubles()+",");
        } else {
            buffer.append(0+",");
        }

        if(this.getMyExcessDrivers_withNOVias_withNODoubles() != null) {
            buffer.append(this.getMyExcessDrivers_withNOVias_withNODoubles()+",");
        } else {
            buffer.append(0+",");
        }


        return buffer.toString();
    }




    public int compare(ModelRunID_Sic_excessDriverCounts o1, ModelRunID_Sic_excessDriverCounts o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(ModelRunID_Sic_excessDriverCounts obj) {

        return this.toString().equals(obj.toString());
    }

    public String toString() {
        return ModelRunID_Sic_excessDriverCounts.getUniqueIdentifier(this.getMyModelRunID(), this.getMySic());
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(ModelRunID_Sic_excessDriverCounts obj) {

        return this.toString().compareTo(obj.toString());

    }




}
