package DB.business;

import DB.db.DbException;
import DB.db.WordDb;
import DB.dto.WordDto;
import java.util.Collection;

/**
 * Business rules for Words.
 *
 * @author G43197
 */
public class WordBr {

    static WordDto findById(int id) throws DbBusinessException {
        Collection<WordDto> result = find(new WordDto(id));
        if (result.size() < 1) {
            throw new DbBusinessException("No word found with id = " + id);
        } else if (result.size() > 1) {
            throw new DbBusinessException("Multiples words found with id = " + id);
        } else {
            return result.iterator().next();
        }
    }

    static Collection<WordDto> findAll() throws DbBusinessException {
        try {
            return WordDb.getAllWords();
        } catch (DbException ex) {
            throw new DbBusinessException("Error finding all Words", ex);
        }
    }

    static Collection<WordDto> find(WordDto sel) throws DbBusinessException {
        try {
            return WordDb.getCollection(sel);
        } catch (DbException ex) {
            throw new DbBusinessException("Erreur finding Word", ex);
        }
    }

    static int add(WordDto word) throws DbBusinessException {
        try {
            if (!word.isPersistant()) {
                return WordDb.insertDb(word);
            } else {
                throw new DbBusinessException("Word: on ne peut rendre persistant un objet déjà persistant!");
            }
        } catch (DbException ex) {
            throw new DbBusinessException(ex);
        }
    }

    static void update(WordDto brand) throws DbBusinessException {
        try {
            WordDb.updateDb(brand);
        } catch (DbException eDB) {
            throw new DbBusinessException(eDB);
        }
    }

    static void delete(int id) throws DbBusinessException {
        try {
            WordDb.deleteDb(id);
        } catch (DbException eDB) {
            throw new DbBusinessException(eDB);
        }
    }
}
