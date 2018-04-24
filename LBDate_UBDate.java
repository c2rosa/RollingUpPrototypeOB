import java.util.Date;

/**
 * Created by rosa.charles on 1/19/2017.
 */
public class LBDate_UBDate {

    private Date myLB = null;
    private Date myUB = null;

    public LBDate_UBDate(
            Date myLB_input,
            Date myUB_input
    ) {
        this.myLB = myLB_input;
        this.myUB = myUB_input;
    }

    public Date getMyLB() {
        return myLB;
    }

    public Date getMyUB() {
        return myUB;
    }

    public boolean inInterval(Date myDate) {
        boolean isInterval = true;
        if(myDate.before(myLB)) {
            isInterval = false;
            return isInterval;
        }

        if(myDate.after(myUB)) {
            isInterval = false;
            return isInterval;
        }

        return isInterval;
    }


}
