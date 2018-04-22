package message;

/**
 *
 * @author Philippe
 */
public class MessageExitTable implements Message {

    public MessageExitTable() {
    }

    @Override
    public Type getType() {
        return Type.EXIT_TABLE;
    }

    @Override
    public Object getContent() {
        return null;
    }

}
