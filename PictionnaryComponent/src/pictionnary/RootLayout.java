package pictionnary;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Philippe
 */
public class RootLayout extends BorderPane {

    @FXML
    private MenuItem close;

    /**
     * Creates a Border Pane with a simple menu bar
     */
    public RootLayout() {
        super();
        loadingFxml();
    }

    private void loadingFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RootLayout.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void initialize() {
        close.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
    }

    @FXML
    private void closeOnAction() {
        Platform.exit();
    }
}
