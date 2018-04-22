package message;

/**
 *
 * @author Philippe
 */
public class MessageGuess implements Message {

    private final String guess;

    public MessageGuess(String guess) {
        this.guess = guess;
    }

    @Override
    public Type getType() {
        return Type.GUESS;
    }

    @Override
    public Object getContent() {
        return guess;
    }

}
