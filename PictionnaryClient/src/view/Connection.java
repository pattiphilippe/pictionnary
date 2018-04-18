package view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Client;

/**
 *
 * @author Philippe
 */
public class Connection extends Dialog<Boolean> {

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
    private final Alert error;

    private Client client;

    public Connection() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Connection.fxml"));
        loader.setController(this);
        try {
            this.getDialogPane().setContent(loader.load());
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setTitle("Pictionnary - Connection");
        //TODO stage.getIcons().add(ICON);

        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = this.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
        
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
                try {
                    client = new Client(host.getText(), Integer.parseInt(portTxt), username.getText());
                    this.setResult(Boolean.TRUE);
                } catch (IOException ex) {
                    error.setHeaderText("Connection error!");
                    error.setContentText(ex.getMessage());
                    error.showAndWait();
                }
            }
        });
        cancelBtn.setOnAction(e -> {
            this.setResult(Boolean.FALSE);
        });
    }

    Client getClient() {
        return client;
    }

}
