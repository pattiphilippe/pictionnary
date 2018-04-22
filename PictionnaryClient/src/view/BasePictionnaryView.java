package view;

import Controller.Controller;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Philippe
 */
public class BasePictionnaryView extends BorderPane {

    @FXML
    private VBox wordToGuess;
    @FXML
    private Label guessTitle;
    @FXML
    private Button leaveTable;
    private Controller controller;

    public BasePictionnaryView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BasePictionnaryView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void initiliaze() {
        leaveTable.setOnAction(e -> {System.out.println("in leave on action");controller.exitTable();});
        wordToGuess.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(20))));
        wordToGuess.setEffect(new DropShadow());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void addToWordToGuessBox(Node node) {
        wordToGuess.getChildren().add(node);
    }

    public void setGuessTitle(String txt) {
        guessTitle.setText(txt);
    }

}
