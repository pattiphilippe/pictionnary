package view;

import Controller.Controller;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import message.Message;
import message.Type;
import client.Model;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import message.util.Table;

/**
 *
 * @author Philippe
 */
public class TableSelection extends HBox implements Observer {

    /**
     * Should be Collections unmodifiableList<>()
     */
    private List<Table> tables;
    private boolean isModelSet;

    @FXML
    private TableView<TableItem> tableView;
    @FXML
    private TableColumn<TableItem, String> tableIdCol;
    @FXML
    private TableColumn<TableItem, String> stateCol;
    @FXML
    private TableColumn<TableItem, String> drawerCol;
    @FXML
    private TableColumn<TableItem, String> guesserCol;
    @FXML
    private TextField tableTfd;
    @FXML
    private Button createBtn;
    @FXML
    private Button exitBtn;
    private Controller controller;

    public TableSelection() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TableSelection.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.isModelSet = false;
    }

    @FXML
    private void initialize() {
        tableView.setRowFactory(tv -> {
            TableRow<TableItem> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (!row.isEmpty())) {
                    String tableId = (String) tableView.getColumns().get(0)
                            .getCellData(row.getIndex());
                    controller.join(tableId);
                }
            });
            return row;
        });
        tableIdCol.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        drawerCol.setCellValueFactory(new PropertyValueFactory<>("drawerName"));
        guesserCol.setCellValueFactory(new PropertyValueFactory<>("guesserName"));

        tableView.setEditable(true);
        createBtn.setOnAction(e -> {
            controller.createTable(tableTfd.getText());
        });
        exitBtn.setOnAction(e -> {
            controller.exit();
        });
    }

    public void setModel(Model client) {
        client.addObserver(this);
        this.tables = client.getTables();
        updateTableView();
        isModelSet = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Message) {
            Message msg = (Message) arg;
            if (msg.getType() == Type.TABLES) {
                Platform.runLater(() -> updateTableView());
            }
        }
    }

    private void updateTableView() {
        tableView.getItems().clear();
        for (Table t : tables) {
            // BUG pas possible avec observable list (voir Mr Lechien)
            tableView.getItems().add(new TableItem(t));
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public boolean isIsModelSet() {
        return isModelSet;
    }

}
