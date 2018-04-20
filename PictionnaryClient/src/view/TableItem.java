package view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import message.util.Table;

/**
 *
 * @author Philippe
 */
public class TableItem {

    private final SimpleStringProperty tableId;
    private final SimpleBooleanProperty isOpen;
    private final SimpleStringProperty drawerName;
    private final SimpleStringProperty guesserName;

    public TableItem(Table table) {
        this.tableId = new SimpleStringProperty(table.getTableId());
        this.isOpen = new SimpleBooleanProperty(table.isOpen());
        this.drawerName = new SimpleStringProperty(table.getDrawerName());
        this.guesserName = new SimpleStringProperty(table.getGuesserName());
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

    @Override
    public String toString() {
        return "TableItem{" + "tableId=" + tableId + ", isOpen=" + isOpen + ", drawerName=" + drawerName + ", guesserName=" + guesserName + '}';
    }

}
