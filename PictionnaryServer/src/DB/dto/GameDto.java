package DB.dto;

import java.sql.Time;

/**
 *
 * @author G43197
 */
public class GameDto {

    private final int gId;
    private final int gDrawer;
    private final int gPartner;
    private final Time gStartTime;
    private final Time gEndTime;
    private final int gStopPlayer;

    //TODO possibly another constructor
    public GameDto(int gId, int gDrawer, int gPartner, Time gStartTime, Time gEndTime, int gStopPlayer) {
        this.gId = gId;
        this.gDrawer = gDrawer;
        this.gPartner = gPartner;
        this.gStartTime = gStartTime;
        this.gEndTime = gEndTime;
        this.gStopPlayer = gStopPlayer;
    }

    public GameDto(int player, boolean isDrawer) {
        this(0, isDrawer ? player : 0, isDrawer ? 0 : player, null, null, 0);
    }

    public GameDto() {
        this(0, 0, 0, null, null, 0);
    }

    public int getId() {
        return gId;
    }

    public int getDrawer() {
        return gDrawer;
    }

    public int getPartner() {
        return gPartner;
    }

    public Time getStartTime() {
        return gStartTime;
    }

    public Time getEndTime() {
        return gEndTime;
    }

    public int getStopPlayer() {
        return gStopPlayer;
    }

}
