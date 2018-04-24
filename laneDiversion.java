/**
 * Created by rosa.charles on 3/26/15.
 */
public class laneDiversion {

    private String RUN_ID = null;
    private String CURR_SHIFT_CD = null;
    private String CURR_SIC_CD = null;
    private String SHIFT_SEQ_NBR = null;
    private String DIVER_INSTR_SEQ_NBR = null;
    private String ORGNL_LOAD_DEST_SIC_CD = null;
    private String ORGNL_UNLOAD_SHIFT_CD = null;
    private String ORGNL_MOVE_TO_SIC_CD = null;
    private String ORGNL_FREIGHT_TYP_CD = null;
    private String ORGNL_MOVE_MODE_CD = null;
    private String DIVER_LOAD_DEST_SIC_CD = null;
    private String DIVER_UNLOAD_SHIFT_CD = null;
    private String DIVER_MOVE_TO_SIC_CD = null;
    private String DIVER_FREIGHT_TYP_CD = null;
    private String DIVER_MOVE_MODE_CD = null;
    private String DIVER_CUBE_PCT = null;
    private String DIVER_WGT_LBS = null;
    private String DVRSN_TYP_CD = null;
    private String hasLddkVol = null;

    private psblDestForDvrsn my_psblDestForDvrsn = null;

    public laneDiversion(
            String RUN_ID_input,
            String CURR_SHIFT_CD_input,
            String CURR_SIC_CD_input,
            String SHIFT_SEQ_NBR_input,
            String DIVER_INSTR_SEQ_NBR_input,
            String ORGNL_LOAD_DEST_SIC_CD_input,
            String ORGNL_UNLOAD_SHIFT_CD_input,
            String ORGNL_MOVE_TO_SIC_CD_input,
            String ORGNL_FREIGHT_TYP_CD_input,
            String ORGNL_MOVE_MODE_CD_input,
            String DIVER_LOAD_DEST_SIC_CD_input,
            String DIVER_UNLOAD_SHIFT_CD_input,
            String DIVER_MOVE_TO_SIC_CD_input,
            String DIVER_FREIGHT_TYP_CD_input,
            String DIVER_MOVE_MODE_CD_input,
            String DIVER_CUBE_PCT_input,
            String DIVER_WGT_LBS_input,
            String DVRSN_TYP_CD_input,
            String hasLddkVol_input
    ) {
        this.RUN_ID = RUN_ID_input;
        this.CURR_SHIFT_CD = CURR_SHIFT_CD_input;
        this.CURR_SIC_CD = CURR_SIC_CD_input;
        this.SHIFT_SEQ_NBR = SHIFT_SEQ_NBR_input;
        this.DIVER_INSTR_SEQ_NBR = DIVER_INSTR_SEQ_NBR_input;
        this.ORGNL_LOAD_DEST_SIC_CD = ORGNL_LOAD_DEST_SIC_CD_input;
        this.ORGNL_UNLOAD_SHIFT_CD = ORGNL_UNLOAD_SHIFT_CD_input;
        this.ORGNL_MOVE_TO_SIC_CD = ORGNL_MOVE_TO_SIC_CD_input;
        this.ORGNL_FREIGHT_TYP_CD = ORGNL_FREIGHT_TYP_CD_input;
        this.ORGNL_MOVE_MODE_CD = ORGNL_MOVE_MODE_CD_input;
        this.DIVER_LOAD_DEST_SIC_CD = DIVER_LOAD_DEST_SIC_CD_input;
        this.DIVER_UNLOAD_SHIFT_CD = DIVER_UNLOAD_SHIFT_CD_input;
        this.DIVER_MOVE_TO_SIC_CD = DIVER_MOVE_TO_SIC_CD_input;
        this.DIVER_FREIGHT_TYP_CD = DIVER_FREIGHT_TYP_CD_input;
        this.DIVER_MOVE_MODE_CD = DIVER_MOVE_MODE_CD_input;
        this.DIVER_CUBE_PCT = DIVER_CUBE_PCT_input;
        this.DIVER_WGT_LBS = DIVER_WGT_LBS_input;
        this.DVRSN_TYP_CD = DVRSN_TYP_CD_input;
        this.hasLddkVol = hasLddkVol_input;
    }

    public static String myHeader =
                "RUN_ID,"+
                "CURR_SHIFT_CD,"+
                "CURR_SIC_CD,"+
                "SHIFT_SEQ_NBR,"+
                "DIVER_INSTR_SEQ_NBR,"+
                "ORGNL_LOAD_DEST_SIC_CD,"+
                "ORGNL_UNLOAD_SHIFT_CD,"+
                "ORGNL_MOVE_TO_SIC_CD,"+
                "ORGNL_FREIGHT_TYP_CD,"+
                "ORGNL_MOVE_MODE_CD,"+
                "DIVER_LOAD_DEST_SIC_CD,"+
                "DIVER_UNLOAD_SHIFT_CD,"+
                "DIVER_MOVE_TO_SIC_CD,"+
                "DIVER_FREIGHT_TYP_CD,"+
                "DIVER_MOVE_MODE_CD,"+
                "DIVER_CUBE_PCT,"+
                "DIVER_WGT_LBS,"+
                "DVRSN_TYP_CD,"+
                "hasLddkVol"
            ;

    public psblDestForDvrsn getMy_psblDestForDvrsn() {
        return my_psblDestForDvrsn;
    }

