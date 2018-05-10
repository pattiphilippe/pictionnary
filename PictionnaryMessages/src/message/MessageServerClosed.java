package message;

/**
 *
 * @author Philippe
 */
public class MessageServerClosed implements Message {

    //TODO class MessageNotif(Type type)
    public MessageServerClosed() {
    }

    @Override
    public Type getType() {
        return Type.SERVER_CLOSED;
    }

    @Override
    public Object getContent() {
        return null;
    }

}
