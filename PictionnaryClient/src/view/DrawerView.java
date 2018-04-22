package view;

import Controller.Controller;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import message.Message;
import message.util.Player;
import pictionnary.drawingPane.DrawingPaneControl;

/**
 *
 * @author Philippe
 */
public class DrawerView extends VBox implements Observer {

    private final BasePictionnaryView baseView;
    private final Label word;
    private final DrawingPaneControl drawingPane;
    private final Controller controller;

    public DrawerView(Controller controller) {
        word = new Label();
        drawingPane = new DrawingPaneControl();
        drawingPane.maxWidth(Double.MAX_VALUE);
        drawingPane.maxHeight(Double.MAX_VALUE);
        this.baseView = new BasePictionnaryView();
        baseView.setCenter(drawingPane);
        baseView.setGuessTitle("To draw :");
        baseView.setGameState("Waiting for partner");
        this.controller = controller;
        baseView.setController(controller);
        drawingPane.setModifiable(false);
        this.drawingPane.lastLineProperty().addListener((obs, oldVal, newVal) -> {
            if (drawingPane.isModifiable() && newVal != null) {
                this.controller.drawLine(newVal);
            }
        });
        this.getChildren().add(baseView);
        word.setAlignment(Pos.CENTER);
        word.setMaxWidth(Double.MAX_VALUE);
        word.setFont(new Font("Berlin Sans FB", 17));
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
                        baseView.setGameState("In game");
                        break;
                    case PROFILE:
                        Player p = (Player) msg.getContent();
                        drawingPane.setModifiable(p.hasPartner());
                        break;
                    case GUESS:
                        baseView.addGuess((String) msg.getContent());
                        break;
                    case WON:
                        this.drawingPane.setModifiable(true);
                        baseView.setGameState("Won");
                        break;
                }
            });
        }
    }

}
