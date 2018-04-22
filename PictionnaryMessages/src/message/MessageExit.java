package message;


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
    public Object getContent() {
        return null;
    }

}
