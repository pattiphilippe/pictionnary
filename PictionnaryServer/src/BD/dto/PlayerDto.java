package BD.dto;

/**
 *
 * @author G43197
 */
public class PlayerDto {
    private final int pId;
    private final String pName;

    public PlayerDto(int pId, String pName) {
        this.pId = pId;
        this.pName = pName;
    }

    public int getpId() {
        return pId;
    }

    public String getpName() {
        return pName;
    }
    
    
}
