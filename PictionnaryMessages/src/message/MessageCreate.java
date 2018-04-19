package message;

import message.util.User;

/**
 *
 * @author Philippe
 */
public class MessageCreate implements Message {

    private final String tableId;

    public MessageCreate(String tableId) {
        this.tableId = tableId;
    }

    @Override
    public Type getType() {
        return Type.CREATE;
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
