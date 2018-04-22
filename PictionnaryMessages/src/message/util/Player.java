package message.util;

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class Player implements Serializable {

    private final String username;
    private final PlayerRole role;

    public Player(String username, PlayerRole role) {
        this.username = username;
        this.role = role;
    }

    public Player(String name) {
        this(name, PlayerRole.NONE);
    }

    public String getUsername() {
        return username;
    }

    public PlayerRole getRole() {
        return role;
    }

}
