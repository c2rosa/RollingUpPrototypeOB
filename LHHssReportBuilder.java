import com.con_way.network.io.NetworkBuilder;
import com.con_way.network.model.*;
import com.con_way.network.model.LoadLeg;
import com.con_way.network.sl.api.NetworkAPI;

import CharlieNetwork.*;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rosa.charles on 7/22/14.
 */
public class LHHssReportBuilder {

    com.con_way.network.model.Network network = null;
    NetworkAPI api = null;

    public static final String FILE_SIC ="lho_sic.csv";
    public static final String FILE_LP ="lho_flow_lp.csv";
    public static final String FILE_MP ="lho_flow_mp.csv";

    public static int pounds_per_pup = 22150;
    public static double cube_per_pup = 100.0;

    public static double epsilon = 0.00001;

    public static String OB = "O";
    public static String FAC = "F";
    public static String IB = "I";
    public static String Day = "D";

    public static String L = "L";
    public static String S = "S";
    public static String M = "M";

    public static String PST = "PST";
    public static String CST = "CST";

    public static String CubedOutAndWeighedOut = "CubedOutAndWeighedOut";
    public static String CubedOut = "CubedOut";
    public static String WeighedOut = "WeighedOut";
    public static String Total = "Total";

    public static String TractorCountThroughOMT=new String("TractorCountThroughOMT");
    public static String LoadedPupCountThroughOMT_OV=new String("LoadedPupCountThroughOMT_OV");
    public static String LoadedPupCountThroughOMT_VD=new String("LoadedPupCountThroughOMT_VD");
    public static String LoadedPupCountThroughOMT_OD=new String("LoadedPupCountThroughOMT_OD");

    public static String totalTractorCount = new String("totalTractorCount");
    public static String totalCube_OV = new String("totalCube_OV");
    public static String totalCube_VD = new String("totalCube_VD");
    public static String totalCube_OD = new String("totalCube_OD");
    public static String totalWeight_OV = new String("totalWeight_OV");
    public static String totalWeight_VD = new String("totalWeight_VD");
    public static String totalWeight_OD = new String("totalWeight_OD");
    public static String ladenTrailer_OV = new String("ladenTrailer_OV");
    public static String ladenTrailer_VD = new String("ladenTrailer_VD");
    public static String ladenTrailer_OD = new String("ladenTrailer_OD");
    public static String emptyTrailer_OV = new String("emptyTrailer_OV");
    public static String emptyTrailer_VD = new String("emptyTrailer_VD");
    public static String emptyTrailer_OD = new String("emptyTrailer_OD");

    public static String TU=new String("TU");
    public static String LoadedPupCount=new String("LoadedPupCount");
    public static String EmptyPupCount=new String("EmptyPupCount");
    public static String SU=new String("SU");
    public static String LF=new String("LF");
    public static String EmptyPercent=new String("EmptyPercent");
    public static String TotalMiles=new String("TotalMiles");
    public static String TotalCost=new String("TotalCost");

    public static String Loaded_HSS=new String("Loaded_HSS");
    public static String Scheduled_HSS=new String("Scheduled_HSS");
    public static String Unscheduled_HSS=new String("Unscheduled_HSS");
    public static String Supp_HSS_Cancels=new String("Supp_HSS_Cancels");
    public static String Excl_HSS_Cancels=new String("Excl_HSS_Cancels");
    public static String Supp_HSS_Adds=new String("Supp_HSS_Adds");
    public static String Excl_HSS_Adds=new String("Excl_HSS_Adds");
    public static String HSS_TU=new String("HSS_TU");
    public static String HSS_LF=new String("HSS_LF");
    public static String HSS_Miles=new String("HSS_Miles");
    public static String HSS_Miles_prorated=new String("HSS_Miles_prorated");
    public static String HSS_Cost=new String("HSS_Cost");
    public static String HSS_Cost_prorated=new String("HSS_Cost_prorated");

    PrintWriter logFile;
    FileOutputStream log;
    String header="";

    PrintWriter logFile_viaSummary;
    FileOutputStream log_viaSummary;

    Map<OrigLocationViaLocationDestLocation, HistoricalDouble_ModelDouble> myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation = null;

    Map<String, OBLocMoveToLocFAC> myMapOfOBLocMoveToLocFACByString = null;

    Map<String, Trailer> myMapOfTrailerByString = null;

    Map<String, OrigLocationViaLocationDestLocation> myMapOfOrigLocationViaLocationDestLocationByString = null;

    Map<String, CharlieNetwork.O_OShift_Dest_Service__isEarly_cube_weight> myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString = null;
    Map<String, CharlieNetwork.LoadLeg> myMapOfLoadLegByString = null;

    Map<String, ModelRunID_Sic_excessDriverCounts> myMapOfModelRunID_Sic_excessDriverCountsByString = null;
    Map<String, ModelRunID_Sic_driverUsageCounts> myMapOfModelRunID_Sic_driverUsageCountsByString = null;

    public  CharlieNetwork.O_OShift_Dest_Service__isEarly_cube_weight getO_OShift_Dest_Service__isEarly_cube_weight(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            Boolean isEarly_input,
            Integer serviceLevel_input,
            Integer certificationLevel_input,
            boolean storeIfNotYetThere
    ) throws Exception {

        if(this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString == null) {
            this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString = new HashMap<String, O_OShift_Dest_Service__isEarly_cube_weight>();
        }

        O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight = this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString.get(
                O_OShift_Dest_Service__isEarly_cube_weight.getUniqueIdentifier(
                        originLocation_input,
                        originShift_input,
                        destLocation_input,
                        destShift_input,
                        isEarly_input,
                        serviceLevel_input,
                        certificationLevel_input
                ));
        if(myO_OShift_Dest_Service__isEarly_cube_weight == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            // Store the Location_Location_ADirectional_LocationAndVia
            this.storeO_OShift_Dest_Service__isEarly_cube_weight(
                    originLocation_input,
                    originShift_input,
                    destLocation_input,
                    destShift_input,
                    isEarly_input,
                    serviceLevel_input,
                    certificationLevel_input
            );
            myO_OShift_Dest_Service__isEarly_cube_weight = this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString.get(O_OShift_Dest_Service__isEarly_cube_weight.getUniqueIdentifier(
                    originLocation_input,
                    originShift_input,
                    destLocation_input,
                    destShift_input,
                    isEarly_input,
                    serviceLevel_input,
                    certificationLevel_input
            ));
        }
        return myO_OShift_Dest_Service__isEarly_cube_weight;
    }

    public  void storeO_OShift_Dest_Service__isEarly_cube_weight(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            Boolean isEarly_input,
            Integer serviceLevel_input,
            Integer certificationLevel_input
    ) throws Exception {

        if(this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString == null) {
            this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString = new HashMap<String, O_OShift_Dest_Service__isEarly_cube_weight>();
        }

        O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight_existing = this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString.get(O_OShift_Dest_Service__isEarly_cube_weight.getUniqueIdentifier(
                originLocation_input,
                originShift_input,
                destLocation_input,
                destShift_input,
                isEarly_input,
                serviceLevel_input,
                certificationLevel_input
        ));
        if(myO_OShift_Dest_Service__isEarly_cube_weight_existing == null) {
            O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight = new O_OShift_Dest_Service__isEarly_cube_weight(
                    originLocation_input,
                    originShift_input,
                    destLocation_input,
                    destShift_input,
                    isEarly_input,
                    serviceLevel_input,
                    certificationLevel_input
            );
            this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString.put(myO_OShift_Dest_Service__isEarly_cube_weight.toString(), myO_OShift_Dest_Service__isEarly_cube_weight);

        }
    }

    public  CharlieNetwork.LoadLeg getLoadLeg(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            String myMode_input,
            boolean storeIfNotYetThere
    ) throws Exception {

        if(this.myMapOfLoadLegByString == null) {
            this.myMapOfLoadLegByString = new HashMap<String, CharlieNetwork.LoadLeg>();
        }

        CharlieNetwork.LoadLeg myLoadLeg = this.myMapOfLoadLegByString.get(
                CharlieNetwork.LoadLeg.getUniqueIdentifier(
                        originLocation_input,
                        originShift_input,
                        destLocation_input,
                        destShift_input,
                        myMode_input
                ));
        if(myLoadLeg == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            // Store the Location_Location_ADirectional_LocationAndVia
            this.storeLoadLeg(
                    originLocation_input,
                    originShift_input,
                    destLocation_input,
                    destShift_input,
                    myMode_input
            );
            myLoadLeg = this.myMapOfLoadLegByString.get(CharlieNetwork.LoadLeg.getUniqueIdentifier(
                    originLocation_input,
                    originShift_input,
                    destLocation_input,
                    destShift_input,
                    myMode_input
            ));
        }
        return myLoadLeg;
    }

    public  void storeLoadLeg(
            String originLocation_input,
            String originShift_input,
            String destLocation_input,
            String destShift_input,
            String myMode_input
    ) throws Exception {

        if(this.myMapOfLoadLegByString == null) {
            this.myMapOfLoadLegByString = new HashMap<String, CharlieNetwork.LoadLeg>();
        }

        CharlieNetwork.LoadLeg myLoadLeg_existing = this.myMapOfLoadLegByString.get(CharlieNetwork.LoadLeg.getUniqueIdentifier(
                originLocation_input,
                originShift_input,
                destLocation_input,
                destShift_input,
                myMode_input
        ));
        if(myLoadLeg_existing == null) {
            CharlieNetwork.LoadLeg myLoadLeg = new CharlieNetwork.LoadLeg(
                    originLocation_input,
                    originShift_input,
                    destLocation_input,
                    destShift_input,
                    myMode_input
            );
            this.myMapOfLoadLegByString.put(myLoadLeg.toString(), myLoadLeg);

        }
    }

    public  OrigLocationViaLocationDestLocation getOrigLocationViaLocationDestLocation(String myOBLocation_input,
                                                                                       String myViaLocation_input,
                                                                                       String myFACLocation_input,
                                                                                       boolean storeIfNotYetThere) throws Exception {

        if(this.myMapOfOrigLocationViaLocationDestLocationByString == null) {
            this.myMapOfOrigLocationViaLocationDestLocationByString = new HashMap<String, OrigLocationViaLocationDestLocation>();
        }

        OrigLocationViaLocationDestLocation myOrigLocationViaLocationDestLocation = this.myMapOfOrigLocationViaLocationDestLocationByString.get(OrigLocationViaLocationDestLocation.getUniqueIdentifier(myOBLocation_input,
                                                                                                                                                                                                        myViaLocation_input,
                                                                                                                                                                                                        myFACLocation_input));
        if(myOrigLocationViaLocationDestLocation == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            // Store the Location_Location_ADirectional_LocationAndVia
            this.storeOrigLocationViaLocationDestLocation(myOBLocation_input,
                    myViaLocation_input,
                    myFACLocation_input);
            myOrigLocationViaLocationDestLocation = this.myMapOfOrigLocationViaLocationDestLocationByString.get(OrigLocationViaLocationDestLocation.getUniqueIdentifier(
                    myOBLocation_input,
                    myViaLocation_input,
                    myFACLocation_input));
        }
        return myOrigLocationViaLocationDestLocation;
    }

    public  void storeOrigLocationViaLocationDestLocation(String myOBLocation_input,
                                                          String myViaLocation_input,
                                                          String myFACLocation_input) throws Exception {

        if(this.myMapOfOrigLocationViaLocationDestLocationByString == null) {
            this.myMapOfOrigLocationViaLocationDestLocationByString = new HashMap<String, OrigLocationViaLocationDestLocation>();
        }

        OrigLocationViaLocationDestLocation myOrigLocationViaLocationDestLocation_existing = this.myMapOfOrigLocationViaLocationDestLocationByString.get(OrigLocationViaLocationDestLocation.getUniqueIdentifier(
                myOBLocation_input,
                myViaLocation_input,
                myFACLocation_input));
        if(myOrigLocationViaLocationDestLocation_existing == null) {
            OrigLocationViaLocationDestLocation myOrigLocationViaLocationDestLocation = new OrigLocationViaLocationDestLocation(
                    myOBLocation_input,
                    myViaLocation_input,
                    myFACLocation_input);
            this.myMapOfOrigLocationViaLocationDestLocationByString.put(myOrigLocationViaLocationDestLocation.toString(), myOrigLocationViaLocationDestLocation);

        }
    }

    public  ModelRunID_Sic_excessDriverCounts getModelRunID_Sic_excessDriverCounts(    Integer myModelRunID_input,
                                                                                       String mySic_input,
                                                                                       boolean storeIfNotYetThere) throws Exception {

        if(this.myMapOfModelRunID_Sic_excessDriverCountsByString == null) {
            this.myMapOfModelRunID_Sic_excessDriverCountsByString = new HashMap<String, ModelRunID_Sic_excessDriverCounts>();
        }

        ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts = this.myMapOfModelRunID_Sic_excessDriverCountsByString.get(ModelRunID_Sic_excessDriverCounts.getUniqueIdentifier(
                myModelRunID_input,
                mySic_input));
        if(myModelRunID_Sic_excessDriverCounts == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            // Store the Location_Location_ADirectional_LocationAndVia
            this.storeModelRunID_Sic_excessDriverCounts(
                    myModelRunID_input,
                    mySic_input);
            myModelRunID_Sic_excessDriverCounts = this.myMapOfModelRunID_Sic_excessDriverCountsByString.get(ModelRunID_Sic_excessDriverCounts.getUniqueIdentifier(
                    myModelRunID_input,
                    mySic_input));
        }
        return myModelRunID_Sic_excessDriverCounts;
    }

    public  void storeModelRunID_Sic_excessDriverCounts(Integer myModelRunID_input,
                                                        String mySic_input) throws Exception {

        if(this.myMapOfModelRunID_Sic_excessDriverCountsByString == null) {
            this.myMapOfModelRunID_Sic_excessDriverCountsByString = new HashMap<String, ModelRunID_Sic_excessDriverCounts>();
        }

        ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts_existing = this.myMapOfModelRunID_Sic_excessDriverCountsByString.get(ModelRunID_Sic_excessDriverCounts.getUniqueIdentifier(
                myModelRunID_input,
                mySic_input));
        if(myModelRunID_Sic_excessDriverCounts_existing == null) {
            ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts = new ModelRunID_Sic_excessDriverCounts(
                    myModelRunID_input,
                    mySic_input);
            this.myMapOfModelRunID_Sic_excessDriverCountsByString.put(myModelRunID_Sic_excessDriverCounts.toString(), myModelRunID_Sic_excessDriverCounts);

        }
    }

    public  ModelRunID_Sic_driverUsageCounts getModelRunID_Sic_driverUsageCounts(    Integer myModelRunID_input,
                                                                                       String mySic_input,
                                                                                       boolean storeIfNotYetThere) throws Exception {

        if(this.myMapOfModelRunID_Sic_driverUsageCountsByString == null) {
            this.myMapOfModelRunID_Sic_driverUsageCountsByString = new HashMap<String, ModelRunID_Sic_driverUsageCounts>();
        }

        ModelRunID_Sic_driverUsageCounts myModelRunID_Sic_driverUsageCounts = this.myMapOfModelRunID_Sic_driverUsageCountsByString.get(ModelRunID_Sic_driverUsageCounts.getUniqueIdentifier(
                myModelRunID_input,
                mySic_input));
        if(myModelRunID_Sic_driverUsageCounts == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            // Store the Location_Location_ADirectional_LocationAndVia
            this.storeModelRunID_Sic_driverUsageCounts(
                    myModelRunID_input,
                    mySic_input);
            myModelRunID_Sic_driverUsageCounts = this.myMapOfModelRunID_Sic_driverUsageCountsByString.get(ModelRunID_Sic_driverUsageCounts.getUniqueIdentifier(
                    myModelRunID_input,
                    mySic_input));
        }
        return myModelRunID_Sic_driverUsageCounts;
    }

    public  void storeModelRunID_Sic_driverUsageCounts(Integer myModelRunID_input,
                                                        String mySic_input) throws Exception {

        if(this.myMapOfModelRunID_Sic_driverUsageCountsByString == null) {
            this.myMapOfModelRunID_Sic_driverUsageCountsByString = new HashMap<String, ModelRunID_Sic_driverUsageCounts>();
        }

        ModelRunID_Sic_driverUsageCounts myModelRunID_Sic_driverUsageCounts_existing = this.myMapOfModelRunID_Sic_driverUsageCountsByString.get(ModelRunID_Sic_driverUsageCounts.getUniqueIdentifier(
                myModelRunID_input,
                mySic_input));
        if(myModelRunID_Sic_driverUsageCounts_existing == null) {
            ModelRunID_Sic_driverUsageCounts myModelRunID_Sic_driverUsageCounts = new ModelRunID_Sic_driverUsageCounts(
                    myModelRunID_input,
                    mySic_input);
            this.myMapOfModelRunID_Sic_driverUsageCountsByString.put(myModelRunID_Sic_driverUsageCounts.toString(), myModelRunID_Sic_driverUsageCounts);

        }
    }


    public  OBLocMoveToLocFAC getOBLocMoveToLocFAC(String myOBLocation_input,
                                                   String myOBShift_input,
                                                   String myMoveToLocation_input,
                                                   String myMoveToShift_input,
                                                   String myFACLocation_input,
                                                   boolean storeIfNotYetThere) throws Exception {

        if(this.myMapOfOBLocMoveToLocFACByString == null) {
            this.myMapOfOBLocMoveToLocFACByString = new HashMap<String, OBLocMoveToLocFAC>();
        }

        OBLocMoveToLocFAC myOBLocMoveToLocFAC = this.myMapOfOBLocMoveToLocFACByString.get(OBLocMoveToLocFAC.getUniqueIdentifier(
                myOBLocation_input,
                myOBShift_input,
                myMoveToLocation_input,
                myMoveToShift_input,
                myFACLocation_input));
        if(myOBLocMoveToLocFAC == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            // Store the Location_Location_ADirectional_LocationAndVia
            this.storeOBLocMoveToLocFAC(
                    myOBLocation_input,
                    myOBShift_input,
                    myMoveToLocation_input,
                    myMoveToShift_input,
                    myFACLocation_input);
            myOBLocMoveToLocFAC = this.myMapOfOBLocMoveToLocFACByString.get(OBLocMoveToLocFAC.getUniqueIdentifier(
                    myOBLocation_input,
                    myOBShift_input,
                    myMoveToLocation_input,
                    myMoveToShift_input,
                    myFACLocation_input));
        }
        return myOBLocMoveToLocFAC;
    }

    public  void storeOBLocMoveToLocFAC(String myOBLocation_input,
                                        String myOBShift_input,
                                        String myMoveToLocation_input,
                                        String myMoveToShift_input,
                                      String myFACLocation_input) throws Exception {

        if(this.myMapOfOBLocMoveToLocFACByString == null) {
            this.myMapOfOBLocMoveToLocFACByString = new HashMap<String, OBLocMoveToLocFAC>();
        }

        OBLocMoveToLocFAC myOBLocMoveToLocFAC_existing = this.myMapOfOBLocMoveToLocFACByString.get(OBLocMoveToLocFAC.getUniqueIdentifier(
                myOBLocation_input,
                myOBShift_input,
                myMoveToLocation_input,
                myMoveToShift_input,
                myFACLocation_input));
        if(myOBLocMoveToLocFAC_existing == null) {
            OBLocMoveToLocFAC myOBLocMoveToLocFAC = new OBLocMoveToLocFAC(
                    myOBLocation_input,
                    myOBShift_input,
                    myMoveToLocation_input,
                    myMoveToShift_input,
                    myFACLocation_input);
            this.myMapOfOBLocMoveToLocFACByString.put(myOBLocMoveToLocFAC.toString(), myOBLocMoveToLocFAC);

        }
    }

    public  Trailer getTrailer(Integer myID_input, boolean storeIfNotYetThere) throws Exception {

        if(this.myMapOfTrailerByString == null) {
            this.myMapOfTrailerByString = new HashMap<String, Trailer>();
        }

        Trailer myTrailer = this.myMapOfTrailerByString.get(Trailer.getUniqueIdentifier(
                myID_input));
        if(myTrailer == null) {
            if(!storeIfNotYetThere) {
                return null;
            }
            // Store the Location_Location_ADirectional_LocationAndVia
            this.storeTrailer(
                    myID_input);
            myTrailer = this.myMapOfTrailerByString.get(Trailer.getUniqueIdentifier(
                    myID_input));
        }
        return myTrailer;
    }

    public  void storeTrailer(Integer myID_input) throws Exception {

        if(this.myMapOfTrailerByString == null) {
            this.myMapOfTrailerByString = new HashMap<String, Trailer>();
        }

        Trailer myTrailer_existing = this.myMapOfTrailerByString.get(Trailer.getUniqueIdentifier(
                myID_input));
        if(myTrailer_existing == null) {
            Trailer myTrailer = new Trailer(
                    myID_input);
            this.myMapOfTrailerByString.put(myTrailer.toString(), myTrailer);

        }
    }

    private Map<String, String> myMapOfTypeBySic = null;
    public Map<String, String> myMapOfRegionBySic = null;
    public Map<String, String> myMapOfDarkLocationIndBySic = null;
    public Map<String, String> myMapOfHasFACShiftIndBySic = null;
    public Map<String, String> myMapOfHostSicBySic = null;

    public SortedMap<String, String> myMapOfSubDirectoryOffsetByCubeExpansionScenario = null;
    public SortedMap<String, Double> myMapOfPupCubeByCubeExpansionScenario = null;
    public SortedMap<String, Double> myMapOfVanCubeByCubeExpansionScenario = null;

    public SortedMap<String, String> myMapOfSubDirectoryOffsetByDayLineHaulLimitsScenario = null;
    public SortedMap<String, Double> myMapOfPupCubeByDayLineHaulLimitsScenario = null;
    public SortedMap<String, Double> myMapOfVanCubeByDayLineHaulLimitsScenario = null;

    public static Map<LBDate_UBDate, Double> myMapOfCubicFeetPerHundredCube = null;

    public LHHssReportBuilder() throws Exception {

        this.myMapOfSubDirectoryOffsetByCubeExpansionScenario = new TreeMap<String, String>();
        this.myMapOfSubDirectoryOffsetByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_0, "\\in\\Output\\OptimOutput_0\\");
        this.myMapOfSubDirectoryOffsetByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_025, "\\in\\Output\\OptimOutput_025\\");
        this.myMapOfSubDirectoryOffsetByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_05, "\\in\\Output\\OptimOutput_05\\");
        this.myMapOfSubDirectoryOffsetByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_1, "\\in\\Output\\OptimOutput_1\\");

        this.myMapOfPupCubeByCubeExpansionScenario = new TreeMap<String, Double>();
        this.myMapOfPupCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_0, 100.0);
        this.myMapOfPupCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_025, 102.5);
        this.myMapOfPupCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_05, 105.0);
        this.myMapOfPupCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_1, 110.0);

        this.myMapOfVanCubeByCubeExpansionScenario = new TreeMap<String, Double>();
        this.myMapOfVanCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_0, 205.0);
        this.myMapOfVanCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_025, 210.125);
        this.myMapOfVanCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_05, 215.25);
        this.myMapOfVanCubeByCubeExpansionScenario.put(Constant.CubeExpansionScenarios.base0_1, 225.5);

        this.myMapOfSubDirectoryOffsetByDayLineHaulLimitsScenario = new TreeMap<String, String>();
        this.myMapOfSubDirectoryOffsetByDayLineHaulLimitsScenario.put(Constant.DayLineHaulLimitationsScenarios.limited, "\\in\\Output\\OptimOutput_limitedDayLine\\");
        this.myMapOfSubDirectoryOffsetByDayLineHaulLimitsScenario.put(Constant.DayLineHaulLimitationsScenarios.unlimited, "\\in\\Output\\OptimOutput_unlimitedDayLine\\");

        this.myMapOfPupCubeByDayLineHaulLimitsScenario = new TreeMap<String, Double>();
        this.myMapOfPupCubeByDayLineHaulLimitsScenario.put(Constant.DayLineHaulLimitationsScenarios.limited, 100.0);
        this.myMapOfPupCubeByDayLineHaulLimitsScenario.put(Constant.DayLineHaulLimitationsScenarios.unlimited, 100.0);

        this.myMapOfVanCubeByDayLineHaulLimitsScenario = new TreeMap<String, Double>();
        this.myMapOfVanCubeByDayLineHaulLimitsScenario.put(Constant.DayLineHaulLimitationsScenarios.limited, 205.0);
        this.myMapOfVanCubeByDayLineHaulLimitsScenario.put(Constant.DayLineHaulLimitationsScenarios.unlimited, 205.0);

        LHHssReportBuilder.myMapOfCubicFeetPerHundredCube = new HashMap<LBDate_UBDate, Double>();

        {

            Date date_LB = new SimpleDateFormat("yyyy-MM-dd").parse("2001-10-09");
            Date date_UB = new SimpleDateFormat("yyyy-MM-dd").parse("2015-12-06");

            LBDate_UBDate myLBDate_UBDate = new LBDate_UBDate(date_LB, date_UB);

            LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.put(myLBDate_UBDate, 1156.8);

        }

        {

            Date date_LB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2015-12-07");
            Date date_UB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-17");

            LBDate_UBDate myLBDate_UBDate1 = new LBDate_UBDate(date_LB_1, date_UB_1);

            LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.put(myLBDate_UBDate1, 1166.4);

        }

        {

            Date date_LB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-18");
            Date date_UB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-10-08");

            LBDate_UBDate myLBDate_UBDate1 = new LBDate_UBDate(date_LB_1, date_UB_1);

            LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.put(myLBDate_UBDate1, 1171.2);

        }

        {

            Date date_LB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-10-09");
            Date date_UB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-07");

            LBDate_UBDate myLBDate_UBDate1 = new LBDate_UBDate(date_LB_1, date_UB_1);

            LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.put(myLBDate_UBDate1, 1179.9);

        }

        {

            Date date_LB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-08");
            Date date_UB_1 = new SimpleDateFormat("yyyy-MM-dd").parse("2999-12-31");

            LBDate_UBDate myLBDate_UBDate1 = new LBDate_UBDate(date_LB_1, date_UB_1);

            LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.put(myLBDate_UBDate1, 1209.4);

        }

    }

    public static void main(String[] arg) throws Exception {
        // which subroutine to run
        String subroutineToRun = arg[0];

        LHHssReportBuilder rb = new LHHssReportBuilder();

        //rb.runLHO_summarizeViaInfo(); /* we did this analysis to see what was the impact of modifying our penalties around use of vias to be based on incremental distance and $3.00/mile for that incremental mileage */
        //rb.runLHO_moveLaneSummary();
        //rb.runLHO_coLoadedTrailers();
        //rb.runLHO_coLoadedTrailers_findLHVansLoadedFromOBToIBOrLater();
        //rb.runLHO_RollUpTimingStatistics();
        //rb.doIntegerAdditionMultExperiment();
        //rb.runLHO_rollUpHazMatBLStatsByModelRunID();
        //rb.runLHO_LookForHazMatShipmentCountsByDesignatedHSSLane();
        //rb.rollup_RandomForestRegressorModelOutput();   // run to roll up random forest model output results.
        //rb.runLHO_NumCubedOutVersusWeightedOutPups();
        //rb.runLHO_HSSTrailers_andHowTheyMatchUp();
        //rb.runLHO_RollUpTractorCount_LadenTrailerCount_WithAndWithoutBypassThroughOMTs();
        //rb.runLHO_RollUpSolutionStatistics();
        //rb.runLHO_RollUpSolutionStatisticsByShift();
        //rb.runLHO_CalculatingSU_TU_Miles_byShift_total_forAllDaysWeeks_CST_PST_terminatingInJune();
        if(subroutineToRun.equals("calculateCostPerTrailer_ForGivenDate")) {
            String myDate = arg[1];
            Data myData = new Data();
            rb.calculateCostPerTrailer_ForGivenDate(myDate, myData);  // method to try and determine the cost per trailer by lane...and then apply this to BI report to
            // determine cost attributable to particular sics for their being over/under on trailer count relative to
            // what model wanted.
        } else if(subroutineToRun.equals("calculateTotalTractorAndTotalCubeTotalWeightByDate")) {
            String useJamieFile_string = arg[1];
            boolean useJamieFile = false;
            try {
                useJamieFile = new Boolean(useJamieFile_string);
            } catch(Exception ex) {
                System.out.println("Jamie file usage flag indeterminate");
                throw ex;
            }
            String dateString = arg[2];
            if(useJamieFile) {
                rb.calculateTotalTractorAndTotalCubeTotalWeightByDate_usingJamieFile();
            } else {
                rb.calculateTotalTractorAndTotalCubeTotalWeightByDate_usingBIReadyFiles(dateString);
            }
        } else if(subroutineToRun.equals("doCostComparisonAnalysis")) {
            String typeOfAnalysis = arg[1];

            int runID_lb = new Integer(arg[2]);
            int runID_ub = new Integer(arg[3]);

            rb.doCostComparisonAnalysis(typeOfAnalysis, runID_lb, runID_ub);
        } else if(subroutineToRun.equals("modifyShipmentFilesForAggregationModel")) {

            rb.modifyShipmentFilesForAggregationModel();

        } else if(subroutineToRun.equals("modifyConstraintFilesForAggregationModel")) {

            rb.modifyConstraintFilesForAggregationModel();

        } else if(subroutineToRun.equals("TBL_FLO_LOAD_OPTION_RDS")) {
            rb.analyzeTBL_FLO_LOAD_OPTION_RDS();
        } else if(subroutineToRun.equals("Verify_All_RuleSets_have_tariff_and_AMC_clusterIDs")) {
            rb.verify_All_RuleSets_have_tariff_and_AMC_clusterIDs();
        } else if(subroutineToRun.equals("createMovePathForAllODs")) {
            rb.createMovePathForAllODs();
        } else if(subroutineToRun.equals("solveMathProblem")) {
            rb.solveMathProblem();
        } else if(subroutineToRun.equals("findODsAffectedByMissingMoveLegs")) {
            Data myData = new Data();
            rb.findODsAffectedByMissingMoveLegs(myData);
        } else if(subroutineToRun.equals("findODsAffectedByMissingLoadLegs")) {
            Data myData = new Data();
            rb.findODsAffectedByMissingLoadLegs(myData);
        } else if(subroutineToRun.equals("findDaysOfServiceAndPresenceOfExcHSSForAllODs")) {
            rb.findDaysOfServiceAndPresenceOfExcHSSForAllODs();
        } else if(subroutineToRun.equals("readInSampleFile")) {
            rb.readInSampleFile();
        } else if(subroutineToRun.equals("DoAMCAnalysis")) {
            rb.doAMCAnalysis();
        } else if(subroutineToRun.equals("DoAnalysisOfRuleSetOutputOfAggModel")) {
            rb.doAnalysisOfRuleSetOutputOfAggModel();
        } else if(subroutineToRun.equals("consolidateMachineLearningResults")) {

            String myInputSubdirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\RevMan_python_dataAnalysis\\data\\data_AMC_Tariff_20180514_outputAndGeneratedInput\\AMC\\updatedData_optExistingModel_20180515\\";
            String myX_fileName = "XMatrix__True_283_2018_02_14.csv";
            String myY_fileName = "YVector__True_283_2018_02_14.csv";
            String myLeaf_fileName = "leafIds.csv";

            String delimiterForOutput = "\t";
            rb.consolidateMachineLearningResults(myInputSubdirectory, myX_fileName, myY_fileName, myLeaf_fileName,delimiterForOutput);
        } else if(subroutineToRun.equals("rollUpMachineLearningLeafModelResultsResults")) {

            String modelType = "AMC";
            String myInputSubdirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\RevMan_python_dataAnalysis\\data\\data_AMC_Tariff_20180514_outputAndGeneratedInput\\"+modelType+"\\updatedData_optExistingModel_20180515\\ExpAmntOfSegSpecData_pGenLeaf\\";
            String myFilePrefixToDetermineSegments = "leafOutputFile_fieldsConsidered_";
            String myLeafModel_Results_output = "LeafModel_Results_AMC.csv";

            String delimiterForOutput = "\t";
            rb.rollUpMachineLearningLeafModelResultsResults(modelType, myInputSubdirectory, myFilePrefixToDetermineSegments, myLeafModel_Results_output);
        } else {
            throw new Exception("nothing run");
        }
        //rb.runTrailerRoutingAnalysis();
        //rb.runLHO_RollUpTimingStatisticsAsFunctionOfDirectOptionsAndDirectUsed();
        //rb.runLHO_RollUpStatsOnNumberOfDiversions();
        //rb.runLHO_FindEntriesInLho_Lane_Diversions_NotInPsblDestToDivert();
        //rb.runLHO_FindEntriesInLho_Lane_Diversions_ThatMatchAndHaveMatchingPsblDestToDivertWithSameDest();
        //rb.createNew_lho_omt_lane_withDirectsInPlace();
        //rb.rollUpJamieFileResults_vias();
        //rb.rollUpExcessDriversResults();
        //rb.runLHO_trailerModificationSummary();
        //rb.rollUpDriverUsageResults();
        //rb.aggregateExcessDriversResults();
        //rb.runLHO_compare_coLoadedTrailers_Desktop_and_production();
        //rb.runLHO_LookForDiversionsIntoOBLocationsTwoAndMoreDaysOut();
        //rb.determineNumODAndNumLoadPathsByModelRunID(false);
        //rb.determineNumODAndNumLoadPathsByModelRunID(true);
        //rb.runLHO_rollupHzBLresults();
        //rb.runLHO_rollupCMSresults();
        //rb.runLHO_Origin_OShift_Dest_FreightThatIsCoLoaded();
        //rb.rollUpJamieFileForEricC();
    }

    public void runLHO_FindEntriesInLho_Lane_Diversions_NotInPsblDestToDivert() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\", "DiversionsInLaneDiversionNotInPSBLDestToDivert.csv");

        this.logFile.println("Model Run Id,DIVER_INSTR_SEQ_NBR");

        //String myDirectoryString_OBModelPhaseII = "\\\\nfs\\appdatadev\\CTS3\\OperationsResearch\\LHOptimizer\\";
        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        boolean anyIssues = false;
        for (int id = 1916; id <= 1987; id=id+1){
            System.out.println("id="+id);
            String runId = id+"";
            //String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\out\\";
            String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\OptimOutput\\";
            File df_OptimOutput = new File(dir_OptimOutput);
            if (!df_OptimOutput.exists()) continue;


            String file_lane_diversion = dir_OptimOutput+"lho_lane_diversion.csv";

            SortedSet<String> mySetOf_lane_Diversion_DIVER_INSTR_SEQ_NBR = null;
            try {
                mySetOf_lane_Diversion_DIVER_INSTR_SEQ_NBR = this.readLaneDiversionsFileToGetSetOf_DIVER_INSTR_SEQ_NBR(file_lane_diversion);
            } catch(Exception ex) {
                int temp_a = 0;
            }

            String file_psbl_dest_to_divert = dir_OptimOutput+"lho_psbl_dest_for_dvrsn.csv";

            SortedSet<String> mySetOf_lane_psbl_Dest_ToDivert_DIVER_INSTR_SEQ_NBR = null;
            try {
                mySetOf_lane_psbl_Dest_ToDivert_DIVER_INSTR_SEQ_NBR = this.readLaneDiversionsFileToGetSetOf_DIVER_INSTR_SEQ_NBR(file_psbl_dest_to_divert);
            } catch(Exception ex) {
                int temp_a = 0;
            }

            boolean isThereSomethingInLaneDiversionNotInPsblDestToDivert = false;
            for(String myDIVER_INSTR_SEQ_NBR : mySetOf_lane_Diversion_DIVER_INSTR_SEQ_NBR) {
                if(!mySetOf_lane_psbl_Dest_ToDivert_DIVER_INSTR_SEQ_NBR.contains(myDIVER_INSTR_SEQ_NBR)) {
                    isThereSomethingInLaneDiversionNotInPsblDestToDivert = true;

                    this.logFile.println(id+","+myDIVER_INSTR_SEQ_NBR);
                    System.out.println("\t"+id+","+myDIVER_INSTR_SEQ_NBR);

                    anyIssues = true;
                }
            }

            if(isThereSomethingInLaneDiversionNotInPsblDestToDivert) {
                int temp_a = 0;
            }


        }

        if(!anyIssues) {
            this.logFile.println("No Issues!!!!!!!!!!!!!!!!!!!");
        }

        closeFile();

    }

    public void runLHO_FindEntriesInLho_Lane_Diversions_ThatMatchAndHaveMatchingPsblDestToDivertWithSameDest() throws Exception {
        String[] inpNames = {};

        String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        String fileName_detailed = "Diversions_duplicateSics.csv";

        openFile(dir, fileName_detailed);

        String fileName_summary = "Diversions_duplicateSics_summary.csv";

        FileOutputStream fStream_summary = null;
        PrintWriter pWriter_summary = null;
        try{
            fStream_summary = new FileOutputStream( new File(dir+fileName_summary), false);
            pWriter_summary = new PrintWriter(fStream_summary);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        pWriter_summary.println("Model Run Id,totNumDiversions,dupSicDiversions,fractionIncidence");

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("Model Run Id" + ",");
        buffer_header.append("totNumDiversions" + ",");
        buffer_header.append("incidentCounter" + ",");
        buffer_header.append(laneDiversion.myHeader + ",");
        buffer_header.append("finalDest");
        this.logFile.println(buffer_header.toString());

        //String myDirectoryString_OBModelPhaseII = "\\\\nfs\\appdatadev\\CTS3\\OperationsResearch\\LHOptimizer\\";
        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        for (int id = 1916; id <= 1987; id=id+1){

            Map<String, Set<laneDiversion>> myMapOfSetOfLaneDiversionByStringAggregate = new HashMap<String, Set<laneDiversion>>();
            Map<String, Set<laneDiversion>> myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR = new HashMap<String, Set<laneDiversion>>();

            System.out.println("id="+id);
            String runId = id+"";
            //String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\out\\";
            String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\OptimOutput\\";
            File df_OptimOutput = new File(dir_OptimOutput);
            if (!df_OptimOutput.exists()) continue;

            String file_lane_diversion = dir_OptimOutput+"lho_lane_diversion.csv";

            try {
                this.readLaneDiversions(file_lane_diversion, myMapOfSetOfLaneDiversionByStringAggregate, myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR);
            } catch(Exception ex) {
                int temp_a = 0;
            }

            String file_psbl_dest_to_divert = dir_OptimOutput+"lho_psbl_dest_for_dvrsn.csv";

            try {
                this.readPsblDestForDvrsn(file_psbl_dest_to_divert, myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR);
            } catch(Exception ex) {
                int temp_a = 0;
            }


            // check to determine whether, for a given string aggregate, whether we have two instances
            // of laneDiversion with the same associated final dest.

            int totalNumDiversions = 0;
            for(String myDIVER_INSTR_SEQ_NBR : myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR.keySet()) {
                Set<laneDiversion> mySet = myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR.get(myDIVER_INSTR_SEQ_NBR);
                totalNumDiversions += mySet.size();
            }

            int incidentCounter=0;

            for(String myStringAggregate : myMapOfSetOfLaneDiversionByStringAggregate.keySet()) {
                Set<laneDiversion> mySetOfLaneDiversion = myMapOfSetOfLaneDiversionByStringAggregate.get(myStringAggregate);

                if(mySetOfLaneDiversion == null || mySetOfLaneDiversion.size()==1) {
                    continue;
                }

                Map<String, Set<laneDiversion>> myMapOf_SetOflaneDiversion_ByFinalDest = new HashMap<String, Set<laneDiversion>>();

                for(laneDiversion my_laneDiversion : mySetOfLaneDiversion) {
                    psblDestForDvrsn my_psblDestForDvrsn = my_laneDiversion.getMy_psblDestForDvrsn();

                    String myDest_sic_cd =my_psblDestForDvrsn.getDEST_SIC_CD();

                    Set<laneDiversion> mySetOflaneDiversion = myMapOf_SetOflaneDiversion_ByFinalDest.get(myDest_sic_cd);
                    if(mySetOflaneDiversion==null) {
                        mySetOflaneDiversion = new HashSet<laneDiversion>();
                        myMapOf_SetOflaneDiversion_ByFinalDest.put(myDest_sic_cd, mySetOflaneDiversion);
                    }

                    mySetOflaneDiversion.add(my_laneDiversion);

                }

                for(String myFinalDest : myMapOf_SetOflaneDiversion_ByFinalDest.keySet()) {
                    Set<laneDiversion> mySetOflaneDiversion = myMapOf_SetOflaneDiversion_ByFinalDest.get(myFinalDest);
                    if(mySetOflaneDiversion != null && mySetOflaneDiversion.size()>1) {

                        incidentCounter++;
                        // duplicate sic
                        System.out.println(myFinalDest);
                        for(laneDiversion mylaneDiversion : mySetOflaneDiversion) {
                            System.out.println("\t"+mylaneDiversion.getDIVER_INSTR_SEQ_NBR());

                            StringBuffer buffer = new StringBuffer();
                            buffer.append(id+",");
                            buffer.append(totalNumDiversions+",");
                            buffer.append(incidentCounter+",");
                            buffer.append(mylaneDiversion.toString()+",");
                            buffer.append(myFinalDest+",");
                            this.logFile.println(buffer.toString());

                        }
                        int temp_a = 0;

                    }
                }
            }


            double fractionIncidence = (incidentCounter+0.0)/(totalNumDiversions+LHHssReportBuilder.epsilon);
            pWriter_summary.println(id+","+totalNumDiversions+","+incidentCounter+","+fractionIncidence);

        }
        closeFile();

        pWriter_summary.close();
        fStream_summary.close();

    }

    public void run() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Documents and Settings\\raskina.olga\\My Documents\\Projects\\Linehaul\\FAC\\Validation\\HSS\\", "HssSummary8-15_9-12.csv");
        for (int id = 10886; id <= 10925; id=id+1){
            if (id ==10797) continue;
            String runId = id+"";
            String hssFile = "C:\\Documents and Settings\\raskina.olga\\My Documents\\Projects\\Linehaul\\FAC\\ProdDailyExtracts\\"+runId+"\\in\\Output\\OptimOutputHSS\\hss_summary.csv";

            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile,runId+",", false);
        }
        closeFile();
    }

    public void runList() throws Exception {
        String path = "C:\\Projects\\PROsToDiversionsAllocationOrig\\Output\\";
        openFile("C:\\Documents and Settings\\raskina.olga\\My Documents\\Projects\\Linehaul\\FAC\\PRO optimizer\\", "DivsSummaryOrig6-3_7-15.csv");

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++){
            if (listOfFiles[i].isFile()){
                files = listOfFiles[i].getName();
                if (!files.startsWith("PRO_toDiversionAllocation_AllFACs")) continue;
                String end = files.substring(33);
                end = end.replaceAll(".csv","");
                String date = end.substring(0,10);
                String area = end.substring(10);
                String pref = date+","+area+",";
                if (header.length() < 1){
                    getHeader(path+files);
                    addLine(header);
                }
                addFile(path+files, pref, false);
            }
        }
        closeFile();
    }

    public String[] readModelRunFile(String dir, String name) throws Exception {
        File f = null;
        BufferedReader br = null;
        String dbRecord = null;
        try {
            String fileName = dir+name;
            f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            int rowCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {
                rowCounter++;
                StringTokenizer st = new StringTokenizer(dbRecord, ",");

                if(rowCounter==1) {
                    // skip the header row
                    continue;
                }

                String[] val = dbRecord.split(",");
                String myModelGrp = val[1].trim();
                String myDate = val[2].trim();

                String[] myReturn = new String[2];

                myReturn[0] = myModelGrp;
                myReturn[1] = myDate;

                return myReturn;

            }

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            // if the file opened okay, make sure we close it
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    throw ioe;
                }
            } // end if
        } // end finally

        return null;

    }

    public void readSics(String dir, String name) throws Exception {

        if(this.myMapOfTypeBySic == null) {
            this.myMapOfTypeBySic = new HashMap<String, String>();
        }

        if(this.myMapOfRegionBySic == null) {
            this.myMapOfRegionBySic = new HashMap<String, String>();
        }

        if(this.myMapOfDarkLocationIndBySic == null) {
            this.myMapOfDarkLocationIndBySic = new HashMap<String, String>();
        }

        if(this.myMapOfHasFACShiftIndBySic == null) {
            this.myMapOfHasFACShiftIndBySic = new HashMap<String, String>();
        }

        if(this.myMapOfHostSicBySic == null) {
            this.myMapOfHostSicBySic = new HashMap<String, String>();
        }

        File f = null;
        BufferedReader br = null;
        String dbRecord = null;
        try {
            String fileName = dir+name;
            f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            int rowCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {
                rowCounter++;
                StringTokenizer st = new StringTokenizer(dbRecord, ",");

                if(rowCounter==1) {
                    // skip the header row
                    continue;
                }

                String[] val = dbRecord.split(",");
                String mySic = val[0].trim();
                String myType = val[1].trim();
                String myRegion = val[3].trim();
                String darkLocInd = val[10].trim();
                String hasFACShiftInd = val[11].trim();
                String myHostSic = val[7].trim();

                this.myMapOfTypeBySic.put(mySic, myType);
                this.myMapOfRegionBySic.put(mySic, myRegion);
                this.myMapOfDarkLocationIndBySic.put(mySic, darkLocInd);
                this.myMapOfHasFACShiftIndBySic.put(mySic, hasFACShiftInd);
                this.myMapOfHostSicBySic.put(mySic, myHostSic);

            }

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            // if the file opened okay, make sure we close it
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    throw ioe;
                }
            } // end if
        } // end finally

    }

    public void rollUpJamieFileForEricC() throws Exception {
        String[] inpNames = {};
        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String baseDirectory_remote = "\\\\nfs\\appdatadev\\CTS3\\OperationsResearch\\LHOptimizer\\";
        this.readSics(baseDirectory_local+"Jul21_weekly\\", "lho_sic.csv");
        this.openFile(baseDirectory_local, "file_JamiesMasterTractorTrailerMovementFile_obviaibvia_aggregation.csv");
        for (int id = 1301; id <= 1314; id=id+1){
            String runId = id+"";
            //String dir = baseDirectory_remote+runId+"\\in\\Output\\OptimOutput\\";
            String dir = baseDirectory_remote+runId+"\\out\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String jamieFile = dir+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";
            if (header.length() < 1){
                getHeader(jamieFile);
                header = "modelRunID, ModelGrp, Date, OriginType, ViaType, DestType,"+header;
                this.addLine(header);
            }
            String[] myReturnStringArray = this.readModelRunFile(baseDirectory_remote+runId+"\\in\\", "lho_model_run.csv");
            String myModelGrp = myReturnStringArray[0];
            String myDate = myReturnStringArray[1];

            System.out.println("Reading "+jamieFile);

            this.addFile(new Integer(id).toString(), myModelGrp, myDate, jamieFile);
        }
        this.closeFile();
    }

    public void readFileAndDumpToLog(String file_lho_model_run, int segment, boolean isThisFirstTime, PrintWriter myDetailedModelResultsPrintWriter) throws Exception {

        if(isThisFirstTime) {
            if(myDetailedModelResultsPrintWriter != null) {
                StringBuffer buffer_header = new StringBuffer();

                buffer_header.append("segment"+",");
                buffer_header.append("oldModelQuality"+",");
                buffer_header.append("newModelQuality"+",");
                buffer_header.append("newModelBetter?"+",");
                buffer_header.append("diffInModelQuality"+",");

                myDetailedModelResultsPrintWriter.println(buffer_header);
            }
        }

        BufferedReader br = null;
        String dbRecord;

        Double myOldScore = null;
        Double myNewScore = null;

        try {
            File f = new File(file_lho_model_run);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            this.logFile.println("segment="+segment);

            while ( (dbRecord = br.readLine()) != null) {

                this.logFile.println(dbRecord);

                // parse this line a bit

                String[] val = dbRecord.split("=");

                if(val == null || val.length==0) {
                    continue;
                }

                String oldOrNew = val[0].trim();
                String myScore = null;
                if(val.length>=2) {
                    myScore = val[1].trim();
                }

                if(oldOrNew.equals("score_test_old")) {
                    try {
                        myOldScore = new Double(myScore);
                    } catch(Exception ex) {
                        // don't stop
                        int temp_a = 0;
                    }
                } else if(oldOrNew.equals("score_test_new")) {
                    try {
                        myNewScore = new Double(myScore);
                    } catch(Exception ex) {
                        // don't stop
                        int temp_a = 0;
                    }

                }

            }
        }catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        StringBuffer buffer = new StringBuffer();

        buffer.append(segment+",");
        buffer.append(myOldScore+",");
        buffer.append(myNewScore+",");
        if(myOldScore != null && myNewScore != null && myNewScore>myOldScore) {
            buffer.append(true+",");
        } else {
            buffer.append(false+",");
        }

        if(myOldScore != null && myNewScore != null) {
            double diffInModelQuality = myNewScore-myOldScore;
            buffer.append(diffInModelQuality+",");
        } else {
            buffer.append(null+",");
        }

        myDetailedModelResultsPrintWriter.println(buffer);


    }

    public boolean isThisModelRunHaveModGrpCd(String file_lho_model_run, String dir_in, String modelGrpCd) throws Exception {

        if(modelGrpCd == null) {
            return false;
        }

        File df = new File(dir_in);
        if (!df.exists()) {
            return false;
        }

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(file_lho_model_run);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String modelGroupCd = val[1];

                if(modelGroupCd != null) {
                    if(modelGroupCd.equals(modelGrpCd)) {
                        return true;
                    } else {
                        int temp_a = 0;
                    }
                } else {
                    int temp_a = 0;
                }


            }
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return false;


    }

    public void runLHO_Origin_OShift_Dest_FreightThatIsCoLoaded() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\coLoadedAnalysis_LDDKPups\\", "Origin_OShift_Dest_FreightThatIsCoLoaded_aggregation.csv");

        for (int id=1423; id<=1521; id++ ){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String dir_in = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            if(!this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                continue;
            }
            String hssFile = dir+"Origin_OShift_Dest_FreightThatIsCoLoaded.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile, false);
        }
        closeFile();

    }

    public void determineInconsistencesBetweenSpmtInfoFileAndDiversions_LDDKStuff() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\coLoadedAnalysis_LDDKPups\\", "coLoadedTrailers_aggregation.csv");

        for (int id=1499; id<=1533; id++ ){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String hssFile = dir+"coLoadedTrailers.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile, false);
        }
        closeFile();

    }

    public void rollUpJamieFileResults_vias() throws Exception {
        String[] inpNames = {};
        //openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\coLoadedAnalysis_LDDKPups\\", "coLoadedTrailers_aggregation.csv");

        System.out.println("id,originShift, numTractorsThroughVias, numUniqueVias");

        for (int id=1623; id<=1652; id++ ){
            String runId = id+"";
            //String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\OptimOutput\\";
            String dir = "\\\\nfs\\appdatadev\\CTS3\\OperationsResearch\\LHOptimizer\\"+runId+"\\out\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String jamieFile = dir+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";
            //if (header.length() < 1){
            //    getHeader(hssFile);
            //    addLine(header);
            //}
            //addFile(hssFile, false);
            try {
                readAndGenerateSummaryViaStats(id, jamieFile);
            } catch(Exception ex) {
                // do nothing....just skip
            }
        }
        //closeFile();

    }

    public static String LOAD_PATH_COUNT = "LOAD_PATH_COUNT";
    public static String TOTAL_CUBE = "TOTAL_CUBE";
    public static String TOTAL_WEIGHT = "TOTAL_WEIGHT";

    public void determineNumODAndNumLoadPathsByModelRunID(boolean isProduction) throws Exception {

        String outputDir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\problemSizeResults\\";
        String outputFile = "problemSizeResults.csv";

        if(isProduction) {
            outputFile = "problemSizeResults_production.csv";
        }

        String dir_base_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String dir_base_production = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        FileOutputStream myFileOutputStream_problemSize_summary = new FileOutputStream( new File(outputDir+outputFile), false);
        PrintWriter myPrintWriter_problemSize_summary = new PrintWriter(myFileOutputStream_problemSize_summary);

        List<String> myShiftStringCodeList = new ArrayList<String>();
        myShiftStringCodeList.add("1");
        myShiftStringCodeList.add("2");
        myShiftStringCodeList.add("3");
        myShiftStringCodeList.add("5");

        myPrintWriter_problemSize_summary.println("Year, Month, Date, Day, numODs, OB_pathCnt, OB_weight, OB_cube, FAC_pathCnt, FAC_weight, FAC_cube, IB_pathCnt, IB_weight, IB_cube, Day_pathCnt, Day_weight, Day_cube, totalPathCount, totalWeight, totalCube,"+DriverCountExcessDriversUsedTotalDriversUsed.myHeader+","+LHHssReportBuilder.myJamieFileSummaryHeader);

        for (int id=1520; id<=1686; id++ ){
        //for (int id=1392; id<=1680; id++ ){

            System.out.println(id);

            String runId = id+"";
            String dir_in = dir_base_local+runId+"\\in\\";

            String dir_base_production_runID = dir_base_production+runId+"\\";

            if(isProduction) {
                dir_in = dir_base_production_runID+"in\\";
            }

            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String dir_preprocessorOutput = dir_in+"Output\\";
            if(isProduction) {
                dir_preprocessorOutput = dir_base_production_runID+"out\\preprocessor\\";
            }
            File df_preprocessorOutput = new File(dir_preprocessorOutput);
            if (!df_preprocessorOutput.exists()) continue;

            String dir_coreModelOutput = dir_preprocessorOutput+"OptimOutput\\";
            if(isProduction) {
                dir_coreModelOutput = dir_base_production_runID+"out\\";
            }
            File df_coreModelOutput = new File(dir_coreModelOutput);
            if (!df_coreModelOutput.exists()) continue;

            try {
                Date myDate = this.readModelRunFile(dir_in, true, true);
                if(myDate == null) {
                    continue;
                } else {
                    SortedMap<String, Map<String, Integer>> myMap = this.readAggShpmtFile(dir_preprocessorOutput);
                    int numODs = this.readAggShpmtFile_getNumODs(dir_preprocessorOutput);
                    DriverCountExcessDriversUsedTotalDriversUsed myDriverCountExcessDriversUsedTotalDriversUsed = this.readExcessDriverInfo(dir_coreModelOutput);
                    String myJamieSummaryString = this.readJamieFileSummary(dir_coreModelOutput);
                    if(myMap == null) {
                        continue;
                    } else {

                        Calendar myCalendarDate = new GregorianCalendar();
                        myCalendarDate.setTime(myDate);

                        StringBuffer buffer = new StringBuffer();
                        buffer.append(myCalendarDate.get(Calendar.YEAR)+",");
                        buffer.append(myCalendarDate.get(Calendar.MONTH)+",");
                        buffer.append(myCalendarDate.get(Calendar.DATE)+",");
                        buffer.append(myCalendarDate.get(Calendar.DAY_OF_WEEK)+",");
                        buffer.append(numODs+",");
                        int totalPathCount = 0;
                        int totalWeight = 0;
                        int totalCube = 0;
                        for(String myShiftStringCode : myShiftStringCodeList) {
                            Map<String, Integer> myMap_inner = myMap.get(myShiftStringCode);

                            Integer myLoadPathCount = null;
                            Integer myWeight = null;
                            Integer myCube = null;

                            if(myMap_inner != null) {
                                myLoadPathCount = myMap_inner.get(LHHssReportBuilder.LOAD_PATH_COUNT);
                                myWeight = myMap_inner.get(LHHssReportBuilder.TOTAL_WEIGHT);
                                myCube = myMap_inner.get(LHHssReportBuilder.TOTAL_CUBE);
                            }

                            if(myLoadPathCount != null) {
                                buffer.append(myLoadPathCount+",");
                                totalPathCount += myLoadPathCount;
                            } else {
                                buffer.append(0+",");
                            }

                            if(myWeight != null) {
                                buffer.append(myWeight+",");
                                totalWeight += myWeight;
                            } else {
                                buffer.append(0+",");
                            }

                            if(myCube != null) {
                                buffer.append(myCube+",");
                                totalCube += myCube;
                            } else {
                                buffer.append(0+",");
                            }


                        }
                        buffer.append(totalPathCount+",");
                        buffer.append(totalWeight+",");
                        buffer.append(totalCube+",");
                        buffer.append(myDriverCountExcessDriversUsedTotalDriversUsed.toString()+",");
                        buffer.append(myJamieSummaryString);

                        myPrintWriter_problemSize_summary.println(buffer);
                    }
                }

            } catch(Exception ex) {
                // do nothing....just skip
                ex.printStackTrace();
                int temp_a = 0;
            }
        }

        myPrintWriter_problemSize_summary.close();
        myFileOutputStream_problemSize_summary.close();

    }

    public void rollUpExcessDriversResults() throws Exception {

        //System.out.println("id,originShift, numTractorsThroughVias, numUniqueVias");

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String dir_base_production = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        boolean isProduction = true;

        //for (int id=3561; id<=3565; id++ ){
        for (int id=3561; id<=3732; id++ ){

            String runId = id+"";

            System.out.println("id="+id);

            String dir_in = null;
            if(isProduction) {
                dir_in = dir_base_production+runId+"\\in\\";
            } else {
                dir_in = baseDirectory_local+runId+"\\in\\";
            }
            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            String dir_optOutput = null;
            if(!isProduction) {
                dir_optOutput = baseDirectory_local+runId+"\\in\\Output\\OptimOutput\\";
            } else {
                dir_optOutput = dir_base_production+runId+"\\out\\";
            }
            File df_optOutput = new File(dir_optOutput);
            if (!df_optOutput.exists()) continue;

            String excessDriverFile = dir_optOutput+"file_excessDriversByLocation_obviaibvia.csv";
            String excessDriver_with_shiftSpecificOveragesFile = dir_optOutput+"file_excessDriversByLocationWithOverages_obviaibvia.csv";
            try {
                this.readAndGenerateExcessDriverStats(id, myModelGroupID, myDateForThisRunID, excessDriverFile, excessDriver_with_shiftSpecificOveragesFile, isProduction);
            } catch(Exception ex) {
                // do nothing....just skip
            }
        }

        String outputDir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\excessDriverResults\\";
        String outputFile = "DriverOverageResults.csv";

        FileOutputStream myFileOutputStream_excessDrivers_summary = new FileOutputStream( new File(outputDir+outputFile), false);
        PrintWriter myPrintWriter_excessDrivers_summary = new PrintWriter(myFileOutputStream_excessDrivers_summary);

        myPrintWriter_excessDrivers_summary.println(ModelRunID_Sic_excessDriverCounts.myHeader);

        for(String myModelRunID_Sic_excessDriverCountsString : this.myMapOfModelRunID_Sic_excessDriverCountsByString.keySet()) {
            ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts = this.myMapOfModelRunID_Sic_excessDriverCountsByString.get(myModelRunID_Sic_excessDriverCountsString);

            if(!isProduction) {
                Integer numExcessDrivers_new = myModelRunID_Sic_excessDriverCounts.getMyExcessDrivers_new();
                if(numExcessDrivers_new == null) {
                    continue;
                }

                if(numExcessDrivers_new.intValue()<=0) {
                    continue;
                }

                myPrintWriter_excessDrivers_summary.println(myModelRunID_Sic_excessDriverCounts.getStringOutputFileFile_new());
            } else {
                Integer numExcessDrivers_old = myModelRunID_Sic_excessDriverCounts.getMyExcessDrivers_old();
                if(numExcessDrivers_old == null) {
                    continue;
                }

                if(numExcessDrivers_old.intValue()<0) {
                    continue;
                }

                myPrintWriter_excessDrivers_summary.println(myModelRunID_Sic_excessDriverCounts.getStringOutputFileFile_old());

            }
        }

        myPrintWriter_excessDrivers_summary.close();
        myFileOutputStream_excessDrivers_summary.close();

    }

    public void rollUpDriverUsageResults() throws Exception {

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        String dir_base_production = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        String myLimitedDriverOffset = "Output_limitedDrivers\\";
        String myUnlimitedDriverOffset = "Output_unlimitedDrivers\\";

        Map<Boolean, String> myMapOfDriverOffsetByIsLimited = new HashMap<Boolean, String>();
        myMapOfDriverOffsetByIsLimited.put(true, myLimitedDriverOffset);
        myMapOfDriverOffsetByIsLimited.put(false, myUnlimitedDriverOffset);

        for (int id=3345; id<=3382; id++ ){

            String runId = id+"";

            String dir_in = baseDirectory_local+runId+"\\in\\";
            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            for(Boolean isDriverUsageLimited : myMapOfDriverOffsetByIsLimited.keySet()) {

                String myDriverOffset = myMapOfDriverOffsetByIsLimited.get(isDriverUsageLimited);

                String dir_optOutput = baseDirectory_local+runId+"\\in\\"+myDriverOffset+"\\OptimOutput\\";
                File df_optOutput = new File(dir_optOutput);
                if (!df_optOutput.exists()) continue;

                String excessDriverFile = dir_optOutput+"file_excessDriversByLocation_obviaibvia.csv";
                try {
                    this.readAndGenerateDriverUsageStats(id, myModelGroupID, myDateForThisRunID, excessDriverFile, isDriverUsageLimited);
                } catch(Exception ex) {
                    // do nothing....just skip
                }

            }
        }

        String outputDir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\driverUsageResults\\";
        String outputFile = "DriverUsageResults.csv";

        FileOutputStream myFileOutputStream_driverUsage_summary = new FileOutputStream( new File(outputDir+outputFile), false);
        PrintWriter myPrintWriter_driverUsage_summary = new PrintWriter(myFileOutputStream_driverUsage_summary);

        myPrintWriter_driverUsage_summary.println(ModelRunID_Sic_driverUsageCounts.myHeader);

        for(String myModelRunID_Sic_driverUsageCountsString : this.myMapOfModelRunID_Sic_driverUsageCountsByString.keySet()) {
            ModelRunID_Sic_driverUsageCounts myModelRunID_Sic_driverUsageCounts = this.myMapOfModelRunID_Sic_driverUsageCountsByString.get(myModelRunID_Sic_driverUsageCountsString);

            myPrintWriter_driverUsage_summary.println(myModelRunID_Sic_driverUsageCounts.getStringOutputFileFile_isDriversLimited());
            myPrintWriter_driverUsage_summary.println(myModelRunID_Sic_driverUsageCounts.getStringOutputFileFile_isDriversUnlimited());
        }

        myPrintWriter_driverUsage_summary.close();
        myFileOutputStream_driverUsage_summary.close();

    }

    public void aggregateExcessDriversResults() throws Exception {

        //System.out.println("id,originShift, numTractorsThroughVias, numUniqueVias");

        String inputAndOutputDir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\excessDriverResults\\";

        Map<String, ViaYesNoDoublesYesNo> myMapOfViaYesNoDoublesYesNoByFileNameString = new HashMap<String, ViaYesNoDoublesYesNo>();

        myMapOfViaYesNoDoublesYesNoByFileNameString.put("DriverOverageResults_withVias_withDoubleTurns.csv", new ViaYesNoDoublesYesNo(true, true));
        myMapOfViaYesNoDoublesYesNoByFileNameString.put("DriverOverageResults_withVias_withNODoubleTurns.csv", new ViaYesNoDoublesYesNo(true, false));
        myMapOfViaYesNoDoublesYesNoByFileNameString.put("DriverOverageResults_withNOVias_withNODoubleTurns.csv", new ViaYesNoDoublesYesNo(false, false));
        myMapOfViaYesNoDoublesYesNoByFileNameString.put("DriverOverageResults_withNOVias_withDoubleTurns.csv", new ViaYesNoDoublesYesNo(false, true));

        for (String myFileName : myMapOfViaYesNoDoublesYesNoByFileNameString.keySet()){

            ViaYesNoDoublesYesNo myViaYesNoDoublesYesNo = myMapOfViaYesNoDoublesYesNoByFileNameString.get(myFileName);
            String fileNameToRead = inputAndOutputDir+myFileName;
            try {
                this.readAggregatedExcessDriverStatsForParticularViaDoublesSetting(fileNameToRead, myViaYesNoDoublesYesNo);
            } catch(Exception ex) {
                // do nothing....just skip
            }
        }


        String outputFile = "AggregatedDriverOverageResults.csv";

        FileOutputStream myFileOutputStream_aggregatedExcessDrivers_summary = new FileOutputStream( new File(inputAndOutputDir+outputFile), false);
        PrintWriter myPrintWriter_aggregatedExcessDrivers_summary = new PrintWriter(myFileOutputStream_aggregatedExcessDrivers_summary);

        myPrintWriter_aggregatedExcessDrivers_summary.println(ModelRunID_Sic_excessDriverCounts.myAggregatedHeader);

        for(String myModelRunID_Sic_excessDriverCountsString : this.myMapOfModelRunID_Sic_excessDriverCountsByString.keySet()) {
            ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts = this.myMapOfModelRunID_Sic_excessDriverCountsByString.get(myModelRunID_Sic_excessDriverCountsString);

            myPrintWriter_aggregatedExcessDrivers_summary.println(myModelRunID_Sic_excessDriverCounts.getStringOutputFileFile_aggregated());
        }

        myPrintWriter_aggregatedExcessDrivers_summary.close();
        myFileOutputStream_aggregatedExcessDrivers_summary.close();

    }

    public void runLHO_coLoadedTrailers() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\coLoadedAnalysis_LDDKPups\\", "coLoadedTrailers_aggregation.csv");

        for (int id = 1643; id <= 1712; id=id+1){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String hssFile = dir+"coLoadedTrailers.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile, false);
        }
        closeFile();

    }

    public void runLHO_coLoadedTrailers_findLHVansLoadedFromOBToIBOrLater() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\ModelRunsWithCoLoadedLHVansLoadingFromOBToIBOrLater\\", "modelRunsWithCoLoadedLHVansLoadedFromOBToIBOrLater.csv");

        this.logFile.println("ModelRunID");

        for (int id = 1715; id <= 1765; id=id+1){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String dir_in = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            if(!this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                continue;
            }
            String coLoadedTrailerFileName = dir+"coLoadedTrailers.csv";
            boolean haveCoLoadedLHVanLoadedToIBOrBeyond = this.readCoLoadedFileAndDetermineWhetherThereAreLHVansLoadedFromOBToIBOrLater(coLoadedTrailerFileName);
            if(haveCoLoadedLHVanLoadedToIBOrBeyond) {
                this.logFile.println(id);
            }
        }
        closeFile();

    }

    public void runLHO_compare_coLoadedTrailers_Desktop_and_production() throws Exception {

        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\PreBuildTrailers_comparison_test_prod\\", "PreBuildTrailers_comparison_test_prod.csv");

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelRunID"+",");
        buffer_header.append("modelGroupID"+",");
        buffer_header.append("Date"+",");
        buffer_header.append("numPreloaded_HSS_local"+",");
        buffer_header.append("numPreloaded_HSS_remote"+",");
        buffer_header.append("diff_HSS"+",");
        buffer_header.append("percDiff_HSS"+",");
        buffer_header.append("numPreloaded_LH_local"+",");
        buffer_header.append("numPreloaded_LH_remote"+",");
        buffer_header.append("diff_LH"+",");
        buffer_header.append("percDiff_LH"+",");

        buffer_header.append("In Local: TrailerID"+",");
        buffer_header.append("In Local: trailerPrefix"+",");
        buffer_header.append("In Local: trailerSuffix"+",");
        buffer_header.append("In Local: obLocation"+",");
        buffer_header.append("In Local: loadStatus"+",");
        buffer_header.append("In Local: loadToLocation"+",");
        buffer_header.append("In Local: loadToShift"+",");
        buffer_header.append("In Local: loadToDay"+",");
        buffer_header.append("In Local: isLH"+",");

        buffer_header.append("In Remote: TrailerID"+",");
        buffer_header.append("In Remote: trailerPrefix"+",");
        buffer_header.append("In Remote: trailerSuffix"+",");
        buffer_header.append("In Remote: obLocation"+",");
        buffer_header.append("In Remote: loadStatus"+",");
        buffer_header.append("In Remote: loadToLocation"+",");
        buffer_header.append("In Remote: loadToShift"+",");
        buffer_header.append("In Remote: loadToDay"+",");
        buffer_header.append("In Remote: isLH"+",");


        StringBuffer buffer_InLocal_empty = new StringBuffer();
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");
        buffer_InLocal_empty.append(" "+",");

        StringBuffer buffer_InRemote_empty = new StringBuffer();
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");
        buffer_InRemote_empty.append(" "+",");

        this.logFile.println(buffer_header);

        for (int id = 2511; id <= 2516; id=id+1){
            System.out.println(id);
            String runId = id+"";

            String dir_preprocessorOutput_local = baseDirectory_local+runId+"\\in\\Output\\";
            File df_preprocessorOutput_local = new File(dir_preprocessorOutput_local);
            if (!df_preprocessorOutput_local.exists()) continue;

            String dir_preprocessorOutput_remote = baseDirectory_remote+runId+"\\out\\preprocessor\\";
            File df_preprocessorOutput_remote = new File(dir_preprocessorOutput_remote);
            if (!df_preprocessorOutput_remote.exists()) continue;

            String dir_in_local = baseDirectory_local+runId+"\\in\\";
            String file_lho_model_run = dir_in_local+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in_local, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in_local, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            String coLoadedTrailerFileName_local = dir_preprocessorOutput_local+"coLoadedTrailers.csv";
            Map<Integer, Trailer> myMap_ByTrailerID_local = this.readCoLoadedFile_determineNumberEntries(coLoadedTrailerFileName_local);

            Integer count_LH_local = this.getNumTrailers(myMap_ByTrailerID_local, true);
            Integer count_HSS_local = this.getNumTrailers(myMap_ByTrailerID_local, false);

            String coLoadedTrailerFileName_remote = dir_preprocessorOutput_remote+"coLoadedTrailers.csv";
            Map<Integer, Trailer> myMap_ByTrailerID_remote = this.readCoLoadedFile_determineNumberEntries(coLoadedTrailerFileName_remote);

            Integer count_LH_remote = this.getNumTrailers(myMap_ByTrailerID_remote, true);
            Integer count_HSS_remote = this.getNumTrailers(myMap_ByTrailerID_remote, false);

            StringBuffer buffer_main = new StringBuffer();
            buffer_main.append(runId+",");
            buffer_main.append(myModelGroupID+",");
            buffer_main.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");
            buffer_main.append(count_HSS_local+",");
            buffer_main.append(count_HSS_remote+",");
            int diff_HSS = count_HSS_local-count_HSS_remote;
            buffer_main.append(diff_HSS+",");
            buffer_main.append((diff_HSS+LHHssReportBuilder.epsilon)/(Math.max(count_HSS_local, count_HSS_remote)+LHHssReportBuilder.epsilon)+",");
            buffer_main.append(count_LH_local+",");
            buffer_main.append(count_LH_remote+",");
            int diff_LH = count_LH_local-count_LH_remote;
            buffer_main.append(diff_LH+",");
            buffer_main.append((diff_LH+LHHssReportBuilder.epsilon)/(Math.max(count_LH_local, count_LH_remote)+LHHssReportBuilder.epsilon)+",");

            Set<Integer> setOfTrailerIDInLocalNotInRemote = this.getSetOfTrailerIDInFirstAndNotInSecond(myMap_ByTrailerID_local, myMap_ByTrailerID_remote);

            Set<Integer> setOfTrailerIDInRemoteNotInLocal = this.getSetOfTrailerIDInFirstAndNotInSecond(myMap_ByTrailerID_remote, myMap_ByTrailerID_local);

            for(Integer myTrailerID : setOfTrailerIDInLocalNotInRemote) {
                Trailer myTrailer = myMap_ByTrailerID_local.get(myTrailerID);
                if(myTrailer == null) {
                    continue;
                }
                this.logFile.println(buffer_main.toString()+myTrailer.getDescriptorString()+buffer_InRemote_empty.toString());
            }

            for(Integer myTrailerID : setOfTrailerIDInRemoteNotInLocal) {
                Trailer myTrailer = myMap_ByTrailerID_remote.get(myTrailerID);
                if(myTrailer == null) {
                    continue;
                }
                this.logFile.println(buffer_main.toString()+buffer_InLocal_empty.toString()+myTrailer.getDescriptorString());
            }

        }
        closeFile();

    }

    public void runLHO_rollUpHazMatBLStatsByModelRunID() throws Exception {

        List<String> listOfModelsRunIDsToRun = new ArrayList<String>();

        listOfModelsRunIDsToRun.add("2531");
        listOfModelsRunIDsToRun.add("2532");
        listOfModelsRunIDsToRun.add("2535");
        listOfModelsRunIDsToRun.add("2536");
        listOfModelsRunIDsToRun.add("2539");
        listOfModelsRunIDsToRun.add("2540");
        listOfModelsRunIDsToRun.add("2543");
        listOfModelsRunIDsToRun.add("2544");
        listOfModelsRunIDsToRun.add("2547");
        listOfModelsRunIDsToRun.add("2548");

        listOfModelsRunIDsToRun.add("2551");
        listOfModelsRunIDsToRun.add("2552");
        listOfModelsRunIDsToRun.add("2555");
        listOfModelsRunIDsToRun.add("2556");
        listOfModelsRunIDsToRun.add("2559");
        listOfModelsRunIDsToRun.add("2560");
        listOfModelsRunIDsToRun.add("2563");
        listOfModelsRunIDsToRun.add("2564");
        listOfModelsRunIDsToRun.add("2567");
        listOfModelsRunIDsToRun.add("2568");

        listOfModelsRunIDsToRun.add("2631");
        listOfModelsRunIDsToRun.add("2632");
        listOfModelsRunIDsToRun.add("2636");
        listOfModelsRunIDsToRun.add("2637");
        listOfModelsRunIDsToRun.add("2640");
        listOfModelsRunIDsToRun.add("2641");
        listOfModelsRunIDsToRun.add("2645");
        listOfModelsRunIDsToRun.add("2646");
        listOfModelsRunIDsToRun.add("2649");
        listOfModelsRunIDsToRun.add("2650");

        listOfModelsRunIDsToRun.add("2653");
        listOfModelsRunIDsToRun.add("2654");
        listOfModelsRunIDsToRun.add("2657");
        listOfModelsRunIDsToRun.add("2658");
        listOfModelsRunIDsToRun.add("2661");
        listOfModelsRunIDsToRun.add("2662");
        listOfModelsRunIDsToRun.add("2665");
        listOfModelsRunIDsToRun.add("2666");
        listOfModelsRunIDsToRun.add("2669");
        listOfModelsRunIDsToRun.add("2670");

        listOfModelsRunIDsToRun.add("2705");
        listOfModelsRunIDsToRun.add("2706");
        listOfModelsRunIDsToRun.add("2709");
        listOfModelsRunIDsToRun.add("2710");
        listOfModelsRunIDsToRun.add("2713");
        listOfModelsRunIDsToRun.add("2714");
        listOfModelsRunIDsToRun.add("2717");
        listOfModelsRunIDsToRun.add("2718");
        listOfModelsRunIDsToRun.add("2721");
        listOfModelsRunIDsToRun.add("2722");

        listOfModelsRunIDsToRun.add("2725");
        listOfModelsRunIDsToRun.add("2726");
        listOfModelsRunIDsToRun.add("2729");
        listOfModelsRunIDsToRun.add("2730");
        listOfModelsRunIDsToRun.add("2733");
        listOfModelsRunIDsToRun.add("2734");
        listOfModelsRunIDsToRun.add("2737");
        listOfModelsRunIDsToRun.add("2738");
        listOfModelsRunIDsToRun.add("2741");
        listOfModelsRunIDsToRun.add("2742");

        listOfModelsRunIDsToRun.add("2789");
        listOfModelsRunIDsToRun.add("2790");
        listOfModelsRunIDsToRun.add("2793");
        listOfModelsRunIDsToRun.add("2794");
        listOfModelsRunIDsToRun.add("2797");
        listOfModelsRunIDsToRun.add("2798");
        listOfModelsRunIDsToRun.add("2801");
        listOfModelsRunIDsToRun.add("2802");
        listOfModelsRunIDsToRun.add("2805");
        listOfModelsRunIDsToRun.add("2806");

        listOfModelsRunIDsToRun.add("2809");
        listOfModelsRunIDsToRun.add("2810");
        listOfModelsRunIDsToRun.add("2813");
        listOfModelsRunIDsToRun.add("2814");
        listOfModelsRunIDsToRun.add("2817");
        listOfModelsRunIDsToRun.add("2818");
        listOfModelsRunIDsToRun.add("2821");
        listOfModelsRunIDsToRun.add("2822");
        listOfModelsRunIDsToRun.add("2825");
        listOfModelsRunIDsToRun.add("2826");

        String outputFileSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\HazMatBLShipmentStatsByModelrun\\";

        openFile(outputFileSubDirectory, "HazMatBLShipmentStatsByModelrun.csv");

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\VariableService_simulation\\";
        //String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelRunID"+",");
        buffer_header.append("modelGroupID"+",");
        buffer_header.append("Date"+",");
        buffer_header.append("HazMat"+",");
        buffer_header.append("ServiceLevelViolation"+",");
        buffer_header.append("PROCount"+",");
        buffer_header.append("Cube"+",");
        buffer_header.append("Weight"+",");

        this.logFile.println(buffer_header);

        for (String id : listOfModelsRunIDsToRun){
            System.out.println(id);
            String runId = id+"";

            String dir_preprocessorOutput_local = baseDirectory_local+runId+"\\in\\Output\\";
            File df_preprocessorOutput_local = new File(dir_preprocessorOutput_local);
            if (!df_preprocessorOutput_local.exists()) continue;

            String dir_coreModelOutput_local = baseDirectory_local+runId+"\\in\\Output\\OptimOutput\\";
            File df_coreModelOutput_local = new File(dir_coreModelOutput_local);
            if (!df_coreModelOutput_local.exists()) continue;

            //String dir_coreModelOutput_remote = baseDirectory_remote+runId+"\\out\\";
            //File df_coreModelOutput_remote = new File(dir_coreModelOutput_remote);
            //if (!df_coreModelOutput_remote.exists()) continue;

            String dir_in_local = baseDirectory_local+runId+"\\in\\";
            String file_lho_model_run = dir_in_local+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in_local, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in_local, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            String aggShipmentsFileName_local = dir_preprocessorOutput_local+"AggShpmt_forCoreOptimizer.csv";
            SortedMap<Integer, PROCount_Cube_Weight> myMap = this.read_file_AggShpmtFile_returnMap(aggShipmentsFileName_local);

            StringBuffer buffer_main = new StringBuffer();
            buffer_main.append(runId+",");
            buffer_main.append(myModelGroupID+",");
            buffer_main.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");

            for(Integer myOverServiceLevel : myMap.keySet()) {
                PROCount_Cube_Weight myPROCount_Cube_Weight = myMap.get(myOverServiceLevel);

                StringBuffer buffer_inner = new StringBuffer();
                if(myOverServiceLevel.intValue()==-1) {
                    // this is NOT hazMat
                    buffer_inner.append("FALSE"+",");
                    buffer_inner.append(0+",");
                } else {
                    buffer_inner.append("TRUE"+",");
                    buffer_inner.append(myOverServiceLevel+",");
                }
                buffer_inner.append(myPROCount_Cube_Weight.getMyProCount()+",");
                buffer_inner.append(myPROCount_Cube_Weight.getMyCube()+",");
                buffer_inner.append(myPROCount_Cube_Weight.getMyWeight()+",");

                this.logFile.println(buffer_main.toString()+buffer_inner.toString());

            }

        }
        closeFile();

    }

    public void runLHO_LookForHazMatShipmentCountsByDesignatedHSSLane() throws Exception {

        Map<Integer, String> mapOfStringDirectory_weeklyByStringDirectory_daily = new HashMap<Integer, String>();

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2810, "Jan18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2809, "Jan18_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2806, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2805, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2802, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2801, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2798, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2797, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2794, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2793, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2790, "Jan11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2789, "Jan11_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2786, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2785, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2782, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2781, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2778, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2777, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2774, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2773, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2770, "Jan04_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2769, "Jan04_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2766, "Dec28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2765, "Dec28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2762, "Dec28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2761, "Dec28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2758, "Dec28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2757, "Dec28_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2754, "Dec21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2753, "Dec21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2750, "Dec21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2749, "Dec21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2746, "Dec21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2745, "Dec21_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2742, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2741, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2738, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2737, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2734, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2733, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2730, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2729, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2726, "Dec14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2725, "Dec14_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2722, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2721, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2718, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2717, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2714, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2713, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2710, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2709, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2706, "Dec07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2705, "Dec07_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2702, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2701, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2698, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2697, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2694, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2693, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2690, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2689, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2686, "Nov30_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2685, "Nov30_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2682, "Nov23_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2681, "Nov23_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2678, "Nov23_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2677, "Nov23_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2674, "Nov23_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2673, "Nov23_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2670, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2669, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2666, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2665, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2662, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2661, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2658, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2657, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2654, "Nov16_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2653, "Nov16_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2650, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2649, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2646, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2645, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2641, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2640, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2637, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2636, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2632, "Nov09_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2631, "Nov09_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2628, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2627, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2624, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2623, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2620, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2619, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2616, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2615, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2612, "Nov02_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2611, "Nov02_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2608, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2607, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2604, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2603, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2600, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2599, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2596, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2595, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2592, "Oct26_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2591, "Oct26_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2588, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2587, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2584, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2583, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2580, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2579, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2576, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2575, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2572, "Oct19_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2571, "Oct19_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2568, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2567, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2564, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2563, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2560, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2559, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2556, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2555, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2552, "Oct12_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2551, "Oct12_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2548, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2547, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2544, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2543, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2540, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2539, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2536, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2535, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2532, "Oct5_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2531, "Oct5_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2528, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2527, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2524, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2523, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2520, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2519, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2516, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2515, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2512, "Sep28_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2511, "Sep28_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2508, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2507, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2504, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2503, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2500, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2499, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2496, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2495, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2492, "Sep21_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2491, "Sep21_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2488, "Sep14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2487, "Sep14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2483, "Sep14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2480, "Sep14_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2479, "Sep14_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2468, "Sep07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2467, "Sep07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2464, "Sep07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2463, "Sep07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2460, "Sep07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2459, "Sep07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2456, "Sep07_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2455, "Sep07_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2452, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2451, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2448, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2447, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2444, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2443, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2440, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2439, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2436, "Aug31_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2435, "Aug31_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2431, "Aug24_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2412, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2411, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2408, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2407, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2404, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2403, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2400, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2399, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2396, "Aug17_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2395, "Aug17_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2392, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2391, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2388, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2387, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2384, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2383, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2380, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2379, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2376, "Aug10_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2375, "Aug10_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2372, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2371, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2368, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2367, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2364, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2363, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2360, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2359, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2356, "Aug03_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2355, "Aug03_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2339, "Jul27_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2335, "Jul27_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2306, "Jul13_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2295, "Jul13_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2294, "Jul13_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2291, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2290, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2287, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2286, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2283, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2282, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2279, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2278, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2275, "Jul06_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2274, "Jul06_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2271, "Jun29_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2270, "Jun29_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2267, "Jun29_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2266, "Jun29_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2263, "Jun29_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2262, "Jun29_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2259, "Jun29_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2258, "Jun29_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2255, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2254, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2251, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2250, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2247, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2246, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2243, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2242, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2239, "Jun22_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2238, "Jun22_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2235, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2234, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2231, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2230, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2227, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2226, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2223, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2222, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2219, "Jun15_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2218, "Jun15_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2215, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2214, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2211, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2210, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2207, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2206, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2203, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2202, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2199, "Jun08_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2198, "Jun08_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2195, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2194, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2191, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2190, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2187, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2186, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2183, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2182, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2179, "Jun01_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2178, "Jun01_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2173, "May25_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2172, "May25_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2169, "May25_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2168, "May25_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2165, "May25_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2164, "May25_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2160, "May25_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2159, "May25_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2156, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2155, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2152, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2151, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2148, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2147, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2144, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2143, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2140, "May18_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2139, "May18_weekly\\");

        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2136, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2135, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2132, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2131, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2128, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2127, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2124, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2123, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2120, "May11_weekly\\");
        mapOfStringDirectory_weeklyByStringDirectory_daily.put(2119, "May11_weekly\\");

        String outputFileSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\ShipmentCountsByDesignatedHSSLane\\";

        openFile(outputFileSubDirectory, "ShipmentCountsByDesignatedHSSLane.csv");

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        //String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelRunID"+",");
        buffer_header.append("modelGroupID"+",");
        buffer_header.append("Date"+",");
        buffer_header.append("LoadLeg: Origin"+",");
        buffer_header.append("LoadLeg: OriginShift"+",");
        buffer_header.append("LoadLeg: Destination"+",");
        buffer_header.append("LoadLeg: DestinationShift"+",");
        buffer_header.append("LoadLeg: Mode"+",");
        buffer_header.append("UniqueODCount"+",");
        buffer_header.append("ShipmentCount"+",");
        buffer_header.append("Cube"+",");
        buffer_header.append("Weight"+",");

        this.logFile.println(buffer_header);

        for (int id = 2483; id <= 2810; id=id+1){
            System.out.println(id);
            String runId = id+"";

            String weeklyOffset = mapOfStringDirectory_weeklyByStringDirectory_daily.get(new Integer(id));

            if(weeklyOffset == null) {
                continue;
            }

            this.readSics(baseDirectory_local+weeklyOffset, "lho_sic.csv");

            this.read_file_certifiedHSSMoveLegs(baseDirectory_local+weeklyOffset+"lho_certified_ml.csv");

            String dir_preprocessorOutput_local = baseDirectory_local+runId+"\\in\\Output\\";
            File df_preprocessorOutput_local = new File(dir_preprocessorOutput_local);
            if (!df_preprocessorOutput_local.exists()) continue;

            String dir_coreModelOutput_local = baseDirectory_local+runId+"\\in\\Output\\OptimOutput\\";
            File df_coreModelOutput_local = new File(dir_coreModelOutput_local);
            if (!df_coreModelOutput_local.exists()) continue;

            //String dir_coreModelOutput_remote = baseDirectory_remote+runId+"\\out\\";
            //File df_coreModelOutput_remote = new File(dir_coreModelOutput_remote);
            //if (!df_coreModelOutput_remote.exists()) continue;

            String dir_in_local = baseDirectory_local+runId+"\\in\\";
            String file_lho_model_run = dir_in_local+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in_local, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in_local, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            String aggShipmentsFileName_local = dir_preprocessorOutput_local+"AggShpmt_forCoreOptimizer.csv";
            this.read_file_AggShpmtFile(aggShipmentsFileName_local);

            String odFlowDetails_diversionsFileName_local = dir_coreModelOutput_local+"file_ODFlowDetails_diversions.csv";
            this.read_file_ODFlowDetails_diversionsFile(odFlowDetails_diversionsFileName_local);

            StringBuffer buffer_main = new StringBuffer();
            buffer_main.append(runId+",");
            buffer_main.append(myModelGroupID+",");
            buffer_main.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");

            for(String myLoadLegString : this.myMapOfLoadLegByString.keySet()) {
                CharlieNetwork.LoadLeg myLoadLeg = this.myMapOfLoadLegByString.get(myLoadLegString);

                String originSic = myLoadLeg.getOriginLocation();
                String originSic_region = this.myMapOfRegionBySic.get(originSic);
                if(originSic_region == null) {
                    continue;
                }

                if(!originSic_region.equals(myModelGroupID)) {
                    continue;
                }

                StringBuffer buffer_inner = new StringBuffer();
                buffer_inner.append(myLoadLeg.getOriginLocation()+",");
                buffer_inner.append(myLoadLeg.getOriginShift()+",");
                buffer_inner.append(myLoadLeg.getDestLocation()+",");
                buffer_inner.append(myLoadLeg.getDestShift()+",");
                buffer_inner.append(myLoadLeg.getMode()+",");
                buffer_inner.append(myLoadLeg.getUniqueODCount()+",");
                buffer_inner.append(myLoadLeg.getShipmentCount()+",");
                buffer_inner.append(myLoadLeg.getTotalCube()+",");
                buffer_inner.append(myLoadLeg.getTotalWeight()+",");

                this.logFile.println(buffer_main.toString()+buffer_inner.toString());

            }

            this.clearOutMapsOfODsAndEmptyODsFromExistingLoadLegsAndclearOutLoadLegMapsAndSicInfoMaps();



        }
        closeFile();

    }

    public void clearOutMapsOfODsAndEmptyODsFromExistingLoadLegsAndclearOutLoadLegMapsAndSicInfoMaps() {

        if(this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString != null && this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString.size()>0) {
            this.myMapOfO_OShift_Dest_Service__isEarly_cube_weightByString.clear();
        }

        for(String myLoadLegString : this.myMapOfLoadLegByString.keySet()) {
            CharlieNetwork.LoadLeg myLoadLeg = this.myMapOfLoadLegByString.get(myLoadLegString);

            myLoadLeg.emptyMapOfODs();
        }

        this.myMapOfLoadLegByString.clear();

        if(this.myMapOfTypeBySic != null) {
            this.myMapOfTypeBySic.clear();
        }

        if(this.myMapOfRegionBySic != null) {
            this.myMapOfRegionBySic.clear();
        }

        if(this.myMapOfDarkLocationIndBySic != null) {
            this.myMapOfDarkLocationIndBySic.clear();
        }

        if(this.myMapOfHasFACShiftIndBySic != null) {
            this.myMapOfHasFACShiftIndBySic.clear();
        }

    }

    public void runLHO_LookForDiversionsIntoOBLocationsTwoAndMoreDaysOut() throws Exception {

        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\DiversionsToOBLocations\\", "DiversionsToOBLocations.csv");

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        //String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelRunID"+",");
        buffer_header.append("modelGroupID"+",");
        buffer_header.append("Date"+",");
        buffer_header.append("Location"+",");
        buffer_header.append("Shift"+",");
        buffer_header.append("Day"+",");
        buffer_header.append("CubeDivertedTo"+",");
        buffer_header.append("WeightDivertedTo"+",");
        buffer_header.append("FractionOfDay0Cube"+",");
        buffer_header.append("FractionOfDay0Weight"+",");

        this.logFile.println(buffer_header);

        for (int id = 2355; id <= 2516; id=id+1){
            System.out.println(id);
            String runId = id+"";

            String dir_coreModelOutput_local = baseDirectory_local+runId+"\\in\\Output\\OptimOutput\\";
            File df_coreModelOutput_local = new File(dir_coreModelOutput_local);
            if (!df_coreModelOutput_local.exists()) continue;

            //String dir_coreModelOutput_remote = baseDirectory_remote+runId+"\\out\\";
            //File df_coreModelOutput_remote = new File(dir_coreModelOutput_remote);
            //if (!df_coreModelOutput_remote.exists()) continue;

            String dir_in_local = baseDirectory_local+runId+"\\in\\";
            String file_lho_model_run = dir_in_local+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in_local, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in_local, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            String odFlowDetails_diversionsFileName_local = dir_coreModelOutput_local+"file_ODFlowDetails_diversions.csv";
            Map<String, Map<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>>> myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation_local = this.read_file_ODFlowDetails_diversionsFile_determineDiversionsIntoOBLocationsByDay(odFlowDetails_diversionsFileName_local);

            StringBuffer buffer_main = new StringBuffer();
            buffer_main.append(runId+",");
            buffer_main.append(myModelGroupID+",");
            buffer_main.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");

            for(String myLocation : myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation_local.keySet()) {
                Map<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>> myMap_outer = myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation_local.get(myLocation);
                if(myMap_outer == null || myMap_outer.size()==0) {
                    continue;
                }

                boolean isThereDayGreaterThanZero = false;
                for(Integer myDay : myMap_outer.keySet()) {
                    if(myDay.intValue()>0) {
                        isThereDayGreaterThanZero = true;
                    }
                }

                if(!isThereDayGreaterThanZero) {
                    continue;
                }

                Cube_Weight myCube_WeightOffOBDay0 = null;

                for(Integer myDay : myMap_outer.keySet()) {
                    Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight> myMap_inner = myMap_outer.get(myDay);
                    if(myMap_inner==null || myMap_inner.size()==0) {
                        continue;
                    }

                    Cube_Weight cube_weight_total = new Cube_Weight(0.0, 0.0);

                    for(O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight : myMap_inner.keySet()) {
                        Cube_Weight myCube_Weight = myMap_inner.get(myO_OShift_Dest_Service__isEarly_cube_weight);
                        if(myCube_Weight == null) {
                            continue;
                        }
                        cube_weight_total.addCube_Weight(myCube_Weight);
                    }

                    if(myDay.intValue()==0) {
                        myCube_WeightOffOBDay0 = new Cube_Weight(cube_weight_total);
                    }

                    StringBuffer myBuffer_inner = new StringBuffer();
                    myBuffer_inner.append(myLocation+",");
                    myBuffer_inner.append("O"+",");
                    myBuffer_inner.append(myDay+",");
                    myBuffer_inner.append(cube_weight_total.getMyCube()+",");
                    myBuffer_inner.append(cube_weight_total.getMyWeight()+",");
                    if(myDay.intValue()==0) {
                        myBuffer_inner.append(1.0+",");
                        myBuffer_inner.append(1.0+",");
                    } else {
                        double cubeOffOfOBDay0 = 0.0;
                        double weightOffOfOBDay0 = 0.0;
                        if(myCube_WeightOffOBDay0 != null) {
                            cubeOffOfOBDay0 = myCube_WeightOffOBDay0.getMyCube();
                            weightOffOfOBDay0 = myCube_WeightOffOBDay0.getMyWeight();
                        }
                        myBuffer_inner.append(cube_weight_total.getMyCube()/(cubeOffOfOBDay0+LHHssReportBuilder.epsilon)+",");
                        myBuffer_inner.append(cube_weight_total.getMyWeight()/(weightOffOfOBDay0+LHHssReportBuilder.epsilon)+",");
                    }

                    this.logFile.println(buffer_main.toString()+myBuffer_inner.toString());
                }
            }



        }
        closeFile();

    }

    public Set<Integer> getSetOfTrailerIDInFirstAndNotInSecond(Map<Integer, Trailer> myMap_ByTrailerID_first, Map<Integer, Trailer> myMap_ByTrailerID_second) {

        Set<Integer> mySetToReturn = new HashSet<Integer>();

        for(Integer myTrailerID : myMap_ByTrailerID_first.keySet()) {
            if(!myMap_ByTrailerID_second.keySet().contains(myTrailerID)) {
                mySetToReturn.add(myTrailerID);
            }
        }

        return mySetToReturn;

    }

    public int getNumTrailers(Map<Integer, Trailer> myMap_ByTrailerID, boolean weWantLH) {
        int numTrailers = 0;

        if(myMap_ByTrailerID == null || myMap_ByTrailerID.size()==0) {
            return 0;
        }

        for(Integer myTrailerID : myMap_ByTrailerID.keySet()) {
            Trailer myTrailer = myMap_ByTrailerID.get(myTrailerID);

            if(weWantLH) {
                if(myTrailer.getIsLH()) {
                    numTrailers++;
                }
            } else {
                if(!myTrailer.getIsLH()) {
                    numTrailers++;
                }
            }
        }

        return numTrailers;
    }

    public void createNew_lho_omt_lane_withDirectsInPlace() throws Exception {

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        this.logFile.println("ModelGroupID, ModelRunID,  TractorCount (no bypass through OMTs), Laden Trailer Count OV (no bypass through OMTs),Laden Trailer Count VD (no bypass through OMTs),Laden Trailer Count OD (no bypass through OMTs), TractorCount (bypass through OMTs), Laden Trailer Count OV (bypass through OMTs),Laden Trailer Count VD (bypass through OMTs),Laden Trailer Count OD (bypass through OMTs),   ");


        List<String> myListOfSubdirectories = new ArrayList<String>();

        myListOfSubdirectories.add("Jan19_weekly");

        for (String mySubdirectory : myListOfSubdirectories){
            String dir_weeklyFiles = myDirectoryString_OBModelPhaseII+mySubdirectory+"\\";
            File df_weeklyFiles = new File(dir_weeklyFiles);
            if (!df_weeklyFiles.exists()) continue;

            String omtFileName = dir_weeklyFiles+"lho_omt_lane.csv";
            String omt_DirectsFileName = dir_weeklyFiles+"lho_omt_directs_lane.csv";
            try {
                String headerRow = this.readOMTFile(omtFileName);
                this.writeOMTDirectFile(headerRow, omt_DirectsFileName);
            } catch(Exception ex) {
                int temp_a = 0;
            }
        }

    }

    public void runLHO_RollUpTimingStatistics() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\TimingStatistics\\", "TimingStatistics_LimitsOnNextDayOBRehandleOrNot.csv");

        String myDirectoryString_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        String myDirectoryString_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        List<String> myListOfDirectories = new ArrayList<String>();
        myListOfDirectories.add(myDirectoryString_local);
        //myListOfDirectories.add(myDirectoryString_remote);

        this.logFile.println("WithLimitsOnNextDayRehandleAtOB, ModelGroupID, ModelRunID, SolutionTime (seconds), SolutionTime (minutes)");

        for(String myDirectoryString : myListOfDirectories) {
            System.out.println(myDirectoryString);
            boolean isLocal = false;
            if(myDirectoryString.equals(myDirectoryString_local)) {
                isLocal = true;
            }
            for (int id = 3253; id <= 3382; id=id+1){
                System.out.println(id);
                String runId = id+"";

                String dir_OptimOutput = null;

                if(isLocal) {
                    dir_OptimOutput = myDirectoryString+runId+"\\in\\Output\\OptimOutput\\";
                } else {
                    dir_OptimOutput = myDirectoryString+runId+"\\out\\";
                }

                File df_OptimOutput = new File(dir_OptimOutput);
                if (!df_OptimOutput.exists()) continue;
                String dir_in = null;
                if(isLocal) {
                    dir_in = myDirectoryString+runId+"\\in\\";
                } else {
                    dir_in = myDirectoryString+runId+"\\in\\";
                }
                String file_lho_model_run = dir_in+"lho_model_run.csv";
                String myModelGroupID = "PST";
                if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                    myModelGroupID = "CST";
                }
                String timingFileName = dir_OptimOutput+"timingFile.csv";
                try {
                    int numSeconds = this.readTimingFile(timingFileName);
                    double numMinutes = (numSeconds+LHHssReportBuilder.epsilon)/60.0;
                    this.logFile.println(isLocal+","+myModelGroupID+","+id+","+numSeconds+","+numMinutes);
                } catch(Exception ex) {
                    int temp_a = 0;
                }
            }
        }
        closeFile();

    }

    public void doIntegerAdditionMultExperiment() {
        boolean shouldWeQuit = false;
        Integer myA = null;
        Integer myB = null;
        for(int A=1; A<100; A++) {
            for(int B=1; B<100; B++) {
                int sum = A+B;
                int prod = A*B;

                if(sum==prod) {
                    shouldWeQuit=true;
                    myA = A;
                    myB = B;
                }

                if(shouldWeQuit) {
                    break;
                }
            }

            if(shouldWeQuit) {
                break;
            }
        }

        if(myA != null && myB != null) {
            System.out.println("myA="+myA+" myB="+myB);
        }
    }

    public void doCostComparisonAnalysis(String typeOfAnalysis, int runID_lb, int runID_ub) throws Exception {
        this.runLHO_RollUpSolutionCostOutput(typeOfAnalysis, runID_lb, runID_ub);
        this.runLHO_RollUpSolutionTUCOutputByShift(typeOfAnalysis, runID_lb, runID_ub);
        this.runLHO_RollUpSolutionTUCOutputByShiftByDate(typeOfAnalysis, runID_lb, runID_ub);
    }

    public void modifyConstraintFilesForAggregationModel() throws Exception {

            String inputDirectoryOffset = "testCase_SD1";
            this.modifyConstraintFilesForAggregationModel_inner("SD1", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD25";
            this.modifyConstraintFilesForAggregationModel_inner("RD25", inputDirectoryOffset);

        /*
            inputDirectoryOffset = "testCase_319_oddSolution";
            this.modifyConstraintFilesForAggregationModel_inner("RD319", inputDirectoryOffset);
        */

            inputDirectoryOffset = "testCase_48_notInfeasibleAsExpected";
            this.modifyConstraintFilesForAggregationModel_inner("RD48", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD17_infeasible2";
            this.modifyConstraintFilesForAggregationModel_inner("RD17", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_StToSt_plus_weightBreaks";
            this.modifyConstraintFilesForAggregationModel_inner("St_to_St_plus_WeightBreaks", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_SD1_notProducingClusterAssignmentsToShipments";
            this.modifyConstraintFilesForAggregationModel_inner("SD1", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_negCDI";
            this.modifyConstraintFilesForAggregationModel_inner("RD25", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_originalConstraintFile";
            this.modifyConstraintFilesForAggregationModel_inner("Constraints_originalFromCharlie", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_USAndCAShipments";
            this.modifyConstraintFilesForAggregationModel_inner("Constraints_originalFromCharlie", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_810";
            this.modifyConstraintFilesForAggregationModel_inner("RD48", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_dummyShipments";
            this.modifyConstraintFilesForAggregationModel_inner("SD1", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_negCDI_2";
            this.modifyConstraintFilesForAggregationModel_inner("SD1", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD123";
            this.modifyConstraintFilesForAggregationModel_inner("RD123", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD221";
            this.modifyConstraintFilesForAggregationModel_inner("RD221", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD417";
            this.modifyConstraintFilesForAggregationModel_inner("RD417", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD116";
            this.modifyConstraintFilesForAggregationModel_inner("RD116", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD172";
            this.modifyConstraintFilesForAggregationModel_inner("RD172", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD662";
            this.modifyConstraintFilesForAggregationModel_inner("RD662", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD343";
            this.modifyConstraintFilesForAggregationModel_inner("RD343", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_Shipment_File_All_Bad_Data";
            this.modifyConstraintFilesForAggregationModel_inner("RD343", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_Shipment_File_Empty_Case";
            this.modifyConstraintFilesForAggregationModel_inner("RD343", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_SD3";
            this.modifyConstraintFilesForAggregationModel_inner("SD3", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_1028";
            this.modifyConstraintFilesForAggregationModel_inner("SD1-ATT", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_longRunTime_24";
            this.modifyConstraintFilesForAggregationModel_inner("RD24", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD17_infeasible";
            this.modifyConstraintFilesForAggregationModel_inner("RD17", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD24Problem";
            this.modifyConstraintFilesForAggregationModel_inner("RD24", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_SD1ATT";
            this.modifyConstraintFilesForAggregationModel_inner("SD1-ATT", inputDirectoryOffset);

        // no constraint versions of above....

            inputDirectoryOffset = "testCase_SD1_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD25_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_StToSt_plus_weightBreaks_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_negCDI_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_originalConstraintFile_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_USAndCAShipments_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_810_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_dummyShipments_noConstraint";
            this.modifyConstraintFilesForAggregationModel_inner("", inputDirectoryOffset);

    }

    public void modifyConstraintFilesForAggregationModel_inner(String constraintFileName, String subdirectory) throws Exception {

        if(constraintFileName==null || constraintFileName.length()==0) {
            return;
        }

        System.out.println("modifyConstraintFilesForAggregationModel_inner: subdirectory="+subdirectory+" constraintFileName="+constraintFileName);

        String constraintFileName_output = constraintFileName+"_new.csv";

        String dir_read = "C:\\Old Drive\\rosa.charles\\workspace_git\\AggregationOptimization_Data\\Input_test\\";

        dir_read = dir_read+subdirectory+"\\";

        String fileName = dir_read+constraintFileName+".csv";

        String fileName_output = dir_read+constraintFileName_output;

        FileWriter fstream = new FileWriter(fileName_output, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        StringBuffer buffer_header = new StringBuffer();

        buffer_header.append("AGG_JOB_ID,");
        buffer_header.append("ACTIVE_IND,");
        buffer_header.append("CONSTRAINT_ID,");
        buffer_header.append("CONSTRAINT_DIMENSION,");
        buffer_header.append("CONSTRAINT_TYPE,");
        buffer_header.append("DIMENSION_SCOPE,");
        buffer_header.append("DIMENSION_UPPER_LIMIT,");
        buffer_header.append("DIMENSION_LOWER_LIMIT,");
        buffer_header.append("PRIORITY,");
        buffer_header.append("AMC_CONSTRAINT_IND,");

        myPrintWriter.println(buffer_header);

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            // read the header line
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String JOB_ID = "";
                String AGGREGATION_MODEL_JOB_ID = "";
                String EFFECTIVE_DATE = "";
                String ACTIVE_IND = "";
                String CONSTRAINT_ID = "";
                String ConstraintDimension = "";
                String ConstraintType = "";
                String Dimension_Scope = "";
                String Dimension_Upper_Limit = "";
                String Dimension_Lower_Limit = "";
                String Priority = "";

                String[] val = dbRecord.split(",");

                try {
                    JOB_ID = val[0].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    AGGREGATION_MODEL_JOB_ID = val[1].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    EFFECTIVE_DATE = val[2].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    ACTIVE_IND = val[3].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    CONSTRAINT_ID = val[4].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    ConstraintDimension = val[5].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    ConstraintType = val[6].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    Dimension_Scope = val[7].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    Dimension_Upper_Limit = val[8].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    Dimension_Lower_Limit = val[9].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    Priority = val[10].trim();
                } catch(Exception ex) {
                    // do nothing
                }

                StringBuffer buffer = new StringBuffer();
                buffer.append(JOB_ID+",");
                buffer.append(ACTIVE_IND+",");
                buffer.append(CONSTRAINT_ID+",");
                buffer.append(ConstraintDimension+",");
                buffer.append(ConstraintType+",");
                buffer.append(Dimension_Scope+",");
                buffer.append(Dimension_Upper_Limit+",");
                buffer.append(Dimension_Lower_Limit+",");
                buffer.append(Priority+",");
                buffer.append("N"+",");

                myPrintWriter.println(buffer);


            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        myPrintWriter.close();
        fstream.close();

        int temp_a = 0;
    }

    public void modifyShipmentFilesForAggregationModel() throws Exception {
            String inputDirectoryOffset = "testCase_SD1";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD25";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

        /*
            inputDirectoryOffset = "testCase_319_oddSolution";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_223", inputDirectoryOffset);
        */

            //inputDirectoryOffset = "testCase_48_notInfeasibleAsExpected";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_41", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD17_infeasible2";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_257", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_StToSt_plus_weightBreaks";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_SD1_notProducingClusterAssignmentsToShipments";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_86", inputDirectoryOffset);

            //inputDirectoryOffset = "testCase_negCDI";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_757", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_originalConstraintFile";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_USAndCAShipments";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202_US_and_CAShipments", inputDirectoryOffset);

            //inputDirectoryOffset = "testCase_810";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_810", inputDirectoryOffset);

            //inputDirectoryOffset = "testCase_dummyShipments";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_negCDI_2";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_853", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD123";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822_addedBillingType", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD221";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822_addedBillingType", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD417";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822_addedBillingType", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD116";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822_addedBillingType", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD172";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822_addedBillingType", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD662";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822_addedBillingType", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD343";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_796", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_Shipment_File_All_Bad_Data";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_796_error", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_Shipment_File_Empty_Case";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_796_error", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_SD3";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_748", inputDirectoryOffset);

            //inputDirectoryOffset = "testCase_1028";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_1028", inputDirectoryOffset);

            //inputDirectoryOffset = "testCase_longRunTime_24";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_172", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD17_infeasible";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_184", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD24Problem";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_86", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_SD1ATT";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_1028", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_SD1_noConstraint";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_RD25_noConstraint";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_StToSt_plus_weightBreaks_noConstraint";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_negCDI_noConstraint";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_757", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_originalConstraintFile_noConstraint";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202", inputDirectoryOffset);

            inputDirectoryOffset = "testCase_USAndCAShipments_noConstraint";
            this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_202_US_and_CAShipments", inputDirectoryOffset);

            //inputDirectoryOffset = "testCase_810_noConstraint";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_Proposal_810", inputDirectoryOffset);

            //inputDirectoryOffset = "testCase_dummyShipments_noConstraint";
            //this.modifyShipmentFilesForAggregationModel_inner("Input_Shipment_File_proposal_822", inputDirectoryOffset);

    }

    public void modifyShipmentFilesForAggregationModel_inner(String shipmentFileName, String subdirectory) throws Exception {

        System.out.println("modifyShipmentFilesForAggregationModel: subdirectory="+subdirectory+" shipmentFileName="+shipmentFileName);

        String shipmentFileName_output = shipmentFileName+"_new.csv";

        String dir_read = "C:\\Old Drive\\rosa.charles\\workspace_git\\AggregationOptimization_Data\\Input_test\\";

        dir_read = dir_read+subdirectory+"\\";

        String fileName = dir_read+shipmentFileName+".csv";

        String fileName_output = dir_read+shipmentFileName_output;

        FileWriter fstream = new FileWriter(fileName_output, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        StringBuffer buffer_header = new StringBuffer();

        buffer_header.append("AGG_JOB_ID,");
        buffer_header.append("RATL_SHIPMENT_ID,");
        buffer_header.append("OZIP,");
        buffer_header.append("OZIP3,");
        buffer_header.append("OSTATE,");
        buffer_header.append("OZIP1,");
        buffer_header.append("OCNTRY,");
        buffer_header.append("OADDRESS,");
        buffer_header.append("DZIP,");
        buffer_header.append("DZIP3,");
        buffer_header.append("DSTATE,");
        buffer_header.append("DZIP1,");
        buffer_header.append("DCNTRY,");
        buffer_header.append("DADDRESS,");
        buffer_header.append("WEIGHT,");
        buffer_header.append("CLASS_WGTED,");
        buffer_header.append("BILLING_TYPE,");
        buffer_header.append("CUST_DIV,");
        buffer_header.append("PALLET_CNT,");
        buffer_header.append("AMC_IND,");
        buffer_header.append("DISCOUNT_TO_CLUSTER,");

        myPrintWriter.println(buffer_header);

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            // read the header line
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String SHPMT_INSTC_ID = "";
                String OZIP = "";
                String OZIP3 = "";
                String OSTATE = "";
                String OZIP1 = "";
                String OCNTRY = "";
                String OADDRESS = "";
                String DZIP = "";
                String DZIP3 = "";
                String DSTATE = "";
                String DZIP1 = "";
                String DCNTRY = "";
                String DADDRESS = "";
                String WEIGHT = "";
                String CLASS_WGTED = "";
                String BILLING_TYPE = "";
                String CUST_DIV = "";
                String PALLET_CNT = "";
                String OCUSTOM_GEO_ID = "";
                String DCUSTOM_GEO_ID = "";
                String CLUSTER_DISCOUNT_PCT = "";

                String[] val = dbRecord.split(",");

                try {
                    SHPMT_INSTC_ID = val[0].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    OZIP = val[1].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    OZIP3 = val[2].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    OSTATE = val[3].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    OZIP1 = val[4].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    OCNTRY = val[5].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    OADDRESS = val[6].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    DZIP = val[7].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    DZIP3 = val[8].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    DSTATE = val[9].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    DZIP1 = val[10].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    DCNTRY = val[11].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    DADDRESS = val[12].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    WEIGHT = val[13].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    CLASS_WGTED = val[14].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    BILLING_TYPE = val[15].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    CUST_DIV = val[16].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    PALLET_CNT = val[17].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    OCUSTOM_GEO_ID = val[18].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    DCUSTOM_GEO_ID = val[19].trim();
                } catch(Exception ex) {
                    // do nothing
                }
                try {
                    CLUSTER_DISCOUNT_PCT = val[20].trim();
                } catch(Exception ex) {
                    // do nothing
                }

                StringBuffer buffer = new StringBuffer();
                buffer.append(1+",");
                buffer.append(SHPMT_INSTC_ID+",");
                buffer.append(OZIP+",");
                buffer.append(OZIP3+",");
                buffer.append(OSTATE+",");
                buffer.append(OZIP1+",");
                buffer.append(OCNTRY+",");
                buffer.append(OADDRESS+",");
                buffer.append(DZIP+",");
                buffer.append(DZIP3+",");
                buffer.append(DSTATE+",");
                buffer.append(DZIP1+",");
                buffer.append(DCNTRY+",");
                buffer.append(DADDRESS+",");
                buffer.append(WEIGHT+",");
                buffer.append(CLASS_WGTED+",");
                buffer.append(BILLING_TYPE+",");
                buffer.append(CUST_DIV+",");
                buffer.append(PALLET_CNT+",");
                buffer.append("N"+",");
                buffer.append(CLUSTER_DISCOUNT_PCT+",");

                myPrintWriter.println(buffer);


            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        myPrintWriter.close();
        fstream.close();

        int temp_a = 0;
    }

    public void runLHO_RollUpSolutionCostOutput(String typeOfAnalysis, int runID_lb, int runID_ub) throws Exception {

        //openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\DiversionsVsNoDiversions\\", "DiversionsVsNoDiversions.csv");

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelGroupID"+",");
        buffer_header.append("ModelRunID"+",");
        buffer_header.append("ModelDate"+",");

        buffer_header.append("LH Cost (base)"+",");
        buffer_header.append("HSS Cost (base)"+",");
        buffer_header.append("HSS Cost Prorated (base)"+",");
        buffer_header.append("Total Cost (base)"+",");
        buffer_header.append("Total Cost Prorated (base)"+",");
        buffer_header.append("LH TU (base)"+",");
        buffer_header.append("LH Pup Count (base)"+",");
        buffer_header.append("HSS TU (base)"+",");
        buffer_header.append("HSS Van Count (base)"+",");
        buffer_header.append("LH+HSS TU (base)"+",");

        SortedMap<String, String> myMapSubDirectoryOffsetsByScenario = null;
        SortedMap<String, Double> myMapOfPupCubeByScenario = null;
        SortedMap<String, Double> myMapOfVanCubeByScenario = null;

        if(typeOfAnalysis.equals(Constant.TypeOfScenarios.cubeExpansion)) {
            myMapSubDirectoryOffsetsByScenario = this.myMapOfSubDirectoryOffsetByCubeExpansionScenario;
            myMapOfPupCubeByScenario = this.myMapOfPupCubeByCubeExpansionScenario;
            myMapOfVanCubeByScenario = this.myMapOfVanCubeByCubeExpansionScenario;
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\CostData_OB_model_MoreCubePerTrailer\\", "MoreCubePerTrailer_costImpact_variousPercentages_Improvement_cost_TU.csv");
        } else if(typeOfAnalysis.equals(Constant.TypeOfScenarios.daylinehaul_limitations)) {
            myMapSubDirectoryOffsetsByScenario = this.myMapOfSubDirectoryOffsetByDayLineHaulLimitsScenario;
            myMapOfPupCubeByScenario = this.myMapOfPupCubeByDayLineHaulLimitsScenario;
            myMapOfVanCubeByScenario = this.myMapOfVanCubeByDayLineHaulLimitsScenario;
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\CostData_OB_model_LimitsOnDayLineHaulDriverAvailability\\", "LimitationsOnDayLineHaulDriverUsage_costImpact_variousPercentages_Improvement_cost_TU.csv");
        }

        String myBaseScenarioName = null;

        int scenarioCounter=0;
        for(String myScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

            scenarioCounter++;

            if(scenarioCounter==1) {
                myBaseScenarioName = myScenario;
                continue;
            }

            buffer_header.append("LH Cost (Scenario_"+myScenario+")"+",");
            buffer_header.append("HSS Cost (Scenario_"+myScenario+")"+",");
            buffer_header.append("HSS Cost Prorated (Scenario_"+myScenario+")"+",");
            buffer_header.append("Total Cost (Scenario_"+myScenario+")"+",");
            buffer_header.append("Total Cost Prorated (Scenario_"+myScenario+")"+",");
            buffer_header.append("Total Cost Diff (Scenario_"+myScenario+")"+",");
            buffer_header.append("Total Cost Prorated Diff (Scenario_"+myScenario+")"+",");
            buffer_header.append("LH TU (Scenario_"+myScenario+")"+",");
            buffer_header.append("LH Pup Count (Scenario_"+myScenario+")"+",");
            buffer_header.append("HSS TU (Scenario_"+myScenario+")"+",");
            buffer_header.append("HSS Van Count (Scenario_"+myScenario+")"+",");
            buffer_header.append("LH+HSS TU (Scenario_"+myScenario+")"+",");

        }

        this.logFile.println(buffer_header.toString());

        for (int id = runID_lb; id <= runID_ub; id=id+1){

            String runId = id+"";
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";

            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            StringBuffer myInLevelBuffer = new StringBuffer();
            myInLevelBuffer.append(myModelGroupID+",");
            myInLevelBuffer.append(id+",");
            myInLevelBuffer.append(myDateForThisRunID+",");

            Map<String, Double> myLHCostByFractionOfTrailer = new HashMap<String, Double>();
            Map<String, Double> myHSSCostByFractionOfTrailer = new HashMap<String, Double>();
            Map<String, Double> myHSSCostProratedByFractionOfTrailer = new HashMap<String, Double>();

            Map<String, Double> myLHTUByFractionOfTrailer = new HashMap<String, Double>();
            Map<String, Double> myLHPupCountByFractionOfTrailer = new HashMap<String, Double>();
            Map<String, Double> myHSSTUByFractionOfTrailer = new HashMap<String, Double>();
            Map<String, Double> myHSSVanCountByFractionOfTrailer = new HashMap<String, Double>();

            for(String myScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

                String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+myMapSubDirectoryOffsetsByScenario.get(myScenario);

                StringBuffer buffer = new StringBuffer();

                buffer.append(myModelGroupID+",");
                buffer.append(id+",");

                Map<String, Double> myMapOfSummaryStats = this.readJamieFileSummary_returnMapOfValues(dir_OptimOutput, "Total", null);
                Map<String, Double> myMapOfSummaryStats_HSS = this.readJamieHSSFileSummary_returnMapOfValues(dir_OptimOutput, "Total", null);

                myLHCostByFractionOfTrailer.put(myScenario, myMapOfSummaryStats.get(LHHssReportBuilder.TotalCost));
                myHSSCostByFractionOfTrailer.put(myScenario, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_Cost));
                myHSSCostProratedByFractionOfTrailer.put(myScenario, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_Cost_prorated));

                myLHTUByFractionOfTrailer.put(myScenario, myMapOfSummaryStats.get(LHHssReportBuilder.TU));
                myLHPupCountByFractionOfTrailer.put(myScenario, myMapOfSummaryStats.get(LHHssReportBuilder.LoadedPupCount));
                myHSSTUByFractionOfTrailer.put(myScenario, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_TU));
                myHSSVanCountByFractionOfTrailer.put(myScenario, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.Loaded_HSS));

            }

            Double LHCost_base = myLHCostByFractionOfTrailer.get(myBaseScenarioName);
            Double HSSCost_base = myHSSCostByFractionOfTrailer.get(myBaseScenarioName);
            Double HSSCostProrated_base = myHSSCostProratedByFractionOfTrailer.get(myBaseScenarioName);
            Double LHTU_base = myLHTUByFractionOfTrailer.get(myBaseScenarioName);
            Double LHPupCount_base = myLHPupCountByFractionOfTrailer.get(myBaseScenarioName);
            Double HSSTU_base = myHSSTUByFractionOfTrailer.get(myBaseScenarioName);
            Double HSSVanCount_base = myHSSVanCountByFractionOfTrailer.get(myBaseScenarioName);

            double scalingFactor_base = myMapOfVanCubeByScenario.get(myBaseScenarioName)/(myMapOfPupCubeByScenario.get(myBaseScenarioName));

            Double LHHSSTU_base = (LHPupCount_base*LHTU_base + HSSVanCount_base*HSSTU_base*(scalingFactor_base))/(LHPupCount_base + HSSVanCount_base*(scalingFactor_base));

            StringBuffer myMetricsBuffer_base = new StringBuffer();
            myMetricsBuffer_base.append(LHCost_base+",");
            myMetricsBuffer_base.append(HSSCost_base+",");
            myMetricsBuffer_base.append(HSSCostProrated_base+",");
            Double totalCost_base = LHCost_base+HSSCost_base;
            Double totalCostProrated_base = LHCost_base+HSSCostProrated_base;
            myMetricsBuffer_base.append(totalCost_base+",");
            myMetricsBuffer_base.append(totalCostProrated_base+",");

            myMetricsBuffer_base.append(LHTU_base + ",");
            myMetricsBuffer_base.append(LHPupCount_base + ",");
            myMetricsBuffer_base.append(HSSTU_base + ",");
            myMetricsBuffer_base.append(HSSVanCount_base + ",");
            myMetricsBuffer_base.append(LHHSSTU_base + ",");

            StringBuffer myMetricsBuffer_EnhancedSpace = new StringBuffer();

            for(String myScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

                if(myScenario.equals(myBaseScenarioName)) {
                    continue;
                }

                Double LHCost_thisScenario = myLHCostByFractionOfTrailer.get(myScenario);
                Double HSSCost_thisScenario = myHSSCostByFractionOfTrailer.get(myScenario);
                Double HSSCostProrated_thisScenario = myHSSCostProratedByFractionOfTrailer.get(myScenario);
                Double LHTU_thisScenario = myLHTUByFractionOfTrailer.get(myScenario);
                Double LHPupCount_thisScenario = myLHPupCountByFractionOfTrailer.get(myScenario);
                Double HSSTU_thisScenario = myHSSTUByFractionOfTrailer.get(myScenario);
                Double HSSVanCount_thisScenario = myHSSVanCountByFractionOfTrailer.get(myScenario);

                double scalingFactor_enhancedSpace = myMapOfVanCubeByScenario.get(myScenario)/(myMapOfPupCubeByScenario.get(myScenario));

                Double LHHSSTU_thisScenario = (LHPupCount_thisScenario*LHTU_thisScenario + HSSVanCount_thisScenario*HSSTU_thisScenario*(scalingFactor_enhancedSpace))/(LHPupCount_thisScenario + HSSVanCount_thisScenario*(scalingFactor_enhancedSpace));

                myMetricsBuffer_EnhancedSpace.append(LHCost_thisScenario + ",");
                myMetricsBuffer_EnhancedSpace.append(HSSCost_thisScenario + ",");
                myMetricsBuffer_EnhancedSpace.append(HSSCostProrated_thisScenario + ",");
                Double totalCost_EnhancedSpace = LHCost_thisScenario+HSSCost_thisScenario;
                Double totalCostProrated_EnhancedSpace = LHCost_thisScenario+HSSCostProrated_thisScenario;
                myMetricsBuffer_EnhancedSpace.append(totalCost_EnhancedSpace + ",");
                myMetricsBuffer_EnhancedSpace.append(totalCostProrated_EnhancedSpace + ",");

                myMetricsBuffer_EnhancedSpace.append(totalCost_base - totalCost_EnhancedSpace + ",");
                myMetricsBuffer_EnhancedSpace.append(totalCostProrated_base - totalCostProrated_EnhancedSpace + ",");

                myMetricsBuffer_EnhancedSpace.append(LHTU_thisScenario + ",");
                myMetricsBuffer_EnhancedSpace.append(LHPupCount_thisScenario + ",");
                myMetricsBuffer_EnhancedSpace.append(HSSTU_thisScenario + ",");
                myMetricsBuffer_EnhancedSpace.append(HSSVanCount_thisScenario + ",");
                myMetricsBuffer_EnhancedSpace.append(LHHSSTU_thisScenario + ",");

            }

            this.logFile.println(myInLevelBuffer.toString() + myMetricsBuffer_base.toString() + myMetricsBuffer_EnhancedSpace.toString());

        }
        closeFile();

    }

    public void runLHO_RollUpSolutionTUCOutputByShift(String typeOfAnalysis, int runID_lb, int runID_ub) throws Exception {

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelGroupID"+",");
        buffer_header.append("ModelRunID"+",");
        buffer_header.append("ModelDate"+",");
        buffer_header.append("Shift"+",");

        buffer_header.append("LH TU (base)"+",");
        buffer_header.append("LH Pup Count (base)"+",");
        buffer_header.append("HSS TU (base)"+",");
        buffer_header.append("HSS Van Count (base)"+",");
        buffer_header.append("LH+HSS TU (base)"+",");

        SortedMap<String, String> myMapSubDirectoryOffsetsByScenario = null;
        SortedMap<String, Double> myMapOfPupCubeByScenario = null;
        SortedMap<String, Double> myMapOfVanCubeByScenario = null;

        if(typeOfAnalysis.equals(Constant.TypeOfScenarios.cubeExpansion)) {
            myMapSubDirectoryOffsetsByScenario = this.myMapOfSubDirectoryOffsetByCubeExpansionScenario;
            myMapOfPupCubeByScenario = this.myMapOfPupCubeByCubeExpansionScenario;
            myMapOfVanCubeByScenario = this.myMapOfVanCubeByCubeExpansionScenario;
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\CostData_OB_model_MoreCubePerTrailer\\", "MoreCubePerTrailer_variousPercentages_PupVanCapacityImprovement_TU.csv");
        } else if(typeOfAnalysis.equals(Constant.TypeOfScenarios.daylinehaul_limitations)) {
            myMapSubDirectoryOffsetsByScenario = this.myMapOfSubDirectoryOffsetByDayLineHaulLimitsScenario;
            myMapOfPupCubeByScenario = this.myMapOfPupCubeByDayLineHaulLimitsScenario;
            myMapOfVanCubeByScenario = this.myMapOfVanCubeByDayLineHaulLimitsScenario;
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\CostData_OB_model_LimitsOnDayLineHaulDriverAvailability\\", "LimitationsOnDayLineHaulDriverUsage_variousPercentages_PupVanCapacityImprovement_TU.csv");
        }

        String myBaseScenarioName = null;

        int scenarioCounter=0;
        for(String myScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

            scenarioCounter++;

            if(scenarioCounter==1) {
                myBaseScenarioName = myScenario;
                continue;
            }

            buffer_header.append("LH TU (Scenario_"+myScenario+")"+",");
            buffer_header.append("LH Pup Count (Scenario_"+myScenario+")"+",");
            buffer_header.append("HSS TU (Scenario_"+myScenario+")"+",");
            buffer_header.append("HSS Van Count (Scenario_"+myScenario+")"+",");
            buffer_header.append("LH+HSS TU (Scenario_"+myScenario+")"+",");

        }

        this.logFile.println(buffer_header.toString());

        List<String> myListOfShifts = new ArrayList<String>();
        //myListOfShifts.add("O");
        //myListOfShifts.add("F");
        //myListOfShifts.add("I");
        //myListOfShifts.add("D");
        myListOfShifts.add("Total");

        for (int id = runID_lb; id <= runID_ub; id=id+1){

            String runId = id+"";
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";

            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            if(!myModelGroupID.equals("PST")) {
                continue;
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            StringBuffer myInLevelBuffer = new StringBuffer();
            myInLevelBuffer.append(myModelGroupID+",");
            myInLevelBuffer.append(id+",");
            myInLevelBuffer.append(myDateForThisRunID+",");

            Map<String, Map<String, Double>> myLHTUByFractionOfTrailerByShift = new HashMap<String, Map<String, Double>>();
            Map<String, Map<String, Double>> myLHPupCountByFractionOfTrailerByShift = new HashMap<String, Map<String, Double>>();
            Map<String, Map<String, Double>> myHSSTUByFractionOfTrailerByShift = new HashMap<String, Map<String, Double>>();
            Map<String, Map<String, Double>> myHSSVanCountByFractionOfTrailerByShift = new HashMap<String, Map<String, Double>>();

            for(String myShift : myListOfShifts) {

                Map<String, Double> myLHTUByFractionOfTrailer = myLHTUByFractionOfTrailerByShift.get(myShift);
                if(myLHTUByFractionOfTrailer == null) {
                    myLHTUByFractionOfTrailer = new HashMap<String, Double>();
                    myLHTUByFractionOfTrailerByShift.put(myShift, myLHTUByFractionOfTrailer);
                }

                Map<String, Double> myLHPupCountByFractionOfTrailer = myLHPupCountByFractionOfTrailerByShift.get(myShift);
                if(myLHPupCountByFractionOfTrailer == null) {
                    myLHPupCountByFractionOfTrailer = new HashMap<String, Double>();
                    myLHPupCountByFractionOfTrailerByShift.put(myShift, myLHPupCountByFractionOfTrailer);
                }

                Map<String, Double> myHSSTUByFractionOfTrailer = myHSSTUByFractionOfTrailerByShift.get(myShift);
                if(myHSSTUByFractionOfTrailer == null) {
                    myHSSTUByFractionOfTrailer = new HashMap<String, Double>();
                    myHSSTUByFractionOfTrailerByShift.put(myShift, myHSSTUByFractionOfTrailer);
                }

                Map<String, Double> myHSSVanCountByFractionOfTrailer = myHSSVanCountByFractionOfTrailerByShift.get(myShift);
                if(myHSSVanCountByFractionOfTrailer == null) {
                    myHSSVanCountByFractionOfTrailer = new HashMap<String, Double>();
                    myHSSVanCountByFractionOfTrailerByShift.put(myShift, myHSSVanCountByFractionOfTrailer);
                }

                for(String myCubeExpansionScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

                    String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+myMapSubDirectoryOffsetsByScenario.get(myCubeExpansionScenario);

                    Map<String, Double> myMapOfSummaryStats = this.readJamieFileSummary_returnMapOfValues(dir_OptimOutput, myShift, null);
                    Map<String, Double> myMapOfSummaryStats_HSS = this.readJamieHSSFileSummary_returnMapOfValues(dir_OptimOutput, myShift, null);

                    myLHTUByFractionOfTrailer.put(myCubeExpansionScenario, myMapOfSummaryStats.get(LHHssReportBuilder.TU));
                    myLHPupCountByFractionOfTrailer.put(myCubeExpansionScenario, myMapOfSummaryStats.get(LHHssReportBuilder.LoadedPupCount));
                    myHSSTUByFractionOfTrailer.put(myCubeExpansionScenario, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_TU));
                    myHSSVanCountByFractionOfTrailer.put(myCubeExpansionScenario, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.Loaded_HSS));

                }

            }

            for(String myShift : myListOfShifts) {

                Map<String, Double> myLHTUByFractionOfTrailer = myLHTUByFractionOfTrailerByShift.get(myShift);

                Map<String, Double> myLHPupCountByFractionOfTrailer = myLHPupCountByFractionOfTrailerByShift.get(myShift);

                Map<String, Double> myHSSTUByFractionOfTrailer = myHSSTUByFractionOfTrailerByShift.get(myShift);

                Map<String, Double> myHSSVanCountByFractionOfTrailer = myHSSVanCountByFractionOfTrailerByShift.get(myShift);

                Double LHTU_base = myLHTUByFractionOfTrailer.get(myBaseScenarioName);
                Double LHPupCount_base = myLHPupCountByFractionOfTrailer.get(myBaseScenarioName);
                Double HSSTU_base = myHSSTUByFractionOfTrailer.get(myBaseScenarioName);
                Double HSSVanCount_base = myHSSVanCountByFractionOfTrailer.get(myBaseScenarioName);

                double scalingFactor_base = myMapOfVanCubeByScenario.get(myBaseScenarioName)/(myMapOfPupCubeByScenario.get(myBaseScenarioName));

                Double LHHSSTU_Current = (LHPupCount_base*LHTU_base + HSSVanCount_base*HSSTU_base*(scalingFactor_base))/(LHPupCount_base + HSSVanCount_base*(scalingFactor_base));

                StringBuffer myMetricsBuffer_base = new StringBuffer();
                myMetricsBuffer_base.append(myShift+",");
                myMetricsBuffer_base.append(LHTU_base+",");
                myMetricsBuffer_base.append(LHPupCount_base+",");
                myMetricsBuffer_base.append(HSSTU_base+",");
                myMetricsBuffer_base.append(HSSVanCount_base+",");
                myMetricsBuffer_base.append(LHHSSTU_Current+",");

                StringBuffer myMetricsBuffer_EnhancedSpace = new StringBuffer();

                for(String myScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

                    if(myScenario.equals(myBaseScenarioName)) {
                        continue;
                    }

                    Double LHTU_EnhancedSpace = myLHTUByFractionOfTrailer.get(myScenario);
                    Double LHPupCount_EnhancedSpace = myLHPupCountByFractionOfTrailer.get(myScenario);
                    Double HSSTU_EnhancedSpace = myHSSTUByFractionOfTrailer.get(myScenario);
                    Double HSSVanCount_EnhancedSpace = myHSSVanCountByFractionOfTrailer.get(myScenario);

                    double scalingFactor_enhancedSpace = myMapOfVanCubeByScenario.get(myScenario)/(myMapOfPupCubeByScenario.get(myScenario));

                    Double LHHSSTU_EnhancedSpace = null;
                    try {
                        LHHSSTU_EnhancedSpace = (LHPupCount_EnhancedSpace*LHTU_EnhancedSpace + HSSVanCount_EnhancedSpace*HSSTU_EnhancedSpace*(scalingFactor_enhancedSpace))/(LHPupCount_EnhancedSpace + HSSVanCount_EnhancedSpace*(scalingFactor_enhancedSpace));
                    } catch(Exception ex) {
                        // don't worry...just skip this
                    }

                    myMetricsBuffer_EnhancedSpace.append(LHTU_EnhancedSpace + ",");
                    myMetricsBuffer_EnhancedSpace.append(LHPupCount_EnhancedSpace + ",");
                    myMetricsBuffer_EnhancedSpace.append(HSSTU_EnhancedSpace + ",");
                    myMetricsBuffer_EnhancedSpace.append(HSSVanCount_EnhancedSpace + ",");
                    myMetricsBuffer_EnhancedSpace.append(LHHSSTU_EnhancedSpace + ",");

                }

                this.logFile.println(myInLevelBuffer.toString() + myMetricsBuffer_base.toString() + myMetricsBuffer_EnhancedSpace.toString());

            }

        }
        closeFile();

    }

    public void runLHO_RollUpSolutionTUCOutputByShiftByDate(String typeOfAnalysis, int runID_lb, int runID_ub) throws Exception {

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelDate"+",");
        buffer_header.append("Shift"+",");
        buffer_header.append("LH+HSS TU (base)"+",");

        SortedMap<String, String> myMapSubDirectoryOffsetsByScenario = null;
        SortedMap<String, Double> myMapOfPupCubeByScenario = null;
        SortedMap<String, Double> myMapOfVanCubeByScenario = null;

        if(typeOfAnalysis.equals(Constant.TypeOfScenarios.cubeExpansion)) {
            myMapSubDirectoryOffsetsByScenario = this.myMapOfSubDirectoryOffsetByCubeExpansionScenario;
            myMapOfPupCubeByScenario = this.myMapOfPupCubeByCubeExpansionScenario;
            myMapOfVanCubeByScenario = this.myMapOfVanCubeByCubeExpansionScenario;
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\CostData_OB_model_MoreCubePerTrailer\\", "MoreCubePerTrailer_variousPercentages_PupVanCapacityImprovement_TUByDate.csv");
        } else if(typeOfAnalysis.equals(Constant.TypeOfScenarios.daylinehaul_limitations)) {
            myMapSubDirectoryOffsetsByScenario = this.myMapOfSubDirectoryOffsetByDayLineHaulLimitsScenario;
            myMapOfPupCubeByScenario = this.myMapOfPupCubeByDayLineHaulLimitsScenario;
            myMapOfVanCubeByScenario = this.myMapOfVanCubeByDayLineHaulLimitsScenario;
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\CostData_OB_model_LimitsOnDayLineHaulDriverAvailability\\", "LimitationsOnDayLineHaulDriverUsage_variousPercentages_PupVanCapacityImprovement_TUByDate.csv");
        }

        String myBaseScenarioName = null;

        int scenarioCounter=0;
        for(String myScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

            scenarioCounter++;

            if(scenarioCounter==1) {
                myBaseScenarioName = myScenario;
                continue;
            }

            buffer_header.append("LH+HSS TU (Scenario_"+myScenario+")"+",");
        }

        this.logFile.println(buffer_header.toString());

        List<String> myListOfShifts = new ArrayList<String>();
        myListOfShifts.add("O");
        myListOfShifts.add("F");
        myListOfShifts.add("I");
        myListOfShifts.add("D");

        SortedMap<Date, Map<String, Map<String, Map<String, Double>>>> myLHTUByModelGroupByFractionOfTrailerByShiftByDate = new TreeMap<Date, Map<String, Map<String, Map<String, Double>>>>();
        SortedMap<Date, Map<String, Map<String, Map<String, Double>>>> myLHPupCountByModelGroupByFractionOfTrailerByShiftByDate = new TreeMap<Date, Map<String, Map<String, Map<String, Double>>>>();
        SortedMap<Date, Map<String, Map<String, Map<String, Double>>>>  myHSSTUByModelGroupByFractionOfTrailerByShiftByDate = new TreeMap<Date, Map<String, Map<String, Map<String, Double>>>>();
        SortedMap<Date, Map<String, Map<String, Map<String, Double>>>>  myHSSVanCountByModelGroupByFractionOfTrailerByShiftByDate = new TreeMap<Date, Map<String, Map<String, Map<String, Double>>>>();

        for (int id = runID_lb; id <= runID_ub; id=id+1){

            String runId = id+"";
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";

            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            if(!myModelGroupID.equals("PST")) {
                continue;
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            Map<String, Map<String, Map<String, Double>>> myLHTUByModelGroupByFractionOfTrailerByShift = myLHTUByModelGroupByFractionOfTrailerByShiftByDate.get(myDateForThisRunID);
            if(myLHTUByModelGroupByFractionOfTrailerByShift == null) {
                myLHTUByModelGroupByFractionOfTrailerByShift = new HashMap<String, Map<String, Map<String, Double>>>();
                myLHTUByModelGroupByFractionOfTrailerByShiftByDate.put(myDateForThisRunID, myLHTUByModelGroupByFractionOfTrailerByShift);
            }

            Map<String, Map<String, Map<String, Double>>> myLHPupCountByModelGroupByFractionOfTrailerByShift = myLHPupCountByModelGroupByFractionOfTrailerByShiftByDate.get(myDateForThisRunID);
            if(myLHPupCountByModelGroupByFractionOfTrailerByShift == null) {
                myLHPupCountByModelGroupByFractionOfTrailerByShift = new HashMap<String, Map<String, Map<String, Double>>>();
                myLHPupCountByModelGroupByFractionOfTrailerByShiftByDate.put(myDateForThisRunID, myLHPupCountByModelGroupByFractionOfTrailerByShift);
            }

            Map<String, Map<String, Map<String, Double>>> myHSSTUByModelGroupByFractionOfTrailerByShift = myHSSTUByModelGroupByFractionOfTrailerByShiftByDate.get(myDateForThisRunID);
            if(myHSSTUByModelGroupByFractionOfTrailerByShift == null) {
                myHSSTUByModelGroupByFractionOfTrailerByShift = new HashMap<String, Map<String, Map<String, Double>>>();
                myHSSTUByModelGroupByFractionOfTrailerByShiftByDate.put(myDateForThisRunID, myHSSTUByModelGroupByFractionOfTrailerByShift);
            }

            Map<String, Map<String, Map<String, Double>>> myHSSVanCountByModelGroupByFractionOfTrailerByShift = myHSSVanCountByModelGroupByFractionOfTrailerByShiftByDate.get(myDateForThisRunID);
            if(myHSSVanCountByModelGroupByFractionOfTrailerByShift == null) {
                myHSSVanCountByModelGroupByFractionOfTrailerByShift = new HashMap<String, Map<String, Map<String, Double>>>();
                myHSSVanCountByModelGroupByFractionOfTrailerByShiftByDate.put(myDateForThisRunID, myHSSVanCountByModelGroupByFractionOfTrailerByShift);
            }

            for(String myShift : myListOfShifts) {

                Map<String, Map<String, Double>> myLHTUByModelGroupByFractionOfTrailer = myLHTUByModelGroupByFractionOfTrailerByShift.get(myShift);
                if(myLHTUByModelGroupByFractionOfTrailer == null) {
                    myLHTUByModelGroupByFractionOfTrailer = new HashMap<String, Map<String, Double>>();
                    myLHTUByModelGroupByFractionOfTrailerByShift.put(myShift, myLHTUByModelGroupByFractionOfTrailer);
                }

                Map<String, Map<String, Double>> myLHPupCountByModelGroupByFractionOfTrailer = myLHPupCountByModelGroupByFractionOfTrailerByShift.get(myShift);
                if(myLHPupCountByModelGroupByFractionOfTrailer == null) {
                    myLHPupCountByModelGroupByFractionOfTrailer = new HashMap<String, Map<String, Double>>();
                    myLHPupCountByModelGroupByFractionOfTrailerByShift.put(myShift, myLHPupCountByModelGroupByFractionOfTrailer);
                }

                Map<String, Map<String, Double>> myHSSTUByModelGroupByFractionOfTrailer = myHSSTUByModelGroupByFractionOfTrailerByShift.get(myShift);
                if(myHSSTUByModelGroupByFractionOfTrailer == null) {
                    myHSSTUByModelGroupByFractionOfTrailer = new HashMap<String, Map<String, Double>>();
                    myHSSTUByModelGroupByFractionOfTrailerByShift.put(myShift, myHSSTUByModelGroupByFractionOfTrailer);
                }

                Map<String, Map<String, Double>> myHSSVanCountByModelGroupByFractionOfTrailer = myHSSVanCountByModelGroupByFractionOfTrailerByShift.get(myShift);
                if(myHSSVanCountByModelGroupByFractionOfTrailer == null) {
                    myHSSVanCountByModelGroupByFractionOfTrailer = new HashMap<String, Map<String, Double>>();
                    myHSSVanCountByModelGroupByFractionOfTrailerByShift.put(myShift, myHSSVanCountByModelGroupByFractionOfTrailer);
                }

                for(String myCubeExpansionScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

                    Map<String, Double> myLHTUByModelGroup = myLHTUByModelGroupByFractionOfTrailer.get(myCubeExpansionScenario);
                    if(myLHTUByModelGroup == null) {
                        myLHTUByModelGroup = new HashMap<String, Double>();
                        myLHTUByModelGroupByFractionOfTrailer.put(myCubeExpansionScenario, myLHTUByModelGroup);
                    }

                    Map<String, Double> myLHPupCountByModelGroup = myLHPupCountByModelGroupByFractionOfTrailer.get(myCubeExpansionScenario);
                    if(myLHPupCountByModelGroup == null) {
                        myLHPupCountByModelGroup = new HashMap<String, Double>();
                        myLHPupCountByModelGroupByFractionOfTrailer.put(myCubeExpansionScenario, myLHPupCountByModelGroup);
                    }

                    Map<String, Double> myHSSTUByModelGroup = myHSSTUByModelGroupByFractionOfTrailer.get(myCubeExpansionScenario);
                    if(myHSSTUByModelGroup == null) {
                        myHSSTUByModelGroup = new HashMap<String, Double>();
                        myHSSTUByModelGroupByFractionOfTrailer.put(myCubeExpansionScenario, myHSSTUByModelGroup);
                    }

                    Map<String, Double> myHSSVanCountByModelGroup = myHSSVanCountByModelGroupByFractionOfTrailer.get(myCubeExpansionScenario);
                    if(myHSSVanCountByModelGroup == null) {
                        myHSSVanCountByModelGroup = new HashMap<String, Double>();
                        myHSSVanCountByModelGroupByFractionOfTrailer.put(myCubeExpansionScenario, myHSSVanCountByModelGroup);
                    }

                    String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+myMapSubDirectoryOffsetsByScenario.get(myCubeExpansionScenario);

                    Map<String, Double> myMapOfSummaryStats = this.readJamieFileSummary_returnMapOfValues(dir_OptimOutput, myShift, null);
                    Map<String, Double> myMapOfSummaryStats_HSS = this.readJamieHSSFileSummary_returnMapOfValues(dir_OptimOutput, myShift, null);

                    myLHTUByModelGroup.put(myModelGroupID, myMapOfSummaryStats.get(LHHssReportBuilder.TU));
                    myLHPupCountByModelGroup.put(myModelGroupID, myMapOfSummaryStats.get(LHHssReportBuilder.LoadedPupCount));
                    myHSSTUByModelGroup.put(myModelGroupID, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_TU));
                    myHSSVanCountByModelGroup.put(myModelGroupID, myMapOfSummaryStats_HSS.get(LHHssReportBuilder.Loaded_HSS));

                }

            }

        }

        for(Date myDate : myLHTUByModelGroupByFractionOfTrailerByShiftByDate.keySet()) {
            Map<String, Map<String, Map<String, Double>>> myLHTUByModelGroupByFractionOfTrailerByShift = myLHTUByModelGroupByFractionOfTrailerByShiftByDate.get(myDate);

            Map<String, Map<String, Map<String, Double>>> myLHPupCountByModelGroupByFractionOfTrailerByShift = myLHPupCountByModelGroupByFractionOfTrailerByShiftByDate.get(myDate);

            Map<String, Map<String, Map<String, Double>>> myHSSTUByModelGroupByFractionOfTrailerByShift = myHSSTUByModelGroupByFractionOfTrailerByShiftByDate.get(myDate);

            Map<String, Map<String, Map<String, Double>>> myHSSVanCountByModelGroupByFractionOfTrailerByShift = myHSSVanCountByModelGroupByFractionOfTrailerByShiftByDate.get(myDate);

            for(String myShift : myLHTUByModelGroupByFractionOfTrailerByShift.keySet()) {

                Map<String, Map<String, Double>> myLHTUByModelGroupByFractionOfTrailer = myLHTUByModelGroupByFractionOfTrailerByShift.get(myShift);

                Map<String, Map<String, Double>> myLHPupCountByModelGroupByFractionOfTrailer = myLHPupCountByModelGroupByFractionOfTrailerByShift.get(myShift);

                Map<String, Map<String, Double>> myHSSTUByModelGroupByFractionOfTrailer = myHSSTUByModelGroupByFractionOfTrailerByShift.get(myShift);

                Map<String, Map<String, Double>> myHSSVanCountByModelGroupByFractionOfTrailer = myHSSVanCountByModelGroupByFractionOfTrailerByShift.get(myShift);

                Double LHHSSTU_Current = this.getSumOverMap(myLHTUByModelGroupByFractionOfTrailer, myLHPupCountByModelGroupByFractionOfTrailer, myHSSTUByModelGroupByFractionOfTrailer, myHSSVanCountByModelGroupByFractionOfTrailer, Constant.CubeExpansionScenarios.base0_0);

                StringBuffer myMetricsBuffer_base = new StringBuffer();
                myMetricsBuffer_base.append(myDate+",");
                myMetricsBuffer_base.append(myShift+",");
                myMetricsBuffer_base.append(LHHSSTU_Current+",");

                StringBuffer myMetricsBuffer_EnhancedSpace = new StringBuffer();

                for(String myScenario : myMapSubDirectoryOffsetsByScenario.keySet()) {

                    if(myScenario.equals(myBaseScenarioName)) {
                        continue;
                    }

                    Double LHHSSTU_EnhancedSpace = this.getSumOverMap(myLHTUByModelGroupByFractionOfTrailer, myLHPupCountByModelGroupByFractionOfTrailer, myHSSTUByModelGroupByFractionOfTrailer, myHSSVanCountByModelGroupByFractionOfTrailer, myScenario);

                    myMetricsBuffer_EnhancedSpace.append(LHHSSTU_EnhancedSpace + ",");

                }

                this.logFile.println(myMetricsBuffer_base.toString() + myMetricsBuffer_EnhancedSpace.toString());

            }

        }

        closeFile();

    }

    public double getSumOverMap(Map<String, Map<String, Double>> myLHTUByModelGroupByFractionOfTrailer, Map<String, Map<String, Double>> myLHPupCountByModelGroupByFractionOfTrailer,  Map<String, Map<String, Double>> myHSSTUByModelGroupByFractionOfTrailer, Map<String, Map<String, Double>> myHSSVanCountByModelGroupByFractionOfTrailer, String myByFractionOfTrailer) {

        double myScalingFactor = 205.0/100.0;
        if(myByFractionOfTrailer == "EnhancedSpace") {
            myScalingFactor = 207.05/101.0;
        }

        Map<String, Double> myLHTUByModelGroup = myLHTUByModelGroupByFractionOfTrailer.get(myByFractionOfTrailer);
        Map<String, Double> myLHPupCountByModelGroup = myLHPupCountByModelGroupByFractionOfTrailer.get(myByFractionOfTrailer);
        Map<String, Double> myHSSTUByModelGroup = myHSSTUByModelGroupByFractionOfTrailer.get(myByFractionOfTrailer);
        Map<String, Double> myHSSVanCountByModelGroup = myHSSVanCountByModelGroupByFractionOfTrailer.get(myByFractionOfTrailer);

        double myNum = 0.0;
        double myDenom = 0.0;

        for(String myString : myLHTUByModelGroup.keySet()) {
            Double myLHTU = myLHTUByModelGroup.get(myString);
            Double myLHPupCount = myLHPupCountByModelGroup.get(myString);
            Double myHSSTU = myHSSTUByModelGroup.get(myString);
            Double myHSSVanCount = myHSSVanCountByModelGroup.get(myString);

            myNum += myLHPupCount*myLHTU + myHSSVanCount*myHSSTU*myScalingFactor;
            myDenom += myLHPupCount + myHSSVanCount*myScalingFactor;

        }

        double myReturnValue = myNum/(myDenom+LHHssReportBuilder.epsilon);

        return myReturnValue;

    }

    public void runLHO_RollUpSolutionStatistics() throws Exception {
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\SolutionStatistics_DirectsVsNoDirects\\", "SolutionStatistics_DirectsVsNoDirects.csv");

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        this.logFile.println("ModelGroupID, ModelRunID,  TU (Directs), LoadedPupCount (Directs), EmptyPupCount (Directs), SU (Directs), LF (Directs), Empty % (Directs), Total Miles (Directs), Total Cost (Directs),HSS TU (Directs), HSS LF (Directs), HSS Miles (Directs), HSS Cost (Directs), TU (No Directs), LoadedPupCount (No Directs), EmptyPupCount (No Directs), SU (No Directs), LF (No Directs), Empty % (No Directs), Total Miles (No Directs), Total Cost (No Directs),HSS TU (No Directs), HSS LF (No Directs), HSS Miles (No Directs), HSS Cost (No Directs)");

        for (int id = 2018; id <= 2068; id=id+1){
            String runId = id+"";
            String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\OptimOutput\\";
            File df_OptimOutput = new File(dir_OptimOutput);
            if (!df_OptimOutput.exists()) continue;
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            StringBuffer buffer = new StringBuffer();

            buffer.append(myModelGroupID+",");
            buffer.append(id+",");

            Map<String, Double> myMapOfSummaryStats_withDirects = this.readJamieFileSummary_returnMapOfValues(dir_OptimOutput, "Total", true);
            Map<String, Double> myMapOfSummaryStats_HSSwithDirects = this.readJamieHSSFileSummary_returnMapOfValues(dir_OptimOutput, "Total", true);

            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.TU)+",");
            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.LoadedPupCount)+",");
            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.EmptyPupCount)+",");
            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.SU)+",");
            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.LF)+",");
            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.EmptyPercent)+",");
            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.TotalMiles)+",");
            buffer.append(myMapOfSummaryStats_withDirects.get(LHHssReportBuilder.TotalCost)+",");

            buffer.append(myMapOfSummaryStats_HSSwithDirects.get(LHHssReportBuilder.HSS_TU)+",");
            buffer.append(myMapOfSummaryStats_HSSwithDirects.get(LHHssReportBuilder.HSS_LF)+",");
            buffer.append(myMapOfSummaryStats_HSSwithDirects.get(LHHssReportBuilder.HSS_Miles)+",");
            buffer.append(myMapOfSummaryStats_HSSwithDirects.get(LHHssReportBuilder.HSS_Cost)+",");

            Map<String, Double> myMapOfSummaryStats_withNoDirects = this.readJamieFileSummary_returnMapOfValues(dir_OptimOutput, "Total", false);
            Map<String, Double> myMapOfSummaryStats_HSSwithNoDirects = this.readJamieHSSFileSummary_returnMapOfValues(dir_OptimOutput, "Total", false);

            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.TU)+",");
            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.LoadedPupCount)+",");
            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.EmptyPupCount)+",");
            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.SU)+",");
            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.LF)+",");
            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.EmptyPercent)+",");
            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.TotalMiles)+",");
            buffer.append(myMapOfSummaryStats_withNoDirects.get(LHHssReportBuilder.TotalCost)+",");

            buffer.append(myMapOfSummaryStats_HSSwithNoDirects.get(LHHssReportBuilder.HSS_TU)+",");
            buffer.append(myMapOfSummaryStats_HSSwithNoDirects.get(LHHssReportBuilder.HSS_LF)+",");
            buffer.append(myMapOfSummaryStats_HSSwithNoDirects.get(LHHssReportBuilder.HSS_Miles)+",");
            buffer.append(myMapOfSummaryStats_HSSwithNoDirects.get(LHHssReportBuilder.HSS_Cost)+",");


            this.logFile.println(buffer);
        }
        closeFile();

    }

    public void runLHO_RollUpSolutionStatisticsByShift() throws Exception {
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\SolutionStatistics_ShiftSpecificCostAndTU\\", "SolutionStatistics_ShiftSpecificCostAndTU.csv");

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        this.logFile.println("ModelGroupID, ModelRunID, Shift, LH TU, LH Miles, LH Cost, HSS TU, HSS Miles, HSS Cost");

        SortedSet<String> mySetOfShift = new TreeSet<String>();
        mySetOfShift.add("O");
        mySetOfShift.add("F");
        mySetOfShift.add("I");
        mySetOfShift.add("D");

        for (int id = 2018; id <= 2306; id=id+1){
            String runId = id+"";
            String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\OptimOutput\\";
            File df_OptimOutput = new File(dir_OptimOutput);
            if (!df_OptimOutput.exists()) continue;
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            for(String myShift : mySetOfShift) {

                StringBuffer buffer = new StringBuffer();

                buffer.append(myModelGroupID+",");
                buffer.append(id+",");
                buffer.append(myShift+",");

                Map<String, Double> myMapOfSummaryStats = null;
                try {
                    myMapOfSummaryStats = this.readJamieFileSummary_returnMapOfValues(dir_OptimOutput, myShift, null);
                } catch(Exception ex) {
                    int temp_a = 0;
                }

                Map<String, Double> myMapOfSummaryStats_HSS = null;
                try {
                    myMapOfSummaryStats_HSS = this.readJamieHSSFileSummary_returnMapOfValues(dir_OptimOutput, myShift, null);
                } catch(Exception ex) {
                    int temp_a = 0;
                }

                if(myMapOfSummaryStats != null) {
                    buffer.append(myMapOfSummaryStats.get(LHHssReportBuilder.TU)+",");
                    buffer.append(myMapOfSummaryStats.get(LHHssReportBuilder.TotalMiles)+",");
                    buffer.append(myMapOfSummaryStats.get(LHHssReportBuilder.TotalCost)+",");
                }

                if(myMapOfSummaryStats_HSS != null) {
                    buffer.append(myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_TU)+",");
                    buffer.append(myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_Miles)+",");
                    buffer.append(myMapOfSummaryStats_HSS.get(LHHssReportBuilder.HSS_Cost)+",");
                }

                this.logFile.println(buffer);

            }
        }
        closeFile();

    }

    public void runLHO_CalculatingSU_TU_Miles_byShift_total_forAllDaysWeeks_CST_PST_terminatingInJune() throws Exception {

        //String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_OB = new SummaryLHStatsForJamie_obviaibvia("O");
        SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_FAC = new SummaryLHStatsForJamie_obviaibvia("F");
        SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_IB = new SummaryLHStatsForJamie_obviaibvia("I");
        SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_Day = new SummaryLHStatsForJamie_obviaibvia("D");
        SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_Total = new SummaryLHStatsForJamie_obviaibvia(null);

        for (int id = 2178; id <= 2259; id=id+1){
            String runId = id+"";

            System.out.println(runId);

            String dir_in = baseDirectory_remote+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            System.out.println("\t Reading remote");
            this.populateSummaryStatsFromJamieFile(true, runId, baseDirectory_remote,
                    mySummaryLHStatsForJamie_obviaibvia_OB,
                    mySummaryLHStatsForJamie_obviaibvia_FAC,
                    mySummaryLHStatsForJamie_obviaibvia_IB,
                    mySummaryLHStatsForJamie_obviaibvia_Day,
                    mySummaryLHStatsForJamie_obviaibvia_Total
                    );
            System.out.println("\t Finished reading remote");

        }


        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\SolutionStatistics_ShiftSpecificCostSU_TU\\", "SolutionStatistics_ShiftSpecificCostSU_TU.csv");
        this.logFile.println("Shift, SU, TU, Miles");

        StringBuffer buffer_OB = new StringBuffer();
        buffer_OB.append(mySummaryLHStatsForJamie_obviaibvia_OB.getMyOutgoingShift()+",");
        buffer_OB.append(mySummaryLHStatsForJamie_obviaibvia_OB.getSU()+",");
        buffer_OB.append(mySummaryLHStatsForJamie_obviaibvia_OB.getTU()+",");
        buffer_OB.append(mySummaryLHStatsForJamie_obviaibvia_OB.getTotalTractorMiles()+",");

        this.logFile.println(buffer_OB.toString());

        StringBuffer buffer_FAC = new StringBuffer();
        buffer_FAC.append(mySummaryLHStatsForJamie_obviaibvia_FAC.getMyOutgoingShift()+",");
        buffer_FAC.append(mySummaryLHStatsForJamie_obviaibvia_FAC.getSU()+",");
        buffer_FAC.append(mySummaryLHStatsForJamie_obviaibvia_FAC.getTU()+",");
        buffer_FAC.append(mySummaryLHStatsForJamie_obviaibvia_FAC.getTotalTractorMiles()+",");

        this.logFile.println(buffer_FAC.toString());

        StringBuffer buffer_IB = new StringBuffer();
        buffer_IB.append(mySummaryLHStatsForJamie_obviaibvia_IB.getMyOutgoingShift()+",");
        buffer_IB.append(mySummaryLHStatsForJamie_obviaibvia_IB.getSU()+",");
        buffer_IB.append(mySummaryLHStatsForJamie_obviaibvia_IB.getTU()+",");
        buffer_IB.append(mySummaryLHStatsForJamie_obviaibvia_IB.getTotalTractorMiles()+",");

        this.logFile.println(buffer_IB.toString());

        StringBuffer buffer_Day = new StringBuffer();
        buffer_Day.append(mySummaryLHStatsForJamie_obviaibvia_Day.getMyOutgoingShift()+",");
        buffer_Day.append(mySummaryLHStatsForJamie_obviaibvia_Day.getSU()+",");
        buffer_Day.append(mySummaryLHStatsForJamie_obviaibvia_Day.getTU()+",");
        buffer_Day.append(mySummaryLHStatsForJamie_obviaibvia_Day.getTotalTractorMiles()+",");

        this.logFile.println(buffer_Day.toString());

        StringBuffer buffer_Total = new StringBuffer();
        buffer_Total.append(mySummaryLHStatsForJamie_obviaibvia_Total.getMyOutgoingShift()+",");
        buffer_Total.append(mySummaryLHStatsForJamie_obviaibvia_Total.getSU()+",");
        buffer_Total.append(mySummaryLHStatsForJamie_obviaibvia_Total.getTU()+",");
        buffer_Total.append(mySummaryLHStatsForJamie_obviaibvia_Total.getTotalTractorMiles()+",");

        this.logFile.println(buffer_Total.toString());

        closeFile();

    }

    public Set<OrigLocDestLoc> readExcessTrailerFile(String fileName, String directoryName, Data myData) throws Exception {

        File df = new File(directoryName);
        if (!df.exists()) {
            return null;
        }

        Set<OrigLocDestLoc> mySetToReturn = new HashSet<OrigLocDestLoc>();

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(directoryName+fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read first line
            dbRecord = br.readLine();

            // read second line
            dbRecord = br.readLine();


            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");

                String myDate = val[0].trim();
                String myOrig = val[2].trim();
                String myDest = val[4].trim();
                Integer myModelPupCount = new Integer(val[8].trim());
                Integer myActualPupCount = new Integer(val[10].trim());
                Integer myModelHSSVanCount = new Integer(val[15].trim());
                Integer myActualHSSVanCount = new Integer(val[17].trim());

                OrigLocDestLoc myOrigLocDestLoc = myData.getOrigLocDestLoc(myOrig, myDest, true);
                myOrigLocDestLoc.setNumPup_model(myModelPupCount);
                myOrigLocDestLoc.setNumPup_actual(myActualPupCount);
                myOrigLocDestLoc.setNumHSS_model(myModelHSSVanCount);
                myOrigLocDestLoc.setNumHSS_actual(myActualHSSVanCount);

                mySetToReturn.add(myOrigLocDestLoc);

            }
        }catch (IOException e) {
            e.printStackTrace();
            return mySetToReturn;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return mySetToReturn;


    }

    public void calculateCostPerTrailer_ForGivenDate(String myDate, Data myData) throws Exception {

        //String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        String baseWeeklyDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\WeeklyFiles\\2017-01-16\\";

        String excessTrailerDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\CostOfExcessTrailers\\";
        String myExcessTrailerFile = "ExcessTrailers_MidWest_"+myDate+".csv";

        // read the excess trailer information...

        // in format "2017-01-11"
        String myDate_string = myDate;

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myDate_string);

        Set<OrigLocDestLoc> mySetOfOrigLocDestLoc = this.readExcessTrailerFile(myExcessTrailerFile, excessTrailerDirectory, myData);

        this.getMilesFrom_lho_miles_File(baseWeeklyDirectory_remote, mySetOfOrigLocDestLoc);

        Map<String, List<Double>> myMapByModeOfListOfAverageTrailerCostPerMile = new HashMap<String, List<Double>>();

        for (int id = 3806; id <= 3828; id=id+1){
            String runId = id+"";

            System.out.println(runId);

            String dir_in = baseDirectory_remote+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            if(!myDateForThisRunID.equals(date)) {
                continue;
            }

            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            System.out.println("\t Reading remote");
            Map<String, Double> myMapByModeOfAverageTrailerCostPerMile = this.getAverageTrailerCostPerMileFromJamieFile(true, runId, baseDirectory_remote);

            if(myMapByModeOfAverageTrailerCostPerMile != null) {
                for (String myMode : myMapByModeOfAverageTrailerCostPerMile.keySet()) {
                    Double myAverage = myMapByModeOfAverageTrailerCostPerMile.get(myMode);
                    if(myAverage == null) {
                        continue;
                    }

                    List<Double> myList = myMapByModeOfListOfAverageTrailerCostPerMile.get(myMode);
                    if(myList == null) {
                        myList = new ArrayList<Double>();
                        myMapByModeOfListOfAverageTrailerCostPerMile.put(myMode, myList);
                    }
                    myList.add(myAverage);
                }
            }

            this.getMilesFromLoadLegFile(true, runId, baseDirectory_remote, mySetOfOrigLocDestLoc);
            this.getMilesFromMoveLegFile(true, runId, baseDirectory_remote, mySetOfOrigLocDestLoc);

            System.out.println("\t Finished reading remote");

        }

        Map<String, Double> myMapByModeOfAverageTrailerCostPerMile = new HashMap<String, Double>();

        for (String myMode : myMapByModeOfListOfAverageTrailerCostPerMile.keySet()) {
            List<Double> myList = myMapByModeOfListOfAverageTrailerCostPerMile.get(myMode);
            if(myList == null || myList.size()==0) {
                continue;
            }

            Double avgValues = LHHssReportBuilder.getAverage(myList, false);

            myMapByModeOfAverageTrailerCostPerMile.put(myMode, avgValues);
        }

        FileOutputStream myFileOutputStream = new FileOutputStream(new File(excessTrailerDirectory + "CostByLaneResults_"+myDate_string+".csv"), false);
        PrintWriter myPrintWriter = new PrintWriter(myFileOutputStream);

        StringBuffer myHeaderBuffer = new StringBuffer();
        myHeaderBuffer.append("Origin"+",");
        myHeaderBuffer.append("Destination"+",");
        myHeaderBuffer.append("Miles"+",");
        myHeaderBuffer.append("NumPup_actual"+",");
        myHeaderBuffer.append("NumPup_model"+",");
        myHeaderBuffer.append("numPup_excess"+",");
        myHeaderBuffer.append("costOfExcessPups"+",");
        myHeaderBuffer.append("NumHSS_actual"+",");
        myHeaderBuffer.append("NumHSS_model"+",");
        myHeaderBuffer.append("numHSSVans_excess"+",");
        myHeaderBuffer.append("costOfExcessHSSVans"+",");

        myPrintWriter.println(myHeaderBuffer);

        for(OrigLocDestLoc myOrigLocDestLoc : mySetOfOrigLocDestLoc) {
            StringBuffer myBuffer = new StringBuffer();
            myBuffer.append(myOrigLocDestLoc.getLocation_origin()+",");
            myBuffer.append(myOrigLocDestLoc.getLocation_destination()+",");
            Double myMiles = myOrigLocDestLoc.getMyMiles(true);
            if(myMiles<epsilon) {
                int temp_a = 0;
            }
            myBuffer.append(myMiles+",");
            boolean isHSS = myOrigLocDestLoc.isHSS();
            myBuffer.append(myOrigLocDestLoc.getNumPup_actual()+",");
            myBuffer.append(myOrigLocDestLoc.getNumPup_model()+",");
            int numPup_excess = myOrigLocDestLoc.getNumPup_excess();
            myBuffer.append(numPup_excess+",");
            double avgCostPerPup = 0.0;
            if(!isHSS) {
                avgCostPerPup = myMiles*myMapByModeOfAverageTrailerCostPerMile.get(LHHssReportBuilder.L);
            }
            double costOfExcessPups = avgCostPerPup*numPup_excess;
            myBuffer.append(costOfExcessPups+",");

            myBuffer.append(myOrigLocDestLoc.getNumHSS_actual()+",");
            myBuffer.append(myOrigLocDestLoc.getNumHSS_model()+",");
            int numHSSVans_excess = myOrigLocDestLoc.getNumHSSVans_excess();
            myBuffer.append(numHSSVans_excess+",");
            double avgCostPerHSSVan = 0.0;
            if(!isHSS) {
                avgCostPerHSSVan = myMiles*myMapByModeOfAverageTrailerCostPerMile.get(LHHssReportBuilder.S);
            }
            double costOfExcessHSSVans = avgCostPerHSSVan*numHSSVans_excess;
            myBuffer.append(costOfExcessHSSVans+",");

            myPrintWriter.println(myBuffer);

        }

        myPrintWriter.close();
        myFileOutputStream.close();



    }

    public void buildNetworkAPIModels(String myNetworkAPI_inputFileSubDirectory) {

        File sicFile = new File(myNetworkAPI_inputFileSubDirectory, LHHssReportBuilder.FILE_SIC);
        File lpFile = new File(myNetworkAPI_inputFileSubDirectory, LHHssReportBuilder.FILE_LP);
        File mpFile = new File(myNetworkAPI_inputFileSubDirectory, LHHssReportBuilder.FILE_MP);

        try {
            NetworkBuilder.CertifiedLegsBuilder builder = NetworkBuilder.buildNetwork()
                    .withLocations(sicFile)
                    .withoutDistances()
                    .withLoadLegs(lpFile)
                    .withMoveLegs(mpFile)
                    .ignoreMoveLegRules()
                    .withoutCmsPulls()
                    .withoutMeetLegs();
            this.network = builder.withoutCertLegs();
        } catch (IOException e) {
            throw new IllegalStateException("cant load network from files due IO error:" + e.getMessage());
        }

        //accessing Def path search
        this.api = new NetworkAPI(network);


    }

    public void findODsAffectedByMissingLoadLegs(Data myData) throws Exception {
        String myNetworkAPI_inputFileSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\findODsAffectedByMissingLoadLegs\\Input Files\\";
        this.buildNetworkAPIModels(myNetworkAPI_inputFileSubDirectory);
        String mySubdirectoryForAllLoadPathsOfAllODs = "C:\\Old Drive\\rosa.charles\\workspace_git\\findODsAffectedByMissingLoadLegs\\Output Files\\";
        this.findODsAffectedAndHowByMissingLoadLegs(mySubdirectoryForAllLoadPathsOfAllODs, myData);
    }

    public void findODsAffectedByMissingMoveLegs(Data myData) throws Exception {
        String myNetworkAPI_inputFileSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\findODsAffectedByMissingMoveLegs\\Input Files\\";
        this.buildNetworkAPIModels(myNetworkAPI_inputFileSubDirectory);
        String mySubdirectoryForAllMovePathsOfAllODs = "C:\\Old Drive\\rosa.charles\\workspace_git\\findODsAffectedByMissingMoveLegs\\Output Files\\";
        this.findODsAffectedAndHowByMissingMoveLegs(mySubdirectoryForAllMovePathsOfAllODs, myData);
    }

    public void findDaysOfServiceAndPresenceOfExcHSSForAllODs() throws Exception {
        String myNetworkAPI_inputFileSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\FFO_to_CostModelBalanceInput-Data\\Input Files\\";
        this.buildNetworkAPIModels(myNetworkAPI_inputFileSubDirectory);
        String mySubdirectory_findDaysOfServiceAndPresenceOfExcHSSForAllODs = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\findDaysOfServiceAndPresenceOfExcHSSForAllODs\\";
        this.constructALLODCombinations_AndCalcService(mySubdirectory_findDaysOfServiceAndPresenceOfExcHSSForAllODs);
    }

    // test method to show how to read a file
    public void readInSampleFile() throws Exception {
        String myInputFileSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\4915\\in\\";
        String myFileName = "lho_shipment_inpt.csv";

        BufferedReader br = null;
        String dbRecord;

        List<DataRecord> myListOfDataRecords = new ArrayList<DataRecord>();

        String myFileNameWithFullPath =  myInputFileSubDirectory+myFileName;

        try {
            File f = new File(myFileNameWithFullPath);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line to get names of columns
            dbRecord = br.readLine();

            Map<String, Integer> myMapOfOffsetByColumnName = new HashMap<String, Integer>();

            String[] columnNames = dbRecord.split(",");

            for(int colOffset=0; colOffset<columnNames.length ; colOffset++) {
                String currentColumnName = columnNames[colOffset];
                myMapOfOffsetByColumnName.put(currentColumnName, colOffset);
            }

            while ( (dbRecord = br.readLine()) != null) {

                String[] myRecordValues = dbRecord.split(",");

                String PUR_SEQ_NBR = myRecordValues[myMapOfOffsetByColumnName.get("PUR_SEQ_NBR")];
                String DEST_SIC_CD = myRecordValues[myMapOfOffsetByColumnName.get("DEST_SIC_CD")];

                Integer PUR_SEQ_NBR_int = null;
                try {
                    PUR_SEQ_NBR_int = new Integer(PUR_SEQ_NBR);
                } catch(Exception ex) {
                    continue;
                }

                DataRecord myDataRecord = new DataRecord(PUR_SEQ_NBR_int, DEST_SIC_CD);

                myListOfDataRecords.add(myDataRecord);


            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        int temp_a = 0;


    }

    public void rollUpMachineLearningLeafModelResultsResults(String modelType, String myInputSubdirectory, String myFilePrefixToDetermineSegments, String myLeafModel_Results_output) throws Exception {

        if(myFilePrefixToDetermineSegments == null) {
            throw new Exception("myFilePrefixToDetermineSegments="+myFilePrefixToDetermineSegments+" is NOT set");
        }

        // does the subdirectory exist

        File subdirectory = new File(myInputSubdirectory);
        if(!subdirectory.exists()) {
            throw new Exception("myInputSubdirectory="+myInputSubdirectory+" does not exist");
        }

        if(!subdirectory.isDirectory()) {
            throw new Exception("myInputSubdirectory="+myInputSubdirectory+" is not a directory");
        }

        // find all of the segment numbers by searching across all files in myInputSubdirectory that begin with myFilePrefixToDetermineSegments

        File[] directoryListing = subdirectory.listFiles();

        if(directoryListing==null) {
            throw new Exception("myInputSubdirectory="+myInputSubdirectory+" contains NO files");
        }

        Map<Integer, MeanVarStdDev_Metric> myMapOfStatsBySegment = new HashMap<>();
        for (File fileWithinSubDirectory : directoryListing) {
            if(fileWithinSubDirectory==null) {
                continue;
            }

            String myFileName = fileWithinSubDirectory.getName();
            // does this file have the appropriate prefix....
            if(myFileName != null && myFileName.startsWith(myFilePrefixToDetermineSegments)) {
                // read until the first digit character
                Integer offsetOfFirstNumber = null;
                for(int i=0; i<myFileName.length(); i++) {
                    if(Character.isDigit(myFileName.charAt(i))) {
                        offsetOfFirstNumber = i;
                        break;
                    }
                }

                if(offsetOfFirstNumber==null) {
                    throw new Exception("myFileName="+myFileName+" has no digits");
                }

                String mySegmentNumberString = myFileName.substring(offsetOfFirstNumber);

                Integer mySegmentNum = new Integer(mySegmentNumberString);

                // read the file to get the mean, var, and stdDev
                MeanVarStdDev_Metric myStats = this.readThisFileAndGetTheseStats(fileWithinSubDirectory);

                if(myStats == null) {
                    throw new Exception("myFileName="+myFileName+" has no MeanVarStdDev_Metric");
                }

                myMapOfStatsBySegment.put(mySegmentNum, myStats);
            }
        }

        if(myMapOfStatsBySegment.size()==0) {
            throw new Exception("myInputSubdirectory="+myInputSubdirectory+" contains no segment specific files with myFilePrefixToDetermineSegments="+myFilePrefixToDetermineSegments);
        }

        // we are now going to, for each segment, read the model results for that segment....and get those selected....

        Map<Integer, SortedMap<Integer, ModelResults>> myMapofMapOfModelResultsByNumVarBySegment = new HashMap<>();
        String myFileNameModelStatsPrefix = "leafOutputFile_modelStats_";
        for(Integer mySegmentNum : myMapOfStatsBySegment.keySet()) {
            SortedMap<Integer, ModelResults> myMap_inner = myMapofMapOfModelResultsByNumVarBySegment.get(mySegmentNum);
            if(myMap_inner==null) {
                myMap_inner = new TreeMap<Integer, ModelResults>();
                myMapofMapOfModelResultsByNumVarBySegment.put(mySegmentNum, myMap_inner);
            }
            String myFileNameOfModelOutputStatsPrefixToMatch = myFileNameModelStatsPrefix+mySegmentNum;
            // read through the model results file for this segment
            for (File fileWithinSubDirectory : directoryListing) {
                if (fileWithinSubDirectory == null) {
                    continue;
                }

                String myFileName = fileWithinSubDirectory.getName();

                // does this file have the appropriate prefix....
                if (myFileName != null && myFileName.startsWith(myFileNameOfModelOutputStatsPrefixToMatch)) {
                    String[] myRecordValues = myFileName.split("_");
                    Integer myNumVar = new Integer(myRecordValues[3].trim());

                    // read the file to get the mean, var, and stdDev
                    ModelResults myModelResults = this.readModelResultsFile(fileWithinSubDirectory, myNumVar);

                    if(myModelResults == null || myModelResults.getNumRMSError()==0) {
                        Integer numRMSError = null;
                        if(myModelResults != null) {
                            numRMSError = myModelResults.getNumRMSError();
                        }
                        throw new Exception("myFileName="+myFileName+" has myModelResults="+myModelResults+" and numRMSError="+numRMSError);
                    }

                    myMap_inner.put(myNumVar, myModelResults);

                }
            }
        }

        int temp_a = 0;

        // output the results into file....

        FileWriter fstream = new FileWriter(myInputSubdirectory+myLeafModel_Results_output, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        StringBuffer header = new StringBuffer();
        header.append("modelType"+",");
        header.append("Segment"+",");
        header.append("mean"+",");
        header.append("variance"+",");
        header.append("StdDev"+",");
        header.append("PredictedMetric"+",");
        header.append("numVariablesInModel"+",");
        header.append("averageRMS"+",");
        header.append("stdDevRMS"+",");
        header.append("numCrossValidationsRMS"+",");

        myPrintWriter.println(header);

        for(Integer mySegmentNum : myMapOfStatsBySegment.keySet()) {
            MeanVarStdDev_Metric myStat = myMapOfStatsBySegment.get(mySegmentNum);

            StringBuffer buffer_segmentLevel = new StringBuffer();
            buffer_segmentLevel.append(modelType+",");
            buffer_segmentLevel.append(mySegmentNum+",");
            buffer_segmentLevel.append(myStat.getMean()+",");
            buffer_segmentLevel.append(myStat.getVariance()+",");
            buffer_segmentLevel.append(myStat.getStdDev()+",");
            buffer_segmentLevel.append(myStat.getNameOfMetric()+",");

            Map<Integer, ModelResults> myMap_inner = myMapofMapOfModelResultsByNumVarBySegment.get(mySegmentNum);
            if(myMap_inner==null) {
                continue;
            }
            for (Integer numVar : myMap_inner.keySet()) {

                ModelResults myModelResults = myMap_inner.get(numVar);

                if(myModelResults==null) {
                    continue;
                }

                StringBuffer buffer_modelLevel = new StringBuffer();
                buffer_modelLevel.append(myModelResults.getNumPredictiveVars()+",");
                buffer_modelLevel.append(myModelResults.getAvgRMSError()+",");
                buffer_modelLevel.append(myModelResults.getStdDevRMSError()+",");
                buffer_modelLevel.append(myModelResults.getNumRMSError()+",");

                myPrintWriter.println(buffer_segmentLevel.toString()+buffer_modelLevel.toString());

            }

        }

















        myPrintWriter.close();
        fstream.close();



    }

    public ModelResults readModelResultsFile(File fileWithinSubDirectory, int myNumPredictiveVar) throws Exception {

        if(fileWithinSubDirectory==null) {
            return null;
        }

        BufferedReader br = null;
        String dbRecord;

        ModelResults myReturnValue = new ModelResults(myNumPredictiveVar);

        try {
            FileInputStream fis = new FileInputStream(fileWithinSubDirectory);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            // read header line to get names of columns
            dbRecord = br.readLine();

            Map<String, Integer> myMapOfOffsetByColumnName = new HashMap<String, Integer>();

            String[] columnNames = dbRecord.split(",");

            for(int colOffset=0; colOffset<columnNames.length ; colOffset++) {
                String currentColumnName = columnNames[colOffset];
                myMapOfOffsetByColumnName.put(currentColumnName, colOffset);
            }

            // read just the first line after the header...and then jump out
            while ( (dbRecord = br.readLine()) != null) {

                String[] myRecordValues = dbRecord.split(",");

                StringBuffer myBuffer = new StringBuffer();
                for(int colOffset=0; colOffset<myRecordValues.length ; colOffset++) {
                    String currentColumnName = columnNames[colOffset];
                    String currentColumnValue = myRecordValues[colOffset].trim();
                    if(currentColumnName != null && currentColumnName.startsWith("score")) {
                        Double negMSE = new Double(currentColumnValue);
                        Double posMSE = -negMSE;
                        Double rms = Math.sqrt(posMSE);
                        myReturnValue.addRMSError(rms);
                    }
                }

            }


        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myReturnValue;

    }

    public MeanVarStdDev_Metric readThisFileAndGetTheseStats(File fileWithinSubDirectory) throws Exception {

        if(fileWithinSubDirectory==null) {
            return null;
        }

        BufferedReader br = null;
        String dbRecord;

        MeanVarStdDev_Metric myReturnValue = null;

        try {
            FileInputStream fis = new FileInputStream(fileWithinSubDirectory);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            Double average = null;
            Double stdDev = null;
            Double variance = null;
            String myMetric = null;

            while ( (dbRecord = br.readLine()) != null) {

                // read until you see the Average or StdDev or Var

                String[] myRecordValues = dbRecord.split(" ");

                String myKey = myRecordValues[0].trim();

                if(myKey==null) {
                    continue;
                }

                if(myKey.equals("Average")) {
                    myMetric = myRecordValues[1].trim();
                    String myValueString = null;
                    Double myValue = null;
                    try {
                        myValueString = myRecordValues[2].trim();
                        myValue = new Double(myValueString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    average = myValue;
                } else if(myKey.equals("StdDev")) {
                    myMetric = myRecordValues[1].trim();
                    String myValueString = null;
                    Double myValue = null;
                    try {
                        myValueString = myRecordValues[2].trim();
                        myValue = new Double(myValueString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stdDev = myValue;
                } else if(myKey.equals("Var")) {
                    myMetric = myRecordValues[1].trim();
                    String myValueString = null;
                    Double myValue = null;
                    try {
                        myValueString = myRecordValues[2].trim();
                        myValue = new Double(myValueString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    variance = myValue;
                }
            }

            if(average != null && stdDev != null && variance != null && myMetric != null) {
                myReturnValue = new MeanVarStdDev_Metric(
                        average,
                        variance,
                        stdDev,
                        myMetric
                );
            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myReturnValue;

    }


    public void consolidateMachineLearningResults(String myInputSubdirectory, String myX_fileName, String myY_fileName, String myLeaf_fileName, String delimiterForOutput) throws Exception {

        List<String> XmatrixList = new ArrayList<>();
        List<Double> YvectorList = new ArrayList<>();
        List<Integer> leafIDList = new ArrayList<>();

        System.out.println("reading X matrix");
        String myXHeader = this.readXmatrix(myInputSubdirectory, myX_fileName, XmatrixList,delimiterForOutput);
        System.out.println("reading Y vector");
        String myYHeader = this.readYVector(myInputSubdirectory, myY_fileName, YvectorList);
        System.out.println("reading Leaf IDs");
        this.readLeafIDVector(myInputSubdirectory, myLeaf_fileName, leafIDList);

        SortedMap<Integer, FileWriter> myMapOfFileWriter = new TreeMap<Integer, FileWriter>();
        SortedMap<Integer, PrintWriter> myMapOfPrintWriter = new TreeMap<Integer, PrintWriter>();

        String mySuffix =".csv";
        if(delimiterForOutput.equals("\t")) {
            mySuffix = ".txt";
        }

        int myOffset = -1;
        for(Integer myLeafID : leafIDList) {
            myOffset++;
            String myXmatrixString = XmatrixList.get(myOffset);
            Double myCDI = YvectorList.get(myOffset);

            FileWriter myFileWriter = myMapOfFileWriter.get(myLeafID);
            if(myFileWriter==null) {
                String myFileName = myInputSubdirectory+"ModelData_"+myLeafID+mySuffix;
                myFileWriter = new FileWriter(myFileName, false);
                myMapOfFileWriter.put(myLeafID,myFileWriter);
            }

            PrintWriter myPrintWriter = myMapOfPrintWriter.get(myLeafID);
            if(myPrintWriter==null) {
                myPrintWriter = new PrintWriter(myFileWriter);
                myMapOfPrintWriter.put(myLeafID,myPrintWriter);
                myPrintWriter.println(myXHeader+myYHeader);
            }

            myPrintWriter.println(myXmatrixString+myCDI);
        }

        for(Integer myLeafID : myMapOfPrintWriter.keySet()) {
            PrintWriter myPrintWriter = myMapOfPrintWriter.get(myLeafID);
            myPrintWriter.close();
        }

        for(Integer myLeafID : myMapOfFileWriter.keySet()) {
            FileWriter myFileWriter = myMapOfFileWriter.get(myLeafID);
            myFileWriter.close();
        }

    }

    public void readLeafIDVector(String myInputSubdirectory, String myLeaf_fileName, List<Integer> leafIDList) throws Exception {

        if(leafIDList==null) {
            return;
        }

        BufferedReader br = null;
        String dbRecord;

        String myFileNameWithFullPath =  myInputSubdirectory+myLeaf_fileName;

        try {
            File f = new File(myFileNameWithFullPath);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line to get names of columns
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] myRecordValues = dbRecord.split(",");

                Integer myLeafModelID = new Integer(myRecordValues[1]);

                leafIDList.add(myLeafModelID);

            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return;

    }

    public String readYVector(String myInputSubdirectory, String myY_fileName, List<Double> YvectorList) throws Exception {

        if(YvectorList==null) {
            return null;
        }

        BufferedReader br = null;
        String dbRecord;

        String myHeader = null;

        String myFileNameWithFullPath =  myInputSubdirectory+myY_fileName;

        try {
            File f = new File(myFileNameWithFullPath);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line to get names of columns
            dbRecord = br.readLine();

            Map<String, Integer> myMapOfOffsetByColumnName = new HashMap<String, Integer>();

            String[] columnNames = dbRecord.split(",");

            if(columnNames.length>2) {
                throw new Exception("myY_fileName="+myY_fileName+" should contain only two columns of values...and we only care about the 2nd column");
            }

            StringBuffer myHeaderBuffer = new StringBuffer();
            for(int colOffset=0; colOffset<columnNames.length ; colOffset++) {
                String currentColumnName = columnNames[colOffset];
                myMapOfOffsetByColumnName.put(currentColumnName, colOffset);
                if(colOffset>0) {
                    myHeaderBuffer.append(currentColumnName);
                }
            }

            myHeader = myHeaderBuffer.toString();

            while ( (dbRecord = br.readLine()) != null) {

                String[] myRecordValues = dbRecord.split(",");

                Double myCDI = new Double(myRecordValues[1]);

                YvectorList.add(myCDI);

            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myHeader;

    }

    public String readXmatrix(String myInputSubdirectory, String myX_fileName, List<String> XmatrixList,String delimiterForOutput) throws Exception {

        if(XmatrixList==null) {
            return null;
        }

        BufferedReader br = null;
        String dbRecord;

        String myHeader = null;

        String myFileNameWithFullPath =  myInputSubdirectory+myX_fileName;

        try {
            File f = new File(myFileNameWithFullPath);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line to get names of columns
            dbRecord = br.readLine();

            Map<String, Integer> myMapOfOffsetByColumnName = new HashMap<String, Integer>();

            String[] columnNames = dbRecord.split(",");

            StringBuffer myHeaderBuffer = new StringBuffer();
            for(int colOffset=0; colOffset<columnNames.length ; colOffset++) {
                String currentColumnName = columnNames[colOffset];
                myMapOfOffsetByColumnName.put(currentColumnName, colOffset);
                if(colOffset>0) {
                    myHeaderBuffer.append(currentColumnName+delimiterForOutput);
                }
            }

            myHeader = myHeaderBuffer.toString();

            while ( (dbRecord = br.readLine()) != null) {

                String[] myRecordValues = dbRecord.split(",");

                StringBuffer myBuffer = new StringBuffer();
                for(int colOffset=0; colOffset<myRecordValues.length ; colOffset++) {
                    String currentColumnValue = myRecordValues[colOffset];
                    if(colOffset>0) {
                        myBuffer.append(currentColumnValue+delimiterForOutput);
                    }
                }

                XmatrixList.add(myBuffer.toString());

            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myHeader;

    }

    public void doAnalysisOfRuleSetOutputOfAggModel() throws Exception {

        Integer number_correctPredictions = 0;
        Integer number_InCorrectPredictions = 0;

        Map<Integer, Integer> mapOfCorrectPredictionsBySegment = new HashMap<Integer, Integer>();
        Map<Integer, Integer> mapOfInCorrectPredictionsBySegment = new HashMap<Integer, Integer>();

        String my_OutputOfAggModelSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\AggregationOptimization\\resource\\dynamicData\\output\\";

        String myRuleSetFileName = "ratl_agg_output_ruleset.csv";

        // we need to iterate through all subdirectories therein....and look at the file ratl_agg_output_ruleset.csv
        // and see whether any of these files have blank origin or destination state.

        File directory = new File(my_OutputOfAggModelSubDirectory);

        if(!directory.isDirectory()) {
            return;
        }

        String[] subDirectories = directory.list();
        if (subDirectories.length > 0) {
            for(int i=0; i<subDirectories.length; i++) {
                String mySubDirectoryName = subDirectories[i];

                String myFileToOpen = my_OutputOfAggModelSubDirectory+mySubDirectoryName + "\\" + myRuleSetFileName;

                boolean containsBlankOStateOrDState = this.isRuleSetFileContainingBlankOStateOrDState(myFileToOpen);

                if(containsBlankOStateOrDState) {
                    System.out.println(myFileToOpen);
                    int temp_a = 0;
                }
            }
        }

        return;

    }

    public boolean isRuleSetFileContainingBlankOStateOrDState(String myFileToOpen) throws Exception {

        File f = null;
        BufferedReader br = null;
        String dbRecord = null;
        try {
            String fileName = myFileToOpen;
            f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            // read header line to get names of columns
            dbRecord = br.readLine();

            Map<String, Integer> myMapOfOffsetByColumnName = new HashMap<String, Integer>();

            String[] columnNames = dbRecord.split(",");

            for(int colOffset=0; colOffset<columnNames.length ; colOffset++) {
                String currentColumnName = columnNames[colOffset];
                myMapOfOffsetByColumnName.put(currentColumnName, colOffset);
            }

            while ( (dbRecord = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(dbRecord, ",");

                String[] val = dbRecord.split(",");

                String OSTATE = val[myMapOfOffsetByColumnName.get("OSTATE")];
                String DSTATE = val[myMapOfOffsetByColumnName.get("DSTATE")];

                OSTATE = OSTATE.trim();
                DSTATE = DSTATE.trim();

                boolean oStateIsBlank = false;
                if(OSTATE==null || OSTATE.length()==0 || OSTATE.equals("") || OSTATE.equals(" ")) {
                    oStateIsBlank = true;
                }

                boolean dStateIsBlank = false;
                if(DSTATE==null || DSTATE.length()==0 || DSTATE.equals("") || DSTATE.equals(" ")) {
                    dStateIsBlank = true;
                }

                boolean isEitherBlank = false;
                if(oStateIsBlank || dStateIsBlank) {
                    isEitherBlank = true;
                }

                if(isEitherBlank) {
                    return isEitherBlank;
                }

            }

        } catch (IOException e) {
            // no file to read - just skip past
            System.out.println("myFileToOpen="+myFileToOpen+" doesn't exist");
            int temp_a = 0;
        } catch (Exception e) {
            // no file to read - just skip past
            System.out.println("myFileToOpen="+myFileToOpen+" doesn't exist");
            int temp_a = 0;
        } finally {
            // if the file opened okay, make sure we close it
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    throw ioe;
                }
            } // end if
        } // end finally

        return false;

    }

    public void doAMCAnalysis() throws Exception {

        Integer number_correctPredictions = 0;
        Integer number_InCorrectPredictions = 0;

        Map<Integer, Integer> mapOfCorrectPredictionsBySegment = new HashMap<Integer, Integer>();
        Map<Integer, Integer> mapOfInCorrectPredictionsBySegment = new HashMap<Integer, Integer>();

        String my_inputFileSubDirectory = "S:\\Operations Research\\Pricing\\AMC Investigation\\";

        String myShipmentFile = "AMC.csv";

        SegmentationDeterminer mySegmentationDeterminer = new SegmentationDeterminer(my_inputFileSubDirectory);

        List<Shipment> myListOfShipments = this.readShipments(my_inputFileSubDirectory, myShipmentFile);

        String myShipmentFile_output = "AMC_output.csv";

        FileWriter fstream = new FileWriter(my_inputFileSubDirectory+myShipmentFile_output, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        StringBuffer header = new StringBuffer();
        header.append("SHPMT_INSTC_ID"+",");
        header.append("WEIGHT"+",");
        header.append("SIC_MILES"+",");
        header.append("CUST_REV"+",");
        header.append("WEIGHTED_CLASS"+",");
        header.append("ABS_MIN_CHG_IND"+",");
        header.append("RATL_AMC"+",");
        header.append("AMC_InAgreement?"+",");
        header.append("Segment"+",");

        myPrintWriter.println(header);

        int totalNumShipments = myListOfShipments.size();

        int shipmentCount = 0;
        for(Shipment myShipment : myListOfShipments) {
            shipmentCount++;

            System.out.println(shipmentCount+" of "+totalNumShipments);

            boolean correctPrediction = true;

            Integer mySegment = mySegmentationDeterminer.getSegment(myShipment);
            boolean isAMC = mySegmentationDeterminer.isAMC(mySegment);

            StringBuffer buffer = new StringBuffer();
            buffer.append(myShipment.getMyShptID()+",");
            buffer.append(myShipment.getMyWeight()+",");
            buffer.append(myShipment.getMySicMiles()+",");
            buffer.append(myShipment.getMyCustRev()+",");
            buffer.append(myShipment.getMyWeightedClass()+",");
            buffer.append(myShipment.getAMC()+",");
            buffer.append(isAMC+",");
            if(myShipment.getAMC().equals(isAMC)) {
                buffer.append(true+",");
            } else {
                buffer.append(false+",");
                correctPrediction = false;
            }
            buffer.append(mySegment+",");

            myPrintWriter.println(buffer);

            if(correctPrediction) {
                number_correctPredictions++;
                Integer currentScore = mapOfCorrectPredictionsBySegment.get(mySegment);
                if(currentScore==null) {
                    mapOfCorrectPredictionsBySegment.put(mySegment,1);
                } else {
                    mapOfCorrectPredictionsBySegment.put(mySegment,currentScore+1);
                }
            } else {
                number_InCorrectPredictions++;
                Integer currentScore = mapOfInCorrectPredictionsBySegment.get(mySegment);
                if(currentScore==null) {
                    mapOfInCorrectPredictionsBySegment.put(mySegment,1);
                } else {
                    mapOfInCorrectPredictionsBySegment.put(mySegment,currentScore+1);
                }

            }

        }

        myPrintWriter.close();
        fstream.close();

        System.out.println("number_correctPredictions="+number_correctPredictions);
        System.out.println("number_InCorrectPredictions="+number_InCorrectPredictions);

        int totalPredictions = number_correctPredictions+number_InCorrectPredictions;

        System.out.println("fraction correct predictions="+number_correctPredictions/(totalPredictions+0.00001));

        Set<Integer> mySetOfCorrectPredictionsSegments = mapOfCorrectPredictionsBySegment.keySet();
        Set<Integer> mySetOfInCorrectPredictionsSegments = mapOfInCorrectPredictionsBySegment.keySet();

        SortedSet<Integer> mySetOfSegments = new TreeSet<Integer>();
        if(mySetOfCorrectPredictionsSegments != null) {
            mySetOfSegments.addAll(mySetOfCorrectPredictionsSegments);
        }
        if(mySetOfInCorrectPredictionsSegments != null) {
            mySetOfSegments.addAll(mySetOfInCorrectPredictionsSegments);
        }

        for(Integer mySegment : mySetOfSegments) {

            boolean isAMC = mySegmentationDeterminer.isAMC(mySegment);

            Integer numCorrectPredictions = mapOfCorrectPredictionsBySegment.get(mySegment);
            Integer numInCorrectPredictions = mapOfInCorrectPredictionsBySegment.get(mySegment);

            int totalNumPredictions = 0;
            if(numCorrectPredictions != null) {
                totalNumPredictions += numCorrectPredictions;
            }

            if(numInCorrectPredictions != null) {
                totalNumPredictions += numInCorrectPredictions;
            }

            double fractionCorrectPredictions = 0.0;
            if(numCorrectPredictions != null) {
                fractionCorrectPredictions = numCorrectPredictions/(totalNumPredictions+0.000001);
            }


            System.out.println("\t segment="+mySegment+" numCorrectPredictions="+numCorrectPredictions);
            System.out.println("\t segment="+mySegment+" numInCorrectPredictions="+numInCorrectPredictions);
            System.out.println("\t segment="+mySegment+" fractionCorrectPredictions="+fractionCorrectPredictions);
            if(isAMC) {
                System.out.println("\t segment=" + mySegment + " is considered AMC by Martin");
            }
        }


    }

    public List<Shipment> readShipments(String dir, String name) throws Exception {

        List<Shipment> myReturnList = new ArrayList<Shipment>();

        File f = null;
        BufferedReader br = null;
        String dbRecord = null;
        try {
            String fileName = dir+name;
            f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            // read header line to get names of columns
            dbRecord = br.readLine();

            Map<String, Integer> myMapOfOffsetByColumnName = new HashMap<String, Integer>();

            String[] columnNames = dbRecord.split(",");

            for(int colOffset=0; colOffset<columnNames.length ; colOffset++) {
                String currentColumnName = columnNames[colOffset];
                myMapOfOffsetByColumnName.put(currentColumnName, colOffset);
            }

            while ( (dbRecord = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(dbRecord, ",");

                String[] val = dbRecord.split(",");

                String SHPMT_INSTC_ID = val[myMapOfOffsetByColumnName.get("SHPMT_INSTC_ID")];
                String WEIGHT = val[myMapOfOffsetByColumnName.get("WEIGHT")];
                String SIC_MILES = val[myMapOfOffsetByColumnName.get("SIC_MILES")];
                String CUST_REV = val[myMapOfOffsetByColumnName.get("CUST_REV")];
                String WEIGHTED_CLASS = val[myMapOfOffsetByColumnName.get("WEIGHTED_CLASS")];
                String ABS_MIN_CHG_IND = val[myMapOfOffsetByColumnName.get("AMC_IND")];

                SHPMT_INSTC_ID = SHPMT_INSTC_ID.trim().replaceAll("\"", "");
                WEIGHT = WEIGHT.trim().replaceAll("\"", "");
                SIC_MILES = SIC_MILES.trim().replaceAll("\"", "");
                CUST_REV = CUST_REV.trim().replaceAll("\"", "");
                WEIGHTED_CLASS = WEIGHTED_CLASS.trim().replaceAll("\"", "");
                ABS_MIN_CHG_IND = ABS_MIN_CHG_IND.trim().replaceAll("\"", "");

                if(!ABS_MIN_CHG_IND.equals("(null)")) {
                    int temp_a = 0;
                }

                if(!ABS_MIN_CHG_IND.equals("Y") && !ABS_MIN_CHG_IND.equals("N")) {
                    continue;
                }

                boolean isAMC_input = false;
                if(ABS_MIN_CHG_IND.equals("Y")) {
                    isAMC_input = true;
                }

                Shipment myShipment =
                        new Shipment(
                                SHPMT_INSTC_ID,
                                new Double(WEIGHT),
                                new Double(SIC_MILES),
                                new Double(CUST_REV),
                                new Double(WEIGHTED_CLASS),
                                isAMC_input
                        );

                myReturnList.add(myShipment);

            }

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            // if the file opened okay, make sure we close it
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    throw ioe;
                }
            } // end if
        } // end finally

        return myReturnList;

    }

    public void createMovePathForAllODs() throws Exception {
        String myNetworkAPI_inputFileSubDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\FFO_to_CostModelBalanceInput-Data\\Input Files\\";
        this.buildNetworkAPIModels(myNetworkAPI_inputFileSubDirectory);
        String mySubdirectoryForAllMovePathsOfAllODs = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\movePathsForAllODs\\";
        this.constructALLODCombinations(myNetworkAPI_inputFileSubDirectory, mySubdirectoryForAllMovePathsOfAllODs);
    }

    public void solveMathProblem() throws Exception {
        int upperBound = 500;
        double errorBound = 0.0001;
        int targetValue = 4;
        for(int ub_current = 1; ub_current<=upperBound; ub_current++) {
            for(int s=0; s<ub_current; s++) {
                for(int b=0; b<ub_current; b++) {
                    for(int p=0; p<ub_current; p++) {
                        if(b==0 && p==0) {
                            continue;
                        }
                        if(s==0 && p==0) {
                            continue;
                        }
                        if(s==0 && b==0) {
                            continue;
                        }
                        double s_double = s;
                        double b_double = b;
                        double p_double = p;
                        double myFunc = ((s_double)/(b_double+p_double)) + ((b_double)/(s_double+p_double)) + ((p_double)/(s_double+b_double));
                        if(myFunc>targetValue-errorBound && myFunc<targetValue+errorBound) {
                            System.out.println("ub_current="+ub_current);
                            System.out.println("\ts="+s);
                            System.out.println("\tb="+b);
                            System.out.println("\tp="+p);
                            System.out.println("\t\tmyFunc=" + myFunc);
                        }
                        if(myFunc < targetValue+epsilon && myFunc > targetValue-epsilon) {
                            System.out.println("ub_current="+ub_current);
                            System.out.println("\ts="+s);
                            System.out.println("\tb="+b);
                            System.out.println("\tp="+p);
                            System.out.println("\t\tmyFunc="+myFunc);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void findODsAffectedAndHowByMissingMoveLegs(String mySubdirectoryForAllMovePathsOfAllODs, Data myData) throws Exception {

        Locations allLocations = this.network.getLocations();

        Set<String> myLocations = allLocations.keys();

        if(myLocations != null) {
            System.out.println("Total # of locations we are considering = " + myLocations.size());
        }

        Set<String> myLocations_outer = new HashSet<String>();
        myLocations_outer.addAll(myLocations);
        Set<String> myLocations_inner = new HashSet<String>();
        myLocations_inner.addAll(myLocations);

        Map<String, Set<com.con_way.network.model.MoveLeg>> myMapOfSetOfAllMoveLegsByStringRep = new HashMap<String, Set<com.con_way.network.model.MoveLeg>>();

        Map<com.con_way.network.model.MoveLeg, Set<com.con_way.network.model.MovePath>> myMapOfSetOfMovePathsByMoveLeg = new HashMap<MoveLeg, Set<MovePath>>();

        Map<com.con_way.network.model.MovePath, Set<com.con_way.network.model.LoadLeg>> myMapOfSetOfLoadLegsByMovePath = new HashMap<com.con_way.network.model.MovePath, Set<com.con_way.network.model.LoadLeg>>();

        Map<com.con_way.network.model.LoadLeg, Set<com.con_way.network.model.LoadPath>> myMapOfSetOfLoadPathsByLoadLeg = new HashMap<com.con_way.network.model.LoadLeg, Set<com.con_way.network.model.LoadPath>>();

        Map<com.con_way.network.model.LoadPath, Set<OrigLocDestLoc>> myMapOfSetOfOrigLocDestLocByLoadPath = new HashMap<com.con_way.network.model.LoadPath, Set<OrigLocDestLoc>>();

        Map<com.con_way.network.model.Facility, Set<com.con_way.network.model.Facility>> mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath = new HashMap<Facility, Set<Facility>>();

        int totalNumLocations = myLocations_outer.size();
        int counter_outer=0;
        for(String originLocation_String : myLocations_outer) {
            counter_outer++;
            int counter_inner=0;
            for(String destinationLocation_String : myLocations_inner) {
                counter_inner++;
                System.out.println(counter_outer+" of "+totalNumLocations+" ... "+counter_inner+" of "+totalNumLocations);
                System.out.println(originLocation_String+","+destinationLocation_String);
                if(originLocation_String.equals(destinationLocation_String)) {
                    continue;
                }

                if(originLocation_String.equals("XBC")) {
                    if(destinationLocation_String.equals("XPI")) {
                        int temp_a = 0;
                    }
                }

                com.con_way.network.model.Facility orig = network.getFacility( network.getLocation(originLocation_String), Operation.O);
                com.con_way.network.model.Facility dest = network.getFacility( network.getLocation(destinationLocation_String), Operation.I);
                if(orig==null || dest==null) {
                    continue;
                }
                final com.con_way.network.model.LoadPath loadPath_default = api.searchLoadPath.defaultFlow(orig, dest);
                if(loadPath_default == null || loadPath_default.isEmpty()) {
                    // if load path null or empty, skip to the next one....
                    Set<com.con_way.network.model.Facility> mySetOfDestFacilities = mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath.get(orig);
                    if(mySetOfDestFacilities==null) {
                        mySetOfDestFacilities = new HashSet<com.con_way.network.model.Facility>();
                        mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath.put(orig, mySetOfDestFacilities);
                    }
                    mySetOfDestFacilities.add(dest);
                    continue;
                }

                OrigLocDestLoc myOrigLocDestLoc = myData.getOrigLocDestLoc(orig.getSic(), dest.getSic(), true);

                // since the default shift of orig is Outbound, we use 2 shifts out....
                Set<LoadPath> myLoadPaths_alternative = null;
                if(orig.getOperation().equals(Operation.O)) {
                    myLoadPaths_alternative = api.searchLoadPath.alternative(orig, dest, 2);
                } else if(orig.getOperation().equals(Operation.F)) {
                    myLoadPaths_alternative = api.searchLoadPath.alternative(orig, dest, 1);
                } else if(orig.getOperation().equals(Operation.I)) {
                    myLoadPaths_alternative = api.searchLoadPath.alternative(orig, dest, 0);
                }

                Set<LoadPath> myLoadPaths_all = new HashSet<LoadPath>();
                myLoadPaths_all.add(loadPath_default);
                if(myLoadPaths_alternative != null) {
                    for(LoadPath myLoadPath_alt : myLoadPaths_alternative) {
                        if(myLoadPath_alt == null || myLoadPath_alt.isEmpty()) {
                            continue;
                        }
                        myLoadPaths_all.add(myLoadPath_alt);
                    }
                }

                for(LoadPath myLoadPath : myLoadPaths_all) {
                    myOrigLocDestLoc.addLoadPath(myLoadPath);
                }

                this.doAnalysisOfLoadPaths(myMapOfSetOfAllMoveLegsByStringRep, myOrigLocDestLoc, myLoadPaths_all, myMapOfSetOfOrigLocDestLocByLoadPath, myMapOfSetOfMovePathsByMoveLeg, myMapOfSetOfLoadLegsByMovePath, myMapOfSetOfLoadPathsByLoadLeg);

            }
        }

        this.printOutODsWithNoLoadPaths(mySubdirectoryForAllMovePathsOfAllODs, mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath);

        Map<com.con_way.network.model.MoveLeg, Set<OrigLocDestLoc>> myMapOfSetOfODsEffectedByAbsenceOfMoveLegs = new HashMap<MoveLeg, Set<OrigLocDestLoc>>();
        int totalNumMoveLegs = myMapOfSetOfAllMoveLegsByStringRep.size();
        int counter_leg = 0;
        for(String myMoveLegString : myMapOfSetOfAllMoveLegsByStringRep.keySet()) {
            counter_leg++;
            System.out.println(counter_leg+" of "+totalNumMoveLegs+" ... "+myMoveLegString);
            Set<com.con_way.network.model.MoveLeg> mySetOfMoveLegsToBeRemoved = myMapOfSetOfAllMoveLegsByStringRep.get(myMoveLegString);
            if(mySetOfMoveLegsToBeRemoved==null || mySetOfMoveLegsToBeRemoved.size()==0) {
                continue;
            }

            com.con_way.network.model.MoveLeg myMoveLeg_OB_FAC = null;
            for(com.con_way.network.model.MoveLeg myMoveLeg : mySetOfMoveLegsToBeRemoved) {
                if(myMoveLeg==null) {
                    continue;
                }

                if(myMoveLeg.getOrig().getOperation().equals(Operation.O)) {
                    myMoveLeg_OB_FAC = myMoveLeg;
                    break;
                }
            }

            if(myMoveLeg_OB_FAC==null) {
                // this is a problem
                continue;
            }

            Set<com.con_way.network.model.MovePath> setOfMovePaths = new HashSet<MovePath>();

            for(com.con_way.network.model.MoveLeg myMoveLeg : mySetOfMoveLegsToBeRemoved) {

                Set<com.con_way.network.model.MovePath> setOfMovePaths_temp = myMapOfSetOfMovePathsByMoveLeg.get(myMoveLeg);
                if(setOfMovePaths_temp==null || setOfMovePaths_temp.size()==0) {
                    continue;
                }
                setOfMovePaths.addAll(setOfMovePaths_temp);
            }

            Set<com.con_way.network.model.LoadLeg> setOfLoadLegs = new HashSet<LoadLeg>();

            for(com.con_way.network.model.MovePath myMovePath : setOfMovePaths) {
                Set<com.con_way.network.model.LoadLeg> setOfLoadLegs_temp = myMapOfSetOfLoadLegsByMovePath.get(myMovePath);
                if(setOfLoadLegs_temp==null || setOfLoadLegs_temp.size()==0) {
                    continue;
                }
                setOfLoadLegs.addAll(setOfLoadLegs_temp);
            }

            Set<com.con_way.network.model.LoadPath> setOfLoadPaths = new HashSet<LoadPath>();

            for(com.con_way.network.model.LoadLeg myLoadLeg : setOfLoadLegs) {
                Set<com.con_way.network.model.LoadPath> setOfLoadPath_temp = myMapOfSetOfLoadPathsByLoadLeg.get(myLoadLeg);
                if(setOfLoadPath_temp==null || setOfLoadPath_temp.size()==0) {
                    continue;
                }
                setOfLoadPaths.addAll(setOfLoadPath_temp);
            }

            for(String myOrigLocDestLocString : myData.myMapOfOrigLocDestLocByString.keySet()) {
                OrigLocDestLoc myOrigLocDestLoc = myData.myMapOfOrigLocDestLocByString.get(myOrigLocDestLocString);
                if(myOrigLocDestLoc == null) {
                    continue;
                }

                Set<LoadPath> myLoadPathsForThisOD = myOrigLocDestLoc.getLoadPath_All();

                boolean thisODHasNoLoadPathsAtAll = false;
                if(myLoadPathsForThisOD==null || myLoadPathsForThisOD.size()==0) {
                    // this is odd.....we are NOT finding any paths load paths at all for these.
                    int temp_a = 0;
                    thisODHasNoLoadPathsAtAll=true;
                }

                // we need to determine if all of this ODs loadpaths are a part of setOfLoadPaths.
                // If they are...this OD is effected.

                boolean areAllThisODsLoadPathsInBigSet = true;
                if(myLoadPathsForThisOD != null && myLoadPathsForThisOD.size()>0) {
                    for (LoadPath thisODLoadPath : myLoadPathsForThisOD) {
                        if (!setOfLoadPaths.contains(thisODLoadPath)) {
                            areAllThisODsLoadPathsInBigSet = false;
                            break;
                        }
                    }
                }

                if(thisODHasNoLoadPathsAtAll) {
                    areAllThisODsLoadPathsInBigSet = false;
                }

                if(areAllThisODsLoadPathsInBigSet) {
                    // add this OD to set of OD broken by absence of these move legs.
                    Set<OrigLocDestLoc> mySetOfODs = myMapOfSetOfODsEffectedByAbsenceOfMoveLegs.get(myMoveLeg_OB_FAC);
                    if(mySetOfODs==null) {
                        mySetOfODs = new HashSet<OrigLocDestLoc>();
                        myMapOfSetOfODsEffectedByAbsenceOfMoveLegs.put(myMoveLeg_OB_FAC, mySetOfODs);
                    }
                    mySetOfODs.add(myOrigLocDestLoc);
                }
            }

        }

        String fileName = mySubdirectoryForAllMovePathsOfAllODs+"MoveLegsAndEffectedODs.csv";

        FileWriter fstream = new FileWriter(fileName, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        StringBuffer header = new StringBuffer();
        header.append("moveLegCounter"+",");
        header.append("numODsEffected"+",");
        header.append("Origin"+",");
        header.append("OriginShift"+",");
        header.append("Destination"+",");
        header.append("DestinationShift"+",");
        header.append("Mode"+",");
        header.append("ODCounter"+",");
        header.append("Origin"+",");
        header.append("Destination"+",");

        myPrintWriter.println(header);

        int counter_moveLeg = 0;
        for(com.con_way.network.model.MoveLeg myMoveLeg : myMapOfSetOfODsEffectedByAbsenceOfMoveLegs.keySet()) {

            Set<OrigLocDestLoc> mySetOfODs = myMapOfSetOfODsEffectedByAbsenceOfMoveLegs.get(myMoveLeg);

            counter_moveLeg++;
            int numOfODs = 0;

            if(mySetOfODs != null) {
                numOfODs = mySetOfODs.size();
            }

            StringBuffer record_moveLeg = new StringBuffer();

            record_moveLeg.append(counter_moveLeg+",");
            record_moveLeg.append(numOfODs+",");
            record_moveLeg.append(myMoveLeg.getOrigSic()+",");
            record_moveLeg.append(myMoveLeg.getOrigOp()+",");
            record_moveLeg.append(myMoveLeg.getDestSic()+",");
            record_moveLeg.append(myMoveLeg.getDestOp()+",");
            record_moveLeg.append(myMoveLeg.getMode()+",");

            if(mySetOfODs != null) {
                int counter_OD = 0;
                for (OrigLocDestLoc myOrigLocDestLoc : mySetOfODs) {
                    counter_OD++;
                    StringBuffer record_OD = new StringBuffer();

                    record_OD.append(counter_OD + ",");
                    record_OD.append(myOrigLocDestLoc.getLocation_origin() + ",");
                    record_OD.append(myOrigLocDestLoc.getLocation_destination() + ",");

                    myPrintWriter.println(record_moveLeg.toString() + record_OD.toString());
                }
            } else {
                StringBuffer record_OD = new StringBuffer();

                record_OD.append(0 + ",");
                record_OD.append("" + ",");
                record_OD.append("" + ",");

                myPrintWriter.println(record_moveLeg.toString() + record_OD.toString());

            }

        }

        myPrintWriter.close();
        fstream.close();

        int temp_a = 0;

    }

    public void printOutODsWithNoLoadPaths(String mySubdirectoryToPrintTo, Map<com.con_way.network.model.Facility, Set<com.con_way.network.model.Facility>> mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath) throws Exception {
        String fileName_ODsWithNoLoadPaths = mySubdirectoryToPrintTo+"ODsWithNoLoadPaths.csv";

        FileWriter fstream_ODsWithNoLoadPaths = new FileWriter(fileName_ODsWithNoLoadPaths, false);
        PrintWriter myPrintWriter_ODsWithNoLoadPaths = new PrintWriter(fstream_ODsWithNoLoadPaths);

        StringBuffer header = new StringBuffer();
        header.append("orig" + ",");
        header.append("origShift" + ",");
        header.append("dest" + ",");
        header.append("destShift" + ",");
        myPrintWriter_ODsWithNoLoadPaths.println(header);

        for(com.con_way.network.model.Facility orig : mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath.keySet()) {
            Set<com.con_way.network.model.Facility> mySetOfDestFacilities = mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath.get(orig);

            for(com.con_way.network.model.Facility dest : mySetOfDestFacilities) {
                StringBuffer record = new StringBuffer();
                record.append(orig + ",");
                record.append(dest + ",");
                myPrintWriter_ODsWithNoLoadPaths.println(record);
            }
        }

        myPrintWriter_ODsWithNoLoadPaths.close();
        fstream_ODsWithNoLoadPaths.close();

    }

    public void findODsAffectedAndHowByMissingLoadLegs(String mySubdirectoryForAllLoadPathsOfAllODs, Data myData) throws Exception {

        Locations allLocations = this.network.getLocations();

        Set<String> myLocations = allLocations.keys();

        if(myLocations != null) {
            System.out.println("Total # of locations we are considering = " + myLocations.size());
        }

        Set<String> myLocations_outer = new HashSet<String>();
        myLocations_outer.addAll(myLocations);
        Set<String> myLocations_inner = new HashSet<String>();
        myLocations_inner.addAll(myLocations);

        Map<com.con_way.network.model.LoadLeg, Set<com.con_way.network.model.LoadPath>> myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg = new HashMap<com.con_way.network.model.LoadLeg, Set<com.con_way.network.model.LoadPath>>();

        Map<com.con_way.network.model.LoadPath, Set<OrigLocDestLoc>> myMapOfSetOfOrigLocDestLocByLoadPath = new HashMap<com.con_way.network.model.LoadPath, Set<OrigLocDestLoc>>();

        Map<com.con_way.network.model.Facility, Set<com.con_way.network.model.Facility>> mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath = new HashMap<Facility, Set<Facility>>();

        Map<com.con_way.network.model.Facility, Set<com.con_way.network.model.LoadLeg>> mapOfSetsOfDepartingLoadLegsByOriginFacility = new HashMap<Facility, Set<LoadLeg>>();

        LoadLegs myLoadLegs = this.network.getLoadLegs();
        for(String myLoadLegString : myLoadLegs.keys()) {
            LoadLeg myLoadLeg = myLoadLegs.get(myLoadLegString);
            Facility myOriginFacility = myLoadLeg.getOrig();
            Set<com.con_way.network.model.LoadLeg> mySet = mapOfSetsOfDepartingLoadLegsByOriginFacility.get(myOriginFacility);
            if(mySet==null) {
                mySet = new HashSet<com.con_way.network.model.LoadLeg>();
                mapOfSetsOfDepartingLoadLegsByOriginFacility.put(myOriginFacility, mySet);
            }
            mySet.add(myLoadLeg);
        }

        int totalNumLocations = myLocations_outer.size();
        int counter_outer=0;
        for(String originLocation_String : myLocations_outer) {
            counter_outer++;
            int counter_inner=0;
            for(String destinationLocation_String : myLocations_inner) {
                counter_inner++;
                System.out.println(counter_outer+" of "+totalNumLocations+" ... "+counter_inner+" of "+totalNumLocations);
                System.out.println(originLocation_String+","+destinationLocation_String);
                if(originLocation_String.equals(destinationLocation_String)) {
                    continue;
                }

                com.con_way.network.model.Facility orig = network.getFacility( network.getLocation(originLocation_String), Operation.O);
                com.con_way.network.model.Facility dest = network.getFacility( network.getLocation(destinationLocation_String), Operation.I);
                if(orig==null || dest==null) {
                    continue;
                }
                final com.con_way.network.model.LoadPath loadPath_default = api.searchLoadPath.defaultFlow(orig, dest);
                if(loadPath_default == null || loadPath_default.isEmpty()) {
                    // if load path null or empty, skip to the next one....
                    Set<com.con_way.network.model.Facility> mySetOfDestFacilities = mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath.get(orig);
                    if(mySetOfDestFacilities==null) {
                        mySetOfDestFacilities = new HashSet<com.con_way.network.model.Facility>();
                        mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath.put(orig, mySetOfDestFacilities);
                    }
                    mySetOfDestFacilities.add(dest);
                    continue;
                }

                OrigLocDestLoc myOrigLocDestLoc = myData.getOrigLocDestLoc(orig.getSic(), dest.getSic(), true);

                myOrigLocDestLoc.setOrig(orig);
                myOrigLocDestLoc.setDest(dest);

                myOrigLocDestLoc.setLoadPath_default(loadPath_default);

                this.doAnalysisOfALoadPath(myOrigLocDestLoc, true, loadPath_default, myMapOfSetOfOrigLocDestLocByLoadPath, myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg, mapOfSetsOfDepartingLoadLegsByOriginFacility);

                // since the default shift of orig is Outbound, we use 2 shifts out....
                Set<LoadPath> myLoadPaths_alternative = null;
                if(orig.getOperation().equals(Operation.O)) {
                    myLoadPaths_alternative = api.searchLoadPath.alternative(orig, dest, 2);
                } else if(orig.getOperation().equals(Operation.F)) {
                    myLoadPaths_alternative = api.searchLoadPath.alternative(orig, dest, 1);
                } else if(orig.getOperation().equals(Operation.I)) {
                    myLoadPaths_alternative = api.searchLoadPath.alternative(orig, dest, 0);
                }

                if(myLoadPaths_alternative != null) {
                    for (LoadPath myLoadPath_alt : myLoadPaths_alternative) {
                        if(myLoadPath_alt == null || myLoadPath_alt.isEmpty()) {
                            continue;
                        }
                        this.doAnalysisOfALoadPath(myOrigLocDestLoc, false, myLoadPath_alt, myMapOfSetOfOrigLocDestLocByLoadPath, myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg, mapOfSetsOfDepartingLoadLegsByOriginFacility);
                    }
                }


            }
        }

        this.printOutODsWithNoLoadPaths(mySubdirectoryForAllLoadPathsOfAllODs, mapOfSetsOfDestFacilitiesByOriginFacilityWithNoDefaultLoadPath);

        Map<com.con_way.network.model.LoadLeg, Set<OrigLocDestLoc>> myMapOfSetOfODsEffectedByAbsenceOfLoadLegs = new HashMap<LoadLeg, Set<OrigLocDestLoc>>();
        int totalNumLoadLegs = myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg.size();
        int counter_LoadLeg = 0;

        Set<com.con_way.network.model.LoadLeg> setOfLoadLegsThatHaveTooManyODsCrossingThem = new HashSet<LoadLeg>();
        Set<com.con_way.network.model.LoadLeg> setOfLoadLegsThatWeWillConsiderCuttingPossibly = new HashSet<LoadLeg>();

        for(com.con_way.network.model.LoadLeg myLoadLeg : myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg.keySet()) {
            counter_LoadLeg++;
            System.out.println(counter_LoadLeg+" of "+totalNumLoadLegs+" ... "+myLoadLeg);

            if(this.isStationary(myLoadLeg)) {
                // don't consider these load legs for cancellation....they wouldn't result in real savings.
                continue;
            }

            if(!this.isOB_FAC_or_FAC_IB(myLoadLeg)) {
                continue;
            }

            boolean isOrigServiceCenter=false;
            if(myLoadLeg.getOrig().getLocation().isServiceCenter()) {
                isOrigServiceCenter = true;
            }

            boolean isDestServiceCenter=false;
            if(myLoadLeg.getDest().getLocation().isServiceCenter()) {
                isDestServiceCenter = true;
            }

            // we will only consider a load leg for cutting if both origin and destination are service centers
            if(!isOrigServiceCenter || !isOrigServiceCenter) {
                continue;
            }

            Set<OrigLocDestLoc> mySetOfODsImpactedByThisLoadLeg = this.getAllODsCrossingThisLoadLeg(myLoadLeg, myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg, myMapOfSetOfOrigLocDestLocByLoadPath);

            if(mySetOfODsImpactedByThisLoadLeg.size()>Constant.NUMBER_OF_ODS_CONSIDERED_TO_BE_TOO_MANY_CROSSING_A_LOADLEG_TO_CONSIDER_CUTTING_THAT_LOADLEG) {
                setOfLoadLegsThatHaveTooManyODsCrossingThem.add(myLoadLeg);
            } else {
                setOfLoadLegsThatWeWillConsiderCuttingPossibly.add(myLoadLeg);
            }

        }

        Map<String, Set<com.con_way.network.model.LoadLeg>> myMapOfSetOfLoadLegByStringOf_OB_FAC_loadleg = new HashMap<String, Set<LoadLeg>>();

        for(com.con_way.network.model.LoadLeg myLoadLeg_thatWeAreConsideringCutting : setOfLoadLegsThatWeWillConsiderCuttingPossibly) {
            String myOB_FACString = this.getOB_FACString(myLoadLeg_thatWeAreConsideringCutting);
            if(myOB_FACString==null) {
                // odd....why???
                int temp_a = 0;
                continue;
            }
            Set<com.con_way.network.model.LoadLeg> mySetOfLoadLeg = myMapOfSetOfLoadLegByStringOf_OB_FAC_loadleg.get(myOB_FACString);
            if(mySetOfLoadLeg==null) {
                mySetOfLoadLeg = new HashSet<com.con_way.network.model.LoadLeg>();
                myMapOfSetOfLoadLegByStringOf_OB_FAC_loadleg.put(myOB_FACString, mySetOfLoadLeg);
            }
            mySetOfLoadLeg.add(myLoadLeg_thatWeAreConsideringCutting);
            if(mySetOfLoadLeg.size()>2) {
                // odd...should only contain the OB-FAC and the FAC_IB load legs
                int temp_a = 0;
            }
        }

        // Iterate across each load leg....
        // find all ODs that cross that load leg.
        // step back on load leg to point that loads to the

        String fileName_LoadLegsToCutAndImplicationsForODs_outAndBack = mySubdirectoryForAllLoadPathsOfAllODs+"LoadLegsToCutAndImplicationsForODs_outAndBack.csv";

        FileWriter fstream_LoadLegsToCutAndImplicationsForODs_outAndBack = new FileWriter(fileName_LoadLegsToCutAndImplicationsForODs_outAndBack, false);
        PrintWriter myPrintWriter_LoadLegsToCutAndImplicationsForODs_outAndBack = new PrintWriter(fstream_LoadLegsToCutAndImplicationsForODs_outAndBack);

        StringBuffer header = new StringBuffer();
        header.append("LL: orig" + ",");
        header.append("LL: origShift" + ",");
        header.append("LL: dest" + ",");
        header.append("LL: destShift" + ",");
        header.append("LL: mode" + ",");
        header.append("LL: Cycle" + ",");
        header.append("LL: totalNumODsThatViolateServiceButStillReachDest" + ",");
        header.append("LL: totalNumODsThatDONOTViolateServiceANDStillReachDest" + ",");
        header.append("LL: totalNumODsThatViolateServiceANDDONOTReachDest" + ",");
        header.append("OD: origin" + ",");
        header.append("OD: originShift" + ",");
        header.append("OD: destination" + ",");
        header.append("OD: destinationShift" + ",");
        header.append("OD: original service" + ",");
        header.append("OD: original mileage" + ",");
        header.append("service violated" + ",");
        header.append("new service" + ",");
        header.append("mileage_new" + ",");
        header.append("newLoadPath" + ",");

        myPrintWriter_LoadLegsToCutAndImplicationsForODs_outAndBack.println(header);

        int totalNumOB_FACString = myMapOfSetOfLoadLegByStringOf_OB_FAC_loadleg.size();
        int myOB_FACStringCounter=0;
        for(String myOB_FACString : myMapOfSetOfLoadLegByStringOf_OB_FAC_loadleg.keySet()) {
            Set<com.con_way.network.model.LoadLeg> mySetOfOB_FAC_and_FAC_IB_LoadLegsWeAreCutting = myMapOfSetOfLoadLegByStringOf_OB_FAC_loadleg.get(myOB_FACString);

            if(mySetOfOB_FAC_and_FAC_IB_LoadLegsWeAreCutting==null) {
                continue;
            }

            if(mySetOfOB_FAC_and_FAC_IB_LoadLegsWeAreCutting.size() != 2) {
                // this is odd...  We should have an OB-FAC and FAC-IB load leg
                continue;
            }

            myOB_FACStringCounter++;

            String mySystemPrintlnBuffer_1 = myOB_FACStringCounter+" of "+totalNumOB_FACString+" : "+myOB_FACString;

            com.con_way.network.model.LoadLeg myLoadLeg_OB_FAC_thatWeAreConsideringCutting = null;
            com.con_way.network.model.LoadLeg myLoadLeg_FAC_IB_thatWeAreConsideringCutting = null;
            for(com.con_way.network.model.LoadLeg myLoadLeg : mySetOfOB_FAC_and_FAC_IB_LoadLegsWeAreCutting) {
                if(this.isOB_FAC(myLoadLeg)) {
                    myLoadLeg_OB_FAC_thatWeAreConsideringCutting = myLoadLeg;
                } else if(this.isFAC_IB(myLoadLeg)) {
                    myLoadLeg_FAC_IB_thatWeAreConsideringCutting = myLoadLeg;
                }
            }

            if(myLoadLeg_OB_FAC_thatWeAreConsideringCutting==null || myLoadLeg_FAC_IB_thatWeAreConsideringCutting==null) {
                // either the OB-FAC or FAC-IB leg is missing....
                continue;
            }

            if(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getOrigSic().equals("XPI")) {
                if(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getDestSic().equals("XNJ")) {
                    int temp_a = 0;
                }
            }

            StringBuffer LL_record = new StringBuffer();
            LL_record.append(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getOrigSic() + ",");
            LL_record.append(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getOrigOp() + ",");
            LL_record.append(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getDestSic() + ",");
            LL_record.append(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getDestOp() + ",");
            LL_record.append(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getMode() + ",");
            LL_record.append(myLoadLeg_OB_FAC_thatWeAreConsideringCutting.getCycle() + ",");

            Set<OrigLocDestLoc> mySetOfODsImpactedByThisLoadLeg_OB_FAC = null;
            Set<OrigLocDestLoc> mySetOfODsImpactedByThisLoadLeg_FAC_IB = null;

            if(myLoadLeg_OB_FAC_thatWeAreConsideringCutting != null) {
                mySetOfODsImpactedByThisLoadLeg_OB_FAC = this.getAllODsCrossingThisLoadLeg(myLoadLeg_OB_FAC_thatWeAreConsideringCutting, myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg, myMapOfSetOfOrigLocDestLocByLoadPath);
            }
            if(myLoadLeg_FAC_IB_thatWeAreConsideringCutting != null) {
                mySetOfODsImpactedByThisLoadLeg_FAC_IB = this.getAllODsCrossingThisLoadLeg(myLoadLeg_FAC_IB_thatWeAreConsideringCutting, myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg, myMapOfSetOfOrigLocDestLocByLoadPath);
            }

            Set<OrigLocDestLoc> mySetOfODsImpactedByThisLoadLeg = new HashSet<OrigLocDestLoc>();
            if(mySetOfODsImpactedByThisLoadLeg_OB_FAC != null) {
                mySetOfODsImpactedByThisLoadLeg.addAll(mySetOfODsImpactedByThisLoadLeg_OB_FAC);
            }
            if(mySetOfODsImpactedByThisLoadLeg_FAC_IB != null) {
                mySetOfODsImpactedByThisLoadLeg.addAll(mySetOfODsImpactedByThisLoadLeg_FAC_IB);
            }

            Map<OrigLocDestLoc, SortedMap<Integer, Set<String>>> mapOfODStringsToPrintByCycleByOD = new HashMap<OrigLocDestLoc, SortedMap<Integer, Set<String>>>();

            int totalNumODs = mySetOfODsImpactedByThisLoadLeg.size();
            int ODCounter=0;
            for(OrigLocDestLoc myOrigLocDestLoc : mySetOfODsImpactedByThisLoadLeg) {
                ODCounter++;

                System.out.println(mySystemPrintlnBuffer_1+"\t"+ODCounter+" of "+totalNumODs+" : "+myOrigLocDestLoc);

                StringBuffer OD_record_constantForThisOD = new StringBuffer();
                OD_record_constantForThisOD.append(myOrigLocDestLoc.getOrig().getSic() + ",");
                OD_record_constantForThisOD.append(myOrigLocDestLoc.getOrig().getOp() + ",");
                OD_record_constantForThisOD.append(myOrigLocDestLoc.getDest().getSic() + ",");
                OD_record_constantForThisOD.append(myOrigLocDestLoc.getDest().getOp() + ",");

                if(myOrigLocDestLoc.getLocation_origin().equals("XHR")) {
                    if(myOrigLocDestLoc.getLocation_destination().equals("XPI")) {
                        int temp_a = 0;
                    }
                }

                LoadPath myLoadPath_default = myOrigLocDestLoc.getLoadPath_default();
                int cycle_loadPath_default = myLoadPath_default.getCycle();
                int distance_loadPath_default = myLoadPath_default.getDistance();

                OD_record_constantForThisOD.append(cycle_loadPath_default + ",");
                OD_record_constantForThisOD.append(distance_loadPath_default + ",");

                Set<LoadPath> mySetOfLoadPaths_all = myOrigLocDestLoc.getLoadPath_All();

                List<LoadPath> myListOfLoadPaths_all = new ArrayList<LoadPath>();

                myListOfLoadPaths_all.add(myLoadPath_default);
                for(LoadPath myLoadPath_current : mySetOfLoadPaths_all) {
                    if(myLoadPath_current.equals(myLoadPath_default)) {
                        continue;
                    }
                    myListOfLoadPaths_all.add(myLoadPath_current);
                }

                int loadPathCounter=0;
                // we will allow ourselves to stop looking at loadpaths...when we find one that
                // gets to the destination and preserves the original service.
                boolean canWeStopLookingAtLoadPathsForThisOD = false;
                for(LoadPath myLoadPath_current : myListOfLoadPaths_all) {
                    loadPathCounter++;

                    boolean isThisTheLastLoadPath = false;
                    if(loadPathCounter == myListOfLoadPaths_all.size()) {
                        isThisTheLastLoadPath = true;
                    }

                    // initialize the meta-loop
                    int numPrecedingLegs = -1;
                    boolean canWeStopSteppingBackAndThenSearchingForwardAgainForThisParticularLoadPathForThisParticularOD = false;
                    while (!canWeStopSteppingBackAndThenSearchingForwardAgainForThisParticularLoadPathForThisParticularOD) {

                        numPrecedingLegs++;

                        //System.out.println("\t\tnumPrecedingLegs=" + numPrecedingLegs);

                        com.con_way.network.model.LoadLeg myLoadLeg_preceding_byNumPrecedingLegs = null;
                        // find the load leg that precedes myLoadLeg....if it exists...
                        if (myLoadPath_current.getLegs().contains(myLoadLeg_OB_FAC_thatWeAreConsideringCutting)) {
                            myLoadLeg_preceding_byNumPrecedingLegs = this.getLoadLeg_preceding(numPrecedingLegs, myLoadLeg_OB_FAC_thatWeAreConsideringCutting, myLoadPath_current);
                        } else if (myLoadPath_current.getLegs().contains(myLoadLeg_FAC_IB_thatWeAreConsideringCutting)) {
                            myLoadLeg_preceding_byNumPrecedingLegs = this.getLoadLeg_preceding(numPrecedingLegs, myLoadLeg_FAC_IB_thatWeAreConsideringCutting, myLoadPath_current);
                        }

                        Facility myFacility_origin = null;
                        if (myLoadLeg_preceding_byNumPrecedingLegs != null) {
                            myFacility_origin = myLoadLeg_preceding_byNumPrecedingLegs.getOrig();
                        }

                        if (myFacility_origin == null) {
                            // Examples where there is no way to make the connection at all.....
                            if(isThisTheLastLoadPath) {
                                StringBuffer OD_record_ending = new StringBuffer();
                                OD_record_ending.append(true + ",");
                                OD_record_ending.append(Constant.EXTREMELY_HIGH_CYCLE_COUNT + ",");
                                OD_record_ending.append(Constant.EXTREMELY_HIGH_CYCLE_COUNT + ",");
                                OD_record_ending.append("NoWayToMakeConnection" + ",");
                                StringBuffer OD_record_total = new StringBuffer();
                                OD_record_total.append(OD_record_constantForThisOD);
                                OD_record_total.append(OD_record_ending);
                                this.addEntryToMap(myOrigLocDestLoc, Constant.EXTREMELY_HIGH_CYCLE_COUNT, OD_record_total, mapOfODStringsToPrintByCycleByOD);
                            }
                            // break and go to the next LoadPath...if it exists
                            break;
                        }

                        SortedMap<Integer, Set<RepointToBypassMissingLoadLeg>> mapOfRepointToBypassMissingLoadLegsThatViolateServiceByDistance = new TreeMap<Integer, Set<RepointToBypassMissingLoadLeg>>();
                        SortedMap<Integer, Set<RepointToBypassMissingLoadLeg>> mapOfRepointToBypassMissingLoadLegsThatDoNotViolateServiceByDistance = new TreeMap<Integer, Set<RepointToBypassMissingLoadLeg>>();

                        // search across all loadlegs originating at myFacility_origin....and find path from
                        // the destination of those loadlegs to our final dest....
                        Set<com.con_way.network.model.LoadLeg> setOfLoadLegsRootedAt_myFacility_origin = mapOfSetsOfDepartingLoadLegsByOriginFacility.get(myFacility_origin);
                        for (com.con_way.network.model.LoadLeg myLoadLegRootedAt_myFacility_origin : setOfLoadLegsRootedAt_myFacility_origin) {
                            if (mySetOfOB_FAC_and_FAC_IB_LoadLegsWeAreCutting.contains(myLoadLegRootedAt_myFacility_origin)) {
                                // we don't want to re-use the legs we are cutting
                                continue;
                            }
                            Facility myDest_myLoadLegRootedAt_myFacility_origin = myLoadLegRootedAt_myFacility_origin.getDest();

                            List<com.con_way.network.model.LoadPath> loadPaths_all_from_myDest_myLoadLegRootedAt_myFacility_origin = this.getLoadPaths(myDest_myLoadLegRootedAt_myFacility_origin, myOrigLocDestLoc.getDest());

                            for(com.con_way.network.model.LoadPath myLoadPath_currentFromMyDest_myLoadLegRootedAt_myFacility_origin : loadPaths_all_from_myDest_myLoadLegRootedAt_myFacility_origin) {

                                List<LoadLeg> myListOfLoadLegs_of_loadPath_currentFromMyDest_myLoadLegRootedAt_myFacility_origin = myLoadPath_currentFromMyDest_myLoadLegRootedAt_myFacility_origin.getLegs();
                                if (myListOfLoadLegs_of_loadPath_currentFromMyDest_myLoadLegRootedAt_myFacility_origin.contains(myLoadLeg_OB_FAC_thatWeAreConsideringCutting)) {
                                    // we don't want to re-use the OB_FAC leg we are cutting
                                    continue;
                                }
                                if (myListOfLoadLegs_of_loadPath_currentFromMyDest_myLoadLegRootedAt_myFacility_origin.contains(myLoadLeg_FAC_IB_thatWeAreConsideringCutting)) {
                                    // we don't want to re-use the FAC_IB leg we are cutting
                                    continue;
                                }

                                RepointToBypassMissingLoadLeg myRepointToBypassMissingLoadLeg = new RepointToBypassMissingLoadLeg(
                                        myLoadPath_current,
                                        myFacility_origin,
                                        myLoadLegRootedAt_myFacility_origin,
                                        myLoadPath_currentFromMyDest_myLoadLegRootedAt_myFacility_origin
                                );
                                int cycle_newLoadPath = myRepointToBypassMissingLoadLeg.getCycle();
                                int distance_newLoadPath = myRepointToBypassMissingLoadLeg.getDistance();
                                boolean violateService = false;
                                if (cycle_newLoadPath > cycle_loadPath_default) {
                                    violateService = true;
                                    Set<RepointToBypassMissingLoadLeg> mySet = mapOfRepointToBypassMissingLoadLegsThatViolateServiceByDistance.get(distance_newLoadPath);
                                    if (mySet == null) {
                                        mySet = new HashSet<RepointToBypassMissingLoadLeg>();
                                        mapOfRepointToBypassMissingLoadLegsThatViolateServiceByDistance.put(distance_newLoadPath, mySet);
                                    }
                                    mySet.add(myRepointToBypassMissingLoadLeg);
                                } else {
                                    Set<RepointToBypassMissingLoadLeg> mySet = mapOfRepointToBypassMissingLoadLegsThatDoNotViolateServiceByDistance.get(distance_newLoadPath);
                                    if (mySet == null) {
                                        mySet = new HashSet<RepointToBypassMissingLoadLeg>();
                                        mapOfRepointToBypassMissingLoadLegsThatDoNotViolateServiceByDistance.put(distance_newLoadPath, mySet);
                                    }
                                    mySet.add(myRepointToBypassMissingLoadLeg);
                                }
                                int temp_a = 0;
                            }
                        }
                        if (mapOfRepointToBypassMissingLoadLegsThatDoNotViolateServiceByDistance != null && mapOfRepointToBypassMissingLoadLegsThatDoNotViolateServiceByDistance.size() > 0) {
                            canWeStopSteppingBackAndThenSearchingForwardAgainForThisParticularLoadPathForThisParticularOD = true;
                            canWeStopLookingAtLoadPathsForThisOD=true;
                            // not violated....
                            StringBuffer OD_record_ending = new StringBuffer();
                            OD_record_ending.append(false + ",");
                            RepointToBypassMissingLoadLeg myRepointToBypassMissingLoadLeg_toUse = null;
                            for (Integer myDist : mapOfRepointToBypassMissingLoadLegsThatDoNotViolateServiceByDistance.keySet()) {
                                Set<RepointToBypassMissingLoadLeg> mySet = mapOfRepointToBypassMissingLoadLegsThatDoNotViolateServiceByDistance.get(myDist);
                                for (RepointToBypassMissingLoadLeg myRepointToBypassMissingLoadLeg : mySet) {
                                    myRepointToBypassMissingLoadLeg_toUse = myRepointToBypassMissingLoadLeg;
                                    break;
                                }
                                if (myRepointToBypassMissingLoadLeg_toUse != null) {
                                    break;
                                }
                            }
                            OD_record_ending.append(myRepointToBypassMissingLoadLeg_toUse.getCycle() + ",");
                            OD_record_ending.append(myRepointToBypassMissingLoadLeg_toUse.getDistance() + ",");
                            OD_record_ending.append(myRepointToBypassMissingLoadLeg_toUse.getMyLoadLegRootedAt_myFacility_origin() + ",");
                            StringBuffer OD_record_total = new StringBuffer();
                            OD_record_total.append(OD_record_constantForThisOD);
                            OD_record_total.append(OD_record_ending);
                            this.addEntryToMap(myOrigLocDestLoc, myRepointToBypassMissingLoadLeg_toUse.getCycle(), OD_record_total, mapOfODStringsToPrintByCycleByOD);
                        } else if (mapOfRepointToBypassMissingLoadLegsThatViolateServiceByDistance != null && mapOfRepointToBypassMissingLoadLegsThatViolateServiceByDistance.size() > 0) {
                            canWeStopSteppingBackAndThenSearchingForwardAgainForThisParticularLoadPathForThisParticularOD = false;
                            // violated
                            StringBuffer OD_record_ending = new StringBuffer();
                            OD_record_ending.append(true + ",");
                            RepointToBypassMissingLoadLeg myRepointToBypassMissingLoadLeg_toUse = null;
                            for (Integer myDist : mapOfRepointToBypassMissingLoadLegsThatViolateServiceByDistance.keySet()) {
                                Set<RepointToBypassMissingLoadLeg> mySet = mapOfRepointToBypassMissingLoadLegsThatViolateServiceByDistance.get(myDist);
                                for (RepointToBypassMissingLoadLeg myRepointToBypassMissingLoadLeg : mySet) {
                                    myRepointToBypassMissingLoadLeg_toUse = myRepointToBypassMissingLoadLeg;
                                    break;
                                }
                                if (myRepointToBypassMissingLoadLeg_toUse != null) {
                                    break;
                                }
                            }
                            OD_record_ending.append(myRepointToBypassMissingLoadLeg_toUse.getCycle() + ",");
                            OD_record_ending.append(myRepointToBypassMissingLoadLeg_toUse.getDistance() + ",");
                            OD_record_ending.append(myRepointToBypassMissingLoadLeg_toUse.getMyLoadLegRootedAt_myFacility_origin() + ",");
                            StringBuffer OD_record_total = new StringBuffer();
                            OD_record_total.append(OD_record_constantForThisOD);
                            OD_record_total.append(OD_record_ending);
                            this.addEntryToMap(myOrigLocDestLoc, myRepointToBypassMissingLoadLeg_toUse.getCycle(), OD_record_total, mapOfODStringsToPrintByCycleByOD);
                        } else {
                            // we didn't find a loadpath that gets you to the final destination
                            canWeStopSteppingBackAndThenSearchingForwardAgainForThisParticularLoadPathForThisParticularOD = false;
                        }
                    }

                    if(canWeStopLookingAtLoadPathsForThisOD) {
                        // If we managed to output above....then we can stop...
                        break;
                    }
                }

            }  // end OD loop

            int totalNumODsThatViolateServiceButStillReachDest = 0;
            int totalNumODsThatDONOTViolateServiceANDStillReachDest = 0;
            int totalNumODsThatViolateServiceANDDONOTReachDest = 0;

            for(OrigLocDestLoc myOrigLocDestLoc: mapOfODStringsToPrintByCycleByOD.keySet()) {
                SortedMap<Integer, Set<String>> mySortedMap = mapOfODStringsToPrintByCycleByOD.get(myOrigLocDestLoc);
                if(mySortedMap==null) {
                    continue;
                }
                int defaultService = myOrigLocDestLoc.getDefaultServiceLevel();
                for(Integer myCycle_new : mySortedMap.keySet()) {
                    if(myCycle_new<=defaultService) {
                        totalNumODsThatDONOTViolateServiceANDStillReachDest++;
                    } else if(myCycle_new>defaultService && myCycle_new != Constant.EXTREMELY_HIGH_CYCLE_COUNT) {
                        totalNumODsThatViolateServiceButStillReachDest++;
                    } else {
                        totalNumODsThatViolateServiceANDDONOTReachDest++;
                    }

                    break;
                }
            }

            LL_record.append(totalNumODsThatViolateServiceButStillReachDest+",");
            LL_record.append(totalNumODsThatDONOTViolateServiceANDStillReachDest+",");
            LL_record.append(totalNumODsThatViolateServiceANDDONOTReachDest+",");

            for(OrigLocDestLoc myOrigLocDestLoc: mapOfODStringsToPrintByCycleByOD.keySet()) {
                SortedMap<Integer, Set<String>> mySortedMap = mapOfODStringsToPrintByCycleByOD.get(myOrigLocDestLoc);
                if(mySortedMap==null) {
                    continue;
                }
                for(Integer myCycle_new : mySortedMap.keySet()) {
                    Set<String> mySetOfString = mySortedMap.get(myCycle_new);
                    if(mySetOfString==null) {
                        continue;
                    }
                    boolean didYouPrintSomething = false;
                    for(String ODString : mySetOfString) {
                        myPrintWriter_LoadLegsToCutAndImplicationsForODs_outAndBack.println(LL_record+ODString);
                        didYouPrintSomething = true;
                        break;
                    }
                    if(didYouPrintSomething) {
                        // only print out the lowest cycle solution...if it exists.
                        break;
                    }
                }
            }

        }

        myPrintWriter_LoadLegsToCutAndImplicationsForODs_outAndBack.close();
        fstream_LoadLegsToCutAndImplicationsForODs_outAndBack.close();

        int temp_a = 0;

    }

    public List<com.con_way.network.model.LoadPath> getLoadPaths(Facility myOrig, Facility myDest) {
        final com.con_way.network.model.LoadPath loadPath_default = api.searchLoadPath.defaultFlow(myOrig, myDest);

        Set<com.con_way.network.model.LoadPath> loadPaths_alt = null;
        if(myOrig.getOperation().equals(Operation.O)) {
            loadPaths_alt = api.searchLoadPath.alternative(myOrig, myDest, 2);
        } else if(myOrig.getOperation().equals(Operation.F)) {
            loadPaths_alt = api.searchLoadPath.alternative(myOrig, myDest, 1);
        } else if(myOrig.getOperation().equals(Operation.D)) {
            loadPaths_alt = api.searchLoadPath.alternative(myOrig, myDest, 0);
        }

        List<com.con_way.network.model.LoadPath> loadPaths_all = new ArrayList<LoadPath>();

        if(loadPath_default != null && !loadPath_default.isEmpty()) {
            loadPaths_all.add(loadPath_default);
        }

        if(loadPaths_alt != null) {
            for (com.con_way.network.model.LoadPath myLoadPath_current : loadPaths_alt) {
                if (myLoadPath_current == null || myLoadPath_current.isEmpty()) {
                    continue;
                }
                loadPaths_all.add(myLoadPath_current);
            }
        }

        return loadPaths_all;

    }

    public void addEntryToMap(OrigLocDestLoc myOrigLocDestLoc, int newCycle, StringBuffer OD_record, Map<OrigLocDestLoc, SortedMap<Integer, Set<String>>> mapOfODStringsToPrintByCycleByOD) {
        SortedMap<Integer, Set<String>> mySortedMap = mapOfODStringsToPrintByCycleByOD.get(myOrigLocDestLoc);
        if(mySortedMap == null) {
            mySortedMap = new TreeMap<Integer, Set<String>>();
            mapOfODStringsToPrintByCycleByOD.put(myOrigLocDestLoc, mySortedMap);
        }
        Set<String> mySetOfExistingStrings = mySortedMap.get(newCycle);
        if(mySetOfExistingStrings==null) {
            mySetOfExistingStrings = new HashSet<String>();
            mySortedMap.put(newCycle, mySetOfExistingStrings);
        }
        mySetOfExistingStrings.add(OD_record.toString());

    }

    public String getSpecialKey(com.con_way.network.model.LoadLeg myLoadLeg) {
        StringBuffer myBuffer = new StringBuffer();
        myBuffer.append(myLoadLeg.getOrigSic());
        myBuffer.append(myLoadLeg.getOrigOp());
        myBuffer.append(myLoadLeg.getMode());
        myBuffer.append(myLoadLeg.getDestSic());
        myBuffer.append(myLoadLeg.getDestOp());
        return myBuffer.toString();
    }

    public String getSpecialKey_flipped(com.con_way.network.model.LoadLeg myLoadLeg) {

        if(!this.isOB_FAC_or_FAC_IB(myLoadLeg)) {
            return null;
        }

        if(this.isOB_FAC(myLoadLeg)) {
            StringBuffer myBuffer = new StringBuffer();

            myBuffer.append(myLoadLeg.getDestSic());
            myBuffer.append(Operation.F);
            myBuffer.append(myLoadLeg.getMode());
            myBuffer.append(myLoadLeg.getOrigSic());
            myBuffer.append(Operation.I);

            return myBuffer.toString();
        } else if(this.isFAC_IB(myLoadLeg)) {
            StringBuffer myBuffer = new StringBuffer();

            myBuffer.append(myLoadLeg.getDestSic());
            myBuffer.append(Operation.O);
            myBuffer.append(myLoadLeg.getMode());
            myBuffer.append(myLoadLeg.getOrigSic());
            myBuffer.append(Operation.F);

            return myBuffer.toString();
        }

        return null;

    }

    public String getOB_FACString(com.con_way.network.model.LoadLeg myLoadLeg) {

        if(this.isStationary(myLoadLeg)) {
            // don't consider these load legs for cancellation....they wouldn't result in real savings.
            return null;
        }

        if(!this.isOB_FAC_or_FAC_IB(myLoadLeg)) {
            return null;
        }

        boolean isThisLHLoadLegOffOfOBAndToFACAndOneShift = this.isOB_FAC(myLoadLeg);

        boolean isThisLHLoadLegOffOfFACAndToIBAndOneShift = this.isFAC_IB(myLoadLeg);

        if(isThisLHLoadLegOffOfOBAndToFACAndOneShift) {
            String myStringToReturn = this.getSpecialKey(myLoadLeg);
            return myStringToReturn;
        } else if(isThisLHLoadLegOffOfFACAndToIBAndOneShift) {
            com.con_way.network.model.LoadLeg myLoadLeg_flippedLeg = this.getFlippedLoadLeg(myLoadLeg);
            if(myLoadLeg_flippedLeg != null) {
                String myStringToReturn = this.getSpecialKey(myLoadLeg_flippedLeg);
                return myStringToReturn;
            }
        }

        return null;

    }

    public boolean isStationary(com.con_way.network.model.LoadLeg myLoadLeg) {
        boolean isThisAStationaryLoadLeg = false;
        if(myLoadLeg.getOrigSic().equals(myLoadLeg.getDestSic())) {
            isThisAStationaryLoadLeg = true;
        }

        if(isThisAStationaryLoadLeg) {
            return true;
        }

        return false;

    }

    public boolean isFAC_IB(com.con_way.network.model.LoadLeg myLoadLeg) {

        if(myLoadLeg==null) {
            return false;
        }

        if(myLoadLeg.getOrig().getOperation().equals(Operation.F)) {
            if(myLoadLeg.getDest().getOperation().equals(Operation.I)) {
                if(myLoadLeg.getCycle()==1) {
                    if(myLoadLeg.getMode().equals(MoveMode.L)) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    public boolean isOB_FAC(com.con_way.network.model.LoadLeg myLoadLeg) {

        if(myLoadLeg==null) {
            return false;
        }

        if(myLoadLeg.getOrig().getOperation().equals(Operation.O)) {
            if(myLoadLeg.getDest().getOperation().equals(Operation.F)) {
                if(myLoadLeg.getCycle()==0) {
                    if(myLoadLeg.getMode().equals(MoveMode.L)) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    public boolean isOB_FAC_or_FAC_IB(com.con_way.network.model.LoadLeg myLoadLeg) {
        boolean isThisLHLoadLegOffOfOBAndToFACAndOneShift = this.isOB_FAC(myLoadLeg);

        boolean isThisLHLoadLegOffOfFACAndToIBAndOneShift = this.isFAC_IB(myLoadLeg);

        if(isThisLHLoadLegOffOfOBAndToFACAndOneShift || isThisLHLoadLegOffOfFACAndToIBAndOneShift) {
            return true;
        }

        return false;

    }

    public com.con_way.network.model.LoadLeg getFlippedLoadLeg(com.con_way.network.model.LoadLeg myLoadLeg) {

        if(!this.isOB_FAC_or_FAC_IB(myLoadLeg)) {
            return null;
        }

        String mySpecialKey_flipped = this.getSpecialKey_flipped(myLoadLeg);

        if(mySpecialKey_flipped != null) {
            com.con_way.network.model.LoadLeg myLoadLeg_flipped = this.network.getLoadLegs().get(mySpecialKey_flipped);
            return myLoadLeg_flipped;
        }

        return null;

    }

    public com.con_way.network.model.LoadLeg getLoadLeg_preceding(int numPrecedingLegs, com.con_way.network.model.LoadLeg myLoadLeg, LoadPath myLoadPath) throws Exception {
        if(myLoadLeg == null) {
            return null;
        }
        if(myLoadPath == null) {
            return null;
        }

        final List<com.con_way.network.model.LoadLeg> loadLegs_inCurrentLoadPath = myLoadPath.getLegs();

        if(loadLegs_inCurrentLoadPath==null || loadLegs_inCurrentLoadPath.size()==0) {
            return null;
        }

        if(!loadLegs_inCurrentLoadPath.contains(myLoadLeg)) {
            return null;
        }

        // find the offset that is the current leg.
        Integer offsetOfmyLoadLeg = null;
        int myCounter=-1;
        for(com.con_way.network.model.LoadLeg myLoadLeg_current : loadLegs_inCurrentLoadPath) {
            myCounter++;

            if(myLoadLeg_current.equals(myLoadLeg)) {
                offsetOfmyLoadLeg = myCounter;
            }

        }

        if(offsetOfmyLoadLeg==null) {
            throw new Exception("Can't find myLoadLeg="+myLoadLeg+" in the list of loadlegs in the path="+loadLegs_inCurrentLoadPath);
        }

        int offsetOfLegWeWillReturn = offsetOfmyLoadLeg-numPrecedingLegs;

        myCounter=-1;
        for(com.con_way.network.model.LoadLeg myLoadLeg_current : loadLegs_inCurrentLoadPath) {
            myCounter++;

            if(myCounter==offsetOfLegWeWillReturn) {
                return myLoadLeg_current;
            }

        }

        return null;
    }

    public Set<OrigLocDestLoc> getAllODsCrossingThisLoadLeg(com.con_way.network.model.LoadLeg myLoadLeg, Map<com.con_way.network.model.LoadLeg, Set<com.con_way.network.model.LoadPath>> myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg, Map<com.con_way.network.model.LoadPath, Set<OrigLocDestLoc>> myMapOfSetOfOrigLocDestLocByLoadPath) {
        Set<com.con_way.network.model.LoadPath> setOfDefaultLoadPaths = myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg.get(myLoadLeg);

        Set<OrigLocDestLoc> mySetOfODsImpactedByThisLoadLeg = new HashSet<OrigLocDestLoc>();

        for(com.con_way.network.model.LoadPath myLoadPath_default : setOfDefaultLoadPaths) {
            Set<OrigLocDestLoc> mySetOfODsForThisLoadPath = myMapOfSetOfOrigLocDestLocByLoadPath.get(myLoadPath_default);

            mySetOfODsImpactedByThisLoadLeg.addAll(mySetOfODsForThisLoadPath);
        }

        return mySetOfODsImpactedByThisLoadLeg;

    }

    public boolean isXPI_O_XNJ_F_orXNJ_F_XPI_I(com.con_way.network.model.MoveLeg myMoveLeg) {
        if(myMoveLeg==null) {
            return false;
        }
        if(!myMoveLeg.getMode().equals(MoveMode.L)) {
            return false;
        }

        if(myMoveLeg.getOrigSic().equals("XPI")) {
            if(myMoveLeg.getOrig().getOperation().equals(Operation.O)) {
                if(myMoveLeg.getDestSic().equals("XNJ")) {
                    if(myMoveLeg.getDest().getOperation().equals(Operation.F)) {
                        return true;
                    }
                }
            }
        }

        if(myMoveLeg.getOrigSic().equals("XNJ")) {
            if(myMoveLeg.getOrig().getOperation().equals(Operation.F)) {
                if(myMoveLeg.getDestSic().equals("XPI")) {
                    if(myMoveLeg.getDest().getOperation().equals(Operation.I)) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    public void doAnalysisOfLoadPaths(Map<String, Set<com.con_way.network.model.MoveLeg>> myMapOfSetOfAllMoveLegsByStringRep, OrigLocDestLoc myOrigLocDestLoc, Set<LoadPath> myLoadPaths_all, Map<com.con_way.network.model.LoadPath, Set<OrigLocDestLoc>> myMapOfSetOfOrigLocDestLocByLoadPath, Map<com.con_way.network.model.MoveLeg, Set<com.con_way.network.model.MovePath>> myMapOfSetOfMovePathsByMoveLeg, Map<com.con_way.network.model.MovePath, Set<com.con_way.network.model.LoadLeg>> myMapOfSetOfLoadLegsByMovePath, Map<com.con_way.network.model.LoadLeg, Set<com.con_way.network.model.LoadPath>> myMapOfSetOfLoadPathsByLoadLeg) {

        for(LoadPath myLoadPath_generic : myLoadPaths_all) {
            if(myLoadPath_generic==null) {
                continue;
            }

            final List<com.con_way.network.model.LoadLeg> loadLegs_inCurrentLoadPath = myLoadPath_generic.getLegs();
            if(loadLegs_inCurrentLoadPath == null || loadLegs_inCurrentLoadPath.size()==0) {
                // if list of load legs null or empty, skip to the next one
                continue;
            }

            Set<OrigLocDestLoc> mySetOfOrigLocDestLoc = myMapOfSetOfOrigLocDestLocByLoadPath.get(myLoadPath_generic);
            if(mySetOfOrigLocDestLoc==null) {
                mySetOfOrigLocDestLoc = new HashSet<OrigLocDestLoc>();
                myMapOfSetOfOrigLocDestLocByLoadPath.put(myLoadPath_generic, mySetOfOrigLocDestLoc);
            }
            mySetOfOrigLocDestLoc.add(myOrigLocDestLoc);

            myOrigLocDestLoc.addLoadPath(myLoadPath_generic);

            for (com.con_way.network.model.LoadLeg ll: loadLegs_inCurrentLoadPath) {

                Set<com.con_way.network.model.LoadPath> mySetOfLoadPaths = myMapOfSetOfLoadPathsByLoadLeg.get(ll);
                if(mySetOfLoadPaths== null) {
                    mySetOfLoadPaths = new HashSet<com.con_way.network.model.LoadPath>();
                    myMapOfSetOfLoadPathsByLoadLeg.put(ll, mySetOfLoadPaths);
                }
                mySetOfLoadPaths.add(myLoadPath_generic);

                final com.con_way.network.model.MovePath movePath_default = api.searchMovePath.defaultFlow(ll);
                if(movePath_default == null || movePath_default.isEmpty()) {
                    continue;
                }

                Set<MovePath> movePaths_alternative = api.searchMovePath.alternative(ll, Operation.O, 1.6);

                if(movePaths_alternative==null) {
                    continue;
                }

                Set<MovePath> myMovePaths_all = new HashSet<MovePath>();
                myMovePaths_all.add(movePath_default);
                myMovePaths_all.addAll(movePaths_alternative);

                for(MovePath myMovePath : myMovePaths_all) {
                    final List<com.con_way.network.model.MoveLeg> moveLegs = myMovePath.getLegs();
                    if(moveLegs == null || moveLegs.size()==0) {
                        continue;
                    }

                    Set<com.con_way.network.model.LoadLeg> mySetOfLoadLeg = myMapOfSetOfLoadLegsByMovePath.get(myMovePath);
                    if(mySetOfLoadLeg==null) {
                        mySetOfLoadLeg = new HashSet<com.con_way.network.model.LoadLeg>();
                        myMapOfSetOfLoadLegsByMovePath.put(myMovePath, mySetOfLoadLeg);
                    }
                    mySetOfLoadLeg.add(ll);

                    for(com.con_way.network.model.MoveLeg ml : moveLegs) {
                        if(ml==null) {
                            continue;
                        }

                        String originLocation = ml.getOrigSic();
                        Operation myOriginOp = ml.getOrig().getOperation();
                        String destinationLocation = ml.getDestSic();
                        Operation myDestinationOp = ml.getDest().getOperation();
                        int cycle = ml.getCycle();
                        MoveMode mode = ml.getMode();

                        StringBuffer buffer = new StringBuffer();
                        if(mode.equals(MoveMode.L)) {

                            if(myOriginOp.equals(Operation.O)) {
                                if(myDestinationOp.equals(Operation.F)) {
                                    if(cycle==0) {
                                        // this is an OB->FAC LH run
                                        buffer.append(originLocation+"_");
                                        buffer.append(myOriginOp+"_");
                                        buffer.append(destinationLocation+"_");
                                        buffer.append(myDestinationOp+"_");
                                    }
                                }
                            } else if(myOriginOp.equals(Operation.F)) {
                                if(myDestinationOp.equals(Operation.I)) {
                                    if(cycle==1) {
                                        // this is an OB->FAC LH run
                                        buffer.append(destinationLocation+"_");
                                        buffer.append(Operation.O+"_");
                                        buffer.append(originLocation+"_");
                                        buffer.append(Operation.F+"_");
                                    }
                                }
                            } else {
                                buffer=null;
                            }
                        } else {
                            buffer=null;
                        }

                        if(buffer != null && buffer.length()>0) {
                            Set<com.con_way.network.model.MoveLeg> mySetOfMoveLegs = myMapOfSetOfAllMoveLegsByStringRep.get(buffer.toString());
                            if(mySetOfMoveLegs==null) {
                                mySetOfMoveLegs = new HashSet<com.con_way.network.model.MoveLeg>();
                                myMapOfSetOfAllMoveLegsByStringRep.put(buffer.toString(), mySetOfMoveLegs);
                            }
                            mySetOfMoveLegs.add(ml);
                        }

                        Set<com.con_way.network.model.MovePath> mySetOfMovePath = myMapOfSetOfMovePathsByMoveLeg.get(ml);
                        if(mySetOfMovePath==null) {
                            mySetOfMovePath = new HashSet<com.con_way.network.model.MovePath>();
                            myMapOfSetOfMovePathsByMoveLeg.put(ml, mySetOfMovePath);
                        }
                        mySetOfMovePath.add(myMovePath);
                    }

                }


            }
        }

    }

    public void doAnalysisOfALoadPath(OrigLocDestLoc myOrigLocDestLoc, boolean isDefaultLoadPath, LoadPath myLoadPath_input, Map<com.con_way.network.model.LoadPath, Set<OrigLocDestLoc>> myMapOfSetOfOrigLocDestLocByLoadPath, Map<com.con_way.network.model.LoadLeg, Set<com.con_way.network.model.LoadPath>> myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg,  Map<com.con_way.network.model.Facility, Set<com.con_way.network.model.LoadLeg>> mapOfSetsOfDepartingLoadLegsByOriginFacility) {

        if(myLoadPath_input==null) {
            return;
        }

        final List<com.con_way.network.model.LoadLeg> loadLegs_inCurrentLoadPath = myLoadPath_input.getLegs();
        if(loadLegs_inCurrentLoadPath == null || loadLegs_inCurrentLoadPath.size()==0) {
            // if list of load legs null or empty, skip to the next one
            return;
        }

        Set<OrigLocDestLoc> mySetOfOrigLocDestLoc = myMapOfSetOfOrigLocDestLocByLoadPath.get(myLoadPath_input);
        if(mySetOfOrigLocDestLoc==null) {
            mySetOfOrigLocDestLoc = new HashSet<OrigLocDestLoc>();
            myMapOfSetOfOrigLocDestLocByLoadPath.put(myLoadPath_input, mySetOfOrigLocDestLoc);
        }
        mySetOfOrigLocDestLoc.add(myOrigLocDestLoc);

        myOrigLocDestLoc.addLoadPath(myLoadPath_input);

        for (com.con_way.network.model.LoadLeg ll: loadLegs_inCurrentLoadPath) {

            com.con_way.network.model.Facility myFacility_origin = ll.getOrig();
            Set<com.con_way.network.model.LoadLeg> mySetOfDepartingLoadLegs = mapOfSetsOfDepartingLoadLegsByOriginFacility.get(myFacility_origin);
            if(mySetOfDepartingLoadLegs==null) {
                mySetOfDepartingLoadLegs = new HashSet<com.con_way.network.model.LoadLeg>();
                mapOfSetsOfDepartingLoadLegsByOriginFacility.put(myFacility_origin, mySetOfDepartingLoadLegs);
            }
            mySetOfDepartingLoadLegs.add(ll);

            Set<com.con_way.network.model.LoadPath> mySetOfDefaultLoadPaths = myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg.get(ll);
            if(mySetOfDefaultLoadPaths== null) {
                mySetOfDefaultLoadPaths = new HashSet<com.con_way.network.model.LoadPath>();
                myMapOfSetOfLoadPathsThatAreDefaultAndCrossThisLoadLegByLoadLeg.put(ll, mySetOfDefaultLoadPaths);
            }
            if(isDefaultLoadPath) {
                mySetOfDefaultLoadPaths.add(myLoadPath_input);
            }

        }

    }

    public void constructALLODCombinations_AndCalcService(String mySubdirectoryForAllMovePathsOfAllODs) throws Exception {
        String fileName = mySubdirectoryForAllMovePathsOfAllODs+"AllODsAndTheirServiceTime.csv";

        FileWriter fstream = new FileWriter(fileName, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        StringBuffer header = new StringBuffer();
        header.append("Origin"+",");
        header.append("Destination"+",");
        header.append("Service"+",");
        header.append("DoesDefaultLoadPathIncludeExcHSS"+",");
        header.append("Orig_sic"+",");
        header.append("Orig_Shift"+",");
        header.append("Dest_sic"+",");
        header.append("Dest_Shift"+",");
        header.append("MoveMode"+",");
        header.append("Cycle"+",");

        myPrintWriter.println(header);

        Locations allLocations = this.network.getLocations();

        Set<String> myLocations = allLocations.keys();

        if(myLocations != null) {
            System.out.println("Total # of locations we are considering = " + myLocations.size());
        }

        Set<String> myLocations_outer = new HashSet<String>();
        myLocations_outer.addAll(myLocations);
        Set<String> myLocations_inner = new HashSet<String>();
        myLocations_inner.addAll(myLocations);

        for(String originLocation_String : myLocations_outer) {
            for(String destinationLocation_String : myLocations_inner) {
                System.out.println(originLocation_String+","+destinationLocation_String);
                if(originLocation_String.equals(destinationLocation_String)) {
                    continue;
                }

                com.con_way.network.model.Facility orig = network.getFacility( network.getLocation(originLocation_String), Operation.O);
                com.con_way.network.model.Facility dest = network.getFacility( network.getLocation(destinationLocation_String), Operation.I);
                final com.con_way.network.model.LoadPath loadPath = api.searchLoadPath.defaultFlow(orig, dest);
                if(loadPath == null || loadPath.isEmpty()) {
                    // if load path null or empty, skip to the next one....
                    continue;
                }

                final List<com.con_way.network.model.LoadLeg> loadLegs = loadPath.getLegs();
                if(loadLegs == null || loadLegs.size()==0) {
                    // if list of load legs null or empty, skip to the next one
                    continue;
                }

                int myCycle = loadPath.getCycle();

                boolean includeExcHSS = false;
                com.con_way.network.model.LoadLeg ll_excHss = null;
                for (com.con_way.network.model.LoadLeg ll: loadLegs) {
                    if(ll==null) {
                        continue;
                    }
                    if(ll.isExclusiveLeg()) {
                        ll_excHss = ll;
                        includeExcHSS = true;
                        break;
                    }
                }

                StringBuffer buffer = new StringBuffer();
                buffer.append(originLocation_String+",");
                buffer.append(destinationLocation_String+",");
                buffer.append(myCycle+",");
                buffer.append(includeExcHSS+",");
                if(ll_excHss != null) {
                    buffer.append(ll_excHss.getOrigSic()+",");
                    buffer.append(ll_excHss.getOrigOp()+",");
                    buffer.append(ll_excHss.getDestSic()+",");
                    buffer.append(ll_excHss.getDestOp()+",");
                    buffer.append(ll_excHss.getMode()+",");
                    buffer.append(ll_excHss.getCycle()+",");
                } else {
                    buffer.append(""+",");
                    buffer.append(""+",");
                    buffer.append(""+",");
                    buffer.append(""+",");
                    buffer.append(""+",");
                    buffer.append(""+",");
                }

                myPrintWriter.println(buffer);

            }
        }

        myPrintWriter.close();
        fstream.close();


    }

    public void constructALLODCombinations(String myNetworkAPI_inputFileSubDirectory, String mySubdirectoryForAllMovePathsOfAllODs) throws Exception {

        this.readSics(myNetworkAPI_inputFileSubDirectory, LHHssReportBuilder.FILE_SIC);

        String fileName = mySubdirectoryForAllMovePathsOfAllODs+"AllMovePathsOfAllODs.csv";

        FileWriter fstream = new FileWriter(fileName, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        StringBuffer header = new StringBuffer();
        header.append("Origin"+",");
        header.append("Destination"+",");
        header.append("Current Day"+",");
        header.append("Leg Origin"+",");
        header.append("Leg Origin Shift"+",");
        header.append("Leg Destination"+",");
        header.append("Leg Destination Shift"+",");
        header.append("Cycle"+",");
        header.append("Mode"+",");

        myPrintWriter.println(header);

        Set<String> myLocations = this.myMapOfHostSicBySic.keySet();

        if(myLocations != null) {
            System.out.println("Total # of locations we are considering = " + myLocations.size());
        }

        Set<String> myLocations_outer = new HashSet<String>();
        myLocations_outer.addAll(myLocations);
        Set<String> myLocations_inner = new HashSet<String>();
        myLocations_inner.addAll(myLocations);

        Set<String> setOfOriginsMissingKeyDataFromFFOForAMoveLegFoundInNetworkAPI = new HashSet<String>();
        int odComboFailures_missingKeyDataFromFFOForAMoveLegFoundInNetworkAPI = 0;
        int odComboFailures_missingLoadPath_missingLoadLeg_missingAnyMoveLegs = 0;
        int odComboSuccesses = 0;
        int myTries = 0;
        for(String originLocation_String : myLocations_outer) {
            for(String destinationLocation_String : myLocations_inner) {
                System.out.println(originLocation_String+","+destinationLocation_String);
                myTries++;
                if(originLocation_String.equals(destinationLocation_String)) {
                    continue;
                }

                com.con_way.network.model.Facility orig = null;
                String hostSic_origin = this.myMapOfHostSicBySic.get(originLocation_String);
                if(hostSic_origin == null || hostSic_origin.equals("null")) {
                    orig = network.getFacility( network.getLocation(originLocation_String), Operation.O);
                } else {
                    orig = network.getFacility( network.getLocation(hostSic_origin), Operation.O);
                }

                com.con_way.network.model.Facility dest = null;
                String hostSic_destination = this.myMapOfHostSicBySic.get(destinationLocation_String);
                if(hostSic_destination == null || hostSic_destination.equals("null")) {
                    dest = network.getFacility( network.getLocation(destinationLocation_String), Operation.I);
                } else {
                    dest = network.getFacility( network.getLocation(hostSic_destination), Operation.I);
                }

                if(orig==null || dest==null) {
                    continue;
                }

                final com.con_way.network.model.LoadPath loadPath = api.searchLoadPath.defaultFlow(orig, dest);
                if(loadPath == null || loadPath.isEmpty()) {
                    // if load path null or empty, skip to the next one....
                    odComboFailures_missingLoadPath_missingLoadLeg_missingAnyMoveLegs++;
                    continue;
                }

                final List<com.con_way.network.model.LoadLeg> loadLegs = loadPath.getLegs();
                if(loadLegs == null || loadLegs.size()==0) {
                    // if list of load legs null or empty, skip to the next one
                    odComboFailures_missingLoadPath_missingLoadLeg_missingAnyMoveLegs++;
                    continue;
                }

                int myCycle = loadPath.getCycle();

                if(myCycle<3) {
                    // only want the load paths with cycles >= 3
                    continue;
                }

                // does the load path include Exc HSS
                boolean doesLoadPathIncludeExcHss = false;
                for (com.con_way.network.model.LoadLeg ll: loadLegs) {
                    if(ll.isExclusiveLeg()) {
                        doesLoadPathIncludeExcHss = true;
                        break;
                    }
                }

                if(!doesLoadPathIncludeExcHss) {
                    continue;
                }

                List<com.con_way.network.model.MoveLeg> listOfMoveLegs = new ArrayList<MoveLeg>();
                boolean missingAnyMoveLegs = false;
                for (com.con_way.network.model.LoadLeg ll: loadLegs) {
                    final com.con_way.network.model.MovePath movePath = api.searchMovePath.defaultFlow(ll);
                    if(movePath == null || movePath.isEmpty()) {
                        missingAnyMoveLegs = true;
                        break;
                    }

                    final List<com.con_way.network.model.MoveLeg> moveLegs = movePath.getLegs();
                    if(moveLegs == null || moveLegs.size()==0) {
                        missingAnyMoveLegs = true;
                        break;
                    }
                    for(com.con_way.network.model.MoveLeg ml : moveLegs) {
                        if(ml==null) {
                            missingAnyMoveLegs = true;
                            break;
                        }
                        listOfMoveLegs.add(ml);
                    }

                    if(missingAnyMoveLegs) {
                        break;
                    }
                }

                if(missingAnyMoveLegs) {
                    // if missing any move legs, skip to the next
                    odComboFailures_missingLoadPath_missingLoadLeg_missingAnyMoveLegs++;
                    continue;
                }

                boolean areWeMissingAnInstanceOf_myOriginOriginShiftDestinationDestinationShiftDate_singleShift = false;
                int currentDay = 0;
                for(MoveLeg myMoveLeg : listOfMoveLegs) {

                    String myOrigin_leg = myMoveLeg.getOrigSic();
                    String myOriginShift_leg = myMoveLeg.getOrigOp();
                    String myDestination_leg = myMoveLeg.getDestSic();
                    String myDestinationShift_leg = myMoveLeg.getDestOp();
                    Integer myCycle_leg = myMoveLeg.getCycle();
                    MoveMode myMoveMode_leg = myMoveLeg.getMode();


                    StringBuffer buffer = new StringBuffer();
                    buffer.append(originLocation_String+",");
                    buffer.append(destinationLocation_String+",");
                    buffer.append(currentDay+",");
                    buffer.append(myOrigin_leg+",");
                    buffer.append(myOriginShift_leg+",");
                    buffer.append(myDestination_leg+",");
                    buffer.append(myDestinationShift_leg+",");
                    buffer.append(myCycle_leg+",");
                    buffer.append(myMoveMode_leg+",");

                    myPrintWriter.println(buffer);

                    currentDay += myMoveLeg.getCycle();
                }

                if(areWeMissingAnInstanceOf_myOriginOriginShiftDestinationDestinationShiftDate_singleShift) {
                    // if missing any move legs, skip to the next
                    odComboFailures_missingLoadPath_missingLoadLeg_missingAnyMoveLegs++;
                    odComboFailures_missingKeyDataFromFFOForAMoveLegFoundInNetworkAPI++;
                    continue;
                }

                odComboSuccesses++;

            }
        }

        myPrintWriter.close();
        fstream.close();

        System.out.println("myTries="+myTries);
        System.out.println("odComboFailures_missingLoadPath_missingLoadLeg_missingAnyMoveLegs=" + odComboFailures_missingLoadPath_missingLoadLeg_missingAnyMoveLegs);
        System.out.println("odComboSuccesses="+odComboSuccesses);

        System.out.println("odComboFailures_missingKeyDataFromFFOForAMoveLegFoundInNetworkAPI="+odComboFailures_missingKeyDataFromFFOForAMoveLegFoundInNetworkAPI);
        // print out setOfOriginsMissingKeyDataFromFFOForAMoveLegFoundInNetworkAPI as well.
        int temp_a = 0;

    }

    public void verify_All_RuleSets_have_tariff_and_AMC_clusterIDs() throws Exception {
        String base_Directory = "C:\\Old Drive\\rosa.charles\\workspace_git\\AggregationOptimization\\resource\\dynamicData\\output\\";

        String fileNameWeWillRead = "ratl_agg_output_ruleset.csv";

        File currentDir = new File(base_Directory); // base directory

        boolean wereThereAnyMissingClusterIDs = false;

        try {
            File[] files = currentDir.listFiles();
            for (File file : files) {
                System.out.println(file.toString());
                if(file.toString().contains("testCase_700002")) {
                    continue;
                }
                if (file.isDirectory()) {
                    //System.out.println("directory:" + file.getCanonicalPath());

                    boolean isMissingClusterID = this.readFileAndLookForBlanks(file, fileNameWeWillRead);
                    if(isMissingClusterID) {
                        wereThereAnyMissingClusterIDs = true;
                        System.out.println(file.getAbsolutePath());
                    } else {
                        int temp_a = 0;
                    }
                } else {
                    System.out.println("     file:" + file.getCanonicalPath()+" Nothing to do here");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(wereThereAnyMissingClusterIDs) {
            System.out.println("There were some missing cluster IDs...see the relative directory paths above to see where");
        } else {
            System.out.println("There were NO missing cluster IDs in any of our test cases");
        }

        int temp_a = 0;

    }

    public boolean readFileAndLookForBlanks(File directory, String fileNameWeWillRead) throws Exception {

        //System.out.println("readFileAndLookForBlanks");

        BufferedReader br = null;
        String dbRecord;

        String fileName =  directory.getAbsolutePath()+"\\"+fileNameWeWillRead;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String TARIFF_CLUSTER_ID = val[16].trim();
                String AMC_CLUSTER_ID = val[17].trim();

                boolean isMissingClusterID = false;

                if(TARIFF_CLUSTER_ID.length()==0) {
                    isMissingClusterID = true;
                }
                if(AMC_CLUSTER_ID.length()==0) {
                    isMissingClusterID = true;
                }

                if(isMissingClusterID) {
                    return isMissingClusterID;
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return false;

    }

    public void analyzeTBL_FLO_LOAD_OPTION_RDS() throws Exception {

        String TBL_FLO_LOAD_OPTION_RDS_Directory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\TBL_FLO_LOAD_OPTION_RDS\\";
        String myTBL_FLO_LOAD_OPTION_RDS_File_input = "TBL_FLO_LOAD_OPTION_RDS.csv";

        String myTBL_FLO_LOAD_OPTION_RDS_File_output = "TBL_FLO_LOAD_OPTION_RDS_output.csv";

        FileOutputStream myFileOutputStream = new FileOutputStream(new File(TBL_FLO_LOAD_OPTION_RDS_Directory + myTBL_FLO_LOAD_OPTION_RDS_File_output), false);
        PrintWriter myPrintWriter = new PrintWriter(myFileOutputStream);

        StringBuffer myHeaderBuffer = new StringBuffer();

        myHeaderBuffer.append("LD_AT_NODE_NM"+",");
        myHeaderBuffer.append("FNL_DEST_NODE_NM"+",");

        myPrintWriter.println(myHeaderBuffer);

        SortedMap<String, SortedSet<String>> mapOfLoadTosByLoadAt = new TreeMap<String, SortedSet<String>>();
        this.readTBL_FLO_LOAD_OPTION_RDSFile_first(TBL_FLO_LOAD_OPTION_RDS_Directory, mapOfLoadTosByLoadAt);

        SortedMap<String, SortedSet<String>> mapOfFinalDestsByLoadAt = new TreeMap<String, SortedSet<String>>();
        this.readTBL_FLO_LOAD_OPTION_RDSFile_second(TBL_FLO_LOAD_OPTION_RDS_Directory, mapOfLoadTosByLoadAt, mapOfFinalDestsByLoadAt);

        for(String LD_AT_NODE_NM : mapOfFinalDestsByLoadAt.keySet()) {
            Set<String> setOfFinalDests = mapOfFinalDestsByLoadAt.get(LD_AT_NODE_NM);
            if(setOfFinalDests == null || setOfFinalDests.size() == 0) {
                continue;
            }

            for(String FNL_DEST_NODE_NM : setOfFinalDests) {
                StringBuffer myBuffer = new StringBuffer();
                myBuffer.append(LD_AT_NODE_NM + ",");
                myBuffer.append(FNL_DEST_NODE_NM + ",");
                myPrintWriter.println(myBuffer);
            }
        }

        myPrintWriter.close();
        myFileOutputStream.close();

    }

    public void calculateTotalTractorAndTotalCubeTotalWeightByDate_usingJamieFile() throws Exception {

        //String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        String totalTractor_totalCube_totalWeight_Directory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\TotalTractor_TotalCube_TotalWeight_byDate\\";
        String myTotalTractor_totalCube_totalWeight_File = "TotalTractor_totalCube_totalWeight.csv";

        FileOutputStream myFileOutputStream = new FileOutputStream(new File(totalTractor_totalCube_totalWeight_Directory + myTotalTractor_totalCube_totalWeight_File), false);
        PrintWriter myPrintWriter = new PrintWriter(myFileOutputStream);

        StringBuffer myHeaderBuffer = new StringBuffer();

        myHeaderBuffer.append("myDateForThisRunID"+",");
        myHeaderBuffer.append("cubicFeetPerHundredCube"+",");
        myHeaderBuffer.append("BothCSTAndPSTPresent?"+",");
        myHeaderBuffer.append("runId_CST"+",");
        myHeaderBuffer.append("totalTractorCount_CST"+",");
        myHeaderBuffer.append("totalCube_CST"+",");
        myHeaderBuffer.append("totalWeight_CST"+",");
        myHeaderBuffer.append("totalLadenTrailer_CST"+",");
        myHeaderBuffer.append("totalEmptyTrailer_CST"+",");
        myHeaderBuffer.append("totalTrailer_CST"+",");
        myHeaderBuffer.append("runId_PST"+",");
        myHeaderBuffer.append("totalTractorCount_PST"+",");
        myHeaderBuffer.append("totalCube_PST"+",");
        myHeaderBuffer.append("totalWeight_PST"+",");
        myHeaderBuffer.append("totalLadenTrailer_PST"+",");
        myHeaderBuffer.append("totalEmptyTrailer_PST"+",");
        myHeaderBuffer.append("totalTrailer_PST"+",");
        myHeaderBuffer.append("totalTractorCount_Total"+",");
        myHeaderBuffer.append("totalCube_Total"+",");
        myHeaderBuffer.append("totalWeight_Total"+",");
        myHeaderBuffer.append("totalLadenTrailer_Total"+",");
        myHeaderBuffer.append("totalEmptyTrailer_Total"+",");
        myHeaderBuffer.append("totalTrailer_Total"+",");
        myHeaderBuffer.append("cubicFeet_Total"+",");
        myHeaderBuffer.append("totalCubePerTractor_Total"+",");
        myHeaderBuffer.append("totalWeightPerTractor_Total"+",");
        myHeaderBuffer.append("totalCubicFeetPerTractor_Total"+",");
        myHeaderBuffer.append("percentageEmptyTrailers_Total"+",");

        SortedMap<Date, SortedMap<String, TractorCount_TotalCube_TotalWeight>> myMapOfTractorCountResultsByModelGroupByDate = new TreeMap<Date, SortedMap<String, TractorCount_TotalCube_TotalWeight>>();

        myPrintWriter.println(myHeaderBuffer);

        //3836
        for (int id = 3646; id <= 3840; id=id+1){
            String runId = id+"";

            System.out.println(runId);

            String dir_in = baseDirectory_remote+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            String myModelGroupID = LHHssReportBuilder.PST;
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, LHHssReportBuilder.CST)) {
                myModelGroupID = LHHssReportBuilder.CST;
            }
            
            System.out.println("\t Reading remote");
            Map<String, Double> myMapOfMetricByCode = this.readJamieFile_returnMapWithSumOfTractorCount_TotalCube_TotalWeight(runId, baseDirectory_remote);

            if(myMapOfMetricByCode != null) {

                Double totalTractorCount = myMapOfMetricByCode.get(LHHssReportBuilder.totalTractorCount);
                Double totalCube_OV = myMapOfMetricByCode.get(LHHssReportBuilder.totalCube_OV);
                Double totalCube_VD = myMapOfMetricByCode.get(LHHssReportBuilder.totalCube_VD);
                Double totalCube_OD = myMapOfMetricByCode.get(LHHssReportBuilder.totalCube_OD);
                Double totalWeight_OV = myMapOfMetricByCode.get(LHHssReportBuilder.totalWeight_OV);
                Double totalWeight_VD = myMapOfMetricByCode.get(LHHssReportBuilder.totalWeight_VD);
                Double totalWeight_OD = myMapOfMetricByCode.get(LHHssReportBuilder.totalWeight_OD);
                Double ladenTrailer_OV = myMapOfMetricByCode.get(LHHssReportBuilder.ladenTrailer_OV);
                Double ladenTrailer_VD = myMapOfMetricByCode.get(LHHssReportBuilder.ladenTrailer_VD);
                Double ladenTrailer_OD = myMapOfMetricByCode.get(LHHssReportBuilder.ladenTrailer_OD);
                Double emptyTrailer_OV = myMapOfMetricByCode.get(LHHssReportBuilder.emptyTrailer_OV);
                Double emptyTrailer_VD = myMapOfMetricByCode.get(LHHssReportBuilder.emptyTrailer_VD);
                Double emptyTrailer_OD = myMapOfMetricByCode.get(LHHssReportBuilder.emptyTrailer_OD);

                double totalCube = totalCube_OV+totalCube_VD+totalCube_OD;
                double totalWeight = totalWeight_OV+totalWeight_VD+totalWeight_OD;
                double totalLadenTrailers = ladenTrailer_OV+ladenTrailer_VD+ladenTrailer_OD;
                double totalEmptyTrailers = emptyTrailer_OV+emptyTrailer_VD+emptyTrailer_OD;

                Date myDate_match = null;
                for(Date myDate : myMapOfTractorCountResultsByModelGroupByDate.keySet()) {
                    if(myDate.equals(myDateForThisRunID)) {
                        myDate_match = myDate;
                    }
                }

                SortedMap<String, TractorCount_TotalCube_TotalWeight> myMap_inner = null;
                if(myDate_match == null) {
                    myMap_inner = new TreeMap<String, TractorCount_TotalCube_TotalWeight>();
                    myMapOfTractorCountResultsByModelGroupByDate.put(myDateForThisRunID, myMap_inner);
                } else {
                    myMap_inner = myMapOfTractorCountResultsByModelGroupByDate.get(myDate_match);
                }

                TractorCount_TotalCube_TotalWeight myTractorCount_TotalCube_TotalWeight = new TractorCount_TotalCube_TotalWeight(
                        totalTractorCount,
                        totalCube,
                        totalWeight,
                        id,
                        totalLadenTrailers,
                        totalEmptyTrailers
                );

                myMap_inner.put(myModelGroupID, myTractorCount_TotalCube_TotalWeight);

            }

            System.out.println("\t Finished reading remote");

        }

        for(Date myDate : myMapOfTractorCountResultsByModelGroupByDate.keySet()) {
            SortedMap<String, TractorCount_TotalCube_TotalWeight> myMap_inner = myMapOfTractorCountResultsByModelGroupByDate.get(myDate);
            if(myMap_inner==null) {
                continue;
            }

            LBDate_UBDate myLBDate_UBDate_toUse = null;
            for(LBDate_UBDate myLBDate_UBDate : LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.keySet()) {
                if(myLBDate_UBDate.inInterval(myDate)) {
                    myLBDate_UBDate_toUse = myLBDate_UBDate;
                    break;
                }
            }

            if(myLBDate_UBDate_toUse == null) {
                continue;
            }

            Double cubicFeetPerHundredCube = LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.get(myLBDate_UBDate_toUse);

            TractorCount_TotalCube_TotalWeight myTractorCount_TotalCube_TotalWeight_CST = myMap_inner.get(LHHssReportBuilder.CST);
            TractorCount_TotalCube_TotalWeight myTractorCount_TotalCube_TotalWeight_PST = myMap_inner.get(LHHssReportBuilder.PST);

            boolean isCSTPresent = false;
            if(myTractorCount_TotalCube_TotalWeight_CST != null) {
                isCSTPresent = true;
            }

            boolean isPSTPresent = false;
            if(myTractorCount_TotalCube_TotalWeight_PST != null) {
                isPSTPresent = true;
            }

            Double totalTractorCount = null;
            Double totalCube = null;
            Double totalWeight = null;
            Double totalLadenTrailer = null;
            Double totalEmptyTrailer = null;
            if(isCSTPresent && isPSTPresent) {
                totalTractorCount = myTractorCount_TotalCube_TotalWeight_CST.getTotalTractorCount();
                totalTractorCount += myTractorCount_TotalCube_TotalWeight_PST.getTotalTractorCount();

                totalCube = myTractorCount_TotalCube_TotalWeight_CST.getTotalCube();
                totalCube += myTractorCount_TotalCube_TotalWeight_PST.getTotalCube();

                totalWeight = myTractorCount_TotalCube_TotalWeight_CST.getTotalWeight();
                totalWeight += myTractorCount_TotalCube_TotalWeight_PST.getTotalWeight();

                totalLadenTrailer = myTractorCount_TotalCube_TotalWeight_CST.getTotalLadenTrailers();
                totalLadenTrailer += myTractorCount_TotalCube_TotalWeight_PST.getTotalLadenTrailers();

                totalEmptyTrailer = myTractorCount_TotalCube_TotalWeight_CST.getTotalEmptyTrailers();
                totalEmptyTrailer += myTractorCount_TotalCube_TotalWeight_PST.getTotalEmptyTrailers();

            }

            StringBuffer myBuffer = new StringBuffer();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            myBuffer.append(dateFormat.format(myDate)+",");
            myBuffer.append(cubicFeetPerHundredCube+",");
            if(isCSTPresent && isPSTPresent) {
                myBuffer.append(true+",");
            } else {
                myBuffer.append(false+",");
            }
            if(isCSTPresent) {
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getRunID() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalTractorCount() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalCube() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalWeight() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalLadenTrailers() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalEmptyTrailers() + ",");
                double totalTrailers = myTractorCount_TotalCube_TotalWeight_CST.getTotalLadenTrailers()+myTractorCount_TotalCube_TotalWeight_CST.getTotalEmptyTrailers();
                myBuffer.append(totalTrailers + ",");
            } else {
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null+ ",");
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
            }

            if(isPSTPresent) {
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getRunID() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalTractorCount() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalCube() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalWeight() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalLadenTrailers() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalEmptyTrailers() + ",");
                double totalTrailers = myTractorCount_TotalCube_TotalWeight_PST.getTotalLadenTrailers()+myTractorCount_TotalCube_TotalWeight_PST.getTotalEmptyTrailers();
                myBuffer.append(totalTrailers + ",");
            } else {
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null+ ",");
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
            }

            Double cubicFeet_Total = totalCube*cubicFeetPerHundredCube/100.0;

            myBuffer.append(totalTractorCount + ",");
            myBuffer.append(totalCube + ",");
            myBuffer.append(totalWeight + ",");
            myBuffer.append(totalLadenTrailer + ",");
            myBuffer.append(totalEmptyTrailer + ",");
            myBuffer.append(totalLadenTrailer+totalEmptyTrailer + ",");
            myBuffer.append(cubicFeet_Total + ",");

            Double cubePerTractor = null;
            Double weightPerTractor = null;
            if(totalTractorCount != null && totalCube != null && totalWeight != null) {
                cubePerTractor = totalCube/(totalTractorCount+epsilon);
                weightPerTractor = totalWeight/(totalTractorCount+epsilon);
            }

            Double cubicFeetPerTractor = null;
            if(totalTractorCount != null && cubicFeet_Total != null) {
                cubicFeetPerTractor = cubicFeet_Total/(totalTractorCount+epsilon);
            }

            myBuffer.append(cubePerTractor + ",");
            myBuffer.append(weightPerTractor + ",");
            myBuffer.append(cubicFeetPerTractor + ",");
            myBuffer.append(totalEmptyTrailer/(totalLadenTrailer+totalEmptyTrailer+epsilon) + ",");

            myPrintWriter.println(myBuffer);

        }


        myPrintWriter.close();
        myFileOutputStream.close();

    }

    public SortedMap<Integer, Map<String, java.sql.Timestamp>> getMapOfOBModelRunDatesByModelRunID(String dateString) throws Exception {
        // sicName = XLA
        // timeStamp_LB =  '9/14/2016'
        // timeStamp_UB =  '9/15/2016'

        Connection theConn = null;

        SortedMap<Integer, Map<String, java.sql.Timestamp>> myMap = new TreeMap<Integer, Map<String, Timestamp>>();

        try {
            // connection to Netezza
            theConn = DatabaseAccess.getConnection_PRD_WHSEVIEW();

            String sql_updated = "Select run_nbr, run_dt, mod_area_grp_cd from ADMIN.LHO_MODEL_RUN_VW where mod_run_snpsht_nm='OPTIMIZER_OUTPUT' and run_shft_cd='OTB' and run_dt>'"+dateString+"' order by run_dt,run_nbr";

            System.out.println("This is the query we issued:");
            System.out.println(sql_updated);

            Statement stmt = theConn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_updated);
            //rs.setFetchSize(40000);

            Integer schedule_nbr = null;

            int rowCounter=0;
            while (rs.next()) {

                Integer myRUN_NBR = rs.getInt("RUN_NBR");

                if(myRUN_NBR==null || myRUN_NBR<0) {
                    continue;
                }

                java.sql.Timestamp my_run_date = rs.getTimestamp("RUN_DT");

                if(my_run_date == null) {
                    continue;
                }

                String myMOD_AREA_GRP_CD = rs.getString("MOD_AREA_GRP_CD");

                Map<String, java.sql.Timestamp> myMap_inner = myMap.get(myRUN_NBR);
                if(myMap_inner==null) {
                    myMap_inner = new HashMap<String, java.sql.Timestamp>();
                    myMap.put(myRUN_NBR, myMap_inner);
                }

                myMap_inner.put(myMOD_AREA_GRP_CD, my_run_date);

            }

            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (theConn != null) theConn.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return myMap;

    }

    public void calculateTotalTractorAndTotalCubeTotalWeightByDate_usingBIReadyFiles(String dateString) throws Exception {

        //String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        String totalTractor_totalCube_totalWeight_Directory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\TotalTractor_TotalCube_TotalWeight_byDate\\";
        String myTotalTractor_totalCube_totalWeight_fromBIDataSource_File = "TotalTractor_totalCube_totalWeight_BIFiles.csv";

        FileOutputStream myFileOutputStream = new FileOutputStream(new File(totalTractor_totalCube_totalWeight_Directory + myTotalTractor_totalCube_totalWeight_fromBIDataSource_File), false);
        PrintWriter myPrintWriter = new PrintWriter(myFileOutputStream);

        StringBuffer myHeaderBuffer = new StringBuffer();

        myHeaderBuffer.append("myDateForThisRunID"+",");
        myHeaderBuffer.append("cubicFeetPerHundredCube"+",");
        myHeaderBuffer.append("BothCSTAndPSTPresent?"+",");
        myHeaderBuffer.append("runId_CST"+",");
        myHeaderBuffer.append("totalTractorCount_CST"+",");
        myHeaderBuffer.append("totalCube_CST"+",");
        myHeaderBuffer.append("totalWeight_CST"+",");
        myHeaderBuffer.append("runId_PST"+",");
        myHeaderBuffer.append("totalTractorCount_PST"+",");
        myHeaderBuffer.append("totalCube_PST"+",");
        myHeaderBuffer.append("totalWeight_PST"+",");
        myHeaderBuffer.append("totalTractorCount_Total"+",");
        myHeaderBuffer.append("totalCube_Total"+",");
        myHeaderBuffer.append("totalWeight_Total"+",");
        myHeaderBuffer.append("cubicFeet_Total"+",");
        myHeaderBuffer.append("totalCubePerTractor_Total"+",");
        myHeaderBuffer.append("totalWeightPerTractor_Total"+",");
        myHeaderBuffer.append("totalCubicFeetPerTractor_Total"+",");

        SortedMap<Date, SortedMap<String, TractorCount_TotalCube_TotalWeight>> myMapOfTractorCountResultsByModelGroupByDate = new TreeMap<Date, SortedMap<String, TractorCount_TotalCube_TotalWeight>>();

        myPrintWriter.println(myHeaderBuffer);

        SortedMap<Integer, Map<String, java.sql.Timestamp>> myMap_timeStamp_by_modelGroup_by_runID = this.getMapOfOBModelRunDatesByModelRunID(dateString);

        int totalNumRunIDsWeWant=20000;

        int myNumRunIdsLookedAt = 0;
        //3836
        for (Integer id : myMap_timeStamp_by_modelGroup_by_runID.keySet()){

            myNumRunIdsLookedAt++;

            if(myNumRunIdsLookedAt>totalNumRunIDsWeWant) {
                break;
            }

            System.out.println(id);

            Map<String, java.sql.Timestamp> myMap_timeStamp_by_modelGroup = myMap_timeStamp_by_modelGroup_by_runID.get(id);

            for(String myModelGroupID : myMap_timeStamp_by_modelGroup.keySet()) {

                Date myDateForThisRunID = myMap_timeStamp_by_modelGroup.get(myModelGroupID);
                if (myDateForThisRunID == null) {
                    continue;
                }

                System.out.println(myModelGroupID);
                System.out.println(myDateForThisRunID);

                double totalTractorCount = this.read_driver_summary_totalTractorCount_fromDB(id);

                Cube_Weight myCube_Weight_total = this.read_load_lane_summary_totalCube_totalWeight_fromDB(id);

                if (myCube_Weight_total != null) {

                    double totalCube = myCube_Weight_total.getMyCube();
                    double totalWeight = myCube_Weight_total.getMyWeight();

                    Date myDate_match = null;
                    for (Date myDate : myMapOfTractorCountResultsByModelGroupByDate.keySet()) {
                        if (myDate.equals(myDateForThisRunID)) {
                            myDate_match = myDate;
                        }
                    }

                    SortedMap<String, TractorCount_TotalCube_TotalWeight> myMap_inner = null;
                    if (myDate_match == null) {
                        myMap_inner = new TreeMap<String, TractorCount_TotalCube_TotalWeight>();
                        myMapOfTractorCountResultsByModelGroupByDate.put(myDateForThisRunID, myMap_inner);
                    } else {
                        myMap_inner = myMapOfTractorCountResultsByModelGroupByDate.get(myDate_match);
                    }

                    TractorCount_TotalCube_TotalWeight myTractorCount_TotalCube_TotalWeight = new TractorCount_TotalCube_TotalWeight(
                            totalTractorCount,
                            totalCube,
                            totalWeight,
                            id,
                            null,
                            null
                    );

                    myMap_inner.put(myModelGroupID, myTractorCount_TotalCube_TotalWeight);

                }

                System.out.println("\t Finished reading remote");

            }

        }

        for(Date myDate : myMapOfTractorCountResultsByModelGroupByDate.keySet()) {
            SortedMap<String, TractorCount_TotalCube_TotalWeight> myMap_inner = myMapOfTractorCountResultsByModelGroupByDate.get(myDate);
            if(myMap_inner==null) {
                continue;
            }

            LBDate_UBDate myLBDate_UBDate_toUse = null;
            for(LBDate_UBDate myLBDate_UBDate : LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.keySet()) {
                if(myLBDate_UBDate.inInterval(myDate)) {
                    myLBDate_UBDate_toUse = myLBDate_UBDate;
                    break;
                }
            }

            if(myLBDate_UBDate_toUse == null) {
                continue;
            }

            Double cubicFeetPerHundredCube = LHHssReportBuilder.myMapOfCubicFeetPerHundredCube.get(myLBDate_UBDate_toUse);

            TractorCount_TotalCube_TotalWeight myTractorCount_TotalCube_TotalWeight_CST = myMap_inner.get(LHHssReportBuilder.CST);
            TractorCount_TotalCube_TotalWeight myTractorCount_TotalCube_TotalWeight_PST = myMap_inner.get(LHHssReportBuilder.PST);

            boolean isCSTPresent = false;
            if(myTractorCount_TotalCube_TotalWeight_CST != null) {
                isCSTPresent = true;
            }

            boolean isPSTPresent = false;
            if(myTractorCount_TotalCube_TotalWeight_PST != null) {
                isPSTPresent = true;
            }

            Double totalTractorCount = 0.0;
            Double totalCube = 0.0;
            Double totalWeight = 0.0;
            if(isCSTPresent || isPSTPresent) {

                if(myTractorCount_TotalCube_TotalWeight_CST != null) {
                    totalTractorCount += myTractorCount_TotalCube_TotalWeight_CST.getTotalTractorCount();
                    totalCube += myTractorCount_TotalCube_TotalWeight_CST.getTotalCube();
                    totalWeight += myTractorCount_TotalCube_TotalWeight_CST.getTotalWeight();
                }

                if(myTractorCount_TotalCube_TotalWeight_PST != null) {
                    totalTractorCount += myTractorCount_TotalCube_TotalWeight_PST.getTotalTractorCount();
                    totalCube += myTractorCount_TotalCube_TotalWeight_PST.getTotalCube();
                    totalWeight += myTractorCount_TotalCube_TotalWeight_PST.getTotalWeight();
                }

            }

            StringBuffer myBuffer = new StringBuffer();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            myBuffer.append(dateFormat.format(myDate)+",");
            myBuffer.append(cubicFeetPerHundredCube+",");
            if(isCSTPresent && isPSTPresent) {
                myBuffer.append(true+",");
            } else {
                myBuffer.append(false+",");
            }
            if(isCSTPresent) {
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getRunID() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalTractorCount() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalCube() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_CST.getTotalWeight() + ",");
            } else {
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null+ ",");
                myBuffer.append(null + ",");
            }

            if(isPSTPresent) {
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getRunID() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalTractorCount() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalCube() + ",");
                myBuffer.append(myTractorCount_TotalCube_TotalWeight_PST.getTotalWeight() + ",");
            } else {
                myBuffer.append(null + ",");
                myBuffer.append(null + ",");
                myBuffer.append(null+ ",");
                myBuffer.append(null + ",");
            }

            Double cubicFeet_Total = totalCube*cubicFeetPerHundredCube/100.0;

            myBuffer.append(totalTractorCount + ",");
            myBuffer.append(totalCube + ",");
            myBuffer.append(totalWeight + ",");
            myBuffer.append(cubicFeet_Total + ",");

            Double cubePerTractor = null;
            Double weightPerTractor = null;
            if(totalTractorCount != null && totalCube != null && totalWeight != null) {
                cubePerTractor = totalCube/(totalTractorCount+epsilon);
                weightPerTractor = totalWeight/(totalTractorCount+epsilon);
            }

            Double cubicFeetPerTractor = null;
            if(totalTractorCount != null && cubicFeet_Total != null) {
                cubicFeetPerTractor = cubicFeet_Total/(totalTractorCount+epsilon);
            }

            myBuffer.append(cubePerTractor + ",");
            myBuffer.append(weightPerTractor + ",");
            myBuffer.append(cubicFeetPerTractor + ",");

            myPrintWriter.println(myBuffer);

        }


        myPrintWriter.close();
        myFileOutputStream.close();

    }

    public void runTrailerRoutingAnalysis() throws Exception {

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        //String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        this.readSics(baseDirectory_local+"Jul27_weekly\\", "lho_sic.csv");

        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\TrailerHoldsOffOfOBShift\\", "TrailerHoldsOffOfOBShift.csv");

        this.logFile.println("Date, Region, runID, TrailerID, OriginServiceCenter, ServiceCenterType, darkLocInd, hasFACShiftInd");

        for (int id = 2178; id <= 2339; id=id+1){
            String runId = id+"";

            System.out.println(runId);

            String dir_in = baseDirectory_local+runId+"\\in\\";
            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            boolean didWeReadThe_lho_trailer_routing_fileCorrectly = this.read_lho_trailer_routing(dir_in+"lho_trailer_routing.csv");

            for(String myTrailerString : this.myMapOfTrailerByString.keySet()) {
                Trailer myTrailer = this.myMapOfTrailerByString.get(myTrailerString);

                LocationShiftTrailerActionDate myLocationShiftTrailerActionDate = myTrailer.doesThisHoldOffOBOnThisDateAndFromThisRegion(myDateForThisRunID, myModelGroupID, this);
                if(myLocationShiftTrailerActionDate == null) {
                    continue;
                }

                String myLocation = myLocationShiftTrailerActionDate.getMyLocation();
                String typeOfLocation = this.myMapOfTypeBySic.get(myLocation);
                String darkLocInd = this.myMapOfDarkLocationIndBySic.get(myLocation);
                String hasFACShiftInd = this.myMapOfHasFACShiftIndBySic.get(myLocation);

                StringBuffer buffer = new StringBuffer();
                buffer.append(LHHssReportBuilder.getDateNoTime(myDateForThisRunID)+",");
                buffer.append(myModelGroupID+",");
                buffer.append(id+",");
                buffer.append(myTrailer.getMyID()+",");
                buffer.append(myLocation+",");
                buffer.append(typeOfLocation+",");
                buffer.append(darkLocInd+",");
                buffer.append(hasFACShiftInd+",");

                this.logFile.println(buffer.toString());

            }


        }

        closeFile();

    }

    public static String getDateNoTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String res = "";
        try{
            res = sdf.format(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public void runLHO_RollUpTractorCount_LadenTrailerCount_WithAndWithoutBypassThroughOMTs() throws Exception {

        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\BypassOMTResults\\", "BypassOMTResults.csv");

        String baseDirectory_local = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        String baseDirectory_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        this.logFile.println("ModelGroupID, ModelRunID,  TractorCount (no bypass through OMTs), Laden Trailer Count OV (no bypass through OMTs),Laden Trailer Count VD (no bypass through OMTs),Laden Trailer Count OD (no bypass through OMTs), TractorCount (bypass through OMTs), Laden Trailer Count OV (bypass through OMTs),Laden Trailer Count VD (bypass through OMTs),Laden Trailer Count OD (bypass through OMTs),   ");

        for (int id = 2139; id <= 2251; id=id+1){
            String runId = id+"";

            System.out.println(runId);

            String dir_in = baseDirectory_local+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            System.out.println("\t Reading remote");
            Map<String, Double> myMapOfSummaryStats_remote = this.getMapShowingTractorCountAndLadenPupCountThroughOMTs(true, runId, baseDirectory_remote);
            System.out.println("\t Finished reading remote");

            System.out.println("\t Reading local");
            Map<String, Double> myMapOfSummaryStats_local = this.getMapShowingTractorCountAndLadenPupCountThroughOMTs(false, runId, baseDirectory_local);
            System.out.println("\t Finished reading local");

            if(myMapOfSummaryStats_remote != null && myMapOfSummaryStats_local != null) {

                StringBuffer buffer = new StringBuffer();

                buffer.append(myModelGroupID+",");
                buffer.append(id+",");

                buffer.append(myMapOfSummaryStats_remote.get(LHHssReportBuilder.TractorCountThroughOMT)+",");
                buffer.append(myMapOfSummaryStats_remote.get(LHHssReportBuilder.LoadedPupCountThroughOMT_OV)+",");
                buffer.append(myMapOfSummaryStats_remote.get(LHHssReportBuilder.LoadedPupCountThroughOMT_VD)+",");
                buffer.append(myMapOfSummaryStats_remote.get(LHHssReportBuilder.LoadedPupCountThroughOMT_OD)+",");

                buffer.append(myMapOfSummaryStats_local.get(LHHssReportBuilder.TractorCountThroughOMT)+",");
                buffer.append(myMapOfSummaryStats_local.get(LHHssReportBuilder.LoadedPupCountThroughOMT_OV)+",");
                buffer.append(myMapOfSummaryStats_local.get(LHHssReportBuilder.LoadedPupCountThroughOMT_VD)+",");
                buffer.append(myMapOfSummaryStats_local.get(LHHssReportBuilder.LoadedPupCountThroughOMT_OD)+",");

                this.logFile.println(buffer);

            }

        }
        closeFile();

    }

    public void populateSummaryStatsFromJamieFile(
            boolean isRemote,
            String runId,
            String BaseDirectory,
            SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_OB,
            SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_FAC,
            SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_IB,
            SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_Day,
            SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_Total
    ) throws Exception {

        String dir_OptimOutput = null;

        if(!isRemote) {
            dir_OptimOutput = BaseDirectory+runId+"\\in\\Output\\OptimOutput\\";
        } else {
            dir_OptimOutput = BaseDirectory+runId+"\\out\\";
        }

        File df_OptimOutput = new File(dir_OptimOutput);
        if (!df_OptimOutput.exists()) {
            System.out.println("dir_OptimOutput = " + dir_OptimOutput + " does NOT exist");
            return;
        }

        String fileName = dir_OptimOutput+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originShift_input = val[1].trim();
                String originLocation_input = val[2].trim();
                String destinationShift_input = val[11].trim();
                String destinationLocation_input = val[9].trim();
                String myMode_input = val[14].trim();

                OriginLocationDestinationLocation myOriginLocationDestinationLocation = new OriginLocationDestinationLocation(
                        originLocation_input,
                        originShift_input,
                        destinationLocation_input,
                        destinationShift_input,
                        myMode_input
                );

                Double PricePerMile_input = new Double(val[51].trim());
                Double tractorTotal_input = new Double(val[24].trim());
                Double numTrailersPerTractor_input = new Double(val[32].trim());
                Double miles_OD_input = new Double(val[20].trim());
                Double miles_OV_input = new Double(val[18].trim());
                Double miles_VD_input = new Double(val[19].trim());
                Double numLadenPups_OD_input = new Double(val[35].trim());
                Double numLadenPups_OV_input = new Double(val[33].trim());
                Double numLadenPups_VD_input = new Double(val[34].trim());
                Double numEmptyPups_OD_input = new Double(val[39].trim());
                Double numEmptyPups_OV_input = new Double(val[37].trim());
                Double numEmptyPups_VD_input = new Double(val[38].trim());
                Double cube_OD_input = new Double(val[42].trim());
                Double cube_OV_input = new Double(val[40].trim());
                Double cube_VD_input = new Double(val[41].trim());
                Double weight_OD_input = new Double(val[46].trim());
                Double weight_OV_input = new Double(val[44].trim());
                Double weight_VD_input = new Double(val[45].trim());

                SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia_toUse = null;

                if(originShift_input.equals("O")) {
                    mySummaryLHStatsForJamie_obviaibvia_toUse = mySummaryLHStatsForJamie_obviaibvia_OB;
                } else if(originShift_input.equals("F")) {
                    mySummaryLHStatsForJamie_obviaibvia_toUse = mySummaryLHStatsForJamie_obviaibvia_FAC;
                } else if(originShift_input.equals("I")) {
                    mySummaryLHStatsForJamie_obviaibvia_toUse = mySummaryLHStatsForJamie_obviaibvia_IB;
                } else if(originShift_input.equals("D")) {
                    mySummaryLHStatsForJamie_obviaibvia_toUse = mySummaryLHStatsForJamie_obviaibvia_Day;
                }

                this.addRecord(
                        mySummaryLHStatsForJamie_obviaibvia_toUse,
                        myOriginLocationDestinationLocation,
                        PricePerMile_input,
                        tractorTotal_input,
                        numTrailersPerTractor_input,
                        miles_OD_input,
                        miles_OV_input,
                        miles_VD_input,
                        numLadenPups_OD_input,
                        numLadenPups_OV_input,
                        numLadenPups_VD_input,
                        numEmptyPups_OD_input,
                        numEmptyPups_OV_input,
                        numEmptyPups_VD_input,
                        cube_OD_input,
                        cube_OV_input,
                        cube_VD_input,
                        weight_OD_input,
                        weight_OV_input,
                        weight_VD_input
                );

                // Always add all records to the totals summary LH stats
                this.addRecord(
                        mySummaryLHStatsForJamie_obviaibvia_Total,
                        myOriginLocationDestinationLocation,
                        PricePerMile_input,
                        tractorTotal_input,
                        numTrailersPerTractor_input,
                        miles_OD_input,
                        miles_OV_input,
                        miles_VD_input,
                        numLadenPups_OD_input,
                        numLadenPups_OV_input,
                        numLadenPups_VD_input,
                        numEmptyPups_OD_input,
                        numEmptyPups_OV_input,
                        numEmptyPups_VD_input,
                        cube_OD_input,
                        cube_OV_input,
                        cube_VD_input,
                        weight_OD_input,
                        weight_OV_input,
                        weight_VD_input

                );


            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


        return;

    }

    public void getMilesFrom_lho_miles_File(
            String BaseDirectory,
            Set<OrigLocDestLoc> mySetOfOrigLocDestLoc
    ) throws Exception {
        String dir_OptimOutput = BaseDirectory;

        File df_OptimOutput = new File(dir_OptimOutput);
        if (!df_OptimOutput.exists()) {
            System.out.println("dir_OptimOutput = " + dir_OptimOutput + " does NOT exist");
            return;
        }

        String fileName = dir_OptimOutput+"lho_mile.csv";

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String origin = val[0].trim();
                String dest = val[1].trim();
                String miles_string = val[2].trim();

                Double miles = null;
                try {
                    miles = new Double(miles_string);
                } catch(Exception ex) {
                    continue;
                }

                OrigLocDestLoc myOrigLocDestLoc_match = null;
                for(OrigLocDestLoc myOrigLocDestLoc : mySetOfOrigLocDestLoc) {
                    if(myOrigLocDestLoc.getLocation_origin().equals(origin)) {
                        if(myOrigLocDestLoc.getLocation_destination().equals(dest)) {
                            myOrigLocDestLoc_match = myOrigLocDestLoc;
                            break;
                        }
                    }
                }

                if(myOrigLocDestLoc_match==null) {
                    continue;
                }

                myOrigLocDestLoc_match.addBackupMiles(miles);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return;

    }

    public void getMilesFromLoadLegFile(
            boolean isRemote,
            String runId,
            String BaseDirectory,
            Set<OrigLocDestLoc> mySetOfOrigLocDestLoc
    ) throws Exception {

        String dir_OptimOutput = null;

        if(!isRemote) {
            dir_OptimOutput = BaseDirectory+runId+"\\in\\Output\\";
        } else {
            dir_OptimOutput = BaseDirectory+runId+"\\out\\preprocessor\\";
        }

        File df_OptimOutput = new File(dir_OptimOutput);
        if (!df_OptimOutput.exists()) {
            System.out.println("dir_OptimOutput = " + dir_OptimOutput + " does NOT exist");
            return;
        }

        String fileName = dir_OptimOutput+"LoadLegs.csv";

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String origin = val[1].trim();
                String originShift = val[2].trim();
                String dest = val[3].trim();
                String destShift = val[4].trim();
                String mode = val[5].trim();
                String miles_string = val[25].trim();
                String isHSS = val[11].trim();

                Double miles = null;
                try {
                    miles = new Double(miles_string);
                } catch(Exception ex) {
                    continue;
                }

                OrigLocDestLoc myOrigLocDestLoc_match = null;
                for(OrigLocDestLoc myOrigLocDestLoc : mySetOfOrigLocDestLoc) {
                    if(myOrigLocDestLoc.getLocation_origin().equals(origin)) {
                        if(myOrigLocDestLoc.getLocation_destination().equals(dest)) {
                            myOrigLocDestLoc_match = myOrigLocDestLoc;
                            break;
                        }
                    }
                }

                if(myOrigLocDestLoc_match==null) {
                    continue;
                }

                if(isHSS.equals("1")) {
                    myOrigLocDestLoc_match.setHSS(true);
                } else {
                    myOrigLocDestLoc_match.setHSS(false);
                }

                myOrigLocDestLoc_match.addMiles(miles);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return;

    }

    public void getMilesFromMoveLegFile(
            boolean isRemote,
            String runId,
            String BaseDirectory,
            Set<OrigLocDestLoc> mySetOfOrigLocDestLoc
    ) throws Exception {

        String dir_OptimOutput = null;

        if(!isRemote) {
            dir_OptimOutput = BaseDirectory+runId+"\\in\\Output\\";
        } else {
            dir_OptimOutput = BaseDirectory+runId+"\\out\\preprocessor\\";
        }

        File df_OptimOutput = new File(dir_OptimOutput);
        if (!df_OptimOutput.exists()) {
            System.out.println("dir_OptimOutput = " + dir_OptimOutput + " does NOT exist");
            return;
        }

        String fileName = dir_OptimOutput+"MoveLegs.csv";

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String origin = val[1].trim();
                String dest = val[3].trim();
                String miles_string = val[16].trim();

                Double miles = null;
                try {
                    miles = new Double(miles_string);
                } catch(Exception ex) {
                    continue;
                }

                OrigLocDestLoc myOrigLocDestLoc_match = null;
                for(OrigLocDestLoc myOrigLocDestLoc : mySetOfOrigLocDestLoc) {
                    if(myOrigLocDestLoc.getLocation_origin().equals(origin)) {
                        if(myOrigLocDestLoc.getLocation_destination().equals(dest)) {
                            myOrigLocDestLoc_match = myOrigLocDestLoc;
                            break;
                        }
                    }
                }

                if(myOrigLocDestLoc_match==null) {
                    continue;
                }

                myOrigLocDestLoc_match.addMiles(miles);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return;

    }

    public static Double getAverage(List<Double> listOfDouble, boolean ignoreZeros) {
        double sum = 0.0;
        double counter=0.0;
        for(Double myDouble : listOfDouble) {
            if(ignoreZeros) {
                if(myDouble<epsilon) {
                    continue;
                }
            }
            sum += myDouble;
            counter++;
        }
        double avg = sum/(counter+epsilon);

        return avg;
    }

    public Map<String, Double> getAverageTrailerCostPerMileFromJamieFile(
            boolean isRemote,
            String runId,
            String BaseDirectory
    ) throws Exception {

        Map<String, Double> myMapByMode = new HashMap<String, Double>();

        String dir_OptimOutput = null;

        if(!isRemote) {
            dir_OptimOutput = BaseDirectory+runId+"\\in\\Output\\OptimOutput\\";
        } else {
            dir_OptimOutput = BaseDirectory+runId+"\\out\\";
        }

        File df_OptimOutput = new File(dir_OptimOutput);
        if (!df_OptimOutput.exists()) {
            System.out.println("dir_OptimOutput = " + dir_OptimOutput + " does NOT exist");
            return myMapByMode;
        }

        String fileName = dir_OptimOutput+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";

        BufferedReader br = null;
        String dbRecord;

        List<Double> listOfHSSCostPerMilePerTrailer = new ArrayList<Double>();
        List<Double> listOfLHCostPerMilePerTrailer = new ArrayList<Double>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originShift_input = val[1].trim();
                String originLocation_input = val[2].trim();
                String viaLocation_input = val[4].trim();
                String destinationShift_input = val[11].trim();
                String destinationLocation_input = val[9].trim();
                String myMode_input = val[15].trim();

                OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode = new OrigLocOrigShiftDestLocDestShift_mode(
                        originLocation_input,
                        originShift_input,
                        viaLocation_input,
                        destinationLocation_input,
                        destinationShift_input,
                        myMode_input
                );

                if(myOrigLocOrigShiftDestLocDestShift_mode.hasVia()) {
                    continue;
                }

                Double myTractorCount_total = new Double(val[25].trim());

                if(myTractorCount_total==null || myTractorCount_total.intValue()==0) {
                    continue;
                }

                boolean isHSS = false;
                if(!myOrigLocOrigShiftDestLocDestShift_mode.isLineHaul()) {
                    isHSS = true;
                }

                Double myTractorMiles_sched = new Double(val[30].trim());
                Double myTractorMiles_unsched = new Double(val[31].trim());
                Double myTractorMiles_aboveUnsched = new Double(val[32].trim());

                Double myCost_sched = new Double(val[49].trim());
                Double myCost_unsched = new Double(val[50].trim());
                Double myCost_aboveUnsched = new Double(val[51].trim());

                double costPerMilePerTractor = (myCost_sched+myCost_unsched+myCost_aboveUnsched)/(myTractorMiles_sched+myTractorMiles_unsched+myTractorMiles_aboveUnsched+epsilon);

                if(isHSS) {

                    // this is HSS

                    listOfHSSCostPerMilePerTrailer.add(costPerMilePerTractor);

                } else {

                    // this is LH

                    Double myPup_OV_laden_input = new Double(val[34].trim());
                    Double myPup_VD_laden_input = new Double(val[35].trim());
                    Double myPup_OD_laden_input = new Double(val[36].trim());

                    Double myPup_OV_empty_input = new Double(val[38].trim());
                    Double myPup_VD_empty_input = new Double(val[39].trim());
                    Double myPup_OD_empty_input = new Double(val[40].trim());

                    Double myPupCount = myPup_OV_laden_input+myPup_VD_laden_input+myPup_OD_laden_input+myPup_OV_empty_input+myPup_VD_empty_input+myPup_OD_empty_input;
                    //Double myPupCount = myPup_OV_laden_input+myPup_VD_laden_input+myPup_OD_laden_input;

                    double myRegularization = myPupCount/myTractorCount_total;

                    if(myRegularization<epsilon) {
                        continue;
                    }

                    double costPerMilePerTrailer = costPerMilePerTractor/(myRegularization+epsilon);

                    listOfLHCostPerMilePerTrailer.add(costPerMilePerTrailer);

                }

                //Double myTractorCount_sched = new Double(val[22].trim());
                //Double myTractorCount_unsched = new Double(val[23].trim());
                //Double myTractorCount_aboveUnsched = new Double(val[24].trim());

            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        double avgLH = LHHssReportBuilder.getAverage(listOfLHCostPerMilePerTrailer, false);

        double avgHSS = LHHssReportBuilder.getAverage(listOfHSSCostPerMilePerTrailer, false);

        myMapByMode.put(LHHssReportBuilder.L, avgLH);
        myMapByMode.put("S", avgHSS);

        return myMapByMode;

    }

    public void addRecord(
            SummaryLHStatsForJamie_obviaibvia mySummaryLHStatsForJamie_obviaibvia,
            OriginLocationDestinationLocation myOriginLocationDestinationLocation,
            Double PricePerMile_input,
            Double tractorTotal_input,
            Double numTrailersPerTractor_input,
            Double miles_OD_input,
            Double miles_OV_input,
            Double miles_VD_input,
            Double numLadenPups_OD_input,
            Double numLadenPups_OV_input,
            Double numLadenPups_VD_input,
            Double numEmptyPups_OD_input,
            Double numEmptyPups_OV_input,
            Double numEmptyPups_VD_input,
            Double cube_OD_input,
            Double cube_OV_input,
            Double cube_VD_input,
            Double weight_OD_input,
            Double weight_OV_input,
            Double weight_VD_input
    ) throws Exception {
        mySummaryLHStatsForJamie_obviaibvia.addRecord(
                myOriginLocationDestinationLocation,
                PricePerMile_input,
                tractorTotal_input,
                numTrailersPerTractor_input,
                miles_OD_input,
                miles_OV_input,
                miles_VD_input,
                numLadenPups_OD_input,
                numLadenPups_OV_input,
                numLadenPups_VD_input,
                numEmptyPups_OD_input,
                numEmptyPups_OV_input,
                numEmptyPups_VD_input,
                cube_OD_input,
                cube_OV_input,
                cube_VD_input,
                weight_OD_input,
                weight_OV_input,
                weight_VD_input
        );
    }

    public Map<String, Double> getMapShowingTractorCountAndLadenPupCountThroughOMTs(boolean isRemote, String runId, String BaseDirectory) throws Exception {

        String dir_OptimOutput = null;

        if(!isRemote) {
            dir_OptimOutput = BaseDirectory+runId+"\\in\\Output\\OptimOutput\\";
        } else {
            dir_OptimOutput = BaseDirectory+runId+"\\out\\";
        }

        File df_OptimOutput = new File(dir_OptimOutput);
        if (!df_OptimOutput.exists()) {
            System.out.println("dir_OptimOutput = " + dir_OptimOutput + " does NOT exist");
            return null;
        }

        Map<String, Double> myMapOfSummaryStats = this.readJamieFile_returnMapWithSumOfTractorCountAndLadenPupCountThroughOMTs(dir_OptimOutput);

        return myMapOfSummaryStats;

    }

    public void runLHO_HSSTrailers_andHowTheyMatchUp() throws Exception {

        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\TrailerCMSMatching\\", "TrailerCMSMatching.csv");

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        this.logFile.println("Model Run Id,Region,Date,TotalHSSTrailers,TotalNonCMSMatch_firstPhase, TotalNonCMSMatch_final, BeforeLoadReleaseNumber");

        for (int id = 2531; id <= 2560; id=id+1){
            boolean isBeforeLoadReleaseNumber = true;
            if(id>=2551) {
                isBeforeLoadReleaseNumber = false;
            }
            String runId = id+"";
            String dir_preprocessorOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\";
            File df_preprocessorOutput = new File(dir_preprocessorOutput);
            if (!df_preprocessorOutput.exists()) continue;
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            String hssTrailerFileName = dir_preprocessorOutput+"trailers_HSS_AndTheirMatchingCMS_ifAny.csv";
            HSSTrailerInformation myHSSTrailerInformation = this.read_trailers_HSS_AndTheirMatchingCMS_ifAny(hssTrailerFileName);

            StringBuffer buffer = new StringBuffer();
            buffer.append(id+",");
            buffer.append(myModelGroupID+",");
            buffer.append(myDateForThisRunID+",");
            buffer.append(myHSSTrailerInformation.getNumHSSTrailer()+",");
            buffer.append(myHSSTrailerInformation.getNumHSSWithNonGoodCMSMatch()+",");
            buffer.append(myHSSTrailerInformation.getNumHSSWithNotAnyCMSMatch()+",");
            buffer.append(isBeforeLoadReleaseNumber+",");

            this.logFile.println(buffer.toString());
        }
        closeFile();

    }

    public void runLHO_RollUpTimingStatisticsAsFunctionOfDirectOptionsAndDirectUsed() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\TimingStatistics\\", "TimingStatisticsAsFunctionOfDirectOptionsAndUse.csv");

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        this.logFile.println("Model Run Id,Region,RunTime (seconds),NumberDirectLoadPaths,Number of Directs Used");

        for (int id = 1755; id <= 1845; id=id+1){
            String runId = id+"";
            String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\OptimOutput\\";
            File df_OptimOutput = new File(dir_OptimOutput);
            if (!df_OptimOutput.exists()) continue;
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }
            String timingFileName = dir_OptimOutput+"timingFile.csv";
            int numSeconds = -99;
            try {
                numSeconds = this.readTimingFile(timingFileName);

            } catch(Exception ex) {
                int temp_a = 0;
            }

            String dir_preprocessorOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\";
            String file_LoadPaths = dir_preprocessorOutput+"LoadPaths_forCoreOptimizer.csv";
            int numDirectLoadPaths = -99;
            try {
                numDirectLoadPaths = this.readLoadPathFile(file_LoadPaths);

            } catch(Exception ex) {
                int temp_a = 0;
            }

            String file_JamieFile = dir_OptimOutput+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";
            int numDirectsUsed = -99;
            try {
                numDirectsUsed = this.readJamieFile(file_JamieFile);

            } catch(Exception ex) {
                int temp_a = 0;
            }


            this.logFile.println(id+","+myModelGroupID+","+numSeconds+","+numDirectLoadPaths+","+numDirectsUsed);
        }
        closeFile();

    }

    public void runLHO_RollUpStatsOnNumberOfDiversions() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\DiversionStatistics\\", "DiversionStatistics.csv");

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        this.logFile.println("Model Run Id,Region,Shift, OBSic, NumberDiversions_fewerDiversions, NumberDiversions_moreDiversions, percentageChange");

        for (int id = 1836; id <= 1836; id=id+1){
            String runId = id+"";
            String dir_OptimOutput = myDirectoryString_OBModelPhaseII+runId+"\\in\\Output\\OptimOutput\\";
            File df_OptimOutput = new File(dir_OptimOutput);
            if (!df_OptimOutput.exists()) continue;
            String dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";
            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            SortedMap<String, SortedMap<String, DiversionSummary>> myMap_Diversions_BySicByShift = new TreeMap<String, SortedMap<String, DiversionSummary>>();

            String file_lane_diversion_fewerDiversions = dir_OptimOutput+"lho_lane_diversion_fewerDiversions.csv";

            try {
                this.readLoadDiversionsFile(file_lane_diversion_fewerDiversions, myMap_Diversions_BySicByShift, true);
            } catch(Exception ex) {
                int temp_a = 0;
            }

            String file_lane_diversion_moreDiversions = dir_OptimOutput+"lho_lane_diversion_moreDiversions.csv";
            try {
                this.readLoadDiversionsFile(file_lane_diversion_moreDiversions, myMap_Diversions_BySicByShift, false);
            } catch(Exception ex) {
                int temp_a = 0;
            }

            for(String myShift : myMap_Diversions_BySicByShift.keySet()) {
                SortedMap<String, DiversionSummary> myMap_Diversions_BySic = myMap_Diversions_BySicByShift.get(myShift);
                for(String mySic : myMap_Diversions_BySic.keySet()) {
                    DiversionSummary myDiversionSummary = myMap_Diversions_BySic.get(mySic);
                    Integer diversions_fewer = myDiversionSummary.getMyDiversion_fewer();
                    Integer diversions_more = myDiversionSummary.getMyDiversion_more();
                    double percentageChange = (diversions_more-diversions_fewer)/(diversions_fewer+LHHssReportBuilder.epsilon);
                    this.logFile.println(id+","+myModelGroupID+","+myShift+","+mySic+","+diversions_fewer+","+diversions_more+","+percentageChange);
                }
            }

        }
        closeFile();

    }

    public void runLHO_trailerModificationSummary() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\RollUpTrailerModifications\\", "myTrailerModifications.csv");

        for (int id=3253; id<=3449; id++ ){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String hssFile = dir+"trailerCleanup_changingSelectOBPlans.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile);
        }
        closeFile();
    }

    public void runLHO_moveLaneSummary() throws Exception {
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\RollUpPROToDivResults\\", "DivsWithNoAssignment_aggregation_newModel.csv");

        for (int id=1423; id<=1517; id++ ){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\OptimOutput\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String hssFile = dir+"DivsWithNoAssignment.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile, false);
        }
        closeFile();
    }

    public void runLHO() throws Exception{
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\", "lho_load_lane_summary_aggregation_2.csv");
        for (int id = 1076; id <= 1246; id=id+1){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\OptimOutput\\";//"\\out\\FacPreProcessor\\";//"\\in\\";//
            File df = new File(dir);
            if (!df.exists()) continue;
            String hssFile = dir+"lho_load_lane_summary.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile, false);
        }
        closeFile();
    }

    public void rollup_RandomForestRegressorModelOutput() throws Exception {


        String myBaseDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\RevMan_python_dataAnalysis\\data\\results\\2016_12_08-250_180\\RandomForestRegressor\\";

        List<String> fileNameToRead_list = new ArrayList<String>();
        fileNameToRead_list.add("summary_new");
        //fileNameToRead_list.add("AttributeImportance");

        for (String fileNameToRead : fileNameToRead_list) {

            openFile(myBaseDirectory, fileNameToRead+"_total.csv");

            FileOutputStream myDetailedModelResultsFileStream = null;
            PrintWriter myDetailedModelResultsPrintWriter = null;

            if(fileNameToRead.equals("summary_new")) {
                myDetailedModelResultsFileStream = new FileOutputStream(new File(myBaseDirectory + fileNameToRead + "_Detailed_total.csv"), false);
                myDetailedModelResultsPrintWriter = new PrintWriter(myDetailedModelResultsFileStream);
            }

            int iterationCounter=0;
            for (int segment = 1; segment <= 32; segment=segment+1){

                boolean shouldContinue = true;
                if(segment == 30 || segment ==28 || segment==27) {
                    //shouldContinue = false;
                }

                if(!shouldContinue) {
                    continue;
                }

                iterationCounter++;

                boolean isThisFirstTime = false;
                if(iterationCounter==1) {
                    isThisFirstTime = true;
                }

                String dir_segment = null;
                dir_segment = myBaseDirectory+segment+"\\";

                File df_in = new File(dir_segment);
                if (!df_in.exists()) {
                    continue;
                }

                String file_ToRead = dir_segment+segment+fileNameToRead+".txt";
                this.readFileAndDumpToLog(file_ToRead, segment, isThisFirstTime, myDetailedModelResultsPrintWriter);


            }

            closeFile();

            if(myDetailedModelResultsPrintWriter != null) {
                myDetailedModelResultsPrintWriter.close();
            }

            if(myDetailedModelResultsFileStream != null) {
                myDetailedModelResultsFileStream.close();
            }

        }

    }

    public void runLHO_NumCubedOutVersusWeightedOutPups() throws Exception{

        boolean runLocally = false;
        if(runLocally) {
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\NumCubedVersusWeighedOutPups\\", "NumCubedVersusWeighedOutPups_local.csv");
        } else {
            openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\NumCubedVersusWeighedOutPups\\", "NumCubedVersusWeighedOutPups_remote.csv");
        }

        String myDirectoryString_OBModelPhaseII = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        String myDirectoryString_OBModelPhaseII_remote = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        StringBuffer buffer_header = new StringBuffer();
        buffer_header.append("ModelDate"+",");
        buffer_header.append("ModelGroupID"+",");
        buffer_header.append("Shift"+",");
        buffer_header.append("TotalPups"+",");
        buffer_header.append("NumPups_CubedOut"+",");
        buffer_header.append("NumPups_WeighedOut"+",");
        buffer_header.append("NumPups_CubedOutWeighedOut"+",");
        //buffer_header.append("FractionCubedOut"+",");
        //buffer_header.append("FractionWeighedOut"+",");

        this.logFile.println(buffer_header.toString());

        int endingRunID = 2704;
        //int endingRunID = 2545;
        for (int id = 2416; id <= endingRunID; id=id+1){

            System.out.println(id);

            String runId = id+"";
            String dir_in = null;
            if(runLocally) {
                dir_in = myDirectoryString_OBModelPhaseII+runId+"\\in\\";
            } else {
                dir_in = myDirectoryString_OBModelPhaseII_remote+runId+"\\in\\";
            }

            File df_in = new File(dir_in);
            if (!df_in.exists()) continue;

            String file_lho_model_run = dir_in+"lho_model_run.csv";
            String myModelGroupID = "PST";
            if(this.isThisModelRunHaveModGrpCd(file_lho_model_run, dir_in, "CST")) {
                myModelGroupID = "CST";
            }

            Date myDateForThisRunID = this.readModelRunFile(dir_in, false, true);
            if(myDateForThisRunID==null) {
                continue;
            }

            StringBuffer buffer_date_modelGroup = new StringBuffer();
            buffer_date_modelGroup.append(myDateForThisRunID+",");
            buffer_date_modelGroup.append(myModelGroupID+",");

            String dir_optimizationOutput = null;
            if(runLocally) {
                dir_optimizationOutput = dir_in+"Output\\OptimOutput\\";
            } else {
                dir_optimizationOutput = myDirectoryString_OBModelPhaseII_remote+runId+"\\"+"out\\";
            }

            SortedMap<String, SortedMap<String, Integer>> mapOfNumCubedOutNumWeighedOutByDepartureShift = this.getMapOfNumCubedOutNumWeighedOutByDepartureShift(dir_optimizationOutput);

            for(String myShift : mapOfNumCubedOutNumWeighedOutByDepartureShift.keySet()) {

                SortedMap<String, Integer> myMap = mapOfNumCubedOutNumWeighedOutByDepartureShift.get(myShift);

                Integer cubedOutAndWeighedOut = myMap.get(LHHssReportBuilder.CubedOutAndWeighedOut);
                Integer cubedOut = myMap.get(LHHssReportBuilder.CubedOut);
                Integer weighedOut = myMap.get(LHHssReportBuilder.WeighedOut);
                Integer total = myMap.get(LHHssReportBuilder.Total);
                double fractionCubedOut = cubedOut/(total+ LHHssReportBuilder.epsilon);
                double fractionWeighedOut = weighedOut/(total+ LHHssReportBuilder.epsilon);

                StringBuffer buffer_shift_counts = new StringBuffer();
                buffer_shift_counts.append(myShift+",");
                buffer_shift_counts.append(total+",");
                buffer_shift_counts.append(cubedOut+",");
                buffer_shift_counts.append(weighedOut+",");
                buffer_shift_counts.append(cubedOutAndWeighedOut+",");
                //buffer_shift_counts.append(total+",");
                //buffer_shift_counts.append(fractionCubedOut+",");
                //buffer_shift_counts.append(fractionWeighedOut+",");

                this.logFile.println(buffer_date_modelGroup.toString()+buffer_shift_counts.toString());

            }

        }

        closeFile();

    }

    public void runLHO_rollupHzBLresults() throws Exception{
        String[] inpNames = {};
        String extraHeader = "ActualNumHSS";
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\HazMat_BL_HSS_reportResults\\", "HazMat_BL_HSS_report_aggregate_new.csv");
        for (int id = 1643; id <= 1712; id=id+1){
            String runId = id+"";
            String preprocessor_dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\";
            File df_preprocessor = new File(preprocessor_dir);
            if (!df_preprocessor.exists()) continue;

            String preprocessor_coreOptimizer_subDir = preprocessor_dir+"OptimOutput\\";
            File df_coreOptimizer = new File(preprocessor_coreOptimizer_subDir);
            if (!df_coreOptimizer.exists()) continue;

            Map<String, Integer> mapOfNumHSSPullsByOrigLocOrigShiftDestLocDestShift_modeString = this.getHSSCountsFromOptimization(preprocessor_coreOptimizer_subDir);

            String hssFile = preprocessor_dir+"HazMat_BL_HSS_report.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                header = header+extraHeader;
                addLine(header);
            }
            addFile(hssFile, mapOfNumHSSPullsByOrigLocOrigShiftDestLocDestShift_modeString, extraHeader);
        }
        closeFile();
    }

    public void runLHO_rollupCMSresults() throws Exception{
        this.header="";
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\HazMat_BL_HSS_reportResults\\", "lho_cms_pulls_modified_aggregate.csv");
        for (int id = 1643; id <= 1712; id=id+1){
        //for (int id = 1711; id <= 1711; id=id+1){
            String runId = id+"";
            String input_dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\";
            File input_preprocessor = new File(input_dir);
            if (!input_preprocessor.exists()) continue;

            String cmsFile = input_dir+"lho_cms_pulls_modified.csv";
            if (header.length() < 1){
                getHeader(cmsFile);
                addLine(header);
            }
            //addFile(cmsFile);
            addFile_cms(cmsFile, (new Integer(id)).toString());
        }
        closeFile();
    }

    private SortedMap<String, SortedMap<String, Integer>> getMapOfNumCubedOutNumWeighedOutByDepartureShift(String preprocessor_coreOptimizer_subDir) throws Exception {

        SortedMap<String, SortedMap<String, Integer>> myMap_return = new TreeMap<String, SortedMap<String, Integer>>();

        BufferedReader br = null;
        String dbRecord;

        String myLoadLaneSummaryFile = preprocessor_coreOptimizer_subDir+"lho_load_lane_summary.csv";

        try {
            File f = new File(myLoadLaneSummaryFile);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originShift = val[1].trim();
                String myMoveMode = val[4].trim();

                if(!myMoveMode.equals(LHHssReportBuilder.L)) {
                    continue;
                }

                Double myCube = new Double(val[11].trim());
                Integer myWeight = new Integer(val[12].trim());
                Integer myPupCount = new Integer(val[13].trim());
                Integer myVanCount = new Integer(val[14].trim());

                if(myPupCount == 0 && myVanCount == 0) {
                    continue;
                }

                double myCubePerPup = myCube/(myPupCount+LHHssReportBuilder.epsilon);
                double myPoundsPerPup = myWeight/(myPupCount+LHHssReportBuilder.epsilon);

                double myRatio_cube = myCubePerPup/(1.0);
                double myRatio_weight = myPoundsPerPup/(LHHssReportBuilder.pounds_per_pup);

                SortedMap<String, Integer> myMap_inside = myMap_return.get(originShift);
                if(myMap_inside == null) {
                    myMap_inside = new TreeMap<String, Integer>();
                    myMap_return.put(originShift, myMap_inside);
                }

                boolean cubedOutAndWeighedOut = false;
                double diff_ratios_abs = Math.abs(myRatio_cube-myRatio_weight);
                if(diff_ratios_abs<0.001) {
                    int temp_a = 0;
                    cubedOutAndWeighedOut = true;
                }

                this.addElementToMap(LHHssReportBuilder.Total, myPupCount, myMap_inside);
                if(cubedOutAndWeighedOut) {
                    int myFullPup = this.getFullPups_cube(myCube, myPupCount);
                    if(myFullPup>0) {
                        this.addElementToMap(LHHssReportBuilder.CubedOutAndWeighedOut, myFullPup, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.WeighedOut, 0, myMap_inside);
                        int temp_a = 0;
                    } else {
                        this.addElementToMap(LHHssReportBuilder.CubedOutAndWeighedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.WeighedOut, 0, myMap_inside);
                        int temp_a = 0;
                    }
                } else if(myRatio_cube>myRatio_weight) {
                    int myFullPup = this.getFullPups_cube(myCube, myPupCount);
                    if(myFullPup>0) {
                        this.addElementToMap(LHHssReportBuilder.CubedOut, myFullPup, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.WeighedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOutAndWeighedOut, 0, myMap_inside);
                        int temp_a = 0;
                    } else {
                        this.addElementToMap(LHHssReportBuilder.CubedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.WeighedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOutAndWeighedOut, 0, myMap_inside);
                        int temp_a = 0;
                    }
                } else {
                    int myFullPup = this.getFullPups_weight(myWeight, myPupCount);
                    if(myFullPup>0) {
                        this.addElementToMap(LHHssReportBuilder.WeighedOut, myFullPup, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOutAndWeighedOut, 0, myMap_inside);
                        int temp_a = 0;
                    } else {
                        this.addElementToMap(LHHssReportBuilder.WeighedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOut, 0, myMap_inside);
                        this.addElementToMap(LHHssReportBuilder.CubedOutAndWeighedOut, 0, myMap_inside);
                        int temp_a = 0;
                    }
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


        return myMap_return;

    }

    public int getFullPups_cube(double myCube_total, int myPupCount) {
        int myFullPup = 0;
        for(myFullPup = myPupCount ; myFullPup>=0; myFullPup--) {
            if(myFullPup < myCube_total+LHHssReportBuilder.epsilon) {
                return myFullPup;
            }
        }

        return myFullPup;
    }

    public int getFullPups_weight(int myWeight_total, int myPupCount) {
        int myFullPup = 0;
        for(myFullPup = myPupCount ; myFullPup>=0; myFullPup--) {
            if(myFullPup*LHHssReportBuilder.pounds_per_pup < myWeight_total+LHHssReportBuilder.epsilon) {
                return myFullPup;
            }
        }

        return myFullPup;
    }


    public void addElementToMap(String key, Integer myValue, SortedMap<String, Integer> myMap) {

        if(key == null) {
            return;
        }
        if(myValue == null) {
            return;
        }
        if(myMap == null) {
            return;
        }

        Integer currentValue = myMap.get(key);
        if(currentValue == null) {
            myMap.put(key, myValue);
        } else {
            myMap.put(key, currentValue+myValue);
        }
    }

    private Map<String, Integer> getHSSCountsFromOptimization(String preprocessor_coreOptimizer_subDir) throws Exception {

        Map<String, Integer> myMap_return = new HashMap<String, Integer>();

        BufferedReader br = null;
        String dbRecord;

        String myLoadLaneSummaryFile = preprocessor_coreOptimizer_subDir+"lho_load_lane_summary.csv";

        try {
            File f = new File(myLoadLaneSummaryFile);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originLoc = val[2];
                String originShift = val[1];

                String destLoc = val[7];
                String destShift = val[8];

                String mode = val[4];

                if(mode.equals(LHHssReportBuilder.L)) {
                    continue;
                }

                Integer numVans = 0;

                try {
                    numVans = new Integer(val[14]);
                } catch(Exception ex) {
                    // don't do anything
                    int temp_a = 0;
                }

                OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode = new OrigLocOrigShiftDestLocDestShift_mode(
                        originLoc,
                        originShift,
                        null,
                        destLoc,
                        destShift,
                        mode
                );

                Integer currentCount = myMap_return.get(myOrigLocOrigShiftDestLocDestShift_mode.toString());
                if(currentCount==null) {
                    myMap_return.put(myOrigLocOrigShiftDestLocDestShift_mode.toString(), numVans);
                } else {
                    myMap_return.put(myOrigLocOrigShiftDestLocDestShift_mode.toString(),currentCount+numVans);
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        String myBypassSummaryFile = preprocessor_coreOptimizer_subDir+"lho_bypass_trlr.csv";

        try {
            File f = new File(myBypassSummaryFile);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originLoc = val[2];
                String originShift = val[1];

                String destLoc = val[7];
                String destShift = val[8];

                String mode = val[4];

                if(mode.equals(LHHssReportBuilder.L)) {
                    continue;
                }

                Integer numVans = 1;

                OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode = new OrigLocOrigShiftDestLocDestShift_mode(
                        originLoc,
                        originShift,
                        null,
                        destLoc,
                        destShift,
                        mode
                );

                Integer currentCount = myMap_return.get(myOrigLocOrigShiftDestLocDestShift_mode.toString());
                if(currentCount==null) {
                    myMap_return.put(myOrigLocOrigShiftDestLocDestShift_mode.toString(),numVans);
                } else {
                    myMap_return.put(myOrigLocOrigShiftDestLocDestShift_mode.toString(),currentCount+numVans);
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myMap_return;

    }

    public void runLHO_summarizeViaInfo() throws Exception {

        if(this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation == null) {
            this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation = new HashMap<OrigLocationViaLocationDestLocation, HistoricalDouble_ModelDouble>();
        }

        this.readHistoricalViaData("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\ViaSummaryResults_1423_1492\\historicalViaInformation.csv");

        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\ViaSummaryResults_1423_1492\\", "Jamie_aggregation.csv");

        openFile_viaSummary("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\ViaSummaryResults_1423_1492\\", "via_aggregation.csv");

        List<Integer> listOfModelRunID = new ArrayList<Integer>();

        listOfModelRunID.add(1492);
        listOfModelRunID.add(1491);
        listOfModelRunID.add(1488);
        listOfModelRunID.add(1487);
        listOfModelRunID.add(1484);
        listOfModelRunID.add(1483);
        listOfModelRunID.add(1480);
        listOfModelRunID.add(1479);

        listOfModelRunID.add(1476);
        listOfModelRunID.add(1475);
        listOfModelRunID.add(1472);
        listOfModelRunID.add(1471);
        listOfModelRunID.add(1468);
        listOfModelRunID.add(1467);
        listOfModelRunID.add(1464);
        listOfModelRunID.add(1463);
        listOfModelRunID.add(1460);
        listOfModelRunID.add(1459);

        listOfModelRunID.add(1456);
        listOfModelRunID.add(1455);
        listOfModelRunID.add(1452);
        listOfModelRunID.add(1451);
        listOfModelRunID.add(1448);
        listOfModelRunID.add(1447);
        listOfModelRunID.add(1444);
        listOfModelRunID.add(1443);
        listOfModelRunID.add(1440);
        listOfModelRunID.add(1439);

        listOfModelRunID.add(1436);
        listOfModelRunID.add(1435);
        listOfModelRunID.add(1432);
        listOfModelRunID.add(1431);
        listOfModelRunID.add(1428);
        listOfModelRunID.add(1427);
        listOfModelRunID.add(1424);
        listOfModelRunID.add(1423);

        for (Integer id  : listOfModelRunID){
            String runId = id+"";
            String dir = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\"+runId+"\\in\\Output\\OptimOutput\\";
            File df = new File(dir);
            if (!df.exists()) continue;
            String hssFile = dir+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";
            if (header.length() < 1){
                getHeader(hssFile);
                addLine(header);
            }
            addFile(hssFile, true);
        }

        writeToFile_viaSummary();

        closeFile();
        closeFile_viaSummary();
    }

    public void runLHO_rollUpForJeffrey(){
        String[] inpNames = {};
        openFile("C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\LoadLaneSummary_resultsFromJulyThroughEndOfAug\\", "lho_load_lane_summary_aggregation2.csv");
        for (int id = 1301; id <= 1419; id=id+1){
            String runId = id+"";
            String hssFile = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\LoadLaneSummary_resultsFromJulyThroughEndOfAug\\lho_load_lane_summary_"+runId+".csv";
            File hssFile_file = new File(hssFile);
            if (!hssFile_file.exists()) continue;
            if (header.length() < 1){
                getHeader(hssFile);
                header = header+", week";
                addLine(header);
            }
            addFile_addWeek(hssFile);
        }
        closeFile();
    }

    public void getHeader(String fileName){
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            header = dbRecord;
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally
    }

    public void readActuals(String modelRunID, String myModelGrp, String Date, String file){
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            header = dbRecord;

            while ( (dbRecord = br.readLine()) != null) {
                addLine(modelRunID, myModelGrp, Date, dbRecord);
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally
    }

    public void readHistoricalViaData(String fileName) throws Exception {
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            if(header.length()<1) {
                header = dbRecord;
            }

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originLocation = val[0];
                String viaLocation = val[1];
                String destinationLocation = val[2];
                Integer numTractors_total = 0;
                try {
                    numTractors_total = new Integer(val[3]);
                } catch(Exception ex) {
                    // don't do anything
                    int temp_a = 0;
                }

                OrigLocationViaLocationDestLocation myOrigLocationViaLocationDestLocation = this.getOrigLocationViaLocationDestLocation(originLocation, viaLocation, destinationLocation, true);

                HistoricalDouble_ModelDouble myHistoricalDouble_ModelDouble = this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.get(myOrigLocationViaLocationDestLocation);
                if(myHistoricalDouble_ModelDouble == null) {
                    this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.put(myOrigLocationViaLocationDestLocation, new HistoricalDouble_ModelDouble(numTractors_total.doubleValue(), 0.0));
                } else {
                    myHistoricalDouble_ModelDouble.addHistoricalDouble_ModelDouble(numTractors_total.doubleValue(), 0.0);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally
    }

    public DriverCountExcessDriversUsedTotalDriversUsed readExcessDriverInfo(String dir) throws Exception {

        String fileName = dir+"file_excessDriversByLocation_obviaibvia.csv";

        System.out.println("readExcessDriverInfo");

        BufferedReader br = null;
        String dbRecord;

        DriverCountExcessDriversUsedTotalDriversUsed myDriverCountExcessDriversUsedTotalDriversUsed = new DriverCountExcessDriversUsedTotalDriversUsed(0,0,0);

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String myShift = val[1];
                if(!myShift.equals("O")) {
                    continue;
                }
                int driverCount = (new Double(val[2])).intValue();
                int excessDrivers = (new Double(val[5])).intValue();
                int totalDrivers = (new Double(val[6])).intValue();

                myDriverCountExcessDriversUsedTotalDriversUsed.addDriverCount(driverCount);
                myDriverCountExcessDriversUsedTotalDriversUsed.addExcessDriversUsed(excessDrivers);
                myDriverCountExcessDriversUsedTotalDriversUsed.addTotalDriversUsed(totalDrivers);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myDriverCountExcessDriversUsedTotalDriversUsed;


    }

    public static String myJamieFileSummaryHeader = new String(" TotalNumShipmentDiversions,TotalNumTrailerDiversions,TotalTractors,PhantomTractors,TotalTractorVias, TU,LoadedPupCount,EmptyPupCount,SU,LF,EmptyPercent,TotalMiles,TotalCost");

    public String readJamieFileSummary(String dir) throws Exception {

        String fileName = dir+"file_JamiesMasterTractorTrailerMovementFile_summary_obviaibvia.csv";

        System.out.println("readJamieFileSummary");

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String myShift = val[0];
                if(!myShift.equals("Total")) {
                    continue;
                }

                StringBuffer myBuffer = new StringBuffer();

                for(int i=1; i<val.length; i++) {
                    myBuffer.append(val[i]);
                    if(i<val.length-1) {
                        myBuffer.append(",");
                    }
                }

                return myBuffer.toString();

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return new String("");


    }

    public Map<String, Double> readJamieFileSummary_returnMapOfValues(String dir, String shiftOrTotal, Boolean withDirects) throws Exception {

        Map<String, Double> returnMap = new HashMap<String, Double>();

        String fileName = null;
        if(withDirects != null) {
            if(withDirects) {
                fileName = dir+"file_JamiesMasterTractorTrailerMovementFile_summary_obviaibvia_Direct.csv";
            } else {
                fileName = dir+"file_JamiesMasterTractorTrailerMovementFile_summary_obviaibvia_noDirect.csv";
            }
        } else {
            fileName = dir+"file_JamiesMasterTractorTrailerMovementFile_summary_obviaibvia.csv";
        }

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String myShift = val[0];
                if(!myShift.equals(shiftOrTotal)) {
                    continue;
                }

                returnMap.put(LHHssReportBuilder.TU, new Double(val[6]));
                returnMap.put(LHHssReportBuilder.LoadedPupCount, new Double(val[7]));
                returnMap.put(LHHssReportBuilder.EmptyPupCount, new Double(val[8]));
                returnMap.put(LHHssReportBuilder.SU, new Double(val[9]));
                returnMap.put(LHHssReportBuilder.LF, new Double(val[10]));
                returnMap.put(LHHssReportBuilder.EmptyPercent, new Double(val[11]));
                returnMap.put(LHHssReportBuilder.TotalMiles, new Double(val[12]));
                returnMap.put(LHHssReportBuilder.TotalCost, new Double(val[13]));

                return returnMap;

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return returnMap;


    }

    public Map<String, Double> readJamieFile_returnMapWithSumOfTractorCount_TotalCube_TotalWeight(String runId, String baseDirectory_remote) throws Exception {

        String fileName = baseDirectory_remote+runId+"\\out\\"+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";

        BufferedReader br = null;
        String dbRecord;

        double totalTractorCount = 0;
        double totalCube_OV = 0;
        double totalCube_VD = 0;
        double totalCube_OD = 0;
        double totalWeight_OV = 0;
        double totalWeight_VD = 0;
        double totalWeight_OD = 0;
        double ladenTrailer_OV = 0;
        double ladenTrailer_VD = 0;
        double ladenTrailer_OD = 0;
        double emptyTrailer_OV = 0;
        double emptyTrailer_VD = 0;
        double emptyTrailer_OD = 0;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");

                try {
                    Double myNum = new Double(val[34].trim());
                    ladenTrailer_OV += myNum;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNum = new Double(val[35].trim());
                    ladenTrailer_VD += myNum;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNum = new Double(val[36].trim());
                    ladenTrailer_OD += myNum;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNum = new Double(val[38].trim());
                    emptyTrailer_OV += myNum;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNum = new Double(val[39].trim());
                    emptyTrailer_VD += myNum;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNum = new Double(val[40].trim());
                    emptyTrailer_OD += myNum;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNumTractors = new Double(val[25].trim());
                    totalTractorCount += myNumTractors;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double cube_OV = new Double(val[41].trim());
                    totalCube_OV += cube_OV;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double cube_VD = new Double(val[42].trim());
                    totalCube_VD += cube_VD;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double cube_OD = new Double(val[43].trim());
                    totalCube_OD += cube_OD;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double weight_OV = new Double(val[45].trim());
                    totalWeight_OV += weight_OV;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double weight_VD = new Double(val[46].trim());
                    totalWeight_VD += weight_VD;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double weight_OD = new Double(val[47].trim());
                    totalWeight_OD += weight_OD;
                } catch(Exception ex) {
                    continue;
                }


            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        Map<String, Double> returnMap = new HashMap<String, Double>();

        returnMap.put(LHHssReportBuilder.totalTractorCount, totalTractorCount);

        returnMap.put(LHHssReportBuilder.totalCube_OV, totalCube_OV);
        returnMap.put(LHHssReportBuilder.totalCube_VD, totalCube_VD);
        returnMap.put(LHHssReportBuilder.totalCube_OD, totalCube_OD);

        returnMap.put(LHHssReportBuilder.totalWeight_OV, totalWeight_OV);
        returnMap.put(LHHssReportBuilder.totalWeight_VD, totalWeight_VD);
        returnMap.put(LHHssReportBuilder.totalWeight_OD, totalWeight_OD);

        returnMap.put(LHHssReportBuilder.ladenTrailer_OV, ladenTrailer_OV);
        returnMap.put(LHHssReportBuilder.ladenTrailer_VD, ladenTrailer_VD);
        returnMap.put(LHHssReportBuilder.ladenTrailer_OD, ladenTrailer_OD);
        returnMap.put(LHHssReportBuilder.emptyTrailer_OV, emptyTrailer_OV);
        returnMap.put(LHHssReportBuilder.emptyTrailer_VD, emptyTrailer_VD);
        returnMap.put(LHHssReportBuilder.emptyTrailer_OD, emptyTrailer_OD);

        return returnMap;

    }

    public Double read_driver_summary_totalTractorCount_fromDB(Integer runId) throws Exception {

        double totalTractorCount = 0;

        Connection theConn = null;

        try {
            // connection to Netezza
            theConn = DatabaseAccess.getConnection_PRD_WHSEVIEW();

            String sql_updated = "Select * from ADMIN.lho_driver_summary_vw where mod_run_snpsht_nm='OPTIMIZER_OUTPUT' and run_nbr="+runId;

            System.out.println("This is the query we issued:");
            System.out.println(sql_updated);

            Statement stmt = theConn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_updated);
            //rs.setFetchSize(40000);

            Integer schedule_nbr = null;

            int rowCounter=0;
            while (rs.next()) {

                Integer myNumTractor = rs.getInt("DRVR_CNT");

                totalTractorCount += myNumTractor;

            }

            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (theConn != null) theConn.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return totalTractorCount;

    }

    public Double read_lho_driver_summary_totalTractorCount(String runId, String baseDirectory_remote) throws Exception {

        String fileName = baseDirectory_remote+runId+"\\out\\"+"lho_driver_summary.csv";

        BufferedReader br = null;
        String dbRecord;

        double totalTractorCount = 0;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");

                try {
                    Double myNum = new Double(val[8].trim());
                    totalTractorCount += myNum;
                } catch(Exception ex) {
                    continue;
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return totalTractorCount;

    }

    public Cube_Weight read_load_lane_summary_totalCube_totalWeight_fromDB(Integer runId) throws Exception {

        Connection theConn = null;

        Map<String, Cube_Weight> myMapOfCube_WeightByString = new HashMap<String, Cube_Weight>();

        try {
            // connection to Netezza
            theConn = DatabaseAccess.getConnection_PRD_WHSEVIEW();

            String sql_updated = "Select * from ADMIN.lho_load_lane_summary_vw where mod_run_snpsht_nm='OPTIMIZER_OUTPUT' and run_nbr="+runId;

            System.out.println("This is the query we issued:");
            System.out.println(sql_updated);

            Statement stmt = theConn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_updated);
            //rs.setFetchSize(40000);

            Integer schedule_nbr = null;

            int rowCounter=0;
            while (rs.next()) {

                Double myCube = rs.getDouble("LNH_OPTMZ_CUBE_PCT");
                Double myWeight = rs.getDouble("LNH_OPTMZ_WGT");

                // All cubes in the lho_load_lane_summary are in units of 1 cube unit per pup....rather than 100 cube per pup.
                // This is why we have to multiply out by 100.0 here....
                myCube = 100.0*myCube;

                Cube_Weight myCube_Weight = new Cube_Weight(myCube, myWeight);

                myMapOfCube_WeightByString.put(myCube_Weight.toString(), myCube_Weight);

            }

            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (theConn != null) theConn.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        Cube_Weight myCube_WeightToReturn = new Cube_Weight(0.0, 0.0);

        for(String myCube_WeightString : myMapOfCube_WeightByString.keySet()) {
            Cube_Weight myCube_Weight = myMapOfCube_WeightByString.get(myCube_WeightString);
            myCube_WeightToReturn.addCube_Weight(myCube_Weight);
        }

        return myCube_WeightToReturn;

    }

    public Cube_Weight read_lho_load_lane_summary_totalCube_totalWeight(String runId, String baseDirectory_remote) throws Exception {

        String fileName = baseDirectory_remote+runId+"\\out\\"+"lho_load_lane_summary.csv";

        BufferedReader br = null;
        String dbRecord;

        Map<String, Cube_Weight> myMapOfCube_WeightByString = new HashMap<String, Cube_Weight>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");

                Double myCube = null;
                Double myWeight = null;

                try {
                    myCube = new Double(val[11].trim());
                } catch(Exception ex) {
                    continue;
                }

                try {
                    myWeight = new Double(val[12].trim());
                } catch(Exception ex) {
                    continue;
                }

                // All cubes in the lho_load_lane_summary are in units of 1 cube unit per pup....rather than 100 cube per pup.
                // This is why we have to multiply out by 100.0 here....
                myCube = 100.0*myCube;

                Cube_Weight myCube_Weight = new Cube_Weight(myCube, myWeight);

                myMapOfCube_WeightByString.put(myCube_Weight.toString(), myCube_Weight);


            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        Cube_Weight myCube_WeightToReturn = new Cube_Weight(0.0, 0.0);

        for(String myCube_WeightString : myMapOfCube_WeightByString.keySet()) {
            Cube_Weight myCube_Weight = myMapOfCube_WeightByString.get(myCube_WeightString);
            myCube_WeightToReturn.addCube_Weight(myCube_Weight);
        }

        return myCube_WeightToReturn;

    }

    public Map<String, Double> readJamieFile_returnMapWithSumOfTractorCountAndLadenPupCountThroughOMTs(String dir) throws Exception {

        String fileName = dir+"file_JamiesMasterTractorTrailerMovementFile_obviaibvia.csv";

        BufferedReader br = null;
        String dbRecord;

        double totalTractorCountThroughOMTs = 0;
        double totalLadenPupCountThroughOMTs_OV = 0;
        double totalLadenPupCountThroughOMTs_VD = 0;
        double totalLadenPupCountThroughOMTs_OD = 0;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String myOriginShift = val[1].trim();
                if(!myOriginShift.equals("O")) {
                    continue;
                }

                String myOMT = val[13].trim();
                if(!myOMT.equals("OMT")) {
                    continue;
                }

                try {
                    Double myNumTractors = new Double(val[24].trim());
                    totalTractorCountThroughOMTs += myNumTractors;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNumTrailers_OV = new Double(val[33].trim());
                    totalLadenPupCountThroughOMTs_OV += myNumTrailers_OV;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNumTrailers_VD = new Double(val[34].trim());
                    totalLadenPupCountThroughOMTs_VD += myNumTrailers_VD;
                } catch(Exception ex) {
                    continue;
                }

                try {
                    Double myNumTrailers_OD = new Double(val[35].trim());
                    totalLadenPupCountThroughOMTs_OD += myNumTrailers_OD;
                } catch(Exception ex) {
                    continue;
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Couldn't read this file="+fileName+"...skipping this one");
            //throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


        Map<String, Double> returnMap = new HashMap<String, Double>();

        returnMap.put(LHHssReportBuilder.TractorCountThroughOMT, totalTractorCountThroughOMTs);

        returnMap.put(LHHssReportBuilder.LoadedPupCountThroughOMT_OV, totalLadenPupCountThroughOMTs_OV);
        returnMap.put(LHHssReportBuilder.LoadedPupCountThroughOMT_VD, totalLadenPupCountThroughOMTs_VD);
        returnMap.put(LHHssReportBuilder.LoadedPupCountThroughOMT_OD, totalLadenPupCountThroughOMTs_OD);

        return returnMap;

    }

    public Map<String, Double> readJamieHSSFileSummary_returnMapOfValues(String dir, String shiftOrTotal, Boolean withDirects) throws Exception {

        Map<String, Double> returnMap = new HashMap<String, Double>();

        String fileName = null;
        if(withDirects != null) {
            if(withDirects) {
                fileName = dir+"file_JamiesMasterHSSTractorTrailerMovementFile_summary_obviaibvia_Direct.csv";
            } else {
                fileName = dir+"file_JamiesMasterHSSTractorTrailerMovementFile_summary_obviaibvia_noDirect.csv";
            }
        } else {
            fileName = dir+"file_JamiesMasterHSSTractorTrailerMovementFile_summary_obviaibvia.csv";
        }

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String myShift = val[0];
                if(!myShift.equals(shiftOrTotal)) {
                    continue;
                }

                returnMap.put(LHHssReportBuilder.Scheduled_HSS, new Double(val[1]));
                returnMap.put(LHHssReportBuilder.Unscheduled_HSS, new Double(val[2]));
                returnMap.put(LHHssReportBuilder.Supp_HSS_Cancels, new Double(val[3]));
                returnMap.put(LHHssReportBuilder.Excl_HSS_Cancels, new Double(val[4]));
                returnMap.put(LHHssReportBuilder.Supp_HSS_Adds, new Double(val[5]));
                returnMap.put(LHHssReportBuilder.Excl_HSS_Adds, new Double(val[6]));
                double loadedHSS =
                        returnMap.get(LHHssReportBuilder.Scheduled_HSS)+
                        returnMap.get(LHHssReportBuilder.Unscheduled_HSS)-
                        returnMap.get(LHHssReportBuilder.Supp_HSS_Cancels)-
                        returnMap.get(LHHssReportBuilder.Excl_HSS_Cancels)+
                        returnMap.get(LHHssReportBuilder.Supp_HSS_Adds)+
                        returnMap.get(LHHssReportBuilder.Excl_HSS_Adds);

                if(loadedHSS<0) {
                    loadedHSS = LHHssReportBuilder.epsilon;
                }

                returnMap.put(LHHssReportBuilder.Loaded_HSS, loadedHSS);

                returnMap.put(LHHssReportBuilder.HSS_TU, new Double(val[7]));
                returnMap.put(LHHssReportBuilder.HSS_LF, new Double(val[8]));
                returnMap.put(LHHssReportBuilder.HSS_Miles, new Double(val[9]));
                returnMap.put(LHHssReportBuilder.HSS_Miles_prorated, new Double(val[10]));
                returnMap.put(LHHssReportBuilder.HSS_Cost, new Double(val[11]));
                returnMap.put(LHHssReportBuilder.HSS_Cost_prorated, new Double(val[12]));

                return returnMap;

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return returnMap;


    }

    public void readAggregatedExcessDriverStatsForParticularViaDoublesSetting(String excessDriverFile, ViaYesNoDoublesYesNo myViaYesNoDoublesYesNo) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(excessDriverFile);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                Integer myModelRunID = new Integer(val[0]);
                String myModelGroupID = val[1];
                String myDate_string = val[2];

                Date myDate = null;
                if(myViaYesNoDoublesYesNo.isAreDoublesOn() && myViaYesNoDoublesYesNo.isAreViasOn()) {
                    myDate = new SimpleDateFormat("MM/dd/yyyy").parse(myDate_string);
                } else {
                    myDate = new SimpleDateFormat("yyyy-MM-dd").parse(myDate_string);
                }


                String mySic = val[3];
                Integer myReportedAvailableDrivers = new Integer(val[4]);
                Integer myReportedDriverOverages = new Integer(val[5]);

                ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts = this.getModelRunID_Sic_excessDriverCounts(myModelRunID, mySic, true);
                myModelRunID_Sic_excessDriverCounts.setMyModelGroupID(myModelGroupID);
                myModelRunID_Sic_excessDriverCounts.setMyDateForThisRunID(myDate);

                myModelRunID_Sic_excessDriverCounts.setMyReportedAvailableDrivers(myReportedAvailableDrivers);

                if(myViaYesNoDoublesYesNo.isAreDoublesOn() && myViaYesNoDoublesYesNo.isAreViasOn()) {
                    myModelRunID_Sic_excessDriverCounts.setMyExcessDrivers_withVias_withDoubles(myReportedDriverOverages);
                } else if(myViaYesNoDoublesYesNo.isAreDoublesOn() && !myViaYesNoDoublesYesNo.isAreViasOn()) {
                    myModelRunID_Sic_excessDriverCounts.setMyExcessDrivers_withNOVias_withDoubles(myReportedDriverOverages);
                } else if(!myViaYesNoDoublesYesNo.isAreDoublesOn() && myViaYesNoDoublesYesNo.isAreViasOn()) {
                    myModelRunID_Sic_excessDriverCounts.setMyExcessDrivers_withVias_withNODoubles(myReportedDriverOverages);
                } else if(!myViaYesNoDoublesYesNo.isAreDoublesOn() && !myViaYesNoDoublesYesNo.isAreViasOn()) {
                    myModelRunID_Sic_excessDriverCounts.setMyExcessDrivers_withNOVias_withNODoubles(myReportedDriverOverages);
                }


            }
        }catch (Exception e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

    }

    public void readAndGenerateDriverUsageStats(int id, String myModelGroupID, Date myDateForThisRunID, String excessDriverFile, boolean isDriverUsageLimited) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(excessDriverFile);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String mySic = val[0];
                String myShift = val[1];
                if(!myShift.equals("O")) {
                    continue;
                }

                Double excessDrivers = new Double(val[5]);
                Double reportedAvailableDrivers = new Double(val[2]);
                Double totalDriversUsed = new Double(val[6]);

                int excessDrivers_int = (new Double(Math.floor(excessDrivers))).intValue();
                int reportedAvailableDrivers_int = (new Double(Math.floor(reportedAvailableDrivers))).intValue();
                int totalDriversUsed_int = (new Double(Math.floor(totalDriversUsed))).intValue();

                ModelRunID_Sic_driverUsageCounts myModelRunID_Sic_driverUsageCounts = this.getModelRunID_Sic_driverUsageCounts(id, mySic, true);
                myModelRunID_Sic_driverUsageCounts.setMyModelGroupID(myModelGroupID);
                myModelRunID_Sic_driverUsageCounts.setMyDateForThisRunID(myDateForThisRunID);

                if(isDriverUsageLimited) {
                    myModelRunID_Sic_driverUsageCounts.setMyExcessDrivers_isDriversLimited(excessDrivers_int);
                    myModelRunID_Sic_driverUsageCounts.setMyReportedAvailableDrivers_isDriversLimited(reportedAvailableDrivers_int);
                    myModelRunID_Sic_driverUsageCounts.setMyDriversUsed_isDriversLimited(totalDriversUsed_int);
                } else {
                    myModelRunID_Sic_driverUsageCounts.setMyExcessDrivers_isDriversUnLimited(excessDrivers_int);
                    myModelRunID_Sic_driverUsageCounts.setMyReportedAvailableDrivers_isDriversUnLimited(reportedAvailableDrivers_int);
                    myModelRunID_Sic_driverUsageCounts.setMyDriversUsed_isDriversUnLimited(totalDriversUsed_int);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


    }

    public void readAndGenerateExcessDriverStats(int id, String myModelGroupID, Date myDateForThisRunID, String excessDriverFile, String excessDriver_with_shiftSpecificOveragesFile, boolean isProduction) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        int total_reportedAvailableDrivers_int = 0;
        int total_excessDrivers_int = 0;

        try {
            File f = new File(excessDriverFile);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String mySic = val[0];
                String myShift = val[1];
                if(!myShift.equals("O")) {
                    continue;
                }

                Double excessDrivers = new Double(val[5]);
                Double reportedAvailableDrivers = new Double(val[2]);

                int excessDrivers_int = (new Double(Math.floor(excessDrivers))).intValue();
                int reportedAvailableDrivers_int = (new Double(Math.floor(reportedAvailableDrivers))).intValue();

                total_excessDrivers_int += excessDrivers_int;
                total_reportedAvailableDrivers_int += reportedAvailableDrivers_int;

                ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts = this.getModelRunID_Sic_excessDriverCounts(id, mySic, true);
                myModelRunID_Sic_excessDriverCounts.setMyModelGroupID(myModelGroupID);
                myModelRunID_Sic_excessDriverCounts.setMyDateForThisRunID(myDateForThisRunID);

                myModelRunID_Sic_excessDriverCounts.setMyReportedAvailableDrivers(reportedAvailableDrivers_int);

                if(isProduction) {
                    myModelRunID_Sic_excessDriverCounts.setMyExcessDrivers_old(excessDrivers_int);
                } else {
                    myModelRunID_Sic_excessDriverCounts.setMyExcessDrivers_new(excessDrivers_int);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        System.out.println("id="+id+" total_reportedAvailableDrivers_int="+total_reportedAvailableDrivers_int+" total_excessDrivers_int="+total_excessDrivers_int);

        try {
            File f = new File(excessDriver_with_shiftSpecificOveragesFile);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String mySic = val[0];
                String myShift = val[9];

                ModelRunID_Sic_excessDriverCounts myModelRunID_Sic_excessDriverCounts = this.getModelRunID_Sic_excessDriverCounts(id, mySic, true);

                if(!isProduction) {
                    myModelRunID_Sic_excessDriverCounts.setShiftAsCause_new(myShift);
                } else {
                    myModelRunID_Sic_excessDriverCounts.setShiftAsCause_old(myShift);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


    }

    public Date readModelRunFile(String dir_in, boolean doYouOnlyWantToSeeCSTResults, boolean doYouOnlyWantToSeeOBModelResults) throws Exception {

        String fileName = dir_in+"lho_model_run.csv";

        System.out.println("readModelRunFile");

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // this reads header line...
            dbRecord = br.readLine();

            // this reads the data
            dbRecord = br.readLine();

            String[] val = dbRecord.split(",");
            String myRegion_string = val[1];
            String myRunShift_string = val[3];

            if(doYouOnlyWantToSeeCSTResults) {
                if(!myRegion_string.equals("CST")) {
                    // we ONLY want to see CST results.
                    return null;
                }
            }

            if(doYouOnlyWantToSeeOBModelResults) {
                if(!myRunShift_string.equals("O")) {
                    // we ONLY want to see OB model results.
                    return null;
                }
            }

            String myDate_string = val[2];

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myDate_string);

            return date;

        }catch (IOException e) {
            e.printStackTrace();
            //throw e;
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                    return null;
                }
            } // end if
        } // end finally

    }

    public void readTBL_FLO_LOAD_OPTION_RDSFile_first(String dir_in, SortedMap<String, SortedSet<String>> mapOfLoadTosByLoadAt) throws Exception {

        String fileName = dir_in+"TBL_FLO_LOAD_OPTION_RDS.csv";

        System.out.println("readTBL_FLO_LOAD_OPTION_RDSFile_first");

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String LD_AT_NODE_NM = val[1];
                String LD_AT_SHFT_CD = val[2];
                String LD_TO_NODE_NM = val[5];
                String LD_TO_SHFT_CD = val[6];

                String LoadAt = LD_AT_NODE_NM+LD_AT_SHFT_CD;
                String LoadTo = LD_TO_NODE_NM+LD_TO_SHFT_CD;

                SortedSet<String> mySetOfLoadTos = mapOfLoadTosByLoadAt.get(LoadAt);
                if(mySetOfLoadTos == null) {
                    mySetOfLoadTos = new TreeSet<String>();
                    mapOfLoadTosByLoadAt.put(LoadAt, mySetOfLoadTos);
                }

                mySetOfLoadTos.add(LoadTo);

            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

    }

    public void readTBL_FLO_LOAD_OPTION_RDSFile_second(String dir_in, SortedMap<String, SortedSet<String>> mapOfLoadTosByLoadAt, SortedMap<String, SortedSet<String>> mapOfFinalDestsByLoadAt) throws Exception {

        String fileName = dir_in+"TBL_FLO_LOAD_OPTION_RDS.csv";

        System.out.println("readTBL_FLO_LOAD_OPTION_RDSFile_second");

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");

                String LD_AT_NODE_NM = val[1];
                String LD_AT_SHFT_CD = val[2];
                String LD_TO_NODE_NM = val[5];
                String LD_TO_SHFT_CD = val[6];

                String LoadAt = LD_AT_NODE_NM+LD_AT_SHFT_CD;
                String LoadTo = LD_TO_NODE_NM+LD_TO_SHFT_CD;

                String FNL_DEST_NODE_NM = val[3];

                if(FNL_DEST_NODE_NM.equals(LD_TO_NODE_NM)) {
                    continue;
                }

                // only concerned about cases where FNL_DEST_NODE_NM <> LD_TO_NODE_NM

                Set<String> allPossibleLoadTos = mapOfLoadTosByLoadAt.get(LoadAt);

                if(allPossibleLoadTos == null || allPossibleLoadTos.size() == 0) {
                    continue;
                }

                boolean doesAllPossibleLoadTosStrippedOfShiftContainFinalDest = false;
                for(String onePossibleLoadToWithShiftAtEnd : allPossibleLoadTos) {
                    String onePossibleLoadTo = onePossibleLoadToWithShiftAtEnd.substring(0,3);
                    if(onePossibleLoadTo.equals(FNL_DEST_NODE_NM)) {
                        doesAllPossibleLoadTosStrippedOfShiftContainFinalDest = true;
                        break;
                    }
                }

                if(doesAllPossibleLoadTosStrippedOfShiftContainFinalDest) {
                    // Weird.......
                    SortedSet<String> setOfFinalDests = mapOfFinalDestsByLoadAt.get(LoadAt);
                    if(setOfFinalDests == null) {
                        setOfFinalDests = new TreeSet<String>();
                        mapOfFinalDestsByLoadAt.put(LoadAt, setOfFinalDests);
                    }
                    setOfFinalDests.add(FNL_DEST_NODE_NM);
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

    }

    public SortedMap<String, Map<String, Integer>> readAggShpmtFile(String dir_preprocessorOutput) throws Exception {

        String fileName = dir_preprocessorOutput+"AggShpmt_forCoreOptimizer.csv";

        System.out.println("readAggShpmtFile");

        BufferedReader br = null;
        String dbRecord;

        SortedMap<String, Map<String, Integer>> myMap = new TreeMap<String, Map<String, Integer>>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originShift = val[1];
                Integer numLoadPaths = 0;
                try {
                    numLoadPaths = new Integer(val[14]);
                } catch(Exception ex) {
                    // go to next one
                    continue;
                }

                Integer weight = 0;
                try {
                    Double weight_cube = new Double(val[4]);
                    weight = (new Double(Math.round(weight_cube))).intValue();
                } catch(Exception ex) {
                    // go to next one
                    continue;
                }

                Integer cube = 0;
                try {
                    Double cube_double = new Double(val[5]);
                    cube = (new Double(Math.round(cube_double))).intValue();
                } catch(Exception ex) {
                    // go to next one
                    continue;
                }


                Map<String, Integer> myMap_inner = myMap.get(originShift);

                if(myMap_inner == null) {
                    myMap_inner = new HashMap<String, Integer> ();
                    myMap.put(originShift, myMap_inner);
                }

                Integer myNum = myMap_inner.get(LHHssReportBuilder.LOAD_PATH_COUNT);
                if(myNum == null) {
                    myMap_inner.put(LHHssReportBuilder.LOAD_PATH_COUNT, numLoadPaths);
                } else {
                    myMap_inner.put(LHHssReportBuilder.LOAD_PATH_COUNT, myNum+numLoadPaths);
                }

                myNum = myMap_inner.get(LHHssReportBuilder.TOTAL_WEIGHT);
                if(myNum == null) {
                    myMap_inner.put(LHHssReportBuilder.TOTAL_WEIGHT, weight);
                } else {
                    myMap_inner.put(LHHssReportBuilder.TOTAL_WEIGHT, myNum+weight);
                }

                myNum = myMap_inner.get(LHHssReportBuilder.TOTAL_CUBE);
                if(myNum == null) {
                    myMap_inner.put(LHHssReportBuilder.TOTAL_CUBE, cube);
                } else {
                    myMap_inner.put(LHHssReportBuilder.TOTAL_CUBE, myNum+cube);
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myMap;

    }

    public int readAggShpmtFile_getNumODs(String dir_preprocessorOutput) throws Exception {

        String fileName = dir_preprocessorOutput+"AggShpmt_forCoreOptimizer.csv";

        System.out.println("readAggShpmtFile_getNumODs");

        BufferedReader br = null;
        String dbRecord;

        int returnValue = 0;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            // read header line
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                returnValue++;

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return returnValue;

    }

    public boolean read_lho_trailer_routing(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                Integer trailerID = new Integer(val[0].trim());
                Integer seqNbr = new Integer(val[1].trim());
                String myLocation = val[2].trim();
                String myShift = val[3].trim();
                String myTrailerAction = val[4].trim();
                String myDate_string = val[9].trim();

                Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse(myDate_string);

                Trailer myTrailer = this.getTrailer(trailerID, true);

                LocationShiftTrailerActionDate myLocationShiftTrailerActionDate = new LocationShiftTrailerActionDate(
                        myLocation,
                        myShift,
                        myTrailerAction,
                        myDate
                );

                myTrailer.addLocationShiftTrailerActionDate(seqNbr,myLocationShiftTrailerActionDate);


            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return false;

    }

    public boolean readCoLoadedFileAndDetermineWhetherThereAreLHVansLoadedFromOBToIBOrLater(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String trailerType = val[15];
                String isLineHaul = val[16];
                if(!trailerType.equals("VAN")) {
                    continue;
                }

                if(!isLineHaul.equals("true")) {
                    continue;
                }

                String loadToSic = val[9];
                String loadToShift = val[10];
                String loadToDay = val[11];

                if(loadToSic.equals("null")) {
                    continue;
                }
                if(loadToShift.equals("null")) {
                    continue;
                }
                if(loadToDay.equals("null")) {
                    continue;
                }

                int loadToDay_int = new Integer(loadToDay);
                if(loadToDay_int>=1) {
                    return true;
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return false;

    }

    public Map<Integer, Trailer> readCoLoadedFile_determineNumberEntries(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        Map<Integer, Trailer> myMap_ByTrailerID = new HashMap<Integer, Trailer>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            int recordCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {

                recordCounter++;

                String[] val = dbRecord.split(",");
                String proj_ob_svcc_sic = val[23];
                String loadToSic = val[9];
                String loadToShift = val[10];
                String loadToDay = val[11];
                Boolean isLH = new Boolean(val[16]);
                Integer trailerID = new Integer(val[3]);

                String obLocationPlusCount = val[23];

                String obLocation = obLocationPlusCount.substring(0,3);
                String trailerPrefix = val[4];
                String trailerSuffix = val[5];
                String loadStatus = val[12];

                boolean isThisGoodRecordToCount = true;
                if(proj_ob_svcc_sic == null || proj_ob_svcc_sic.equals("null")) {
                    isThisGoodRecordToCount = false;
                }

                if(loadToSic == null || loadToSic.equals("null")) {
                    isThisGoodRecordToCount = false;
                }

                if(loadToShift == null || loadToShift.equals("null")) {
                    isThisGoodRecordToCount = false;
                }

                if(loadToDay == null || loadToDay.equals("null")) {
                    isThisGoodRecordToCount = false;
                }

                if(!isThisGoodRecordToCount) {
                    continue;
                }

                Trailer myTrailer = new Trailer(trailerID);
                myTrailer.setIsLH(isLH);
                myTrailer.setLoadStatus(loadStatus);
                myTrailer.setLoadToDay(loadToDay);
                myTrailer.setLoadToLocation(loadToSic);
                myTrailer.setLoadToShift(loadToShift);
                myTrailer.setObLocation(obLocation);
                myTrailer.setTrailerPrefix(trailerPrefix);
                myTrailer.setTrailerSuffix(trailerSuffix);

                myMap_ByTrailerID.put(trailerID, myTrailer);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myMap_ByTrailerID;

    }

    public SortedMap<Integer, PROCount_Cube_Weight> read_file_AggShpmtFile_returnMap(String fileName) throws Exception {

        SortedMap<Integer, PROCount_Cube_Weight> myMap_toReturn = new TreeMap<Integer, PROCount_Cube_Weight>();

        BufferedReader br = null;
        String dbRecord;

        Map<String,Integer> myMapOfOffsetByFieldName = new HashMap<String, Integer>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            int recordCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {

                recordCounter++;

                String[] val = dbRecord.split(",");

                if(recordCounter == 1) {
                    // read the headers so that I know the offsets....

                    for(int i=0; i<val.length; i++) {
                        String myFieldName = val[i];
                        myMapOfOffsetByFieldName.put(myFieldName, i);
                    }

                    continue;
                }

                Integer proCount = new Integer(val[myMapOfOffsetByFieldName.get("PRO_COUNT")]);
                Double cube = new Double(val[myMapOfOffsetByFieldName.get("CUBE")]);
                Double weight = new Double(val[myMapOfOffsetByFieldName.get("WEIGHT")]);
                Integer certType = new Integer(val[myMapOfOffsetByFieldName.get("CERT_TYPE")]);

                if(certType != null && certType.intValue()==0) {

                    PROCount_Cube_Weight myPROCount_Cube_Weight = myMap_toReturn.get(-1);
                    if(myPROCount_Cube_Weight == null) {
                        myPROCount_Cube_Weight = new PROCount_Cube_Weight(proCount, cube, weight);
                        myMap_toReturn.put(-1, myPROCount_Cube_Weight);
                    } else {
                        myPROCount_Cube_Weight.addPROCount_Cube_Weight(proCount, cube, weight);
                    }

                    continue;
                }

                Integer standardServiceTime = new Integer(val[54]);
                Integer actualServiceTime = new Integer(val[55]);
                Integer overServiceTime = new Integer(val[56]);

                PROCount_Cube_Weight myPROCount_Cube_Weight = myMap_toReturn.get(overServiceTime);
                if(myPROCount_Cube_Weight == null) {
                    myPROCount_Cube_Weight = new PROCount_Cube_Weight(proCount, cube, weight);
                    myMap_toReturn.put(overServiceTime, myPROCount_Cube_Weight);
                } else {
                    myPROCount_Cube_Weight.addPROCount_Cube_Weight(proCount, cube, weight);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myMap_toReturn;

    }

    public void read_file_AggShpmtFile(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        Map<String,Integer> myMapOfOffsetByFieldName = new HashMap<String, Integer>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            int recordCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {

                recordCounter++;

                String[] val = dbRecord.split(",");

                if(recordCounter == 1) {
                    // read the headers so that I know the offsets....

                    for(int i=0; i<val.length; i++) {
                        String myFieldName = val[i];
                        myMapOfOffsetByFieldName.put(myFieldName, i);
                    }

                    continue;
                }

                String origin = val[myMapOfOffsetByFieldName.get("CURR_SIC")];
                String originShift = val[myMapOfOffsetByFieldName.get("CURR_OP")];
                if(originShift.equals("1")) {
                    originShift = "O";
                } else if(originShift.equals("2")) {
                    originShift = "F";
                } else if(originShift.equals("3")) {
                    originShift = "I";
                }
                String destination = val[myMapOfOffsetByFieldName.get("FINAL_SIC")];
                String destinationShift = new String("I");
                Boolean isEarly = new Boolean(val[myMapOfOffsetByFieldName.get(" isEarly")]);
                Integer serviceLevel = new Integer(val[myMapOfOffsetByFieldName.get("DAYS TO DELIV")]);
                Integer certificationType = new Integer(val[myMapOfOffsetByFieldName.get("CERT_TYPE")]);
                Integer shipmentCount = new Integer(val[myMapOfOffsetByFieldName.get("PRO_COUNT")]);

                O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight = this.getO_OShift_Dest_Service__isEarly_cube_weight(
                        origin,
                        originShift,
                        destination,
                        destinationShift,
                        isEarly,
                        serviceLevel,
                        certificationType,
                        true
                );

                double cube_OD = new Double(val[myMapOfOffsetByFieldName.get("WEIGHT")]);
                double weight_OD = new Double(val[myMapOfOffsetByFieldName.get("CUBE")]);

                myO_OShift_Dest_Service__isEarly_cube_weight.setMyCube(cube_OD);
                myO_OShift_Dest_Service__isEarly_cube_weight.setMyWeight(weight_OD);

                myO_OShift_Dest_Service__isEarly_cube_weight.setShipmentCount(shipmentCount);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

    }

    public void read_file_certifiedHSSMoveLegs(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        Map<String,Integer> myMapOfOffsetByFieldName = new HashMap<String, Integer>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            int recordCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {

                recordCounter++;

                String[] val = dbRecord.split(",");

                if(recordCounter == 1) {
                    // read the headers so that I know the offsets....

                    for(int i=0; i<val.length; i++) {
                        String myFieldName = val[i];
                        myMapOfOffsetByFieldName.put(myFieldName, i);
                    }

                    continue;
                }

                String LoadLeg_origin = val[myMapOfOffsetByFieldName.get("#CURRENT_LOC")];
                String LoadLeg_originShift = val[myMapOfOffsetByFieldName.get("CURRENT_OP")];
                String LoadLeg_destination = val[myMapOfOffsetByFieldName.get("MOVE_TO_LOC")];
                String LoadLeg_destinationShift = val[myMapOfOffsetByFieldName.get("MOVE_TO_OP")];
                String LoadLeg_mode = val[myMapOfOffsetByFieldName.get("MOVE_MODE")];

                CharlieNetwork.LoadLeg myLoadLeg = this.getLoadLeg(
                        LoadLeg_origin,
                        LoadLeg_originShift,
                        LoadLeg_destination,
                        LoadLeg_destinationShift,
                        LoadLeg_mode,
                        true
                );

                int temp_a = 0;


            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


    }

    public void read_file_ODFlowDetails_diversionsFile(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        Map<String,Integer> myMapOfOffsetByFieldName = new HashMap<String, Integer>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            int recordCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {

                recordCounter++;

                String[] val = dbRecord.split(",");

                if(recordCounter == 1) {
                    // read the headers so that I know the offsets....

                    for(int i=0; i<val.length; i++) {
                        String myFieldName = val[i];
                        myMapOfOffsetByFieldName.put(myFieldName, i);
                    }

                    continue;
                }

                String origin = val[myMapOfOffsetByFieldName.get("Origin")];
                String originShift = val[myMapOfOffsetByFieldName.get("OriginShift")];
                String destination = val[myMapOfOffsetByFieldName.get("Destination")];
                String destinationShift = new String("I");
                Boolean isEarly = new Boolean(val[myMapOfOffsetByFieldName.get("isEarly")]);
                Integer serviceLevel = new Integer(val[myMapOfOffsetByFieldName.get("Service Level")]);
                Integer certificationType = new Integer(val[myMapOfOffsetByFieldName.get("certificationType")]);

                if(certificationType.intValue() == 0) {
                    // we are interested in non-normal shipments
                    continue;
                }

                O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight = this.getO_OShift_Dest_Service__isEarly_cube_weight(
                        origin,
                        originShift,
                        destination,
                        destinationShift,
                        isEarly,
                        serviceLevel,
                        certificationType,
                        true
                );

                String LoadLeg_origin = val[myMapOfOffsetByFieldName.get("LoadLeg: Origin")];
                String LoadLeg_originShift = val[myMapOfOffsetByFieldName.get("LoadLeg: OriginShift")];
                String LoadLeg_destination = val[myMapOfOffsetByFieldName.get("LoadLeg: Destination")];
                String LoadLeg_destinationShift = val[myMapOfOffsetByFieldName.get("LoadLeg: DestinationShift")];
                String LoadLeg_mode = val[myMapOfOffsetByFieldName.get("LoadLeg: Mode")];

                Double myCube = new Double(val[myMapOfOffsetByFieldName.get("cube_path")]);
                Double myWeight = new Double(val[myMapOfOffsetByFieldName.get("weight_path")]);

                if(LoadLeg_mode.equals(LHHssReportBuilder.L)) {
                    // we only care about HSS legs...
                    continue;
                }

                CharlieNetwork.LoadLeg myLoadLeg = this.getLoadLeg(
                        LoadLeg_origin,
                        LoadLeg_originShift,
                        LoadLeg_destination,
                        LoadLeg_destinationShift,
                        LoadLeg_mode,
                        false
                );

                if(myLoadLeg == null) {
                    continue;
                }

                Cube_Weight myCube_Weight = new Cube_Weight(myCube, myWeight);

                myLoadLeg.addOD(myO_OShift_Dest_Service__isEarly_cube_weight, myCube_Weight);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


    }

    public Map<String, Map<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>>> read_file_ODFlowDetails_diversionsFile_determineDiversionsIntoOBLocationsByDay(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        Map<String,Integer> myMapOfOffsetByFieldName = new HashMap<String, Integer>();

        Map<String, Map<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>>> myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation = new HashMap<String, Map<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>>>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            int recordCounter = 0;
            while ( (dbRecord = br.readLine()) != null) {

                recordCounter++;

                String[] val = dbRecord.split(",");

                if(recordCounter == 1) {
                    // read the headers so that I know the offsets....

                    for(int i=0; i<val.length; i++) {
                        String myFieldName = val[i];
                        myMapOfOffsetByFieldName.put(myFieldName, i);
                    }

                    continue;
                }

                String origin = val[myMapOfOffsetByFieldName.get("Origin")];
                String originShift = val[myMapOfOffsetByFieldName.get("OriginShift")];
                String destination = val[myMapOfOffsetByFieldName.get("Destination")];
                String destinationShift = new String("I");
                Boolean isEarly = new Boolean(val[myMapOfOffsetByFieldName.get("isEarly")]);
                Integer serviceLevel = new Integer(val[myMapOfOffsetByFieldName.get("Service Level")]);
                Integer certificationType = new Integer(val[myMapOfOffsetByFieldName.get("certificationType")]);

                double cube_OD = new Double(val[myMapOfOffsetByFieldName.get("OD Cube")]);
                double weight_OD = new Double(val[myMapOfOffsetByFieldName.get("OD Weight")]);

                O_OShift_Dest_Service__isEarly_cube_weight myO_OShift_Dest_Service__isEarly_cube_weight = this.getO_OShift_Dest_Service__isEarly_cube_weight(
                    origin,
                    originShift,
                    destination,
                    destinationShift,
                    isEarly,
                    serviceLevel,
                    certificationType,
                    true
                );

                myO_OShift_Dest_Service__isEarly_cube_weight.setMyCube(cube_OD);
                myO_OShift_Dest_Service__isEarly_cube_weight.setMyWeight(weight_OD);

                if(originShift.equals("O")) {
                    Integer originDay = 0;
                    Map<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>> myMap_outer = myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation.get(origin);
                    if(myMap_outer == null) {
                        myMap_outer = new HashMap<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>>();
                        myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation.put(origin, myMap_outer);
                    }
                    Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight> myMap_inner = myMap_outer.get(originDay);
                    if(myMap_inner == null) {
                        myMap_inner = new HashMap<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>();
                        myMap_outer.put(originDay, myMap_inner);
                    }
                    Cube_Weight myCube_Weight_existing = myMap_inner.get(myO_OShift_Dest_Service__isEarly_cube_weight);
                    if(myCube_Weight_existing == null) {
                        myMap_inner.put(myO_OShift_Dest_Service__isEarly_cube_weight, new Cube_Weight(cube_OD, weight_OD));
                    } else {
                        myCube_Weight_existing.addCube_Weight(cube_OD, weight_OD);
                    }
                }


                boolean isDefaultLoadPath = new Boolean(val[myMapOfOffsetByFieldName.get("isDefaultLoadPath")]);

                if(isDefaultLoadPath) {
                    // not interested in default paths....
                    continue;
                }

                String LoadLeg_origin = val[myMapOfOffsetByFieldName.get("LoadLeg: Origin")];
                Integer LoadLeg_originDay = new Integer(val[myMapOfOffsetByFieldName.get("LoadLeg: OriginDay")]);
                String LoadLeg_originShift = val[myMapOfOffsetByFieldName.get("LoadLeg: OriginShift")];

                String LoadLeg_dest = val[myMapOfOffsetByFieldName.get("LoadLeg: Destination")];
                Integer LoadLeg_destDay = new Integer(val[myMapOfOffsetByFieldName.get("LoadLeg: DestinationDay")]);
                String LoadLeg_destShift = val[myMapOfOffsetByFieldName.get("LoadLeg: DestinationShift")];

                boolean areWeInterestedInThisLoadLeg = true;
                if(LoadLeg_originDay.intValue() != 0) {
                    areWeInterestedInThisLoadLeg = false;
                }

                if(!LoadLeg_destShift.equals("O")) {
                    areWeInterestedInThisLoadLeg = false;
                }

                if(!areWeInterestedInThisLoadLeg) {
                    // only interested in load legs loading from the first day....to an OB shift
                    continue;
                }

                if(LoadLeg_destDay.intValue()>1) {
                    int temp_a = 0;
                }

                double cube_onPath = new Double(val[myMapOfOffsetByFieldName.get("cube_path")]);
                double weight_onPath = new Double(val[myMapOfOffsetByFieldName.get("weight_path")]);

                Map<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>> myMap_outer = myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation.get(LoadLeg_dest);
                if(myMap_outer == null) {
                    myMap_outer = new HashMap<Integer, Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>>();
                    myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation.put(LoadLeg_dest, myMap_outer);
                }
                Map<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight> myMap_inner = myMap_outer.get(LoadLeg_destDay);
                if(myMap_inner == null) {
                    myMap_inner = new HashMap<O_OShift_Dest_Service__isEarly_cube_weight, Cube_Weight>();
                    myMap_outer.put(LoadLeg_destDay, myMap_inner);
                }
                Cube_Weight myCube_Weight_existing = myMap_inner.get(myO_OShift_Dest_Service__isEarly_cube_weight);
                if(myCube_Weight_existing == null) {
                    myMap_inner.put(myO_OShift_Dest_Service__isEarly_cube_weight, new Cube_Weight(cube_onPath, weight_onPath));
                } else {
                    myCube_Weight_existing.addCube_Weight(cube_onPath, weight_onPath);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myMapOfMapOfMapOfCube_WeightByO_OShift_Dest_Service__isEarly_cube_weightByDayByOBLocation;

    }

    public HSSTrailerInformation read_trailers_HSS_AndTheirMatchingCMS_ifAny(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        int numHSSTrailer = 0;
        int numHSSWithGoodCMSMatch = 0;
        int numHSSWithAnyCMSMatch = 0;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                numHSSTrailer++;

                String[] val = dbRecord.split(",");
                String MatchToCMSPullReason = null;
                try {
                    MatchToCMSPullReason = val[14];
                } catch(Exception ex) {
                    // no entry in that position
                    continue;
                }

                String PullScac = null;
                try {
                    PullScac = val[15];
                } catch(Exception ex) {
                    // no entry in that position
                    int temp_a = 0;
                }

                boolean isMatchedToSomeCMS = false;
                if(PullScac != null && PullScac.length()>1) {
                    isMatchedToSomeCMS = true;
                }

                if(isMatchedToSomeCMS) {
                    numHSSWithAnyCMSMatch++;
                }

                boolean isGoodMatchToCMS = true;
                if(MatchToCMSPullReason != null && MatchToCMSPullReason.equals("null")) {
                    isGoodMatchToCMS = false;
                }

                if(isGoodMatchToCMS) {
                    numHSSWithGoodCMSMatch++;
                } else {
                    int temp_a = 0;
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        HSSTrailerInformation myHSSTrailerInformation = new HSSTrailerInformation(
            numHSSTrailer,
            numHSSWithGoodCMSMatch,
            numHSSWithAnyCMSMatch
        );

        return myHSSTrailerInformation;

    }

    public int readLoadPathFile(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        int numDirects = 0;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String loadPathType = "none specified";
                try {
                    loadPathType = val[13];
                } catch(Exception ex) {
                    // no entry in that position
                    continue;
                }

                if(loadPathType.equals("myLoadPath_OMTOrDirectString")) {
                    numDirects++;
                } else {
                    int temp_a = 0;
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return numDirects;

    }

    public void readLaneDiversions(String fileName,  Map<String, Set<laneDiversion>> myMapOfSetOfLaneDiversionByStringAggregate, Map<String, Set<laneDiversion>> myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");

                String RUN_ID = val[0];
                String CURR_SHIFT_CD = val[1];
                String CURR_SIC_CD = val[2];
                String SHIFT_SEQ_NBR = val[3];
                String DIVER_INSTR_SEQ_NBR = val[4];
                String ORGNL_LOAD_DEST_SIC_CD = val[5];
                String ORGNL_UNLOAD_SHIFT_CD = val[6];
                String ORGNL_MOVE_TO_SIC_CD = val[7];
                String ORGNL_FREIGHT_TYP_CD = val[8];
                String ORGNL_MOVE_MODE_CD = val[9];
                String DIVER_LOAD_DEST_SIC_CD = val[10];
                String DIVER_UNLOAD_SHIFT_CD = val[11];
                String DIVER_MOVE_TO_SIC_CD = val[12];
                String DIVER_FREIGHT_TYP_CD = val[13];
                String DIVER_MOVE_MODE_CD = val[14];
                String DIVER_CUBE_PCT = val[15];
                String DIVER_WGT_LBS = val[16];
                String DVRSN_TYP_CD = val[17];
                String hasLddkVol = val[18];

                laneDiversion myLaneDiversion = new laneDiversion(
                        RUN_ID,
                        CURR_SHIFT_CD,
                        CURR_SIC_CD,
                        SHIFT_SEQ_NBR,
                        DIVER_INSTR_SEQ_NBR,
                        ORGNL_LOAD_DEST_SIC_CD,
                        ORGNL_UNLOAD_SHIFT_CD,
                        ORGNL_MOVE_TO_SIC_CD,
                        ORGNL_FREIGHT_TYP_CD,
                        ORGNL_MOVE_MODE_CD,
                        DIVER_LOAD_DEST_SIC_CD,
                        DIVER_UNLOAD_SHIFT_CD,
                        DIVER_MOVE_TO_SIC_CD,
                        DIVER_FREIGHT_TYP_CD,
                        DIVER_MOVE_MODE_CD,
                        DIVER_CUBE_PCT,
                        DIVER_WGT_LBS,
                        DVRSN_TYP_CD,
                        hasLddkVol
                );

                {
                    String myString_Aggregate = myLaneDiversion.toString_aggregated();
                    Set<laneDiversion> mySet = myMapOfSetOfLaneDiversionByStringAggregate.get(myString_Aggregate);
                    if(mySet == null) {
                        mySet = new HashSet<laneDiversion>();
                        myMapOfSetOfLaneDiversionByStringAggregate.put(myString_Aggregate, mySet);
                    }
                    mySet.add(myLaneDiversion);
                }

                {
                    String myDIVER_INSTR_SEQ_NBR = myLaneDiversion.getDIVER_INSTR_SEQ_NBR();
                    Set<laneDiversion> mySet = myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR.get(myDIVER_INSTR_SEQ_NBR);
                    if(mySet == null) {
                        mySet = new HashSet<laneDiversion>();
                        myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR.put(myDIVER_INSTR_SEQ_NBR, mySet);
                    }
                    mySet.add(myLaneDiversion);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally


    }

    public void readPsblDestForDvrsn(String fileName, Map<String, Set<laneDiversion>> myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");

                String RUN_ID = val[0];
                String CURR_SHIFT_CD = val[1];
                String CURR_SIC_CD = val[2];
                String SHFT_SEQ_NBR = val[3];
                String DIVER_INSTR_SEQ_NBR = val[4];
                String DEST_SIC_CD = val[5];
                String ORIGL_MILES = val[6];
                String ORIGL_CURR_TO_DEST_SVC_DAYS = val[7];
                String DIVER_MILES = val[8];
                String DVRSN_CURR_TO_DEST_SVC_DAYS = val[9];
                String CERT_TYPE = val[10];
                String ORRPT_EARLY_ONLY = val[11];
                String ORRPT_LOAD_PATH_ID = val[12];
                String ORRPT_NUM_PROS = val[13];
                String ORRPT_CanUsePrimPath = val[14];

                psblDestForDvrsn my_psblDestForDvrsn = new psblDestForDvrsn(
                            RUN_ID,
                            CURR_SHIFT_CD,
                            CURR_SIC_CD,
                            SHFT_SEQ_NBR,
                            DIVER_INSTR_SEQ_NBR,
                            DEST_SIC_CD,
                            ORIGL_MILES,
                            ORIGL_CURR_TO_DEST_SVC_DAYS,
                            DIVER_MILES,
                            DVRSN_CURR_TO_DEST_SVC_DAYS,
                            CERT_TYPE,
                            ORRPT_EARLY_ONLY,
                            ORRPT_LOAD_PATH_ID,
                            ORRPT_NUM_PROS,
                            ORRPT_CanUsePrimPath
                        );


                Set<laneDiversion> mySetOfLaneDiversion = myMapOfSetOfLaneDiversionByDIVER_INSTR_SEQ_NBR.get(DIVER_INSTR_SEQ_NBR);

                if(mySetOfLaneDiversion == null) {
                    throw new Exception("No matching laneDiversion!!!");
                }

                if(mySetOfLaneDiversion.size()>1) {
                    throw new Exception("Multiple matching laneDiversion!!!");
                }

                for(laneDiversion my_laneDiversion : mySetOfLaneDiversion) {
                    my_laneDiversion.setMy_psblDestForDvrsn(my_psblDestForDvrsn);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

    }

    public SortedSet<String> readLaneDiversionsFileToGetSetOf_DIVER_INSTR_SEQ_NBR(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        SortedSet<String> myReturnSet = new TreeSet<String>();

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String DIVER_INSTR_SEQ_NBR = val[4];

                myReturnSet.add(DIVER_INSTR_SEQ_NBR);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return myReturnSet;

    }

    public void readLoadDiversionsFile(String fileName, SortedMap<String, SortedMap<String, DiversionSummary>> myMapBySicByShift, boolean isFewer) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originShift = val[1];
                String originSic = val[2];

                SortedMap<String, DiversionSummary> myMapBySic = myMapBySicByShift.get(originShift);

                if(myMapBySic == null) {
                    myMapBySic = new TreeMap<String, DiversionSummary>();
                    myMapBySicByShift.put(originShift, myMapBySic);
                }

                DiversionSummary myDiversionSummary = myMapBySic.get(originSic);

                if(myDiversionSummary==null) {
                    myDiversionSummary = new DiversionSummary();
                    if(isFewer) {
                        myDiversionSummary.incrementDiversion_fewer();
                    } else {
                        myDiversionSummary.incrementDiversion_more();
                    }
                    myMapBySic.put(originSic, myDiversionSummary);
                } else {
                    if(isFewer) {
                        myDiversionSummary.incrementDiversion_fewer();
                    } else {
                        myDiversionSummary.incrementDiversion_more();
                    }
                }


            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

    }

    public int readJamieFile(String fileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        int numDirectsUsed = 0;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originShift = val[1];
                String runType = val[13];

                if(!originShift.equals("O")) {
                    continue;
                }

                if(runType.equals("Direct")) {
                    numDirectsUsed++;
                } else {
                    int temp_a = 0;
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return numDirectsUsed;

    }

    public int readTimingFile(String timingFileName) throws Exception {

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(timingFileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            while ( (dbRecord = br.readLine()) != null) {

                boolean isThisTheRightLine = false;
                if(dbRecord.toLowerCase().contains("days")) {
                    isThisTheRightLine = true;
                }

                if(!isThisTheRightLine) {
                    continue;
                }


                String[] val = dbRecord.split(",");
                int days = this.getNumberFromSpaceDelimitedPairOfStrings(val[0]);
                int hours = this.getNumberFromSpaceDelimitedPairOfStrings(val[1]);
                int minutes = this.getNumberFromSpaceDelimitedPairOfStrings(val[2]);
                int seconds = this.getNumberFromSpaceDelimitedPairOfStrings(val[3]);

                return minutes*60+seconds;

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        throw new Exception("didn't find a number");

    }

    public void writeOMTDirectFile(String headerRow, String omt_DirectsFileName) throws Exception {

        String fileName = omt_DirectsFileName;
        FileWriter fstream = new FileWriter(fileName, false);
        PrintWriter myPrintWriter = new PrintWriter(fstream);

        myPrintWriter.println(headerRow);

        if(this.myMapOfOBLocMoveToLocFACByString != null) {
            for(String myOBLocMoveToLocFACString : this.myMapOfOBLocMoveToLocFACByString.keySet()) {
                OBLocMoveToLocFAC myOBLocMoveToLocFAC = this.myMapOfOBLocMoveToLocFACByString.get(myOBLocMoveToLocFACString);

                Set<String> mySetOfOMTLocations = myOBLocMoveToLocFAC.getMySetOfOMT();

                int seq_nbr = 0;
                for(String myOMTLocation : mySetOfOMTLocations) {
                    seq_nbr++;
                    StringBuffer myBuffer = new StringBuffer();
                    myBuffer.append(myOBLocMoveToLocFAC.printOutString_NetezzaCompatible()+",");
                    myBuffer.append(seq_nbr+",");
                    myBuffer.append(myOMTLocation+",");
                    myPrintWriter.println(myBuffer.toString());
                }

                // print out the OB location
                seq_nbr++;
                StringBuffer myBuffer = new StringBuffer();
                myBuffer.append(myOBLocMoveToLocFAC.printOutString_NetezzaCompatible()+",");
                myBuffer.append(seq_nbr+",");
                myBuffer.append(myOBLocMoveToLocFAC.getMyOBLocation()+",");
                myPrintWriter.println(myBuffer.toString());

                // print out the MoveTo location
                seq_nbr++;
                myBuffer = new StringBuffer();
                myBuffer.append(myOBLocMoveToLocFAC.printOutString_NetezzaCompatible()+",");
                myBuffer.append(seq_nbr+",");
                myBuffer.append(myOBLocMoveToLocFAC.getMyMoveToLocation()+",");
                myPrintWriter.println(myBuffer.toString());

            }
        }

        myPrintWriter.close();
        fstream.close();

    }

    public String readOMTFile(String omtFileName) throws Exception {

        String headerRow = null;

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(omtFileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));

            headerRow = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String myOBLocation = val[0];
                String myOBShift = val[1];
                String myMoveToLocation = val[2];
                String myMoveToShift = val[3];
                String myFACLocation = val[4];

                String myOMTLocation = val[6];

                OBLocMoveToLocFAC myOBLocMoveToLocFAC = this.getOBLocMoveToLocFAC(myOBLocation, myOBShift, myMoveToLocation, myMoveToShift, myFACLocation, true);
                myOBLocMoveToLocFAC.addOMT(myOMTLocation);

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return headerRow;

    }

    private int getNumberFromSpaceDelimitedPairOfStrings(String myString) throws Exception {
        String[] val = myString.split(" ");

        for(int offset=0; offset<val.length ; offset++) {
            String myString_current = val[offset];

            Integer myNumber = null;
            try {
                myNumber = new Integer(myString_current);
            } catch(Exception ex) {
                int temp_a = 0;
            }

            if(myNumber != null) {
                return myNumber;
            }

        }

        throw new Exception("couldn't tease out the number");

    }

    public void readAndGenerateSummaryViaStats(int id, String fileName) throws Exception {

        Map<String, Double> numberOfTractorsThroughViasByShiftCode = new HashMap<String, Double>();
        Map<String, Set<String>> mapOfSetOfUniqueViaLocationsByShiftCode = new HashMap<String, Set<String>>();

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originShift = val[1];
                boolean isThisLaunchingOffOBOrFAC = false;
                if(originShift != null && (originShift.equals("O") || originShift.equals("F"))) {
                    isThisLaunchingOffOBOrFAC = true;
                }

                if(!isThisLaunchingOffOBOrFAC) {
                    continue;
                }

                String viaLocation = val[4];

                boolean isThisGoingThroughVia = false;
                if(viaLocation != null && !viaLocation.equals("null")) {
                    isThisGoingThroughVia = true;
                }

                if(!isThisGoingThroughVia) {
                    continue;
                }

                Set<String> mySetOfUniqueViaLocations = mapOfSetOfUniqueViaLocationsByShiftCode.get(originShift);
                if(mySetOfUniqueViaLocations == null) {
                    mySetOfUniqueViaLocations = new HashSet<String>();
                    mapOfSetOfUniqueViaLocationsByShiftCode.put(originShift, mySetOfUniqueViaLocations);
                }
                mySetOfUniqueViaLocations.add(viaLocation);

                Double numTractors_total = 0.0;
                try {
                    numTractors_total = new Double(val[23]);
                } catch(Exception ex) {
                    // don't do anything
                    int temp_a = 0;
                }

                Double numTractors = numberOfTractorsThroughViasByShiftCode.get(originShift);
                if(numTractors == null) {
                    numberOfTractorsThroughViasByShiftCode.put(originShift, numTractors_total);
                } else {
                    numberOfTractorsThroughViasByShiftCode.put(originShift, numTractors+numTractors_total);
                }

            }
        }catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        List<String> myListOfShifts = new ArrayList<String>();
        myListOfShifts.add("O");
        myListOfShifts.add("F");

        for(String myOriginShift : myListOfShifts) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(id+",");
            buffer.append(myOriginShift+",");

            Double numTractors = numberOfTractorsThroughViasByShiftCode.get(myOriginShift);
            Set<String> mySetOfUniqueViaLocations = mapOfSetOfUniqueViaLocationsByShiftCode.get(myOriginShift);

            if(numTractors != null) {
                buffer.append(numTractors+",");
            } else {
                buffer.append(0.0+",");
            }

            if(mySetOfUniqueViaLocations != null) {
                buffer.append(mySetOfUniqueViaLocations.size()+",");
            } else {
                buffer.append(0.0+",");
            }

            System.out.println(buffer);
        }

    }

    public void readActuals(String fileName, String pref, boolean shouldWeCollectAggregateViaStatistics) throws Exception {
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            if(header.length()<1) {
                header = dbRecord;
            }

            while ( (dbRecord = br.readLine()) != null) {
                addLine(pref+dbRecord);

                if(!shouldWeCollectAggregateViaStatistics) {
                    continue;
                }

                String[] val = dbRecord.split(",");
                String originShift = val[1];
                if(originShift == null || !originShift.equals("O")) {
                    continue;
                }
                String originLocation = val[2];
                String viaLocation = val[4];

                if(viaLocation == null || viaLocation.equals("null")) {
                    continue;
                }

                String destinationLocation = val[9];
                Double numTractors_total = 0.0;
                try {
                    numTractors_total = new Double(val[23]);
                } catch(Exception ex) {
                    // don't do anything
                    int temp_a = 0;
                }

                OrigLocationViaLocationDestLocation myOrigLocationViaLocationDestLocation = this.getOrigLocationViaLocationDestLocation(originLocation, viaLocation, destinationLocation, true);

                HistoricalDouble_ModelDouble myHistoricalDouble_ModelDouble = this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.get(myOrigLocationViaLocationDestLocation);
                if(myHistoricalDouble_ModelDouble == null) {
                    this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.put(myOrigLocationViaLocationDestLocation, new HistoricalDouble_ModelDouble(0.0, numTractors_total));
                } else {
                    myHistoricalDouble_ModelDouble.addHistoricalDouble_ModelDouble(0.0, numTractors_total);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally
    }

    public void readActuals(String fileName, Map<String, Integer> mapOfNumHSSPullsByOrigLocOrigShiftDestLocDestShift_modeString, String extraHeader) throws Exception {
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            if(header.length()<1) {
                header = dbRecord+extraHeader;
            }

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String originLoc = val[5];
                String originShift = val[6];

                String destLoc = val[7];
                String destShift = val[8];

                String mode = val[9];

                OrigLocOrigShiftDestLocDestShift_mode myOrigLocOrigShiftDestLocDestShift_mode = new OrigLocOrigShiftDestLocDestShift_mode(
                        originLoc,
                        originShift,
                        null,
                        destLoc,
                        destShift,
                        mode
                );

                Integer actualNumHSS = mapOfNumHSSPullsByOrigLocOrigShiftDestLocDestShift_modeString.get(myOrigLocOrigShiftDestLocDestShift_mode.toString());
                if(actualNumHSS== null) {
                    actualNumHSS = 0;
                } else {
                    int temp_a = 0;
                }

                addLine(dbRecord+actualNumHSS);

            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally
    }

    public void readActuals_cms(String fileName, String id) throws Exception {
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            if(header.length()<1) {
                header = dbRecord;
            }

            while ( (dbRecord = br.readLine()) != null) {
                String[] val = dbRecord.split(",");
                String scacCode = val[25];
                if(scacCode.equals("CHR_Trucking_Inc")) {
                    continue;
                }

                addLine(dbRecord, id);

            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

    }

    public void readActuals(String fileName, String pref) throws Exception {
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            if(header.length()<1) {
                header = dbRecord;
            }

            while ( (dbRecord = br.readLine()) != null) {
                addLine(pref+dbRecord);

            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally
    }

    public void readActuals_addWeek(String fileName, String pref){
        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();
            if(header.length()<1) {
                header = dbRecord;
            }

            while ( (dbRecord = br.readLine()) != null) {
                String[] val = dbRecord.split(",");
                String myWeek = "Jul21";
                int myModelRunID = new Integer(val[0]).intValue();
                if(myModelRunID>=1301 && myModelRunID<=1317) {
                    myWeek = "Jul21";
                } else if(myModelRunID>=1321 && myModelRunID<=1333) {
                    myWeek = "Jul28";
                } else if(myModelRunID>=1341 && myModelRunID<=1357) {
                    myWeek = "Aug04";
                } else if(myModelRunID>=1361 && myModelRunID<=1379) {
                    myWeek = "Aug11";
                } else if(myModelRunID>=1387 && myModelRunID<=1399) {
                    myWeek = "Aug19";
                } else if(myModelRunID>=1403 && myModelRunID<=1419) {
                    myWeek = "Aug25";
                }

                addLine(pref+dbRecord+myWeek+",");
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally
    }

    public void addFile(String modelRunID, String myModelGrp, String Date, String file){
        readActuals(modelRunID, myModelGrp, Date, file);
    }

    public void addFile(String file, boolean shouldWeCollectAggregateViaStatistics) throws Exception {
        readActuals(file, "", shouldWeCollectAggregateViaStatistics);
    }

    public void addFile_addWeek(String file){
        readActuals_addWeek(file, "");
    }


    public void addFile(String file,String pref, boolean shouldWeCollectAggregateViaStatistics) throws Exception {
        readActuals(file, pref, shouldWeCollectAggregateViaStatistics);
    }

    public void addFile(String file) throws Exception {
        readActuals(file, "");
    }

    public void addFile_cms(String file, String id) throws Exception {
        readActuals_cms(file, id);
    }

    public void addFile(String file, Map<String, Integer> mapOfNumHSSPullsByOrigLocOrigShiftDestLocDestShift_modeString, String extraHeader) throws Exception {
        readActuals(file, mapOfNumHSSPullsByOrigLocOrigShiftDestLocDestShift_modeString, extraHeader);
    }

    public void addLine(String modelRunID, String myModelGrp, String Date, String line){

        String[] val = line.split(",");

        String origin_loc = val[1].trim();
        String via_loc = val[2].trim();
        String dest_loc = val[5].trim();

        String origin_type = this.myMapOfTypeBySic.get(origin_loc);
        String via_type = this.myMapOfTypeBySic.get(via_loc);
        String dest_type = this.myMapOfTypeBySic.get(dest_loc);

        StringBuffer buffer = new StringBuffer();
        buffer.append(modelRunID+",");
        buffer.append(myModelGrp+",");
        buffer.append(Date+",");
        buffer.append(origin_type+",");
        buffer.append(via_type+",");
        buffer.append(dest_type+",");
        buffer.append(line);
        logFile.println(buffer.toString());
    }

    public void addLine(String line){
        logFile.println(line);
    }

    public void addLine(String line, String id){
        logFile.println(line+","+id);
    }

    public void openFile(String dir, String name){
        try{
            log = new FileOutputStream( new File(dir+name), false);
            logFile = new PrintWriter(log);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void openFile_viaSummary(String dir, String name){
        try{
            log_viaSummary = new FileOutputStream( new File(dir+name), false);
            logFile_viaSummary = new PrintWriter(log_viaSummary);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    public void closeFile(){
        try{
            logFile.close();
            log.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void writeToFile_viaSummary() {

        logFile_viaSummary.println("Origin, Via, Destination, numTractors_hist, numTractors_hist(normalized), numTractors_model, numTractors_model(normalized)");

        double totalHistoricalTractors = 0.0;
        double totalModelTractors = 0.0;

        for(OrigLocationViaLocationDestLocation myOrigLocationViaLocationDestLocation : this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.keySet()) {
            HistoricalDouble_ModelDouble myHistoricalDouble_ModelDouble = this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.get(myOrigLocationViaLocationDestLocation);
            totalHistoricalTractors += myHistoricalDouble_ModelDouble.getMyHistoricalDouble();
            totalModelTractors += myHistoricalDouble_ModelDouble.getMyModelDouble();
        }


        for(OrigLocationViaLocationDestLocation myOrigLocationViaLocationDestLocation : this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.keySet()) {
            HistoricalDouble_ModelDouble myHistoricalDouble_ModelDouble = this.myHistoricalDouble_ModelDoubleByOrigLocationViaLocationDestLocation.get(myOrigLocationViaLocationDestLocation);
            double myHistoricalCount = myHistoricalDouble_ModelDouble.getMyHistoricalDouble();
            double myModelCount = myHistoricalDouble_ModelDouble.getMyModelDouble();
            logFile_viaSummary.println(myOrigLocationViaLocationDestLocation.toString()+","+myHistoricalCount+","+myHistoricalCount/(totalHistoricalTractors+LHHssReportBuilder.epsilon)+","+myModelCount+","+myModelCount/(totalModelTractors+LHHssReportBuilder.epsilon));
        }
    }

    public void closeFile_viaSummary(){
        try{
            logFile_viaSummary.close();
            log_viaSummary.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }




}


