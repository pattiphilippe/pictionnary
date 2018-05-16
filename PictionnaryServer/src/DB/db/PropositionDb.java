package DB.db;

import DB.dto.PropositionDto;
import DB.dto.WordDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G43197
 */
public class PropositionDb {

    public static List<PropositionDto> getAllPropositions() throws DbException {
        try {
            return getCollection(new PropositionDto());
        } catch (DbException ex) {
            throw new DbException("Erreur getAllPropositions : " + ex.getMessage());
        }

    }

    public static int insertDb(PropositionDto prop) throws DbException {
        try {
            int num = SequenceDB.getNextNum(SequenceDB.PROPOSITION);
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement insert;

            insert = connexion.prepareStatement("Insert into Proposition(prId,prTxt,prGame) values(?, ?, ?)");
            insert.setInt(1, num);
            insert.setString(2, prop.getTxt());
            insert.setInt(3, prop.getGameId());
            insert.execute();
            return num;
        } catch (DbException | SQLException ex) {
            throw new DbException("Proposition: ajout de Proposition impossible\r" + ex.getMessage());
        }

    }

    public static List<PropositionDto> getCollection(PropositionDto prop) throws DbException {
        ArrayList<PropositionDto> al = new ArrayList<>();
        try {
            String query = "Select "
                    + "prId, prTxt, prGame "
                    + " FROM Proposition ";
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement stmt;
            String where = "";
            if (prop.getId() != 0) {
                where = where + " prId = ? ";
            }
            if (prop.getTxt() != null && !prop.getTxt().equals("")) {
                if (!where.equals("")) {
                    where = where + " AND ";
                }
                where = where + " prTxt like ? ";
            }
            if (prop.getGameId() != 0) {
                if (!where.equals("")) {
                    where = where + " AND ";
                }
                where = where + " prGame = ? ";
            }

            if (where.length() != 0) {
                where = " where " + where;
                query = query + where;
                stmt = connexion.prepareStatement(query);
                int i = 1;
                if (prop.getId() != 0) {
                    stmt.setInt(i, prop.getId());
                    i++;
                }

                if (prop.getTxt() != null && !prop.getTxt().equals("")) {
                    stmt.setString(i, prop.getTxt() + "%");
                    i++;
                }

                if (prop.getGameId() != 0) {
                    stmt.setInt(i, prop.getGameId());
                    i++;
                }

            } else {
                stmt = connexion.prepareStatement(query);
            }

            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                al.add(new PropositionDto(rs.getInt("prId"), rs.getString("prTxt"), rs.getInt("prGame")));
            }
        } catch (SQLException eSQL) {
            eSQL.printStackTrace();
            throw new DbException("Instanciation de Proposition impossible:\rSQLException: " + eSQL.getMessage());
        }
        return al;
    }

    //TODO with wordDto
    public static List<PropositionDto> getPropsWithCount(WordDto word) throws DbException {
        ArrayList<PropositionDto> al = new ArrayList<>();
        try {
            String query = "Select prTxt, COUNT(*)"
                    + " FROM Proposition JOIN game ON prGame = gId "
                    + "WHERE gword = ? "
                    + "GROUP BY prTxt "
                    + "ORDER BY prTxt ";
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement stmt = connexion.prepareStatement(query);
            if (word.getId() != 0) {
                stmt.setInt(1, word.getId());
            }

            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                al.add(new PropositionDto(rs.getString("prTxt"), rs.getInt(2), false));
            }
        } catch (SQLException eSQL) {
            eSQL.printStackTrace();
            throw new DbException("Instanciation de Proposition impossible:\rSQLException: " + eSQL.getMessage());
        }
        return al;
    }

    public static int getAvgProps(WordDto word) throws DbException {
        try {
            String query = "Select AVG(sc.nb)"
                    + " FROM (SELECT COUNT(*) as nb "
                    + "FROM proposition JOIN game ON prGame = gId "
                    + "WHERE gword = ? "
                    + "GROUP BY prGame) sc";
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement stmt = connexion.prepareStatement(query);
            if (word.getId() != 0) {
                stmt.setInt(1, word.getId());
            }
            java.sql.ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException eSQL) {
            eSQL.printStackTrace();
            throw new DbException("Instanciation de Proposition impossible:\rSQLException: " + eSQL.getMessage());
        }
    }

}
