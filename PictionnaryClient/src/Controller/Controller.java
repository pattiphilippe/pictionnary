package Controller;

import client.Client;
import client.Model;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import message.Message;
import message.util.Player;
import message.util.WonInfos;
import pictionnary.drawingPane.DrawingInfos;
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
    private final Dialog won;
    private String errorContext;

    public Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(e -> exit());
        this.primaryStage.getIcons().add(new Image("/Controller/icon.png"));
        this.connection = new Connection();
        this.connection.setController(this);
        this.tableSelection = new TableSelection();
        this.tableSelection.setController(this);
        this.tableScene = new Scene(tableSelection);
        this.drawerView = new DrawerView(this);
        this.drawerScene = new Scene(drawerView);
        this.guesserView = new GuesserView(this);
        this.guesserScene = new Scene(guesserView);
        this.error = new MyAlert(Alert.AlertType.ERROR);
        setDialogIcon(error);
        this.won = new Dialog();
        setDialogIcon(won);
        won.setTitle("Game Won");
    }

    private void setDialogIcon(Dialog dialog) {
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/Controller/icon.png"));
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
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case PROFILE:
                        updateView((Player) msg.getContent());
                        break;
                    case WON:
                        WonInfos wonInfos = (WonInfos) msg.getContent();
                        won.setHeaderText("Congratulations to " + wonInfos.getDrawerName()
                                + " (Drawer) and " + wonInfos.getGuesserName() + " (Guesser)!");
                        won.setContentText("You have gessed the word " + wonInfos.getWordToGuess()
                                + " in " + wonInfos.getNbGuesses() + "tries."
                                + "\n You can now leave the table.");
                        won.show();
                    case ERROR:
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
            try {
                client.exit();
            } catch (IOException ex) {
                exception("Exit Server error", ex);
            }
        }
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

    public void join(String tableId) {
        try {
            client.joinTable(tableId);
        } catch (IOException ex) {
            exception("Joining Table error", ex);
        }
    }

    public void guess(String text) {
        try {
            client.guess(text);
        } catch (IOException ex) {
            exception("Guessing error", ex);
        }
    }

    private void updateView(Player player) {
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

    private void setTablesView() {
        this.errorContext = "Table Selection Error";
        primaryStage.setTitle("Pictionnary - Table Selection");
        primaryStage.setScene(tableScene);
    }

    private void setConnectionView() {
        this.errorContext = "Connection Error";
        primaryStage.setTitle("Pictionnary - Connection");
        primaryStage.setScene(new Scene(connection));
    }

    public void drawLine(DrawingInfos oldVal) {
        try {
            client.drawLine(oldVal);
        } catch (IOException ex) {
            exception("Drawing Line error", ex);
        }
    }

}
