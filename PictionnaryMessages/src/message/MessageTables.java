package message;

import message.util.User;
import message.util.Tables;

/**
 *
 * @author Philippe
 */
public class MessageTables implements Message {

    private final Tables tables;

    public MessageTables(Tables tables) {
        this.tables = tables;
    }

    @Override
    public Type getType() {
        return Type.TABLES;
    }

    @Override
    public User getAuthor() {
        return User.ADMIN;
    }

    @Override
    public User getRecipient() {
        return User.EVERYBODY;
    }

    @Override
    public Object getContent() {
        return tables;
    }

}
