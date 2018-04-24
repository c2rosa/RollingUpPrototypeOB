/**
 * Created by rosa.charles on 2/5/16.
 */
public class PROCount_Cube_Weight implements java.util.Comparator<PROCount_Cube_Weight>, java.lang.Comparable<PROCount_Cube_Weight> {

    Integer myProCount = null;
    Double myCube = null;
    Double myWeight = null;

    public PROCount_Cube_Weight(Integer proCount_input, Double cube_input, Double weight_input) {

        if(proCount_input == null) {
            this.myProCount = new Integer(0);
        } else {
            this.myProCount = new Integer(proCount_input.intValue());
        }

        if(cube_input == null) {
            this.myCube = new Double(0.0);
        } else {
            this.myCube = new Double(cube_input.doubleValue());
        }

        if(weight_input == null) {
            this.myWeight = new Double(0.0);
        } else {
            this.myWeight = new Double(weight_input.doubleValue());
        }
    }

    public Integer getMyProCount() {
        return myProCount;
    }

    public Double getMyCube() {
        return myCube;
    }

    public Double getMyWeight() {
        return myWeight;
    }

    public void addPROCount_Cube_Weight(PROCount_Cube_Weight myPROCount_Cube_Weight_new) {
        this.addPROCount_Cube_Weight(myPROCount_Cube_Weight_new.getMyProCount(), myPROCount_Cube_Weight_new.getMyCube(), myPROCount_Cube_Weight_new.getMyWeight());
    }

    public void addPROCount_Cube_Weight(Integer proCount_input, Double cube_input, Double weight_input) {

        if(proCount_input != null) {
            this.myProCount += new Integer(proCount_input.intValue());
        }

        if(cube_input != null) {
            this.myCube += new Double(cube_input.doubleValue());
        }

        if(weight_input != null) {
            this.myWeight += new Double(weight_input.doubleValue());
        }

    }

    public int compare(PROCount_Cube_Weight o1, PROCount_Cube_Weight o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(PROCount_Cube_Weight obj) {

        if(!this.toString().equals(obj.toString())) {
            return false;
        }

        return true;

    }

    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getMyCube()+"_");
        buffer.append(this.getMyWeight());

        return buffer.toString();
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(PROCount_Cube_Weight obj) {

        return this.toString().compareTo(obj.toString());
    }



}
