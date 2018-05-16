package message;

import message.util.InitInfos;

/**
 *
 * @author Philippe
 */
public class MessageGameInit implements Message {

    private final InitInfos infos;

    public MessageGameInit(InitInfos infos) {
        this.infos = infos;
    }

    @Override
    public Type getType() {
        return Type.GAME_INIT;
    }

    @Override
    public Object getContent() {
        return infos;
    }

}
