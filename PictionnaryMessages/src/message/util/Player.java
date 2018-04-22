package message.util;

import java.io.Serializable;

/**
 *
 * @author Philippe
 */
public class Player implements Serializable {

    private final String username;
    private final PlayerRole role;
    private final boolean hasPartner;

    public Player(String username, PlayerRole role, boolean hasPartner) {
        this.username = username;
        this.role = role;
        this.hasPartner = hasPartner;
    }

    public Player(String name) {
        this(name, PlayerRole.NONE, false);
    }

    public String getUsername() {
        return username;
    }

    public PlayerRole getRole() {
        return role;
    }

    public boolean hasPartner() {
        return hasPartner;
    }

}
