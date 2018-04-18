package pictionnary.drawingPane;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 *
 * @author G43197
 */
public class DrawingPane extends Region implements IDrawing {

    private final Canvas canvas;
    private final GraphicsContext context;
    private final ObjectProperty<Color> color;
    private final ObjectProperty<Integer> thickness;
    private final BooleanProperty modifiable;
    private DrawingInfos drawingInfos;

    /**
     * Creates a drawing pane with all the idrawing methods and possibilities.
     */
    public DrawingPane() {
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setStyle("-fx-background-color : white");

        color = new ObjectPropertyBase<Color>() {
            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "Color";
            }
        };

        thickness = new ObjectPropertyBase<Integer>() {
            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "Thickness";
            }
        };

        modifiable = new BooleanPropertyBase(true) {
            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "Modifiability";
            }
        };

        this.canvas = new Canvas();
        this.context = canvas.getGraphicsContext2D();
        this.drawingInfos = new DrawingInfos();

        getChildren().add(canvas);
        initialize();
    }

    @Override
    public void initialize() {
        //TODO ? purpose of initialize? init canvas pos? properties value? drawing handlers?
        //initialising canvas position with border
        double left = getBorder().getStrokes().get(0).getWidths().getLeft();
        canvas.widthProperty().bind(widthProperty().subtract(left * 2));
        canvas.heightProperty().bind(heightProperty().subtract(left * 2));
        canvas.setTranslateX(left);
        canvas.setTranslateY(left);

        // init properties value
        color.set(Color.BLUE);
        thickness.set(5);

        // addind mouse handlers
        canvas.setOnMousePressed(e -> handleMouseEvent(e));
        canvas.setOnMouseDragged(e -> handleMouseEvent(e));
        canvas.setOnMouseReleased(e -> handleMouseEvent(e));
    }

    @Override
    public void clearPane() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @Override
    public DrawingInfos getDrawingInfos() {
        return drawingInfos;
    }

    @Override
    public void setDrawingInfos(DrawingInfos dInfos) {
        clearPane();
        this.drawingInfos = dInfos;
        for (DrawingInfo point : dInfos) {
            addPoint(point.getX(), point.getY(), point.getThickness(), point.getColor(), point.getMouseEvent());
        }
    }

    @Override
    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color.set(color);
    }

    @Override
    public Color getColor() {
        return this.color.get();
    }

    @Override
    public ObjectProperty<Integer> thicknessProperty() {
        return thickness;
    }

    @Override
    public void setThickness(int thickness) {
        this.thickness.set(thickness);
    }

    @Override
    public int getThickness() {
        return this.thickness.get();
    }

    /**
     * Returns true if it is possible to draw.
     *
     * @return
     */
    public boolean isModifiable() {
        return modifiable.get();
    }

    /**
     * Sets the modifiable property value to true.
     *
     * @param modifiable
     */
    public void setModifiable(boolean modifiable) {
        this.modifiable.set(modifiable);
    }

    /**
     * Returns the modifiable property.
     *
     * @return
     */
    public BooleanProperty modifiableProperty() {
        return modifiable;
    }

    private void handleMouseEvent(MouseEvent e) {
        if (modifiable.get()) {
            drawingInfos.addPoint(e.getX(), e.getY(), thickness.get(), color.get().toString(), e.getEventType());
            this.addPoint(e.getX(), e.getY(), thickness.get(), color.get().toString(), e.getEventType());
        }
    }

    private void addPoint(double x, double y, int thickness, String color, EventType<? extends MouseEvent> type) {
        if (type == MouseEvent.MOUSE_PRESSED) {
            context.beginPath();
            context.moveTo(x, y);
            context.setStroke(Color.valueOf(color));
            context.setLineWidth(thickness);
            context.stroke();
        } else if (type == MouseEvent.MOUSE_DRAGGED) {
            context.lineTo(x, y);
            context.setStroke(Color.valueOf(color));
            context.setLineWidth(thickness);
            context.stroke();
        } else if (type == MouseEvent.MOUSE_RELEASED) {
            x = x - thickness / 2;
            y = y - thickness / 2;
            context.setFill(Color.valueOf(color));
            context.fillRect(x, y, thickness, thickness);
        }
    }
}
