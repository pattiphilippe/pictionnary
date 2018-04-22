package message;

import message.util.Player;
import message.util.PlayerRole;

/**
 *
 * @author Philippe
 */
public class MessageProfile implements Message {

    private final Player p;

    public MessageProfile(String name) {
        this.p = new Player(name);
    }

    public MessageProfile(String name, PlayerRole role, boolean hasPartner) {
        this.p = new Player(name, role, hasPartner);
    }

    @Override
    public Type getType() {
        return Type.PROFILE;
    }

    @Override
    public Object getContent() {
        return p;
    }

}
