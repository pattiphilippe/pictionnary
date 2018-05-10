package MultiPModel;

import DB.db.DbException;
import OneVOneModel.GameException;
import OneVOneModel.PlayerRole;
import OneVOneModel.Table;
import java.util.List;
import java.util.Observable;

/**
 * Facade of a multiplayer Pictionnary game, with multiples tables that can have
 * 2 players on them. This facade is linked to a database to save the players
 * history (based on the username). Words are also searched in the database.
 *
 * @author Philippe
 */
public abstract class MultiPlayerFacade extends Observable {

    //TODO javadoc
    public abstract void createPlayer(String playerName) throws DbException;

    public abstract void createTable(String playerName, String tableName) throws GameException, DbException;

    public abstract void joinTable(String playerName, String tableId) throws GameException;

    public abstract void leaveTable(String playerName) throws GameException;

    public abstract void exitGame(String playerName);

    public abstract void guess(String playerName, String guess) throws GameException;

    public abstract void updateUsername(String oldUsername, String newUsername) throws DbException;

    //TODO check usage, shouldn't return Tables 
    public abstract List<Table> getTables();

    //TODO chech usage, shouldn't return a Table
    public abstract Table getTable(String username);

    public abstract String getPartnerUsername(String username);

    public abstract PlayerRole getRole(String username) throws DbException;
}
