package message;

import message.util.WonInfos;

/**
 *
 * @author Philippe
 */
public class MessageWon implements Message {

    private final WonInfos wonInfos;

    public MessageWon(String drawerName, String guesserName, String wordToGuess, int nbGuesses) {
        this.wonInfos = new WonInfos(drawerName, guesserName, wordToGuess, nbGuesses);
    }

    @Override
    public Type getType() {
        return Type.WON;
    }

    @Override
    public Object getContent() {
        return wonInfos;
    }

}
