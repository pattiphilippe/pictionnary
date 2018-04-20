package Controller;

import client.Client;
import client.Model;
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
    private final Scene scene;

    public Controller(Stage primaryStage) {
        this.connection = new Connection();
        this.connection.setController(this);
        this.tableSelection = new TableSelection();
        this.tableSelection.setController(this);
        this.primaryStage = primaryStage;
        this.error = new Alert(Alert.AlertType.ERROR);
        this.scene = new Scene(connection);
        this.primaryStage.setScene(scene);
    }

    @Override
    public void run() {
        primaryStage.show();
    }

    public void connect(String host, int port, String username) {
        try {
            client = new Client(host, port, username);
        } catch (IOException ex) {
            showError("Connection error", ex);
        }
        tableSelection.setModel(client);
        System.out.println("after client creation is connected : " + client.isConnected());
        scene.setRoot(tableSelection);
        System.out.println("after new Scene is connected : " + client.isConnected());
        this.primaryStage.setScene(scene);
        System.out.println("after set Scene is connected : " + client.isConnected());
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
        System.out.println("before creating table is connected : " + client.isConnected());
        try {
            client.createTable(tableId);
        } catch (IOException ex) {
            showError("Creating Table error", ex);
        }
    }

}
