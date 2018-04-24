import com.con_way.network.model.Facility;
import com.con_way.network.model.LoadLeg;
import com.con_way.network.model.LoadPath;

/**
 * Created by rosa.charles on 9/13/2017.
 */
public class RepointToBypassMissingLoadLeg {

    private LoadPath myLoadPath_default = null;
    private Facility myFacility_origin = null;
    private com.con_way.network.model.LoadLeg myLoadLegRootedAt_myFacility_origin = null;
    private com.con_way.network.model.LoadPath loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin = null;

    public RepointToBypassMissingLoadLeg(
            LoadPath myLoadPath_default_input,
            Facility myFacility_origin_input,
            com.con_way.network.model.LoadLeg myLoadLegRootedAt_myFacility_origin_input,
            com.con_way.network.model.LoadPath loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin_input
    ) {
        this.myLoadPath_default = myLoadPath_default_input;
        this.myFacility_origin = myFacility_origin_input;
        this.myLoadLegRootedAt_myFacility_origin = myLoadLegRootedAt_myFacility_origin_input;
        this.loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin = loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin_input;
    }

    public LoadPath getMyLoadPath_default() {
        return myLoadPath_default;
    }

    public Facility getMyFacility_origin() {
        return myFacility_origin;
    }

    public LoadLeg getMyLoadLegRootedAt_myFacility_origin() {
        return myLoadLegRootedAt_myFacility_origin;
    }

    public LoadPath getLoadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin() {
        return loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin;
    }

    public int getCycle() {
        int myCycleToReturn=0;

        for(LoadLeg myLoadLeg_On_default_path : myLoadPath_default.getLegs()) {
            if(myLoadLeg_On_default_path.getOrig().equals(myFacility_origin)) {
                break;
            }
            myCycleToReturn += myLoadLeg_On_default_path.getCycle();
        }

        myCycleToReturn += myLoadLegRootedAt_myFacility_origin.getCycle();

        for(LoadLeg myLoadLeg_On_remaining_path_to_dest : loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin.getLegs()) {
            myCycleToReturn += myLoadLeg_On_remaining_path_to_dest.getCycle();
        }

        return myCycleToReturn;

    }

    public int getDistance() {
        int myDistanceToReturn=0;

        for(LoadLeg myLoadLeg_On_default_path : myLoadPath_default.getLegs()) {
            if(myLoadLeg_On_default_path.getOrig().equals(myFacility_origin)) {
                break;
            }
            myDistanceToReturn += myLoadLeg_On_default_path.getDistance();
        }

        myDistanceToReturn += myLoadLegRootedAt_myFacility_origin.getDistance();

        for(LoadLeg myLoadLeg_On_remaining_path_to_dest : loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin.getLegs()) {
            myDistanceToReturn += myLoadLeg_On_remaining_path_to_dest.getDistance();
        }

        return myDistanceToReturn;

    }

    public String getLoadPath() {
        StringBuffer returnStringBuffer=new StringBuffer();

        for(LoadLeg myLoadLeg_On_default_path : myLoadPath_default.getLegs()) {
            if(myLoadLeg_On_default_path.getOrig().equals(myFacility_origin)) {
                break;
            }
            returnStringBuffer.append(myLoadLeg_On_default_path+",");
        }

        returnStringBuffer.append(myLoadLegRootedAt_myFacility_origin+",");

        for(LoadLeg myLoadLeg_On_remaining_path_to_dest : loadPath_default_from_myDest_myLoadLegRootedAt_myFacility_origin.getLegs()) {
            returnStringBuffer.append(myLoadLeg_On_remaining_path_to_dest+",");
        }

        return returnStringBuffer.toString();

    }

}
