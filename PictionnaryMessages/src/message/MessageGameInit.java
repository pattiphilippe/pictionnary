package message;


/**
 *
 * @author Philippe
 */
public class MessageGameInit implements Message {

    private final String wordToGuess;

    public MessageGameInit(String wordToGuess) {
        this.wordToGuess = wordToGuess;
    }

    @Override
    public Type getType() {
        return Type.GAME_INIT;
    }

    @Override
    public Object getContent() {
        return wordToGuess;
    }

}
