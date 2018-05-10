package message;

import message.util.GameState;

/**
 *
 * @author Philippe
 */
public class MessageGameState implements Message {

    private final GameState state;

    public MessageGameState(GameState state) {
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
