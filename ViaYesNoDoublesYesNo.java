/**
 * Created by rosa.charles on 9/2/15.
 */
public class ViaYesNoDoublesYesNo {

    boolean areViasOn = false;
    boolean areDoublesOn = false;

    public ViaYesNoDoublesYesNo(
            boolean areViasOn_input,
            boolean areDoublesOn_input
    ) {
        this.areViasOn = areViasOn_input;
        this.areDoublesOn = areDoublesOn_input;
    }

    public boolean isAreViasOn() {
        return areViasOn;
    }

    public boolean isAreDoublesOn() {
        return areDoublesOn;
    }


}
