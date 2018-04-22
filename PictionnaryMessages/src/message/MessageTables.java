package message;

import java.util.List;
import message.util.Table;

/**
 *
 * @author Philippe
 */
public class MessageTables implements Message {

    /**
     * UnmodifiableList of the tables
     */
    private final List<Table> tables;

    public MessageTables(List<Table> tables) {
        this.tables = tables;
    }

    @Override
    public Type getType() {
        return Type.TABLES;
    }

    @Override
    public Object getContent() {
        return tables;
    }

}
