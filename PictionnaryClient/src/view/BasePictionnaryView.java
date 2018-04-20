package view;

import Controller.Controller;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
        leaveTable.setOnAction(e -> controller.exitTable());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    public VBox getWordToGuessBox(){
        return wordToGuess;
    }
    
    public void setGuessTitle(String txt){
        guessTitle.setText(txt);
    }

}
