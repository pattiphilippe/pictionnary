package OneVOneModel;

/**
 *
 * @author Philippe
 */
public class Player {

    private String username;
    private PlayerRole role;

    public Player(String username) {
        this.username = username;
        this.role = PlayerRole.NONE;
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

    public boolean is(String username) {
        return this.username.equals(username);
    }

    public void setUsername(String name) {
        this.username = name;
    }
}
