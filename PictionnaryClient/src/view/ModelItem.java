package view;

import Model.Model;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Philippe
 */
class ModelItem {

    private final SimpleStringProperty tableId;
    private final SimpleBooleanProperty isOpen;
    private final SimpleStringProperty drawerName;
    private final SimpleStringProperty guesserName;

    public ModelItem(Model table) {
        this.tableId = new SimpleStringProperty(table.getId());
        this.isOpen = new SimpleBooleanProperty(table.isOpen());
        String[] playerNames = table.getPlayers();
        if (playerNames[1] == null) {
            playerNames[1] = "";
        }
        this.drawerName = new SimpleStringProperty(playerNames[0]);
        this.guesserName = new SimpleStringProperty(playerNames[1]);
    }

    public String getTableId() {
        return tableId.get();
    }

    public void setTableId(String tableId) {
        this.tableId.set(tableId);
    }

    public boolean isOpen() {
        return isOpen.get();
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen.set(isOpen);
    }

    public String getDrawerName() {
        return drawerName.get();
    }

    public void setDrawerName(String drawerName) {
        this.drawerName.set(drawerName);
    }

    public String getGuesserName() {
        return guesserName.get();
    }

    public void setGuesserName(String guesserName) {
        this.drawerName.set(guesserName);
    }

    public SimpleStringProperty tableIdProperty() {
        return tableId;
    }

    public SimpleBooleanProperty isOpenProperty() {
        return isOpen;
    }

    public SimpleStringProperty drawerNameProperty() {
        return drawerName;
    }

    public SimpleStringProperty guesserNameProperty() {
        return guesserName;
    }

}
