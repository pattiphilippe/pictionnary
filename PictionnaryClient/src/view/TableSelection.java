package view;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import message.Message;
import message.Type;
import client.Client;
import client.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import message.util.Table;

/**
 *
 * @author Philippe
 */
public class TableSelection implements Observer {

    private Model model;
    /**
     * Should be Collections unmodifiableList<>()
     */
    private List<Table> tables;
    private final ObservableList<TableItem> tableItems;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<TableItem, String> tableIdCol;
    @FXML
    private TableColumn<TableItem, Boolean> isOpenCol;
    @FXML
    private TableColumn<TableItem, String> drawerCol;
    @FXML
    private TableColumn<TableItem, String> guesserCol;
    @FXML
    private TableColumn<TableItem, Button> joinCol;
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
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableItems = FXCollections.observableArrayList();
        tableView.setItems(tableItems);

        tableIdCol = new TableColumn<>("Table Id");
        tableIdCol.setCellValueFactory(new PropertyValueFactory<>("tableId"));

        isOpenCol = new TableColumn<>("Is open");
        isOpenCol.setCellValueFactory(new PropertyValueFactory<>("isOpen"));

        drawerCol = new TableColumn<>("Drawer");
        drawerCol.setCellValueFactory(new PropertyValueFactory<>("drawerName"));

        guesserCol = new TableColumn<>("Guesser");
        guesserCol.setCellValueFactory(new PropertyValueFactory<>("guesserName"));
    }

    @FXML
    private void initialize() {
        // TO INITIALIZE COMPONENTS WITH FXML
        createBtn.setOnAction(e -> {
            model.createTable(tableTfd.getText());
        });
        exitBtn.setOnAction(e -> {
            model.exit();
        });
    }

    public void setModel(Client client) {
        client.addObserver(this);
        updateTableView();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Message) {
            Message msg = (Message) arg;
            if (msg.getType() == Type.TABLES) {
                updateTableView();
            }
        }
    }

    private void updateTableView() {
        this.tables = model.getTables();
        tableItems.clear();
        for (Table t : tables) {
            tableItems.add(new TableItem(t));
        }
    }

}
