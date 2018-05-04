package DB.dto;

/**
 *
 * @author G43197
 */
public class PlayerDto {

    private final int pId;
    private final String pName;

    public PlayerDto(int pId, String name) {
        this.pId = pId;
        this.pName = name;
    }

    public PlayerDto(String name) {
        this(0, name);
    }

    public int getId() {
        return pId;
    }

    public String getName() {
        return pName;
    }

}
