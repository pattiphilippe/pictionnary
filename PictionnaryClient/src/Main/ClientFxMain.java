package Main;

import Controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Philippe
 */
public class ClientFxMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller ctrl = new Controller(primaryStage);
        ctrl.run();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
