package message;

import message.util.Stats;

/**
 *
 * @author Philippe
 */
public class MessageStats implements Message {

    private final Stats stats;

    public MessageStats(Stats stats) {
        this.stats = stats;
    }

    @Override
    public Type getType() {
        return Type.STATS;
    }

    @Override
    public Object getContent() {
        return stats;
    }

}
