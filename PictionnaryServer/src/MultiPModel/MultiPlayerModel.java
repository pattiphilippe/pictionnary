package MultiPModel;

import DB.business.AdminFacade;
import DB.business.DbBusinessException;
import DB.db.DbException;
import OneVOneModel.GameException;
import OneVOneModel.GameState;
import OneVOneModel.Model;
import OneVOneModel.Player;
import OneVOneModel.PlayerRole;
import OneVOneModel.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implements the MultiPlayer facade. Tables are stored in tables Hashmap and in
 * every player when that one is on a table. This is redundant, but kept for
 * performances.
 *
 * @author Philippe
 */
public class MultiPlayerModel extends MultiPlayerFacade implements Observer {

    //TODO use words
    private final List<String> words;
    private final HashMap<String, Player> players;
    private final HashMap<String, Table> tables;

    public MultiPlayerModel() throws DbBusinessException {
        words = AdminFacade.loadWords().stream().map(dto -> dto.getTxt()).collect(Collectors.toList());
        players = new HashMap<>();
        tables = new HashMap<>();
    }

    @Override
    public void createPlayer(String playerName) throws GameException, DbBusinessException {
        Player p = players.get(playerName);
        if (p != null) {
            throw new GameException("Someone else is currently playing with that username");
        } else {
            if (!AdminFacade.hasPlayer(playerName)) {
                AdminFacade.savePlayer(playerName);
            }
            p = new Player(playerName);
        }

        players.put(playerName, p);
        setChanged();
        notifyObservers(p);
    }

    @Override
    public void createTable(String playerName, String tableName) throws GameException {
        Player p = players.get(playerName);
        if (p.getTable() != null) {
            throw new GameException("Player already on a table!");
        }
        Table t = tables.get(tableName);
        if (t != null) {
            throw new GameException("Another table has that name!");
        }
        //TODO check if players in Table necessary
        t = new Table(tableName, p, randomWord());
        t.addObserver(this);
        tables.put(tableName, t);
        setChanged();
        notifyObservers(p);
        setChanged();
        notifyObservers(t);
    }

    private String randomWord() {
        int i = (int) (Math.random() * words.size());
        return words.get(i);
    }

    @Override
    public void joinTable(String playerName, String tableName) throws GameException, DbBusinessException {
        Player p = players.get(playerName);
        if (p == null) {
            throw new GameException("Unknown player!");
        }
        Table t = tables.get(tableName);
        if (t == null) {
            throw new GameException("Player not on any table");
        }
        t.addGuesser(p);
        t.setId(AdminFacade.createTable(playerName, t.getPartner(p), t.getWordToGuess(), t.getTableName()));
        setChanged();
        notifyObservers(p);
    }

    @Override
    public void leaveTable(String playerName) throws GameException, DbBusinessException {
        Player p = players.get(playerName);
        if (p != null) {
            Table t = p.getTable();
            if (t != null && t.isOnTable(p)) {
                if (t.getState() != GameState.WAITING_FOR_PARTNER) {
                    try {
                        AdminFacade.leaveTable(playerName, t.getId());
                    } catch (DbBusinessException ex) {
                        Logger.getLogger(MultiPlayerModel.class.getName()).log(Level.SEVERE, null, ex);
                        throw ex;
                    }
                }
                t.removePlayer(p);
                setChanged();
                notifyObservers(p);
            }
        }
    }

    @Override
    public void guess(String playerName, String guess) throws GameException {
        Player p = players.get(playerName);
        Table t = p.getTable();
        t.guess(p, guess);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Model) {
            Model t = (Model) o;
            switch (t.getState()) {
                case EMPTY:
                    tables.remove(t.getTableName());
                    break;
                case WON:
                    try {
                        AdminFacade.gameWon(t.getId());
                    } catch (DbBusinessException ex) {
                        Logger.getLogger(MultiPlayerModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            }
            setChanged();
            notifyObservers(o);
        }
    }

    @Override
    public void updateUsername(String oldUsername, String newUsername) throws GameException {
        throw new GameException("Update username Unsupported yet!");
    }

    @Override
    public List<Table> getTables() {
        return new ArrayList<>(tables.values());
    }

    @Override
    public String getPartnerUsername(String username
    ) {
        Player p = players.get(username);
        if (p != null) {
            Table t = p.getTable();
            if (t != null) {
                return t.getPartner(p);
            }
        }
        return null;
    }

    @Override
    public Table getTable(String username
    ) {
        return players.get(username).getTable();
    }

    @Override
    public void exitGame(String playerName) throws GameException, DbBusinessException {
        Player p = players.get(playerName);
        if (p != null) {
            if (p.getTable() != null) {
                leaveTable(playerName);
            }
            players.remove(playerName);
        }
    }

    @Override
    public PlayerRole getRole(String username) throws DbException {
        Player p = players.get(username);
        if (p != null) {
            return p.getRole();
        } else {
            throw new DbException("Player unknown!");
        }
    }

}
