package pictionnary.drawingPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author G43197
 */
public class DrawingPaneControl extends HBox {

    @FXML
    private DrawingPane drawingPane;
    @FXML
    private ColorPicker colorPk;
    @FXML
    private TextField thicknessTfd;

    private final int thicknessTfdLimit = 500;

    /**
     * Creates a new drawing pane with the possibility of modifying the options,
     * saving and loading a drawing.
     */
    public DrawingPaneControl() {
        loadingFxml();
    }

    private void loadingFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DrawingPaneControl.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DrawingPaneControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void initialize() {
        colorPk.setValue(Color.BLUE);
        drawingPane.setColor(colorPk.getValue());
    }

    @FXML
    private void colorPickerOnAction(ActionEvent e) {
        drawingPane.setColor(colorPk.getValue());
    }

    @FXML
    private void thicknessTfdHandler(KeyEvent e) {
        try {
            int nb = Integer.parseInt(e.getCharacter());
            String nbTot = thicknessTfd.getText() + nb;
            nb = Integer.parseInt(nbTot);
            if (nb > thicknessTfdLimit) {
                e.consume();
            }
        } catch (NumberFormatException ex) {
            e.consume();
        }
    }

    @FXML
    private void thicknessOnAction(ActionEvent e) {
        try {
            int nb = Integer.parseInt(thicknessTfd.getText());
            drawingPane.setThickness(nb);
        } catch (NumberFormatException ex) {
            e.consume();
        }
    }

    @FXML
    private void modifiableOnAction(ActionEvent e) {
        drawingPane.setModifiable(!drawingPane.isModifiable());
    }

    @FXML
    private void clearOnAction(ActionEvent e) {
        drawingPane.clearPane();
    }

    @FXML
    private void saveOnAction(ActionEvent e) throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Drawing");
        Window ownerWindow = ((Node) e.getTarget()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(ownerWindow);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(drawingPane.getDrawingInfos());
        }
//        if (file != null) {
//            Image image = drawingPane.getDrawingInfos().getImage();
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        }
    }

    @FXML
    private void loadOnAction(ActionEvent e) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Drawing");
        Window ownerWindow = ((Node) e.getTarget()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(ownerWindow);
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            drawingPane.setDrawingInfos((DrawingInfos) in.readObject());
        }
//        if (file != null) {
//            Image image = SwingFXUtils.toFXImage(ImageIO.read(file), null);
//            drawingPane.setDrawingInfos(new DrawingInfos(image));
//        }
    }
}
