package message;

import message.util.User;

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
    public User getAuthor() {
        return User.ADMIN;
    }

    @Override
    public User getRecipient() {
        return User.ANYBODY;
    }

    @Override
    public Object getContent() {
        return wordToGuess;
    }

}
