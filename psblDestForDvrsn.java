/**
 * Created by rosa.charles on 3/26/15.
 */
public class psblDestForDvrsn {

    private String RUN_ID = null;
    private String CURR_SHIFT_CD = null;
    private String CURR_SIC_CD = null;
    private String SHFT_SEQ_NBR = null;
    private String DIVER_INSTR_SEQ_NBR = null;
    private String DEST_SIC_CD = null;
    private String ORIGL_MILES = null;
    private String ORIGL_CURR_TO_DEST_SVC_DAYS = null;
    private String DIVER_MILES = null;
    private String DVRSN_CURR_TO_DEST_SVC_DAYS = null;
    private String CERT_TYPE = null;
    private String ORRPT_EARLY_ONLY = null;
    private String ORRPT_LOAD_PATH_ID = null;
    private String ORRPT_NUM_PROS = null;
    private String ORRPT_CanUsePrimPath = null;

    public psblDestForDvrsn(
            String RUN_ID_input,
            String CURR_SHIFT_CD_input,
            String CURR_SIC_CD_input,
            String SHFT_SEQ_NBR_input,
            String DIVER_INSTR_SEQ_NBR_input,
            String DEST_SIC_CD_input,
            String ORIGL_MILES_input,
            String ORIGL_CURR_TO_DEST_SVC_DAYS_input,
            String DIVER_MILES_input,
            String DVRSN_CURR_TO_DEST_SVC_DAYS_input,
            String CERT_TYPE_input,
            String ORRPT_EARLY_ONLY_input,
            String ORRPT_LOAD_PATH_ID_input,
            String ORRPT_NUM_PROS_input,
            String ORRPT_CanUsePrimPath_input
    ) {
        this.RUN_ID = RUN_ID_input;
        this.CURR_SHIFT_CD = CURR_SHIFT_CD_input;
        this.CURR_SIC_CD = CURR_SIC_CD_input;
        this.SHFT_SEQ_NBR = SHFT_SEQ_NBR_input;
        this.DIVER_INSTR_SEQ_NBR = DIVER_INSTR_SEQ_NBR_input;
        this.DEST_SIC_CD = DEST_SIC_CD_input;
        this.ORIGL_MILES = ORIGL_MILES_input;
        this.ORIGL_CURR_TO_DEST_SVC_DAYS = ORIGL_CURR_TO_DEST_SVC_DAYS_input;
        this.DIVER_MILES = DIVER_MILES_input;
        this.DVRSN_CURR_TO_DEST_SVC_DAYS = DVRSN_CURR_TO_DEST_SVC_DAYS_input;
        this.CERT_TYPE = CERT_TYPE_input;
        this.ORRPT_EARLY_ONLY = ORRPT_EARLY_ONLY_input;
        this.ORRPT_LOAD_PATH_ID = ORRPT_LOAD_PATH_ID_input;
        this.ORRPT_NUM_PROS = ORRPT_NUM_PROS_input;
        this.ORRPT_CanUsePrimPath = ORRPT_CanUsePrimPath_input;
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

    public String getSHFT_SEQ_NBR() {
        return SHFT_SEQ_NBR;
    }

    public String getDIVER_INSTR_SEQ_NBR() {
        return DIVER_INSTR_SEQ_NBR;
    }

    public String getDEST_SIC_CD() {
        return DEST_SIC_CD;
    }

    public String getORIGL_MILES() {
        return ORIGL_MILES;
    }

    public String getORIGL_CURR_TO_DEST_SVC_DAYS() {
        return ORIGL_CURR_TO_DEST_SVC_DAYS;
    }

    public String getDIVER_MILES() {
        return DIVER_MILES;
    }

    public String getDVRSN_CURR_TO_DEST_SVC_DAYS() {
        return DVRSN_CURR_TO_DEST_SVC_DAYS;
    }

    public String getCERT_TYPE() {
        return CERT_TYPE;
    }

    public String getORRPT_EARLY_ONLY() {
        return ORRPT_EARLY_ONLY;
    }

    public String getORRPT_LOAD_PATH_ID() {
        return ORRPT_LOAD_PATH_ID;
    }

    public String getORRPT_NUM_PROS() {
        return ORRPT_NUM_PROS;
    }

    public String getORRPT_CanUsePrimPath() {
        return ORRPT_CanUsePrimPath;
    }

}
