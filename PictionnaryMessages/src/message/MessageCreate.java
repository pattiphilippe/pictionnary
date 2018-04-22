package message;


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
    public Object getContent() {
        return tableId;
    }

}
