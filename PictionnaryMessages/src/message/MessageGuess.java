package message;

import message.util.GuessUpdate;

/**
 *
 * @author Philippe
 */
public class MessageGuess implements Message {

    private final GuessUpdate guessUpdate;

    public MessageGuess(GuessUpdate guessUpdate) {
        this.guessUpdate = guessUpdate;
    }

    @Override
    public Type getType() {
        return Type.GUESS;
    }

    @Override
    public Object getContent() {
        return guessUpdate;
    }

}
