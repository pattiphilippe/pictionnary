package message;

import message.util.User;

/**
 *
 * @author Philippe
 */
public class MessageExit implements Message {

    public MessageExit() {
    }

    @Override
    public Type getType() {
        return Type.EXIT;
    }

    @Override
    public User getAuthor() {
        return User.ANYBODY;
    }

    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    @Override
    public Object getContent() {
        return null;
    }

}
