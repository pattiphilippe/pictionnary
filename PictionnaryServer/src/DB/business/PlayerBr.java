package DB.business;

import DB.db.DbException;
import DB.db.PlayerDb;
import DB.dto.PlayerDto;
import java.util.Collection;

/**
 * Player's business rules.
 *
 * @author G43197
 */
public class PlayerBr {

    static PlayerDto findById(int id) throws DbBusinessException {
        Collection<PlayerDto> result = find(new PlayerDto(id));
        if (result.size() < 1) {
            throw new DbBusinessException("No player found with id = " + id);
        } else if (result.size() > 1) {
            throw new DbBusinessException("Multiples players found with id = " + id);
        } else {
            return result.iterator().next();
        }
    }

    static PlayerDto findByName(String playerName) throws DbBusinessException {
        Collection<PlayerDto> result = find(new PlayerDto(playerName));
        if (result.size() < 1) {
            throw new DbBusinessException("No player found with playerName = " + playerName);
        } else if (result.size() > 1) {
            throw new DbBusinessException("Multiples players found with playerName = " + playerName);
        } else {
            return result.iterator().next();
        }
    }

    static Collection<PlayerDto> findAll() throws DbBusinessException {
        try {
            return PlayerDb.getAllPlayers();
        } catch (DbException ex) {
            throw new DbBusinessException("Error finding all Players", ex);
        }
    }

    static Collection<PlayerDto> find(PlayerDto sel) throws DbBusinessException {
        try {
            return PlayerDb.getCollection(sel);
        } catch (DbException ex) {
            throw new DbBusinessException("Erreur finding Player", ex);
        }
    }

    static int add(PlayerDto player) throws DbBusinessException {
        try {
            if (!player.isPersistant()) {
                return PlayerDb.insertDb(player);
            } else {
                throw new DbBusinessException("Player: on ne peut rendre persistant un objet déjà persistant!");
            }
        } catch (DbException ex) {
            throw new DbBusinessException(ex);
        }
    }

    static void update(PlayerDto player) throws DbBusinessException {
        try {
            PlayerDb.updateDb(player);
        } catch (DbException eDB) {
            throw new DbBusinessException(eDB);
        }
    }

    static void delete(int id) throws DbBusinessException {
        try {
            PlayerDb.deleteDb(id);
        } catch (DbException eDB) {
            throw new DbBusinessException(eDB);
        }
    }

}
