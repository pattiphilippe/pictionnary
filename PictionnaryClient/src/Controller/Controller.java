package Controller;

import client.Client;
import client.Model;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.Connection;
import view.MyAlert;
import view.TableSelection;

/**
 *
 * @author G43197
 */
public class Controller implements Runnable {

    //TODO change to model
    private Client client;
    private final Connection connection;
    private final TableSelection tableSelection;
    private final Stage primaryStage;
    private final MyAlert error;

    public Controller(Stage primaryStage) {
        this.connection = new Connection();
        this.connection.setController(this);
        this.tableSelection = new TableSelection();
        this.tableSelection.setController(this);
        this.primaryStage = primaryStage;
        this.error = new MyAlert(Alert.AlertType.ERROR);
    }

    @Override
    public void run() {
        primaryStage.setScene(new Scene(connection));
        primaryStage.show();
    }

    public void connect(String host, int port, String username) {
        try {
            client = new Client(host, port, username);
            client.addObserver(error);
            tableSelection.setModel(client);
            this.primaryStage.setScene(new Scene(tableSelection));
        } catch (IOException ex) {
            exception("Connection error", ex);
        }
    }

    public void exit() {
        if (client.isConnected()) {
            client.exit();
        }
        primaryStage.close();
        Platform.exit();
    }

    public void exception(String context, Exception ex) {
        error.setHeaderText(context);
        error.setContentText(ex.getMessage());
        error.showAndWait();
    }

    public void createTable(String tableId) {
        try {
            client.createTable(tableId);
        } catch (IOException ex) {
            exception("Creating Table error", ex);
        }
    }

}
