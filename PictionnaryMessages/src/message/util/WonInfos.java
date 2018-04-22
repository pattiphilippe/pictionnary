package message.util;

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class WonInfos implements Serializable {

    private final String drawerName;
    private final String guesserName;
    private final String wordToGuess;
    private final int nbGuesses;

    public WonInfos(String drawerName, String guesserName, String wordToGuess, int nbGuesses) {
        this.drawerName = drawerName;
        this.guesserName = guesserName;
        this.wordToGuess = wordToGuess;
        this.nbGuesses = nbGuesses;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public String getGuesserName() {
        return guesserName;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public int getNbGuesses() {
        return nbGuesses;
    }
}
