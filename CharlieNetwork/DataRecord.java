package CharlieNetwork;

/**
 * Created by rosa.charles on 2/14/2018.
 */
public class DataRecord {


    private Integer PUR_SEQ_NBR = null;
    private String  DEST_SIC_CD = null;

    public DataRecord(
            Integer PUR_SEQ_NBR_input,
            String  DEST_SIC_CD_input
    ) {
        this.PUR_SEQ_NBR = PUR_SEQ_NBR_input;
        this.DEST_SIC_CD = DEST_SIC_CD_input;
    }
}
