package pictionnary.drawingPane;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public interface IDrawing {

    /**
     * Set an initial value to all attributes.
     */
    void initialize();

    /**
     * Clear the drawing pane.
     */
    void clearPane();

    /**
     * Return data needed to draw. These datas can be a set of points with color
     * and thickness.
     *
     * @return data needed to draw
     */
    DrawingInfos getDrawingInfos();

    /**
     * Set all the data needed to draw a picture a component.
     *
     * @param dInfos data needed to draw a picture a component (can be a set of
     * points with color and thickness)
     */
    void setDrawingInfos(DrawingInfos dInfos);

    /**
     * Return the colorProperty of the drawing line.
     *
     * @return the colorProperty of the drawing line
     */
    ObjectProperty<Color> colorProperty();

    /**
     * Set the color value of the drawing line.
     *
     * @param color color value of the drawing line
     */
    void setColor(Color color);

    /**
     * Return the color value of the drawing line.
     *
     * @return the color value of the drawing line
     */
    Color getColor();

    /**
     * Return the thicknessProperty of the drawing line.
     *
     * @return the thicknessProperty of the drawing line
     */
    ObjectProperty<Integer> thicknessProperty();

    /**
     * Set the thickness value of the drawing line.
     *
     * @param thickness thickness value of the drawing line
     */
    void setThickness(int thickness);

    /**
     * Return the thickness value of the drawing line.
     *
     * @return the thickness value of the drawing line
     */
    int getThickness();

}
