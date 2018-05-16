package DB.dto;

import java.util.Date;

/**
 *
 * @author G43197
 */
public class GameDto extends EntityDto<Integer> {

    private final int gDrawer;
    private final int gPartner;
    private final Date gStartTime;
    private final Date gEndTime;
    private final int gStopPlayer;
    private final int playerIdSel;
    private final int gWord;
    private final String gTableName;

    public GameDto(int gId, int gDrawer, int gPartner, Date gStartTime, Date gEndTime, int gStopPlayer, int playerIdSel, int gWord, String gTableName) {
        id = gId;
        this.gDrawer = gDrawer;
        this.gPartner = gPartner;
        this.gStartTime = gStartTime;
        this.gEndTime = gEndTime;
        this.gStopPlayer = gStopPlayer;
        this.playerIdSel = playerIdSel;
        this.gWord = gWord;
        this.gTableName = gTableName;
    }

    public GameDto(int id, boolean gameIdOverPlayerIdSel) {
        this(gameIdOverPlayerIdSel ? id : 0, 0, 0, null, null, 0, gameIdOverPlayerIdSel ? 0 : id, 0, null);
    }

    public GameDto(int drawer, int guesser, Date gStartTime, int word, String tableName) {
        this(0, drawer, guesser, gStartTime, null, 0, 0, word, tableName);
    }

    public GameDto() {
        this(0, true);
    }

    @Override
    public boolean isPersistant() {
        return id != null && id != 0;
    }

    public int getDrawer() {
        return gDrawer;
    }

    public int getPartner() {
        return gPartner;
    }

    public Date getStartTime() {
        return gStartTime;
    }

    public Date getEndTime() {
        return gEndTime;
    }

    public int getStopPlayer() {
        return gStopPlayer;
    }

    public int getPlayerIdSel() {
        return playerIdSel;
    }

    public int getWord() {
        return gWord;
    }

    public String getTableName() {
        return gTableName;
    }

}
