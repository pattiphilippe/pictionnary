package pictionnary;

import pictionnary.drawingPane.DrawingPaneControl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author G43197
 */
public class Pictionnary extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Pictionnary");
        stage.getIcons().add(new Image("/pictionnary/icon.png"));

        RootLayout root = new RootLayout();
        root.setCenter(new DrawingPaneControl());
        Scene scene = new Scene(root);

        stage.setMinHeight(450);
        stage.setMinWidth(850);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
