package message.util;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author G43197
 */
public class GuessUpdate implements Serializable{
    
    private final String lastGuess;
    private final List<Prop> props;

    public GuessUpdate(String lastGuess, List<Prop> props) {
        this.lastGuess = lastGuess;
        this.props = props;
    }

    public String getLastGuess() {
        return lastGuess;
    }

    public List<Prop> getProps() {
        return props;
    }
    
}
