package BD.dto;

/**
 *
 * @author G43197
 */
public class SequencesDto {
    
    private final String sId;
    private final int sValue;

    public SequencesDto(String sId, int sValue) {
        this.sId = sId;
        this.sValue = sValue;
    }

    public String getsId() {
        return sId;
    }

    public int getsValue() {
        return sValue;
    }
    
    
}
