package message.util;

import java.io.Serializable;

/**
 *
 * @author G43197
 */
public class Prop implements Serializable {

    private final String prop;
    private final int count;

    public Prop(String prop, int count) {
        this.prop = prop;
        this.count = count;
    }

    public String getTxt() {
        return prop;
    }

    public int getCount() {
        return count;
    }

}
