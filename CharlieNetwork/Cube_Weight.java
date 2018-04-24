package CharlieNetwork;

/**
 * Created by rosa.charles on 10/6/15.
 */
public class Cube_Weight implements java.util.Comparator<Cube_Weight>, java.lang.Comparable<Cube_Weight> {

    Double myCube = null;
    Double myWeight = null;

    public Cube_Weight(Double cube_input, Double weight_input) {
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

    public double innerProduct(Cube_Weight myCube_Weight_other) {
        double returnValue = 0;
        returnValue += this.getMyCube()*myCube_Weight_other.getMyCube();
        returnValue += this.getMyWeight()*myCube_Weight_other.getMyWeight();
        return returnValue;
    }


    public Cube_Weight getProjectionsOnto(Cube_Weight myCube_WeightThatIamProjectingOnto) throws Exception {
        double innerProduct = this.innerProduct(myCube_WeightThatIamProjectingOnto);
        double length = myCube_WeightThatIamProjectingOnto.getLength();
        if(length == 0.0) {
            throw new Exception("Can project onto myCube_WeightThatIamProjectingOnto="+myCube_WeightThatIamProjectingOnto+" since has 0 length") ;
        }
        double lengthSquared = length*length;

        Cube_Weight returnValue =  myCube_WeightThatIamProjectingOnto.multipliedBy(innerProduct/lengthSquared);

        return returnValue;
    }

    public double getFractionOf(Cube_Weight myCube_Weight_input) throws Exception {

        Double myCube_input = myCube_Weight_input.getMyCube();
        Double myWeight_input = myCube_Weight_input.getMyWeight();

        if(myCube_input > this.getMyCube()) {
            throw new Exception("myCube_input="+myCube_input+" > this.getMyCube()="+this.getMyCube()) ;
        }

        if(myWeight_input > this.getMyWeight()) {
            throw new Exception("myWeight_input="+myWeight_input+" > this.getMyWeight()="+this.getMyWeight()) ;
        }

        double ratioCube = myCube_input/this.getMyCube();

        double ratioWeight = myWeight_input/this.getMyWeight();

        double returnRatio = Math.max(ratioCube, ratioWeight);

        return returnRatio;


    }

    public Cube_Weight(Cube_Weight myCube_Weight_input) {
        this(myCube_Weight_input.getMyCube(), myCube_Weight_input.getMyWeight());
    }

    public Cube_Weight divideBy(double factor) throws Exception {
        if(factor == 0.0) {
            throw new Exception("factor="+factor+" when calling Utility.Cube_Weight::divideBy()");
        }
        Cube_Weight myReturnValue = new Cube_Weight(this.myCube/factor, this.myWeight/factor);

        return myReturnValue;
    }

    public Cube_Weight multipliedBy(double scaleFactor) throws Exception {
        Cube_Weight myReturnValue = new Cube_Weight(this.myCube*scaleFactor, this.myWeight*scaleFactor);

        return myReturnValue;
    }

    public double getLength() {
        double returnValue = 0.0;
        returnValue += this.getMyCube()*this.getMyCube();
        returnValue += this.getMyWeight()*this.getMyWeight();

        returnValue = Math.sqrt(returnValue);

        return returnValue;
    }

    public double findHowToScaleLengthSoThatCorrectLength(Cube_Weight myCube_Weight_other, double min, double max) throws Exception {
        double upperBoundOnLength = myCube_Weight_other.getLength()*max;
        double lowerBoundOnLength = myCube_Weight_other.getLength()*min;

        double currentLength = this.getLength();

        if(currentLength >= lowerBoundOnLength && currentLength <= upperBoundOnLength) {
            return 1.0;
        } else if(currentLength > upperBoundOnLength) {
            return upperBoundOnLength/currentLength;
        } else  {
            if(currentLength == 0.0) {
                throw new Exception("Can not scale up zero length vector having currentLength="+currentLength);
            }
            return lowerBoundOnLength/currentLength;
        }
    }


    public void addCube_Weight(Cube_Weight myCube_Weight_new) {
        this.addCube_Weight(myCube_Weight_new.getMyCube(), myCube_Weight_new.getMyWeight());
    }

    public void addCube_Weight(Double cube_input, Double weight_input) {
        if(cube_input != null) {
            this.myCube += new Double(cube_input.doubleValue());
        }

        if(weight_input != null) {
            this.myWeight += new Double(weight_input.doubleValue());
        }

    }

    public Cube_Weight subtract(Cube_Weight myCube_Weight_input) {
        Cube_Weight returnValue = new Cube_Weight(this.getMyCube()-myCube_Weight_input.getMyCube(), this.getMyWeight()-myCube_Weight_input.getMyWeight());
        return returnValue;
    }

    public boolean isZero() {
        boolean isZeroNow = true;
        if(this.getMyCube()>0.0000001) {
            isZeroNow = false;
        }

        if(this.getMyWeight()>0.00000001) {
            isZeroNow = false;
        }

        return isZeroNow;
    }

    public boolean isLessThanOrEqualTo(Cube_Weight myCube_Weight_input) {
        boolean isItLessThan = true;
        if(this.getMyCube() > myCube_Weight_input.getMyCube()) {
            isItLessThan = false;
        }

        if(this.getMyWeight() > myCube_Weight_input.getMyWeight()) {
            isItLessThan = false;
        }

        return isItLessThan;

    }

    public boolean isGreaterThan(Cube_Weight myCube_Weight_input) {
        boolean isItGreaterThan = true;
        if(this.getMyCube() <= myCube_Weight_input.getMyCube()) {
            isItGreaterThan = false;
        }

        if(this.getMyWeight() <= myCube_Weight_input.getMyWeight()) {
            isItGreaterThan = false;
        }

        return isItGreaterThan;

    }

    public Double getMyCube() {
        return myCube;
    }

    public Double getMyWeight() {
        return myWeight;
    }

    public int compare(Cube_Weight o1, Cube_Weight o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(Cube_Weight obj) {

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

    public int compareTo(Cube_Weight obj) {

        return this.toString().compareTo(obj.toString());
    }




}
