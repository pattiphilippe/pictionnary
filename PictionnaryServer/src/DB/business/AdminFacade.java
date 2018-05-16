package DB.business;

import DB.db.DBManager;
import DB.db.DbException;
import DB.db.WordDb;
import DB.dto.GameDto;
import DB.dto.PlayerDto;
import DB.dto.WordDto;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author G43197
 */
public class AdminFacade extends DefaultUser {

    public static Collection<WordDto> loadWords() throws DbBusinessException {
        WordDto wordSel = new WordDto(0);
        try {
            DBManager.startTransaction();
            Collection<WordDto> col = WordDb.getCollection(wordSel);
            DBManager.validateTransaction();
            return col;
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Liste des Words inaccessible! \n" + msg, eDB);
            }
        }

    }

    public static int savePlayer(String playerName) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            int i = PlayerBr.add(new PlayerDto(playerName));
            DBManager.validateTransaction();
            return i;
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Ajout de player impossible! \n" + msg, eDB);
            }
        }
    }

    public static int createTable(String drawerName, String guesserName, String word, String tableName) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            PlayerDto drawer = getPlayer(drawerName);
            PlayerDto guesser = getPlayer(guesserName);
            WordDto wordDto = WordBr.findByName(word);
            int i = GameBr.add(new GameDto(drawer.getId(), guesser.getId(), new Date(), wordDto.getId(), tableName));
            DBManager.validateTransaction();
            return i;
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Ajout de game/table impossible! \n" + msg, eDB);
            }
        }
    }

    public static void leaveTable(String playerName, int id) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            PlayerDto player = getPlayer(playerName);
            GameDto game = getGameById(id);
            if (game.getEndTime() == null) {
                GameBr.update(new GameDto(id, game.getDrawer(), game.getPartner(), game.getStartTime(), new Date(), player.getId(), 0, game.getWord(), game.getTableName()));
            }
            DBManager.validateTransaction();
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Quitter la game/table impossible! \n" + msg, eDB);
            }
        }
    }

    public static void gameWon(int id) throws DbBusinessException {
        try {
            DBManager.startTransaction();
            GameDto game = getGameById(id);
            if (game.getEndTime() == null) {
                GameBr.update(new GameDto(id, game.getDrawer(), game.getPartner(), game.getStartTime(), new Date(), 0, 0, game.getWord(), game.getTableName()));
            }
            DBManager.validateTransaction();
        } catch (DbException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DbException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DbBusinessException("Ne sait pas faire gagner la game/table! \n" + msg, eDB);
            }
        }
    }

}
