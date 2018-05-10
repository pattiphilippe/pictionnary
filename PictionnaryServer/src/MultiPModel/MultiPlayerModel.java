package MultiPModel;

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

/**
 * Implements the MultiPlayer facade. Tables are stored in tables Hashmap and in
 * every player when that one is on a table. This is redundant, but kept for
 * performances.
 *
 * @author Philippe
 */
public class MultiPlayerModel extends MultiPlayerFacade implements Observer {
    
    private final List<String> words;
    private final HashMap<String, Player> players;
    private final HashMap<String, Table> tables;
    
    public MultiPlayerModel() {
        words = new ArrayList<>();
        //TODO loadWords();
        players = new HashMap<>();
        tables = new HashMap<>();
    }
    
    @Override
    public void createPlayer(String playerName) throws DbException {
        Player p = players.get(playerName);
        //TODO p = AdminFacade.getPlayer(playerName);
        if (p != null) {
            throw new DbException("Someone else has that username");
        }
        if (p == null) {
            p = new Player(playerName);
            //save player to db
            //TODO or dto?
            //TODO AdminFacade.savePlayer(p);
        }
        players.put(playerName, p);
        setChanged();
        notifyObservers(p);
    }
    
    @Override
    public void createTable(String playerName, String tableName) throws GameException, DbException {
        Player p = players.get(playerName);
        if (p.getTable() != null) {
            throw new GameException("Player already on a table!");
        }
        Table t = tables.get(tableName);
        if (t != null) {
            throw new DbException("Another table has that name!");
        }
        //TODO check if players in Table necessary
        t = new Table(tableName, p);
        t.addObserver(this);
        tables.put(tableName, t);
        setChanged();
        notifyObservers(p);
        setChanged();
        notifyObservers(t);
        //create table
    }
    
    @Override
    public void joinTable(String playerName, String tableId) throws GameException {
        Player p = players.get(playerName);
        Table t = tables.get(tableId);
        t.addGuesser(p);
        setChanged();
        notifyObservers(p);

        //TODO or dto?
        //TODO AdminFacade.saveTable(t);
        // save t to db
    }
    
    @Override
    public void leaveTable(String playerName) throws GameException {
        Player p = players.get(playerName);
        if (p != null) {
            Table t = p.getTable();
            if (t != null && t.isOnTable(p)) {
                t.removePlayer(p);
                setChanged();
                notifyObservers(p);
            }
        }
        //TODO or dto?
        //TODO AdminFacade.updateTable(t);
        // update t to db
    }
    
    @Override
    public void guess(String playerName, String guess) throws GameException {
        Player p = players.get(playerName);
        Table t = p.getTable();
        t.guess(p, guess);
        //TODO if (t.isFinished()) {
        //TODO or dto ?
        //TODO AdminFacade.updateTable(t);
        //update t to db
        //}
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void loadWords() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean exists(String playerName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Model) {
            Model t = (Model) o;
            if (t.getState() == GameState.EMPTY) {
                tables.remove(t.getId());
            }
            setChanged();
            notifyObservers(o);
        }
    }
    
    @Override
    public void updateUsername(String oldUsername, String newUsername) throws DbException {
        //TODO implement
        //TODO setChanged();
        //TODO notifyObservers(p);
        throw new DbException("Unsupported operation!");
    }
    
    @Override
    public List<Table> getTables() {
        return new ArrayList<>(tables.values());
    }
    
    @Override
    public String getPartnerUsername(String username) {
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
    public Table getTable(String username) {
        return players.get(username).getTable();
    }
    
    @Override
    public void exitGame(String playerName) {
        Player p = players.get(playerName);
        if (p != null) {
            if (p.getTable() != null) {
                try {
                    leaveTable(playerName);
                } catch (GameException ex) {
                }
            }
            players.remove(playerName);
            //TODO check db consequences
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
