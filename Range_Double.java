/**
 * Created by rosa.charles on 2/27/2018.
 */
public class Range_Double {

    private Double myLB = null;
    private Double myUB = null;

    public Range_Double(
            Double myLB_input,
            Double myUB_input
    ) throws Exception {
        this.myLB = myLB_input;
        this.myUB = myUB_input;

        if(this.myLB>this.myUB) {
            throw new Exception("Range is incorrect: this.myLB="+this.myLB+" and this.myUB="+this.myUB);
        }
    }

    public boolean contains(Double myValue) {
        if(myValue>=this.myLB && myValue<=this.myUB) {
            return true;
        }

        return false;
    }
}
