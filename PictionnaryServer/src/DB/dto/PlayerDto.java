package DB.dto;

/**
 *
 * @author G43197
 */
public class PlayerDto extends EntityDto<Integer> {

    private final String pName;

    public PlayerDto(int pId, String name) {
        this.id = pId;
        this.pName = name;
    }

    public PlayerDto(int pId) {
        this(pId, "");
    }

    public PlayerDto(String name) {
        this(0, name);
    }

    @Override
    public boolean isPersistant() {
        return id != 0;
    }

    public String getName() {
        return pName;
    }

}
