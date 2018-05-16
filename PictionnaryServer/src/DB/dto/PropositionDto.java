package DB.dto;

/**
 *
 * @author G43197
 */
public class PropositionDto extends EntityDto<Integer> {

    private final String txt;
    private final int gameId;
    private final int count;
    private final int avgWrongGuesses;

    public PropositionDto(int id, String text, int gameId, int count, int avgWrongGuesses) {
        this.id = id;
        this.txt = text;
        this.gameId = gameId;
        this.count = count;
        this.avgWrongGuesses = avgWrongGuesses;
    }

    public PropositionDto(int id, String text, int gameId) {
        this(id, text, gameId, 0, 0);
    }

    public PropositionDto(String text, int gameId, boolean gameIdOverCount) {
        this(0, text, gameId);
    }

    public PropositionDto(String text) {
        this(0, text, 0);
    }

    public PropositionDto(int id, boolean propIdOverPGameId) {
        this(propIdOverPGameId ? id : 0, null, propIdOverPGameId ? 0 : id);
    }

    public PropositionDto(int id, int avg) {
        this(id, null, 0, 0, avg);
    }

    public PropositionDto() {
        this(null);
    }

    @Override
    public boolean isPersistant() {
        return id != null && id != 0;
    }

    public String getTxt() {
        return txt;
    }

    public int getGameId() {
        return gameId;
    }

    public int getCount() {
        return count;
    }

    public int getAvgWrongGuesses() {
        return avgWrongGuesses;
    }

}
