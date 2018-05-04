package Model;

import DB.db.DbException;
import OneVOneModel.GameException;
import OneVOneModel.Model;
import OneVOneModel.Player;
import OneVOneModel.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Philippe
 */
public class MultiPlayerModel extends MultiPlayerFacade implements Observer {

    private final List<String> words;
    ;private final HashMap<String, Player> players;
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
        //TODO or dto?
        //TODO AdminFacade.saveTable(t);
        // save t to db
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void leaveTable(String playerName) throws GameException {
        Player p = players.get(playerName);
        Table t = p.getTable();
        t.removePlayer(p);
        //TODO or dto?
        //TODO AdminFacade.updateTable(t);
        // update t to db
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guess(String playerName, String guess) throws GameException {
        Player p = players.get(playerName);
        Table t = p.getTable();
        t.guess(p, guess);
        if (t.isFinished()) {
            //TODO or dto ?
            //TODO AdminFacade.updateTable(t);
            //update t to db
        }
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

}
