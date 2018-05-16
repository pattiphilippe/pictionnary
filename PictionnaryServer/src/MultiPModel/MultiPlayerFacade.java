package MultiPModel;

import DB.business.DbBusinessException;
import DB.db.DbException;
import OneVOneModel.GameException;
import OneVOneModel.Model;
import OneVOneModel.PlayerRole;
import OneVOneModel.Table;
import java.util.List;
import java.util.Observable;
import message.util.Prop;

/**
 * Facade of a multiplayer Pictionnary game, with multiples tables that can have
 * 2 players on them. This facade is linked to a database to save the players
 * history (based on the username). Words are also searched in the database.
 *
 * @author Philippe
 */
public abstract class MultiPlayerFacade extends Observable {

    //TODO javadoc
    public abstract void createPlayer(String playerName) throws GameException, DbBusinessException;

    public abstract void createTable(String playerName, String tableName) throws GameException;

    public abstract void joinTable(String playerName, String tableId) throws GameException, DbBusinessException;

    public abstract void leaveTable(String playerName) throws GameException, DbBusinessException;

    public abstract void exitGame(String playerName) throws GameException, DbBusinessException;

    public abstract void guess(String playerName, String guess) throws GameException;

    public abstract void updateUsername(String oldUsername, String newUsername) throws GameException;

    //TODO check usage, shouldn't return Tables 
    public abstract List<Table> getTables();

    //TODO chech usage, shouldn't return a Table
    public abstract Table getTable(String username);

    public abstract String getPartnerUsername(String username);

    public abstract PlayerRole getRole(String username) throws DbException;

    public abstract int getAvgWrongProps(Model t) throws DbBusinessException;

    public abstract List<Prop> getPropsWithCount(Model t) throws DbBusinessException;
}
