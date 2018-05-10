package OneVOneModel;

import java.util.List;
import java.util.Observable;

/**
 *
 * @author Philippe
 */
public abstract class Model extends Observable {

    /**
     * Returns the id of the model.
     *
     * @return
     */
    public abstract String getId();

    /**
     * Returns true if this table has the given id.
     *
     * @param tableId
     * @return
     */
    public abstract boolean is(String tableId);

    /**
     * Returns the current state of the table.
     *
     * @return
     */
    public abstract GameState getState();

    /**
     * Returns all the previous guesses.
     *
     * @return
     */
    public abstract List<String> getGuesses();

    /**
     * Returns the array containing the player names on the table. There is a
     * maximum of 2 players on a table and a minimum of 1.
     *
     * @return
     */
    public abstract String[] getPlayerNames();

    /**
     * Returns the word to guess.
     *
     * @return
     */
    public abstract String getWordToGuess();

    /**
     * Adds a guesser to the table.
     *
     * @param guesser
     * @throws Exception if the table is not open
     */
    public abstract void addGuesser(Player guesser) throws GameException;

    /**
     * Returns true if the given player is on the table.
     *
     * @param player
     * @return
     */
    public abstract boolean isOnTable(Player player);

    /**
     * Removes a player of the table. Notifies the view of it.
     *
     * @param player
     * @return
     * @throws Exception if the player is not on the table
     */
    public abstract void removePlayer(Player player) throws GameException;

    /**
     * Try to guess the secret word.
     *
     * @param player
     * @param guess
     * @throws Model.GameException if player isn't the guesser
     */
    public abstract void guess(Player player, String guess) throws GameException;

    /**
     * Returns true if there's no player on the table.
     *
     * @return
     */
    public abstract boolean isEmpty();

    /**
     * Returns the last guess.
     *
     * @return
     */
    public abstract String getLastGuess();
}
