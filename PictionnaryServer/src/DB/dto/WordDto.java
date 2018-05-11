package DB.dto;

import java.util.Objects;

/**
 *
 * @author G43197
 */
public class WordDto extends EntityDto<Integer> {

    private final Integer wId;
    private final String wTxt;

    public WordDto(int id) {
        this(id, "");
    }

    public WordDto(String word) {
        this(0, word);
    }

    public WordDto(int id, String word) {
        this.wId = id;
        this.wTxt = word;
    }

    @Override
    public Integer getId() {
        return wId;
    }

    public String getTxt() {
        return wTxt;
    }

    @Override
    public boolean isPersistant() {
        return wId != 0;
    }

}
