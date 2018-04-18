package message.util;

/**
 *
 * @author Philippe
 */
public class Table {

    private final String id;
    private boolean isOpen;
    private final User[] players;

    public Table(String id, boolean isOpen, User... players) throws Exception {
        if (players.length > 2) {
            throw new Exception("Too many players on the table!");
        }
        this.id = id;
        this.isOpen = isOpen;
        this.players = new User[2];

        for (int i = 0; i < players.length; i++) {
            this.players[i] = players[i];
        }
    }

    public String getId() {
        return id;
    }

    public boolean is(String id) {
        return (id == null ? id == null : id.equals(id));
    }

    public boolean isOpen() {
        return isOpen;
    }

    public User[] getPlayers() {
        return players;
    }
}
