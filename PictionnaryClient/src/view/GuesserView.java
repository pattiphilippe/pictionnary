package view;

import Controller.Controller;
import client.Client;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import message.Message;
import pictionnary.drawingPane.DrawingPane;

/**
 *
 * @author Philippe
 */
public class GuesserView extends VBox implements Observer {

    private final BasePictionnaryView baseView;
    private final TextField guess;
    private final DrawingPane drawingPane;
    private Controller controller;

    public GuesserView(Controller controller) {
        this.baseView = new BasePictionnaryView();
        drawingPane = new DrawingPane();
        baseView.setCenter(drawingPane);
        baseView.setGuessTitle("To guess :");
        this.getChildren().add(baseView);
        guess = new TextField();

        VBox wordToGuessBox = baseView.getWordToGuessBox();
        wordToGuessBox.getChildren().add(new TextField("Test"));
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
