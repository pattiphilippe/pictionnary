package Controller;

import client.Client;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import message.Message;
import message.util.Player;
import view.Connection;
import view.DrawerView;
import view.GuesserView;
import view.MyAlert;
import view.TableSelection;

/**
 *
 * @author G43197
 */
public class Controller implements Runnable, Observer {

    //TODO change to model
    private Client client;
    private final Connection connection;
    private final TableSelection tableSelection;
    private final Scene tableScene;
    private final DrawerView drawerView;
    private final Scene drawerScene;
    private final GuesserView guesserView;
    private final Scene guesserScene;
    private final Stage primaryStage;
    private final MyAlert error;
    private String errorContext;

    public Controller(Stage primaryStage) {
        this.connection = new Connection();
        this.connection.setController(this);
        this.tableSelection = new TableSelection();
        this.tableSelection.setController(this);
        this.tableScene = new Scene(tableSelection);
        this.primaryStage = primaryStage;
        this.error = new MyAlert(Alert.AlertType.ERROR);
        this.drawerView = new DrawerView(this);
        this.drawerScene = new Scene(drawerView);
        this.guesserView = new GuesserView(this);
        this.guesserScene = new Scene(guesserView);
        this.primaryStage.setOnCloseRequest(e -> exit());
    }

    @Override
    public void run() {
        setConnectionView();
        primaryStage.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            System.out.println("Controller::update() with message type " + msg.getType());
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case PROFILE:
                        updateView((Player) msg.getContent());
                        break;
                    case ERROR:
                        //TODO gamestate enum : context
                        exception(errorContext, (Exception) msg.getContent());
                        break;
                    default:
                }
            });
        }
    }

    public void connect(String host, int port, String username) {
        try {
            client = new Client(host, port, username);
            initViewsObservable();
        } catch (IOException ex) {
            exception(errorContext, ex);
        }
    }

    private void initViewsObservable() {
        client.addObserver(this);
        client.addObserver(error);
        tableSelection.setModel(client);
        client.addObserver(drawerView);
        client.addObserver(guesserView);
    }

    public void exitTable() {
        try {
            client.exitTable();
        } catch (IOException ex) {
            exception("Exit Table error", ex);
        }
    }

    public void exit() {
        if (client != null && client.isConnected()) {
            client.exit();
        }
        //primaryStage.close();
        Platform.exit();
    }

    public void exception(String context, Exception ex) {
        error.setHeaderText(context);
        error.setContentText(ex.getMessage());
        error.showAndWait();
    }

    public void createTable(String tableId) {
        System.out.println("--> controller::createTable()");
        try {
            client.createTable(tableId);
        } catch (IOException ex) {
            exception("Creating Table error", ex);
        }
    }

    public void join(String tableId) {
        try {
            client.joinTable(tableId);
        } catch (IOException ex) {
            exception("Joining Table exception", ex);
        }
    }

    public void guess(String text) {
        client.guess(text);
    }

    private void updateView(Player player) {
        System.out.println("Controller::updateView with " + player.getRole());
        switch (player.getRole()) {
            case DRAWER:
                setDrawerView();
                break;
            case GUESSER:
                setGuesserView();
                break;
            case NONE:
                setTablesView();
                break;
            default:
                throw new IllegalArgumentException("Unknow role of player!");
        }
    }

    private void setDrawerView() {
        this.errorContext = "Drawer Error";
        primaryStage.setTitle("Pictionnary - Drawer");
        primaryStage.setScene(drawerScene);
    }

    private void setGuesserView() {
        this.errorContext = "Guesser Error";
        primaryStage.setTitle("Pictionnary - Guesser");
        primaryStage.setScene(guesserScene);
    }

    //TODO change to private
    public void setTablesView() {
        this.errorContext = "Table Selection Error";
        primaryStage.setTitle("Pictionnary - Table Selection");
        primaryStage.setScene(tableScene);
    }

    private void setConnectionView() {
        this.errorContext = "Connection Error";
        primaryStage.setTitle("Pictionnary - Connection");
        primaryStage.setScene(new Scene(connection));
    }

}
