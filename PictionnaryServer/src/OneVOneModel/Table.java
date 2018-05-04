package OneVOneModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Philippe
 */
public class Table extends Model {

    private final String id;
    private boolean isOpen;
    private final List<String> guesses;
    private final Player[] players;
    private final String[] playerNames;
    private final String wordToGuess;
    private boolean isFinished;

    public Table(String id, Player drawer) {
        this.id = id;
        this.isOpen = true;
        this.guesses = new ArrayList<>();
        this.wordToGuess = Words.random();
        this.isFinished = false;
        playerNames = new String[2];
        playerNames[0] = drawer.getUsername();
        players = new Player[2];
        players[0] = drawer;
        drawer.setTable(this);
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
    public List<String> getGuesses() {
        return Collections.unmodifiableList(guesses);
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public String[] getPlayerNames() {
        return playerNames;
    }

    @Override
    public String getWordToGuess() {
        return wordToGuess;
    }

    @Override
    public void addGuesser(Player guesser) throws GameException {
        if (!isOpen) {
            throw new GameException("Table is closed!");
        }
        players[1] = guesser;
        playerNames[1] = guesser.getUsername();
        guesser.setTable(this);
        guesser.setRole(PlayerRole.GUESSER);
        this.isOpen = false;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean isOnTable(Player player) {
        for (String username : playerNames) {
            if (player.is(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removePlayer(Player player) throws GameException {
        if (player == null) {
            throw new IllegalArgumentException("Player is null!");
        }
        for (int i = 0; i < playerNames.length; i++) {
            if (player.is(playerNames[i])) {
                playerNames[i] = null;
                player.setRole(PlayerRole.NONE);
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
        if (wordToGuess.equals(guess.toLowerCase())) {
            isFinished = true;
        }
        guesses.add(guess);
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean isEmpty() {
        for (String name : playerNames) {
            if (name != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

}
