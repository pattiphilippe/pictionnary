package message;


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
    public Object getContent() {
        return ex;
    }

}
