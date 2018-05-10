package message.util;

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class Table implements Serializable {

    private final String tableId;
    //TODO delete isOpen in TAble
    private final String drawerName;
    private final String guesserName;
    private final GameState state;

    public Table(String tableId, GameState state, String... playerNames) {
        this.tableId = tableId;
        this.drawerName = playerNames[0];
        this.guesserName = playerNames[1];
        this.state = state;
    }

    public String getTableId() {
        return tableId;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public String getGuesserName() {
        return guesserName;
    }

    public GameState getState() {
        return state;
    }
}
