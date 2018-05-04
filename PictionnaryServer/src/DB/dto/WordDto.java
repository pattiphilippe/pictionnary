package DB.dto;

/**
 *
 * @author G43197
 */
public class WordDto {

    private final int wId;
    private final String wTxt;

    public WordDto(int id, String word) {
        this.wId = id;
        this.wTxt = word;
    }

    public WordDto(String word) {
        this(0, word);
    }

    public int getId() {
        return wId;
    }

    public String getTxt() {
        return wTxt;
    }
}
