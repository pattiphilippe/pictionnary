package message;

/**
 *
 * @author Philippe
 */
public class MessageClearDraw implements Message {

    @Override
    public Type getType() {
        return Type.CLEAR_DRAW;
    }

    @Override
    public Object getContent() {
        return null;
    }

}
