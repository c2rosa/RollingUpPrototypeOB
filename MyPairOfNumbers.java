/**
 * Created by rosa.charles on 10/3/2016.
 */
public class MyPairOfNumbers {

    private Double A = null;
    private Double B = null;

    public MyPairOfNumbers(double A_input, double B_input) {
        this.A = A_input;
        this.B = B_input;
    }

    public void run() {
        if(this.B.doubleValue() == 0.0) {
            System.out.println("A="+this.A);
        } else {
            if(this.A.doubleValue()<this.B.doubleValue()) {
                this.B = this.B-this.A;
                this.run();
            } else {
                double temp = this.A;
                this.A = this.B;
                this.B = temp;

                this.B = this.B-this.A;
                this.run();
            }
        }
    }
}
