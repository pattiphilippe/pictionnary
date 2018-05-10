package view;

import javafx.beans.property.SimpleStringProperty;
import message.util.Table;

/**
 *
 * @author Philippe
 */
public class TableItem {

    private final SimpleStringProperty tableId;
    private final SimpleStringProperty state;
    private final SimpleStringProperty drawerName;
    private final SimpleStringProperty guesserName;

    public TableItem(Table table) {
        this.tableId = new SimpleStringProperty(table.getTableId());
        this.state = new SimpleStringProperty(table.getState().toString());
        this.drawerName = new SimpleStringProperty(table.getDrawerName());
        this.guesserName = new SimpleStringProperty(table.getGuesserName());
    }

    public String getTableId() {
        return tableId.get();
    }

    public void setTableId(String tableId) {
        this.tableId.set(tableId);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
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

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public SimpleStringProperty drawerNameProperty() {
        return drawerName;
    }

    public SimpleStringProperty guesserNameProperty() {
        return guesserName;
    }

    @Override
    public String toString() {
        return "TableItem{" + "tableId=" + tableId + ", state=" + state + ", drawerName=" + drawerName + ", guesserName=" + guesserName + '}';
    }

}
