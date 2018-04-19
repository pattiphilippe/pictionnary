package view;

import Model.Model;
import Model.Table;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import message.Message;
import message.MessageCreate;
import message.Type;
import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

/**
 *
 * @author Philippe
 */
public class TableSelection extends Dialog<Boolean> implements Observer {

    private Message msg;
    private final ObservableList<ModelItem> tableItems;

    @FXML
    private TableView<Model> tables;
    @FXML
    private TableColumn<Model, String> tableCol;
    @FXML
    private TableColumn<Model, Boolean> isOpenCol;
    @FXML
    private TableColumn<Model, String> drawerCol;
    @FXML
    private TableColumn<Model, String> guesserCol;
    @FXML
    private TableColumn<Model, Button> joinCol;
    @FXML
    private TextField tableTfd;
    @FXML
    private Button createBtn;
    @FXML
    private Button exitBtn;

    public TableSelection() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TableSelection.fxml"));
        loader.setController(this);
        try {
            this.getDialogPane().setContent(loader.load());
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setTitle("Pictionnary - Table Selection");
        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = this.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
        tableItems = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        // TO INITIALIZE COMPONENTS WITH FXML
        createBtn.setOnAction(e -> {
            msg = new MessageCreate(tableTfd.getText());
            this.setResult(Boolean.TRUE);
        });
        exitBtn.setOnAction(e -> {
            this.setResult(Boolean.FALSE);
        });
    }

    public void setModel(Client client) {
        client.addObserver(this);
        updateTables(client.getTables());
    }

    public Message getMessage() {
        return msg;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Message) {
            Message msg = (Message) arg;
            if (msg.getType() == Type.TABLES) {
                updateTables((List<Model>) msg.getContent());
            }
        }
    }

    private void updateTables(List<Model> tables) {
        tableItems.clear();
        tables.forEach((model) -> {
            this.tableItems.add(new ModelItem(model));
        });
    }

}
