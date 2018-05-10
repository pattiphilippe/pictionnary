package message.util;

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class Table implements Serializable {

    private final String tableId;
    //TODO delete isOpen
    private final String drawerName;
    private final String guesserName;
    private final String state;

    public Table(String tableId, String state, String... playerNames) {
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

    public String getState() {
        return state;
    }
}
