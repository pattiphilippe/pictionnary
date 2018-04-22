package message;

import pictionnary.drawingPane.DrawingInfos;

/**
 *
 * @author Philippe
 */
public class MessageDrawLine implements Message {

    private final DrawingInfos line;

    public MessageDrawLine(DrawingInfos line) {
        this.line = line;
    }

    @Override
    public Type getType() {
        return Type.DRAW_LINE;
    }

    @Override
    public Object getContent() {
        return line;
    }

}
