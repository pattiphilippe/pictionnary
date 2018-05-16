package message.util;

import java.io.Serializable;

/**
 *
 * @author G43197
 */
public class InitInfos implements Serializable {

    private final String wordToGuess;
    private final int avgWrongGuesses;

    public InitInfos(String wordToGuess, int avgWrongGuesses) {
        this.wordToGuess = wordToGuess;
        this.avgWrongGuesses = avgWrongGuesses;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public int getAvgWrongGuesses() {
        return avgWrongGuesses;
    }

}
