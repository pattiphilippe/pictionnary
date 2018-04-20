package Controller;

import client.Client;
import client.Model;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.Connection;
import view.DrawerView;
import view.GuesserView;
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
    private final DrawerView drawerView;
    private final GuesserView guesserView;
    private final Stage primaryStage;
    private final MyAlert error;
    
    public Controller(Stage primaryStage) {
        this.connection = new Connection();
        this.connection.setController(this);
        this.tableSelection = new TableSelection();
        this.tableSelection.setController(this);
        this.primaryStage = primaryStage;
        this.error = new MyAlert(Alert.AlertType.ERROR);
        this.drawerView = new DrawerView(this);
        this.guesserView = new GuesserView(this);
        this.primaryStage.setOnCloseRequest(e -> exit());
    }
    
    @Override
    public void run() {
        setConnectionView();
        primaryStage.show();
    }
    
    public void connect(String host, int port, String username) {
        try {
            client = new Client(host, port, username);
            initViewsObservable();
            setTablesView();
        } catch (IOException ex) {
            exception("Connection error", ex);
        }
    }
    
    private void initViewsObservable() {
        client.addObserver(error);
        tableSelection.setModel(client);
        client.addObserver(drawerView);
        client.addObserver(guesserView);
    }
    
    public void exitTable() {
        client.exitTable();
        setTablesView();
    }
    
    public void exit() {
        if (client != null && client.isConnected()) {
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
    
    private void setDrawerView() {
        primaryStage.setTitle("Pictionnary - Drawer");
        primaryStage.setScene(new Scene(drawerView));
    }
    
    private void setGuesserView() {
        primaryStage.setTitle("Pictionnary - Guesser");
        primaryStage.setScene(new Scene(guesserView));
    }
    
    private void setTablesView() {
        primaryStage.setTitle("Pictionnary - Table Selection");
        primaryStage.setScene(new Scene(tableSelection));
    }
    
    private void setConnectionView() {
        primaryStage.setTitle("Pictionnary - Connection");
        primaryStage.setScene(new Scene(connection));
    }
    
}
