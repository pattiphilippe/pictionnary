package view;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import message.Message;
import message.Type;
import message.util.Table;
import message.util.Tables;
import model.Client;

/**
 *
 * @author Philippe
 */
public class TableSelection extends Dialog<Message> implements Observer {

    @FXML
    private ListView<Table> tables;

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
    }

    @FXML
    private void initialize() {
        // TO INITIALIZE COMPONENTS WITH FXML
    }

    public void setModel(Client client) {
        client.addObserver(this);
        updateTables(client.getTables());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Message) {
            Message msg = (Message) arg;
            if (msg.getType() == Type.TABLES) {
                updateTables((Tables) msg.getContent());
            }
        }
    }

    private void updateTables(Tables tables) {
        if (this.tables.getItems() != null) {
            this.tables.getItems().clear();
        }
        for (Table t : tables) {
            this.tables.getItems().add(t);
        }
    }

}
