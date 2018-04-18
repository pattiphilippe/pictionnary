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
    private final EventType<? extends MouseEvent> mouseEvent;

    DrawingInfo(double x, double y, int thickness, String color, EventType<? extends MouseEvent> mouseEvent) {
        this.x = x;
        this.y = y;
        this.thickness = thickness;
        this.color = color;
        this.mouseEvent = mouseEvent;
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
        return mouseEvent;
    }
}
