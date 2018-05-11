package DB.business;

import DB.db.DBManager;
import DB.db.DbException;
import DB.dto.GameDto;
import DB.dto.PlayerDto;
import java.util.Collection;

/**
 *
 * @author G43197
 */
public abstract class DefaultUser implements DBFacade {

    public static PlayerDto getPlayer(String playerName) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            PlayerDto player = PlayerBr.findByName(playerName);
            DBManager.validateTransaction();
            return player;
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Player inaccessible! \n" + msg, eDB);
            }
        }
    }

    public static PlayerDto getPlayer(Integer id) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            PlayerDto player = PlayerBr.findById(id);
            DBManager.validateTransaction();
            return player;
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Player inaccessible! \n" + msg, eDB);
            }
        }
    }

    public static boolean hasPlayer(String playerName) {
        try {
            PlayerDto player = getPlayer(playerName);
            return player != null;
        } catch (DbBusinessException e) {
            return false;
        }
    }

    public static GameDto getGameById(int id) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            GameDto game = GameBr.findById(id);
            DBManager.validateTransaction();
            return game;
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Game inaccessible! \n" + msg, eDB);
            }
        }
    }

    public static Collection<GameDto> getGames(String playerName) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            PlayerDto player = getPlayer(playerName);
            Collection<GameDto> game = GameBr.find(new GameDto(player.getId(), false));
            DBManager.validateTransaction();
            return game;
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Game inaccessible! \n" + msg, eDB);
            }
        }
    }

}
