package view;

import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message.Message;
import model.Client;

/**
 *
 * @author Philippe
 */
public class ClientFxMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        Connection connectionPane = new Connection();
        Optional<Boolean> clientOpt = connectionPane.showAndWait();
        if (clientOpt.get() instanceof Boolean && clientOpt.get()) {
            Client client = connectionPane.getClient();

            VBox root = new VBox();

            TableSelection tableSelect = new TableSelection();
            tableSelect.setModel(client);
            Optional<Message> msg = tableSelect.showAndWait();
            Scene scene = new Scene(root, 300, 250);

            primaryStage.setTitle("Pictionnary");
            primaryStage.setScene(scene);
            primaryStage.show();
            //TODO exit to shutdown client
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
