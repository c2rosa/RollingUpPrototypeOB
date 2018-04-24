/**
 * Created by rosa.charles on 9/29/14.
 */
public class HistoricalDouble_ModelDouble implements java.util.Comparator<HistoricalDouble_ModelDouble>, java.lang.Comparable<HistoricalDouble_ModelDouble> {

    Double myHistoricalDouble = null;
    Double myModelDouble = null;

    public HistoricalDouble_ModelDouble(Double myHistoricalDouble_input, Double myModelDouble_input) {
        if(myHistoricalDouble_input == null) {
            this.myHistoricalDouble = new Double(0.0);
        } else {
            this.myHistoricalDouble = new Double(myHistoricalDouble_input.doubleValue());
        }

        if(myModelDouble_input == null) {
            this.myModelDouble = new Double(0.0);
        } else {
            this.myModelDouble = new Double(myModelDouble_input.doubleValue());
        }
    }

    public Double getMyHistoricalDouble() {
        return myHistoricalDouble;
    }

    public Double getMyModelDouble() {
        return myModelDouble;
    }

    public HistoricalDouble_ModelDouble(HistoricalDouble_ModelDouble myHistoricalDouble_ModelDouble_input) {
        this(myHistoricalDouble_ModelDouble_input.getMyHistoricalDouble(), myHistoricalDouble_ModelDouble_input.getMyModelDouble());
    }

    public void addHistoricalDouble_ModelDouble(HistoricalDouble_ModelDouble myHistoricalDouble_ModelDouble_new) {
        this.addHistoricalDouble_ModelDouble(myHistoricalDouble_ModelDouble_new.getMyHistoricalDouble(), myHistoricalDouble_ModelDouble_new.getMyModelDouble());
    }

    public void addHistoricalDouble_ModelDouble(Double myHistoricalDouble_input, Double myModelDouble_input) {
        if(myHistoricalDouble_input != null) {
            this.myHistoricalDouble += new Double(myHistoricalDouble_input.doubleValue());
        }

        if(myModelDouble_input != null) {
            this.myModelDouble += new Double(myModelDouble_input.doubleValue());
        }

    }

    public int compare(HistoricalDouble_ModelDouble o1, HistoricalDouble_ModelDouble o2) {
        return o1.compareTo(o2);
    }

    public boolean equals(HistoricalDouble_ModelDouble obj) {

        if(!this.toString().equals(obj.toString())) {
            return false;
        }

        return true;

    }

    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getMyHistoricalDouble()+",");
        buffer.append(this.getMyModelDouble());

        return buffer.toString();
    }

    public int hashCode() {

        int myFinalValue = (this.toString()).hashCode();

        return myFinalValue;

    }

    public int compareTo(HistoricalDouble_ModelDouble obj) {

        return this.toString().compareTo(obj.toString());
    }




}
