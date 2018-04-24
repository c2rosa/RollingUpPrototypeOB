import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by rosa.charles on 2/27/2018.
 */
public class SegmentationDeterminer {

    public static Integer MAX_SEGMENT = 32;

    public static String WEIGHT = "WEIGHT";
    public static String WEIGHTED_CLASS = "WEIGHTED_CLASS";
    public static String SIC_MILES = "SIC_MILES";
    public static String CUST_REV = "CUST_REV";

    public static String myFileNameOfSegmentDefinition = new String("SegmentDefinition.csv");

    public Map<Integer, Map<String, Range_Double>> myMapOfAttributeRangesBySegmentNumber = null;

    public Map<Integer, Boolean> myMapOfIsAMCBySegmentNumber = null;

    private void readSegmentDefinitionAndInstantiateThisClass(String my_inputFileSubDirectory) throws Exception {

        this.myMapOfIsAMCBySegmentNumber = new HashMap<Integer, Boolean>();

        this.myMapOfAttributeRangesBySegmentNumber = new HashMap<Integer, Map<String, Range_Double>>();

        File f = null;
        BufferedReader br = null;
        String dbRecord = null;
        try {
            String fileName = my_inputFileSubDirectory+SegmentationDeterminer.myFileNameOfSegmentDefinition;
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

                String SEGMENT_ID = val[myMapOfOffsetByColumnName.get("SEGMENT_ID")];
                String MIN_WEIGHT = val[myMapOfOffsetByColumnName.get("MIN_WEIGHT")];
                String MAX_WEIGHT = val[myMapOfOffsetByColumnName.get("MAX_WEIGHT")];
                String MIN_CLASS = val[myMapOfOffsetByColumnName.get("MIN_CLASS")];
                String MAX_CLASS = val[myMapOfOffsetByColumnName.get("MAX_CLASS")];
                String MIN_SIC_MILES = val[myMapOfOffsetByColumnName.get("MIN_SIC_MILES")];
                String MAX_SIC_MILES = val[myMapOfOffsetByColumnName.get("MAX_SIC_MILES")];
                String MIN_CUST_REV = val[myMapOfOffsetByColumnName.get("MIN_CUST_REV")];
                String MAX_CUST_REV = val[myMapOfOffsetByColumnName.get("MAX_CUST_REV")];
                String CDI = val[myMapOfOffsetByColumnName.get("CDI")];
                String r2 = val[myMapOfOffsetByColumnName.get("r2")];
                String AMC = val[myMapOfOffsetByColumnName.get("AMC")];

                Integer mySegment = new Integer(SEGMENT_ID);

                boolean isAMC = false;
                if(AMC.equals("Y")) {
                    isAMC = true;
                }

                this.myMapOfIsAMCBySegmentNumber.put(mySegment, isAMC);

                Map<String, Range_Double> myMap = this.myMapOfAttributeRangesBySegmentNumber.get(mySegment);
                if(myMap==null) {
                    myMap=new HashMap<String, Range_Double>();
                    this.myMapOfAttributeRangesBySegmentNumber.put(mySegment, myMap);
                }

                myMap.put(SegmentationDeterminer.WEIGHT, new Range_Double(new Double(MIN_WEIGHT), new Double(MAX_WEIGHT)));
                myMap.put(SegmentationDeterminer.WEIGHTED_CLASS, new Range_Double(new Double(MIN_CLASS), new Double(MAX_CLASS)));
                myMap.put(SegmentationDeterminer.SIC_MILES, new Range_Double(new Double(MIN_SIC_MILES), new Double(MAX_SIC_MILES)));
                myMap.put(SegmentationDeterminer.CUST_REV, new Range_Double(new Double(MIN_CUST_REV), new Double(MAX_CUST_REV)));


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

        int temp_a = 0;

    }

    public SegmentationDeterminer(String my_inputFileSubDirectory) throws Exception {

        this.readSegmentDefinitionAndInstantiateThisClass(my_inputFileSubDirectory);

    }

    public boolean isAMC(Integer mySegment) {
        boolean myAnswer = this.myMapOfIsAMCBySegmentNumber.get(mySegment);
        return myAnswer;
    }

    public Integer getSegment(Shipment myShipment) throws Exception {

        for(Integer mySegment : this.myMapOfAttributeRangesBySegmentNumber.keySet()) {
            Map<String, Range_Double> myMap = this.myMapOfAttributeRangesBySegmentNumber.get(mySegment);
            if(myMap==null) {
                continue;
            }

            Range_Double myRange_Double_weight = myMap.get(SegmentationDeterminer.WEIGHT);
            if(myRange_Double_weight==null) {
                continue;
            } else {
                if(!myRange_Double_weight.contains(myShipment.getMyWeight())) {
                    continue;
                }
            }

            Range_Double myRange_Double_WEIGHTED_CLASS = myMap.get(SegmentationDeterminer.WEIGHTED_CLASS);
            if(myRange_Double_WEIGHTED_CLASS==null) {
                continue;
            } else {
                if(!myRange_Double_WEIGHTED_CLASS.contains(myShipment.getMyWeightedClass())) {
                    continue;
                }
            }

            Range_Double myRange_Double_SIC_MILES = myMap.get(SegmentationDeterminer.SIC_MILES);
            if(myRange_Double_SIC_MILES==null) {
                continue;
            } else {
                if(!myRange_Double_SIC_MILES.contains(myShipment.getMySicMiles())) {
                    continue;
                }
            }

            Range_Double myRange_Double_CUST_REV = myMap.get(SegmentationDeterminer.CUST_REV);
            if(myRange_Double_CUST_REV==null) {
                continue;
            } else {
                if(!myRange_Double_CUST_REV.contains(myShipment.getMyCustRev())) {
                    continue;
                }
            }

            return mySegment;

        }


        if(1==1) {
            throw new Exception("shipment="+myShipment.getMyShptID()+" has no segment");
        }

        return null;

    }
}

