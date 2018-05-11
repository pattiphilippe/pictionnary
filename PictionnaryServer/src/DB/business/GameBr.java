package DB.business;

import DB.db.DbException;
import DB.db.GameDb;
import DB.dto.GameDto;
import java.util.Collection;

/**
 * Business rules for game.
 *
 * @author G43197
 */
public class GameBr {

    static GameDto findById(int id) throws DbBusinessException {
        Collection<GameDto> result = find(new GameDto(id, true));
        if (result.size() < 1) {
            throw new DbBusinessException("No game found with id = " + id);
        } else if (result.size() > 1) {
            throw new DbBusinessException("Multiples games found with id = " + id);
        } else {
            return result.iterator().next();
        }
    }

    static Collection<GameDto> findAll() throws DbBusinessException {
        try {
            return GameDb.getAllGames();
        } catch (DbException ex) {
            throw new DbBusinessException("Error finding all Games", ex);
        }
    }

    static Collection<GameDto> find(GameDto sel) throws DbBusinessException {
        try {
            return GameDb.getCollection(sel);
        } catch (DbException ex) {
            throw new DbBusinessException("Erreur finding Game", ex);
        }
    }

    static int add(GameDto game) throws DbBusinessException {
        try {
            if (!game.isPersistant()) {
                return GameDb.insertDb(game);
            } else {
                throw new DbBusinessException("Word: on ne peut rendre persistant un objet déjà persistant!");
            }
        } catch (DbException ex) {
            throw new DbBusinessException(ex);
        }
    }

    static void update(GameDto game) throws DbBusinessException {
        try {
            GameDb.updateDb(game);
        } catch (DbException eDB) {
            throw new DbBusinessException(eDB);
        }
    }

    static void delete(int id) throws DbBusinessException {
        try {
            GameDb.deleteDb(id);
        } catch (DbException eDB) {
            throw new DbBusinessException(eDB);
        }
    }

}
