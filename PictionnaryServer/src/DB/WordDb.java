package DB;

import DB.dto.WordDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G43197
 */
public class WordDb {

    public static List<WordDto> getAllWords() throws DbException {
        try {
            return getCollection(new WordDto(""));
        } catch (DbException ex) {
            throw new DbException("Erreur getAllWords : " + ex.getMessage());
        }

    }

    public static void updateDb(WordDto word) throws DbException {
        try {
            java.sql.Connection connection = DBManager.getConnection();

            java.sql.PreparedStatement update;
            update = connection.prepareStatement("Update word set "
                    + " wTxt=?,"
                    + "where wId= ?");
            update.setString(1, word.getTxt());
            update.setInt(2, word.getId());
            update.executeUpdate();
        } catch (Exception ex) {
            throw new DbException("Word, modification impossible:\n" + ex.getMessage());
        }
    }

    public static int insertDb(WordDto word) throws DbException {
        try {
            int num = SequenceDB.getNextNum(SequenceDB.WORD);
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement insert;

            insert = connexion.prepareStatement("Insert into Word(wId,wTxt) values(?, ?)");
            insert.setInt(1, num);
            insert.setString(2, word.getTxt());
            insert.execute();
            return num;
        } catch (DbException | SQLException ex) {
            throw new DbException("Word: ajout de Word impossible\r" + ex.getMessage());
        }

    }

    public static List<WordDto> getCollection(WordDto sel) throws DbException {
        ArrayList<WordDto> al = new ArrayList<>();
        try {
            String query = "Select "
                    + "wId, wTxt "
                    + " FROM Word ";
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement stmt;
            String where = "";
            if (sel.getId() != 0) {
                where = where + " id = ? ";
            }
            if (sel.getTxt() != null && !sel.getTxt().equals("")) {
                if (!where.equals("")) {
                    where = where + " AND ";
                }
                where = where + " wTxt like ? ";
            }

            if (where.length() != 0) {
                where = " where " + where;
                query = query + where;
                stmt = connexion.prepareStatement(query);
                int i = 1;
                if (sel.getId() != 0) {
                    stmt.setInt(i, sel.getId());
                    i++;
                }

                if (sel.getTxt() != null && !sel.getTxt().equals("")) {
                    stmt.setString(i, sel.getTxt() + "%");
                    i++;
                }

            } else {
                stmt = connexion.prepareStatement(query);
            }

            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                al.add(new WordDto(rs.getInt("wId"), rs.getString("wTxt")));
            }
        } catch (SQLException eSQL) {
            eSQL.printStackTrace();
            throw new DbException("Instanciation de Word impossible:\rSQLException: " + eSQL.getMessage());
        }
        return al;

        /*
         * Inspirez-vous de la méthode analogue de CategorieDB pour écrire cette fonction
         *
         *
         */
    }

    public static void deleteDb(int id) throws DbException {
        try {
            java.sql.Statement stmt = DBManager.getConnection().createStatement();
            stmt.execute("Delete from word where id=" + id);
        } catch (Exception ex) {
            throw new DbException("Word: suppression impossible\n" + ex.getMessage());
        }
    }
}