    public void setMy_psblDestForDvrsn(psblDestForDvrsn my_psblDestForDvrsn_input) {

        if(!this.getDIVER_INSTR_SEQ_NBR().equals(my_psblDestForDvrsn_input.getDIVER_INSTR_SEQ_NBR())) {
            return;
        }

        this.my_psblDestForDvrsn = my_psblDestForDvrsn_input;
    }


    public String getRUN_ID() {
        return RUN_ID;
    }

    public String getCURR_SHIFT_CD() {
        return CURR_SHIFT_CD;
    }

    public String getCURR_SIC_CD() {
        return CURR_SIC_CD;
    }

    public String getSHIFT_SEQ_NBR() {
        return SHIFT_SEQ_NBR;
    }

    public String getDIVER_INSTR_SEQ_NBR() {
        return DIVER_INSTR_SEQ_NBR;
    }

    public String getORGNL_LOAD_DEST_SIC_CD() {
        return ORGNL_LOAD_DEST_SIC_CD;
    }

    public String getORGNL_UNLOAD_SHIFT_CD() {
        return ORGNL_UNLOAD_SHIFT_CD;
    }

    public String getORGNL_MOVE_TO_SIC_CD() {
        return ORGNL_MOVE_TO_SIC_CD;
    }

    public String getORGNL_FREIGHT_TYP_CD() {
        return ORGNL_FREIGHT_TYP_CD;
    }

    public String getORGNL_MOVE_MODE_CD() {
        return ORGNL_MOVE_MODE_CD;
    }

    public String getDIVER_LOAD_DEST_SIC_CD() {
        return DIVER_LOAD_DEST_SIC_CD;
    }

    public String getDIVER_UNLOAD_SHIFT_CD() {
        return DIVER_UNLOAD_SHIFT_CD;
    }

    public String getDIVER_MOVE_TO_SIC_CD() {
        return DIVER_MOVE_TO_SIC_CD;
    }

    public String getDIVER_FREIGHT_TYP_CD() {
        return DIVER_FREIGHT_TYP_CD;
    }

    public String getDIVER_MOVE_MODE_CD() {
        return DIVER_MOVE_MODE_CD;
    }

    public String getDIVER_CUBE_PCT() {
        return DIVER_CUBE_PCT;
    }

    public String getDIVER_WGT_LBS() {
        return DIVER_WGT_LBS;
    }

    public String getDVRSN_TYP_CD() {
        return DVRSN_TYP_CD;
    }

    public String getHasLddkVol() {
        return hasLddkVol;
    }

    public String toString_aggregated() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.RUN_ID+",");
        buffer.append(this.CURR_SHIFT_CD+",");
        buffer.append(this.CURR_SIC_CD+",");
        buffer.append(this.SHIFT_SEQ_NBR+",");
        //buffer.append(this.DIVER_INSTR_SEQ_NBR+",");
        buffer.append(this.ORGNL_LOAD_DEST_SIC_CD+",");
        buffer.append(this.ORGNL_UNLOAD_SHIFT_CD+",");
        buffer.append(this.ORGNL_MOVE_TO_SIC_CD+",");
        buffer.append(this.ORGNL_FREIGHT_TYP_CD+",");
        buffer.append(this.ORGNL_MOVE_MODE_CD+",");
        buffer.append(this.DIVER_LOAD_DEST_SIC_CD+",");
        buffer.append(this.DIVER_UNLOAD_SHIFT_CD+",");
        buffer.append(this.DIVER_MOVE_TO_SIC_CD+",");
        buffer.append(this.DIVER_FREIGHT_TYP_CD+",");
        buffer.append(this.DIVER_MOVE_MODE_CD+",");
        //buffer.append(this.DIVER_CUBE_PCT+",");
        //buffer.append(this.DIVER_WGT_LBS+",");
        buffer.append(this.DVRSN_TYP_CD+",");
        buffer.append(this.hasLddkVol+",");

        return buffer.toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.RUN_ID+",");
        buffer.append(this.CURR_SHIFT_CD+",");
        buffer.append(this.CURR_SIC_CD+",");
        buffer.append(this.SHIFT_SEQ_NBR+",");
        buffer.append(this.DIVER_INSTR_SEQ_NBR+",");
        buffer.append(this.ORGNL_LOAD_DEST_SIC_CD+",");
        buffer.append(this.ORGNL_UNLOAD_SHIFT_CD+",");
        buffer.append(this.ORGNL_MOVE_TO_SIC_CD+",");
        buffer.append(this.ORGNL_FREIGHT_TYP_CD+",");
        buffer.append(this.ORGNL_MOVE_MODE_CD+",");
        buffer.append(this.DIVER_LOAD_DEST_SIC_CD+",");
        buffer.append(this.DIVER_UNLOAD_SHIFT_CD+",");
        buffer.append(this.DIVER_MOVE_TO_SIC_CD+",");
        buffer.append(this.DIVER_FREIGHT_TYP_CD+",");
        buffer.append(this.DIVER_MOVE_MODE_CD+",");
        buffer.append(this.DIVER_CUBE_PCT+",");
        buffer.append(this.DIVER_WGT_LBS+",");
        buffer.append(this.DVRSN_TYP_CD+",");
        buffer.append(this.hasLddkVol+",");

        return buffer.toString();
    }


}
