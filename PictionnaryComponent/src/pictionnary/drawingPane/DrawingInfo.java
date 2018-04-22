package pictionnary.drawingPane;

import java.io.Serializable;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Philippe
 */
public class DrawingInfo implements Serializable {

    private final double x;
    private final double y;
    private final int thickness;
    private final String color;
    private final int mouseEventType;

    DrawingInfo(double x, double y, int thickness, String color, EventType<? extends MouseEvent> mouseEventType) {
        this.x = x;
        this.y = y;
        this.thickness = thickness;
        this.color = color;
        if (mouseEventType.equals(MouseEvent.MOUSE_PRESSED)) {
            this.mouseEventType = 1;
        } else if (mouseEventType.equals(MouseEvent.MOUSE_DRAGGED)) {
            this.mouseEventType = 2;
        } else if (mouseEventType.equals(MouseEvent.MOUSE_RELEASED)) {
            this.mouseEventType = 3;
        } else {
            this.mouseEventType = 0;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getThickness() {
        return thickness;
    }

    public String getColor() {
        return color;
    }

    public EventType<? extends MouseEvent> getMouseEvent() {
        switch (mouseEventType) {
            case 1:
                return MouseEvent.MOUSE_PRESSED;
            case 2:
                return MouseEvent.MOUSE_DRAGGED;
            case 3:
                return MouseEvent.MOUSE_RELEASED;
            default:
                return null;
        }
    }
}
