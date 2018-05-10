package OneVOneModel;

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class GameException extends Exception implements Serializable {

    public GameException(String msg) {
        super(msg);
    }

}
