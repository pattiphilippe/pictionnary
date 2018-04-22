package view;

import Controller.Controller;
import client.Client;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import message.Message;
import message.Type;
import pictionnary.drawingPane.DrawingPaneControl;

/**
 *
 * @author Philippe
 */
public class DrawerView extends VBox implements Observer {

    private final BasePictionnaryView baseView;
    private final Label word;
    private final DrawingPaneControl drawingPane;
    private Controller controller;

    public DrawerView(Controller controller) {
        word = new Label();
        drawingPane = new DrawingPaneControl();
        this.baseView = new BasePictionnaryView();
        baseView.setCenter(drawingPane);
        baseView.setGuessTitle("To draw :");
        this.getChildren().add(baseView);
        word.setAlignment(Pos.CENTER);
        word.setMaxWidth(Double.MAX_VALUE);
        word.setFont(new Font("Berlin Sans FB", 17));
    }

    public void setController(Controller controller) {
        this.controller = controller;
        baseView.setController(controller);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case GAME_INIT:
                        word.setText((String) msg.getContent());
                        baseView.addToWordToGuessBox(word);
                        break;
                }
            });
        }
    }

}
