package message;

import message.util.User;

/**
 *
 * @author Philippe
 */
public class MessageProfile implements Message {

    private final String name;

    public MessageProfile(String name) {
        this.name = name;
    }

    @Override
    public Type getType() {
        return Type.PROFILE;
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
        return name;
    }

}
