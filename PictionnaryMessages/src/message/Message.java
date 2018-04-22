package message;

import java.io.Serializable;

/**
 * The <code> Message </code> represents a general message send to a user.
 */
public interface Message extends Serializable {

    /**
     * Return the message type.
     *
     * @return the message type.
     */
    Type getType();

    /**
     * Return the message content.
     *
     * @return the message content.
     */
    Object getContent();

}
