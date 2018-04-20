package Controller;

import client.Client;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.Connection;
import view.TableSelection;

/**
 *
 * @author G43197
 */
public class Controller implements Runnable {

    //TODO CHANGE TO MODEL
    private Client client;
    private final Connection connection;
    private final TableSelection tableSelection;
    private final Stage primaryStage;
    private final Alert error;

    public Controller(Stage primaryStage) {
        this.connection = new Connection();
        this.connection.setController(this);
        this.tableSelection = new TableSelection();
        this.tableSelection.setController(this);
        this.primaryStage = primaryStage;
        this.error = new Alert(Alert.AlertType.ERROR);
    }

    @Override
    public void run() {
        primaryStage.setScene(new Scene(connection));
        primaryStage.show();
    }

    public void connect(String host, int port, String username) {
        try {
            client = new Client(host, port, username);
        } catch (IOException ex) {
            showError("Connection error", ex);
        }
        tableSelection.setModel(client);
        this.primaryStage.setScene(new Scene(tableSelection));
    }

    public void exit() {
        client.exit();
    }

    private void showError(String context, IOException ex) {
        error.setHeaderText(context);
        error.setContentText(ex.getMessage());
        error.showAndWait();
    }

    public void createTable(String tableId) {
        try {
            client.createTable(tableId);
        } catch (IOException ex) {
            showError("Creating Table error", ex);
        }
    }

}
