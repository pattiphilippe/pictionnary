package message.util;

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class Table implements Serializable{
    
    private final String tableId;
    private final boolean isOpen;
    private final String drawerName;
    private final String guesserName;

    public Table(String tableId, boolean isOpen, String... playerNames) {
        this.tableId = tableId;
        this.isOpen = isOpen;
        this.drawerName = playerNames[0];
        this.guesserName = playerNames[1];
    }

    public String getTableId() {
        return tableId;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public String getGuesserName() {
        return guesserName;
    }
}
