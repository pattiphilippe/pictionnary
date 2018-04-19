package Main;

import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.Client;
import view.Connection;
import view.TableSelection;

/**
 *
 * @author Philippe
 */
public class ClientFxMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Connection connectionPane = new Connection();
        Optional<Boolean> clientOpt = connectionPane.showAndWait();
        if (clientOpt.get()) {
            Client client = connectionPane.getClient();

            VBox root = new VBox();
            TableSelection tableSelect = new TableSelection();
            tableSelect.setModel(client);
            Optional<Boolean> msgOpt = tableSelect.showAndWait();
            if (msgOpt.get() instanceof Boolean && msgOpt.get()) {
                client.sendToServer(tableSelect.getMessage());
                tableSelect.show();

                Scene scene = new Scene(root, 300, 250);

                primaryStage.setTitle("Pictionnary");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        }
        //TODO exit to shutdown client
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
