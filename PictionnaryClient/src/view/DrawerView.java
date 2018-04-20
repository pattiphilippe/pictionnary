package view;

import Controller.Controller;
import client.Client;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import message.Message;
import pictionnary.drawingPane.DrawingPaneControl;

/**
 *
 * @author Philippe
 */
public class DrawerView extends VBox implements Observer {

    private final BasePictionnaryView baseView;
    private Label word;
    private final DrawingPaneControl drawingPane;
    private Controller controller;

    public DrawerView(Controller controller) {
        word = new Label();
        drawingPane = new DrawingPaneControl();
        this.baseView = new BasePictionnaryView();
        baseView.setCenter(drawingPane);
        baseView.setGuessTitle("To draw :");
        this.getChildren().add(baseView);

        VBox wordToGuessBox = baseView.getWordToGuessBox();
        wordToGuessBox.getChildren().add(new Label("Test"));
    }

    public void setController(Controller controller) {
        this.controller = controller;
        baseView.setController(controller);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            //TODO implement
        }
    }

}
