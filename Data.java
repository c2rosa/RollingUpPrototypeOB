import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by rosa.charles on 9/11/2017.
 */
public class Data {

    public static final Logger log = Logger.getLogger(Data.class);


    public Map<String, OrigLocDestLoc> myMapOfOrigLocDestLocByString = null;


    public Data(
    ) {

    }

    public OrigLocDestLoc getOrigLocDestLoc(String myOriginLocation_input, String myDestinationLocation_input, boolean storeIfNotYetThere) throws Exception {

        if(this.myMapOfOrigLocDestLocByString == null) {
            this.myMapOfOrigLocDestLocByString = new TreeMap<String, OrigLocDestLoc>();
        }

        OrigLocDestLoc myOrigLocDestLoc = this.myMapOfOrigLocDestLocByString.get(OrigLocDestLoc.getUniqueIdentifier(
                myOriginLocation_input, myDestinationLocation_input));
        if(myOrigLocDestLoc == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            this.storeOrigLocDestLoc(
                    myOriginLocation_input, myDestinationLocation_input);
            myOrigLocDestLoc = this.myMapOfOrigLocDestLocByString.get(OrigLocDestLoc.getUniqueIdentifier(
                    myOriginLocation_input, myDestinationLocation_input));
        }
        return myOrigLocDestLoc;
    }

    public  void storeOrigLocDestLoc(String myOriginLocation_input, String myDestinationLocation_input) throws Exception {

        if(this.myMapOfOrigLocDestLocByString == null) {
            this.myMapOfOrigLocDestLocByString = new TreeMap<String, OrigLocDestLoc>();
        }

        OrigLocDestLoc myOrigLocDestLoc_existing = this.myMapOfOrigLocDestLocByString.get(OrigLocDestLoc.getUniqueIdentifier(
                myOriginLocation_input, myDestinationLocation_input));
        if(myOrigLocDestLoc_existing == null) {
            OrigLocDestLoc myOrigLocDestLoc = new OrigLocDestLoc(
                    this,
                    myOriginLocation_input, myDestinationLocation_input);
            this.myMapOfOrigLocDestLocByString.put(myOrigLocDestLoc.toString(), myOrigLocDestLoc);

        }
    }


}
