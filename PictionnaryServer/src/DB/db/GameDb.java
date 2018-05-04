package DB.db;

import DB.dto.GameDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TODO interface for DBManagement with CRUD
/**
 *
 * @author G43197
 */
public class GameDb {

    public static List<GameDto> getAllGames() throws DbException {
        try {
            return getCollection(new GameDto());
        } catch (DbException ex) {
            throw new DbException("Erreur getAllGames : " + ex.getMessage());
        }

    }

    public static void updateDb(GameDto game) throws DbException {
        try {
            java.sql.Connection connection = DBManager.getConnection();

            java.sql.PreparedStatement update;
            update = connection.prepareStatement("Update Game set "
                    + " gDrawer=?,"
                    + " gPartner=?,"
                    + " gStartTime=?,"
                    + " gEndTime=?,"
                    + " gStopPlayer=?,"
                    + "where gId= ?");
            update.setInt(1, game.getDrawer());
            update.setInt(2, game.getPartner());
            update.setTime(3, game.getStartTime());
            update.setTime(4, game.getEndTime());
            update.setInt(5, game.getStopPlayer());
            update.executeUpdate();
        } catch (Exception ex) {
            throw new DbException("Game, modification impossible:\n" + ex.getMessage());
        }
    }

    public static int insertDb(GameDto game) throws DbException {
        try {
            int num = SequenceDB.getNextNum(SequenceDB.GAME);
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement insert;

            insert = connexion.prepareStatement("Insert into Game(gId, gDrawer, "
                    + "gPartner, gStartTime, gEndTime, gStopPlayer) "
                    + "values(?, ?, ?, ?, ?, ?)");
            insert.setInt(1, num);
            insert.setInt(2, game.getDrawer());
            insert.setInt(3, game.getPartner());
            insert.setTime(4, game.getStartTime());
            insert.setTime(5, game.getEndTime());
            insert.setInt(6, game.getStopPlayer());
            insert.execute();
            return num;
        } catch (DbException | SQLException ex) {
            throw new DbException("Game: ajout de Game impossible\r" + ex.getMessage());
        }

    }

    public static List<GameDto> getCollection(GameDto sel) throws DbException {
        ArrayList<GameDto> al = new ArrayList<>();
        try {
            String query = "Select "
                    + "gId, gDrawer, gPartner, gStartTime, gEndTime, gStopPlayer "
                    + " FROM Game ";
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement stmt;
            String where = "";
            if (sel.getId() != 0) {
                where = where + " gId = ? ";
            }
            if (sel.getDrawer() != 0) {
                if (!where.equals("")) {
                    where = where + " AND ";
                }
                where = where + " gDrawer = ? ";
            }
            if (sel.getPartner() != 0) {
                if (!where.equals("")) {
                    where = where + " AND ";
                }
                where = where + " gPartner = ? ";
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
                if (sel.getDrawer() != 0) {
                    stmt.setInt(i, sel.getDrawer());
                    i++;
                }
                if (sel.getPartner() != 0) {
                    stmt.setInt(i, sel.getPartner());
                    i++;
                }

            } else {
                stmt = connexion.prepareStatement(query);
            }

            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                al.add(new GameDto(rs.getInt("gId"), rs.getInt("gDrawer"),
                        rs.getInt("gPartner"), rs.getTime("gStartTime"),
                        rs.getTime("gEndTime"), rs.getInt("gStopPlayer")));
            }
        } catch (SQLException eSQL) {
            eSQL.printStackTrace();
            throw new DbException("Instanciation de Game impossible:\rSQLException: " + eSQL.getMessage());
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
            stmt.execute("Delete from game where gid=" + id);
        } catch (Exception ex) {
            throw new DbException("Game: suppression impossible\n" + ex.getMessage());
        }
    }
}
