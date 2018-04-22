package view;

import Controller.Controller;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private VBox leftPanel;
    @FXML
    private Label gameState;
    @FXML
    private VBox wordToGuess;
    @FXML
    private Label guessTitle;
    @FXML
    private ListView guesses;
    @FXML
    private Button leaveBtn;
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
    private void initialize() {
        leaveBtn.setOnAction(e -> {
            controller.exitTable();
        });
        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2)));
        for (Node node : leftPanel.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                for (Node child : vbox.getChildren()) {
                    VBox.setMargin(child, new Insets(5));
                }
                vbox.setBorder(border);
            }
        }
    }

    public void addGuess(String guess) {
        guesses.getItems().add(guess);
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

    public void setGameState(String state) {
        gameState.setText(state);
    }

}
