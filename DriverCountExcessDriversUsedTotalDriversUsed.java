/**
 * Created by rosa.charles on 12/4/14.
 */
public class DriverCountExcessDriversUsedTotalDriversUsed {

    private Integer DriverCount = null;
    private Integer ExcessDriversUsed = null;
    private Integer TotalDriversUsed = null;

    public DriverCountExcessDriversUsedTotalDriversUsed(
            Integer DriverCount_input,
            Integer ExcessDriversUsed_input,
            Integer TotalDriversUsed_input
    ) {
        this.DriverCount = DriverCount_input;
        this.ExcessDriversUsed = ExcessDriversUsed_input;
        this.TotalDriversUsed = TotalDriversUsed_input;
    }

    public void addDriverCount(int myDriverCount) {
        this.DriverCount += myDriverCount;
    }

    public void addExcessDriversUsed(int myExcessDriversUsed) {
        this.ExcessDriversUsed += myExcessDriversUsed;
    }

    public void addTotalDriversUsed(int myTotalDriversUsed) {
        this.TotalDriversUsed += myTotalDriversUsed;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.DriverCount+",");
        buffer.append(this.ExcessDriversUsed+",");
        buffer.append(this.TotalDriversUsed);
        return buffer.toString();
    }

    public static String myHeader = new String("DriverCount, ExcessDriversUsed, TotalDriversUsed");


}
