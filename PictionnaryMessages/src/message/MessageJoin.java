package message;


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
    public Object getContent() {
        return tableId;
    }

}
