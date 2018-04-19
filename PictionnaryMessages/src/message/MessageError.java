package message;

import message.util.User;

/**
 *
 * @author Philippe
 */
public class MessageError implements Message {

    private final Throwable ex;

    public MessageError(Throwable ex) {
        this.ex = ex;
    }

    @Override
    public Type getType() {
        return Type.ERROR;
    }

    @Override
    public User getAuthor() {
        return User.ANYBODY;
    }

    @Override
    public User getRecipient() {
        return User.ANYBODY;
    }

    @Override
    public Object getContent() {
        return ex;
    }

}
