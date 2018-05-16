package view;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import message.Message;
import message.util.GuessUpdate;
import message.util.Prop;

/**
 *
 * @author G43197
 */
public class PropsView extends VBox implements Observer {
    
    private final Label title;
    private TableView<PropItem> tableView;
    private TableColumn<TableItem, String> propCol;
    private TableColumn<TableItem, Integer> countCol;
    
    public PropsView(double d) {
        super(d);
        this.tableView = new TableView<>();
        tableView.setEditable(true);
        title = new Label("Propositions with the number of times they have been proposed");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setFont(new Font("Berlin Sans FB", 20));
        
        propCol = new TableColumn<>();
        countCol = new TableColumn<>();
        propCol.setCellValueFactory(new PropertyValueFactory<>("prop"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        
        this.getChildren().addAll(title, tableView);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case GUESS:
                        GuessUpdate guessUpdate = (GuessUpdate) msg.getContent();
                        List<Prop> props = guessUpdate.getProps();
                        tableView.getItems().clear();
                        for (Prop prop : props) {
                            System.out.println("prop x ");
                            tableView.getItems().add(new PropItem(prop));
                        }
                        break;
                }
            });
        }
    }
    
    public void clearValues() {
        tableView.getItems().clear();
    }
    
}
