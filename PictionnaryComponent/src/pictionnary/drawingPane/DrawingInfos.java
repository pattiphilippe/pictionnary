package pictionnary.drawingPane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author G43197
 */
public class DrawingInfos implements Serializable, Iterable<DrawingInfo> {

    private List<DrawingInfo> drawingInfos;

    public DrawingInfos() {
        this.drawingInfos = new ArrayList<>();
    }

    public DrawingInfos(DrawingInfos other) {
        this.drawingInfos = new ArrayList<>();
        for (DrawingInfo point : other) {
            this.drawingInfos.add(point);
        }
    }

    void addPoint(double x, double y, int thickness, String color, EventType<? extends MouseEvent> mouseEvent) {
        drawingInfos.add(new DrawingInfo(x, y, thickness, color, mouseEvent));
    }

    public List<DrawingInfo> getPoints() {
        return drawingInfos;
    }

    @Override
    public Iterator<DrawingInfo> iterator() {
        return drawingInfos.iterator();
    }

    public void clear() {
        drawingInfos.clear();
    }
}
