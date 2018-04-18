package Model;

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
    public abstract int getId();

    /**
     * Returns the state of the game.
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
     * Returns true if the table is open. If the table is open, a player can
     * join it.
     *
     * @return
     */
    public abstract boolean isOpen();

    /**
     * Returns the array containing the player names on the table. There is a
     * maximum of 2 players on a table and a minimum of 1.
     *
     * @return
     */
    abstract String[] getPlayers();

    /**
     * Adds a guesser to the table.
     *
     * @param guesser
     * @throws Exception if the table is not open
     */
    abstract void addGuesser(Player guesser) throws GameException;

    /**
     * Returns true if the given player is on the table.
     *
     * @param player
     * @return
     */
    //TODO REMOVE AND WORK WITH TABLE IDS, PLAYER SHOULD ONLY HAVE A USERNAME, NO ID
    public abstract boolean isOnTable(Player player);

    /**
     * Removes a player of the table. Notifies the view of it.
     *
     * @param player
     * @return
     * @throws Exception if the player is not on the table
     */
    abstract void removePlayer(Player player) throws GameException;

    /**
     * Try to guess the secret word.
     *
     * @param player
     * @param guess
     * @throws Model.GameException if player isn't the guesser
     */
    public abstract void guess(Player player, String guess) throws GameException;
}
