/**
 * Created by rosa.charles on 10/15/15.
 */
public class HSSTrailerInformation {

    private int numHSSTrailer = 0;
    private int numHSSWithGoodCMSMatch = 0;
    private int numHSSWithAnyCMSMatch = 0;

    public HSSTrailerInformation(
            int numHSSTrailer_input,
            int numHSSWithGoodCMSMatch_input,
            int numHSSWithAnyCMSMatch_input
    ) {
        this.numHSSTrailer = numHSSTrailer_input;
        this.numHSSWithGoodCMSMatch = numHSSWithGoodCMSMatch_input;
        this.numHSSWithAnyCMSMatch = numHSSWithAnyCMSMatch_input;
    }

    public int getNumHSSTrailer() {
        return numHSSTrailer;
    }

    public int getNumHSSWithGoodCMSMatch() {
        return numHSSWithGoodCMSMatch;
    }

    public int getNumHSSWithNonGoodCMSMatch() {
        return numHSSTrailer-numHSSWithGoodCMSMatch;
    }


    public int getNumHSSWithAnyCMSMatch() {
        return numHSSWithAnyCMSMatch;
    }

    public int getNumHSSWithNotAnyCMSMatch() {
        return numHSSTrailer-numHSSWithAnyCMSMatch;
    }



}
