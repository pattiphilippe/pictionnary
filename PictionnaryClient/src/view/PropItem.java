package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import message.util.Prop;

/**
 *
 * @author G43197
 */
class PropItem {

    private final StringProperty prop;
    private final IntegerProperty count;

    public PropItem(Prop prop) {
        this.prop = new SimpleStringProperty();
        this.count = new SimpleIntegerProperty();
        this.prop.set(prop.getTxt());
        this.count.set(prop.getCount());
    }

    public int getCount() {
        return count.get();
    }

    public void setCount(int value) {
        count.set(value);
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public String getProp() {
        return prop.get();
    }

    public void setProp(String value) {
        prop.set(value);
    }

    public StringProperty propProperty() {
        return prop;
    }

}
