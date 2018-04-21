package message;

import message.util.User;

/**
 *
 * @author Philippe
 */
public class MessageJoin implements Message {

    private final String tableId;

    public MessageJoin(String tableId) {
        this.tableId = tableId;
    }

    @Override
    public Type getType() {
        return Type.JOIN;
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
        return tableId;
    }

}
