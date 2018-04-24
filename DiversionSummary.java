/**
 * Created by rosa.charles on 2/10/15.
 */
public class DiversionSummary {

    private Integer myDiversion_fewer = null;
    private Integer myDiversion_more = null;

    public DiversionSummary() {
        this.myDiversion_fewer = 0;
        this.myDiversion_more = 0;
    }

    public void incrementDiversion_fewer() {
        this.myDiversion_fewer++;
    }

    public void incrementDiversion_more() {
        this.myDiversion_more++;
    }

    public Integer getMyDiversion_fewer() {
        return myDiversion_fewer;
    }

    public void setMyDiversion_fewer(Integer myDiversion_fewer) {
        this.myDiversion_fewer = myDiversion_fewer;
    }

    public Integer getMyDiversion_more() {
        return myDiversion_more;
    }

    public void setMyDiversion_more(Integer myDiversion_more) {
        this.myDiversion_more = myDiversion_more;
    }

}
