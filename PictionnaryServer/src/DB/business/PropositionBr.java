package DB.business;

import DB.db.DbException;
import DB.db.PropositionDb;
import DB.dto.PropositionDto;
import DB.dto.WordDto;
import java.util.Collection;

/**
 *
 * @author G43197
 */
public class PropositionBr {

    static PropositionDto findByName(String prop) throws DbBusinessException {
        Collection<PropositionDto> result = find(new PropositionDto(prop));
        if (result.size() < 1) {
            throw new DbBusinessException("No word found with given name : " + prop);
        } else if (result.size() > 1) {
            throw new DbBusinessException("Multiples words found with given word : " + prop);
        } else {
            return result.iterator().next();
        }
    }

    static Collection<PropositionDto> findAll() throws DbBusinessException {
        try {
            return PropositionDb.getAllPropositions();
        } catch (DbException ex) {
            throw new DbBusinessException("Error finding all Propositions", ex);
        }
    }

    static Collection<PropositionDto> find(PropositionDto sel) throws DbBusinessException {
        try {
            return PropositionDb.getCollection(sel);
        } catch (DbException ex) {
            throw new DbBusinessException("Erreur finding Proposition", ex);
        }
    }

    static Collection<PropositionDto> getPropsWithCount(String wordToGuess) throws DbBusinessException {
        try {
            return PropositionDb.getPropsWithCount(WordBr.findByName(wordToGuess));
        } catch (DbException ex) {
            throw new DbBusinessException("Erreur with counting Propositions stats", ex);
        }
    }

    static int getAvgProps(String wordToGuess) throws DbBusinessException {
        try {
            return PropositionDb.getAvgProps(WordBr.findByName(wordToGuess));
        } catch (DbException ex) {
            throw new DbBusinessException("Erreur with counting Propositions stats", ex);
        }
    }

    static int add(PropositionDto word) throws DbBusinessException {
        try {
            if (!word.isPersistant()) {
                return PropositionDb.insertDb(word);
            } else {
                throw new DbBusinessException("Proposition: on ne peut rendre persistant un objet déjà persistant!");
            }
        } catch (DbException ex) {
            throw new DbBusinessException(ex);
        }
    }
}
