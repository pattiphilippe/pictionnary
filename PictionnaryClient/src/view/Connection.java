package view;

import Controller.Controller;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import client.Client;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Philippe
 */
public class Connection extends GridPane {

    @FXML
    private TextField username;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private Button connectBtn;
    @FXML
    private Button cancelBtn;
    //TODO check if needed
    private final Alert error;

    private Controller controller;

    public Connection() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Connection.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Input Error");
    }

    @FXML
    private void initialize() {
        port.addEventHandler(KeyEvent.ANY, e -> {
            try {
                if (e.getCode() != KeyCode.BACK_SPACE) {
                    int newPort = Integer.parseInt(port.getText() + e.getCharacter());
                    if (newPort > 0xFFFF) {
                        throw new IllegalArgumentException("Port out of range!");
                    }
                }
            } catch (IllegalArgumentException ex) {
                e.consume();
            }
        });
        connectBtn.setOnAction(e -> {
            String portTxt = port.getText();
            if (portTxt.equals("") || host.getText().equals("")) {
                error.setHeaderText("Port or username empty!");
                error.setContentText("Insert a username and ask the admin for the port.");
                error.showAndWait();
            } else {
                controller.connect(host.getText(), Integer.parseInt(portTxt), username.getText());
            }
        });
        cancelBtn.setOnAction(e -> {
            //TODO
        });
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
