package OneVOneModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static OneVOneModel.GameState.*;

/**
 *
 * @author Philippe
 */
public class Table extends Model {

    private final String id;
    private final List<String> guesses;
    private final Player[] players;
    private final String[] playerNames;
    private final String wordToGuess;
    private GameState state;

    public Table(String id, Player drawer) {
        this.id = id;
        this.guesses = new ArrayList<>();
        this.wordToGuess = Words.random();
        this.state = WAITING_FOR_PARTNER;
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
    public String[] getPlayerNames() {
        return playerNames;
    }

    @Override
    public String getWordToGuess() {
        return wordToGuess;
    }

    @Override
    public void addGuesser(Player guesser) throws GameException {
        if (state != WAITING_FOR_PARTNER) {
            throw new GameException("Not waiting for partner on this table!");
        }
        players[1] = guesser;
        playerNames[1] = guesser.getUsername();
        guesser.setTable(this);
        guesser.setRole(PlayerRole.GUESSER);
        this.state = GameState.INIT;
        setChanged();
        notifyObservers();
        this.state = IN_GAME;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean isOnTable(Player player) {
        if (player == null) {
            return false;
        }
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
                player.setTable(null);
                state = isEmpty() ? EMPTY : PARTNER_LEFT;
                setChanged();
                notifyObservers();
                return;
            }
        }
        // didn't use isOnTable at start of method because we can directly see it
        //as we go through the players anyway
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
            state = WON;
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
    public GameState getState() {
        return state;
    }

    public String getPartner(Player player) {
        if (!isOnTable(player)) {
            return null;
        }
        String name;
        for (int i = 0; i < playerNames.length; i++) {
            name = playerNames[i];
            if (player.is(name)) {
                // following operation based on 2 players
                return playerNames[(i + 1) % 2];
            }
        }
        return null;
    }

    @Override
    public String getLastGuess() {
        return guesses.isEmpty() ? null : guesses.get(guesses.size() - 1);
    }

}
