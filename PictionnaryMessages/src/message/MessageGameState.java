package message;

/**
 *
 * @author Philippe
 */
public class MessageGameState implements Message {

    private final String state;

    public MessageGameState(String state) {
        this.state = state;
    }

    @Override
    public Type getType() {
        return Type.GAME_STATE;
    }

    @Override
    public Object getContent() {
        return state;
    }

}
