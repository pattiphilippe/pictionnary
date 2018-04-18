package Model;

/**
 *
 * @author Philippe
 */
public class Player {

    private final String username;
    private PlayerRole role;

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public PlayerRole getRole() {
        return role;
    }

    void setRole(PlayerRole role) {
        this.role = role;
    }

    public boolean isUsername(String username) {
        return this.username.equals(username);
    }
}
