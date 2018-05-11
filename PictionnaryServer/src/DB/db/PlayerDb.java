package DB.db;

import DB.dto.PlayerDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G43197
 */
public class PlayerDb {

    public static List<PlayerDto> getAllPlayers() throws DbException {
        try {
            return getCollection(new PlayerDto(""));
        } catch (DbException ex) {
            throw new DbException("Erreur getAllPlayers : " + ex.getMessage());
        }

    }

    public static void updateDb(PlayerDto player) throws DbException {
        try {
            java.sql.Connection connection = DBManager.getConnection();

            java.sql.PreparedStatement update;
            update = connection.prepareStatement("Update player set "
                    + " pname=?,"
                    + "where pid= ?");
            update.setString(1, player.getName());
            update.setInt(2, player.getId());
            update.executeUpdate();
        } catch (Exception ex) {
            throw new DbException("Player, modification impossible:\n" + ex.getMessage());
        }
    }

    public static int insertDb(PlayerDto player) throws DbException {
        try {
            int num = SequenceDB.getNextNum(SequenceDB.PLAYER);
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement insert;

            insert = connexion.prepareStatement("Insert into Player(pId,pName) values(?, ?)");
            insert.setInt(1, num);
            insert.setString(2, player.getName());
            insert.execute();
            return num;
        } catch (DbException | SQLException ex) {
            throw new DbException("Player: ajout de Player impossible\r" + ex.getMessage());
        }

    }

    public static List<PlayerDto> getCollection(PlayerDto sel) throws DbException {
        ArrayList<PlayerDto> al = new ArrayList<>();
        try {
            String query = "Select "
                    + "pId, pName "
                    + " FROM Player ";
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement stmt;
            String where = "";
            if (sel.getId() != null && sel.getId() != 0) {
                where = where + " pid = ? ";
            }
            if (sel.getName() != null && !sel.getName().equals("")) {
                if (!where.equals("")) {
                    where = where + " AND ";
                }
                where = where + " pName like ? ";
            }

            if (where.length() != 0) {
                where = " where " + where;
                query = query + where;
                stmt = connexion.prepareStatement(query);
                int i = 1;
                if (sel.getId() != null && sel.getId() != 0) {
                    stmt.setInt(i, sel.getId());
                    i++;
                }

                if (sel.getName() != null && !sel.getName().equals("")) {
                    stmt.setString(i, sel.getName());
                    i++;
                }

            } else {
                stmt = connexion.prepareStatement(query);
            }

            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                al.add(new PlayerDto(rs.getInt("pid"), rs.getString("pName")));
            }
        } catch (SQLException eSQL) {
            eSQL.printStackTrace();
            throw new DbException("Instanciation de Player impossible:\rSQLException: " + eSQL.getMessage());
        }
        return al;
    }

    public static void deleteDb(int id) throws DbException {
        try {
            java.sql.Statement stmt = DBManager.getConnection().createStatement();
            stmt.execute("Delete from player where pid=" + id);
        } catch (Exception ex) {
            throw new DbException("Player: suppression impossible\n" + ex.getMessage());
        }
    }
}
