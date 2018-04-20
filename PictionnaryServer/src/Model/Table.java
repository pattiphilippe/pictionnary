package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Philippe
 */
public class Table extends Model implements Serializable {

    private final String id;
    private boolean isOpen;
    private final List<String> guesses;
    private GameState gameState;
    private final String[] players;
    private final String wordToGuess;

    public Table(String id, Player drawer) {
        this.id = id;
        this.isOpen = true;
        this.guesses = new ArrayList<>();
        this.gameState = GameState.INIT;
        this.wordToGuess = Words.random();
        players = new String[2];
        players[0] = drawer.getUsername();
        drawer.setRole(PlayerRole.DRAWER);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean is(String tableId) {
        return (this.id == null ? tableId == null : this.id.equals(tableId));
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    @Override
    public List<String> getGuesses() {
        return Collections.unmodifiableList(guesses);
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public String[] getPlayers() {
        return players;
    }

    @Override
    public void addGuesser(Player guesser) throws GameException {
        if (!isOpen) {
            throw new GameException("Table is closed!");
        }
        players[1] = guesser.getUsername();
        guesser.setRole(PlayerRole.GUESSER);
        this.isOpen = false;
        this.gameState = GameState.IN_PROGRESS;
    }

    @Override
    public boolean isOnTable(Player player) {
        for (String username : players) {
            if (player.isUsername(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a player from the table.
     *
     * @param player
     * @return true if it did, false if the player was not on this table.
     */
    @Override
    public void removePlayer(Player player) throws GameException {
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player)) {
                players[i] = null;
                this.setChanged();
                notifyObservers();
                return;
            }
        }
        throw new GameException("Player is not on table");
    }

    @Override
    public void guess(Player player, String guess) throws GameException {
        if (!isOnTable(player)) {
            throw new GameException("Player doesn't play on this table!");
        }
        if (player.getRole() != PlayerRole.GUESSER) {
            throw new GameException("Only the guesser can guess the word");
        }
        if (wordToGuess.equals(guess)) {
            gameState = GameState.WON;
        }
        guesses.add(guess);
        setChanged();
        notifyObservers();
    }

}
