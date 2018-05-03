package BD.dto;

/**
 *
 * @author G43197
 */
public class WordDto {
    private final int wId;
    private final String wTxt;

    public WordDto(int wId, String wTxt) {
        this.wId = wId;
        this.wTxt = wTxt;
    }

    public int getwId() {
        return wId;
    }

    public String getwTxt() {
        return wTxt;
    }
}
