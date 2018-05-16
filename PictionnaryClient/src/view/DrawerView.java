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
import message.util.GameState;
import message.util.GuessUpdate;
import message.util.InitInfos;
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
    private final Label guessedIn;

    public DrawerView(Controller controller) {
        word = new Label();
        drawingPane = new DrawingPaneControl();
        drawingPane.maxWidth(Double.MAX_VALUE);
        drawingPane.maxHeight(Double.MAX_VALUE);
        this.baseView = new BasePictionnaryView();
        baseView.setCenter(drawingPane);
        baseView.setGuessTitle("To draw :");
        this.controller = controller;
        baseView.setController(controller);
        drawingPane.setModifiable(false);
        this.drawingPane.lastLineProperty().addListener((obs, oldVal, newVal) -> {
            //TODO check why drawingPane.isModifiable()?
            if (drawingPane.isModifiable()) {
                if (newVal != null) {
                    //TODO clear pane msg?
                    this.controller.drawLine(newVal);
                } else {
                    this.controller.clearDraw();
                }
            }
        });
        this.getChildren().add(baseView);
        word.setAlignment(Pos.CENTER);
        word.setMaxWidth(Double.MAX_VALUE);
        word.setFont(new Font("Berlin Sans FB", 17));
        baseView.addToWordToGuessBox(word);
        guessedIn = new Label();
        guessedIn.setAlignment(Pos.CENTER);
        guessedIn.setMaxWidth(Double.MAX_VALUE);
        guessedIn.setFont(new Font("Berlin Sans FB", 17));
        baseView.addToWordToGuessBox(guessedIn);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case GAME_INIT:
                        InitInfos infos = (InitInfos) msg.getContent();
                        word.setText(infos.getWordToGuess());
                        guessedIn.setText("Guessed in : " + infos.getAvgWrongGuesses());
                        break;
                    case PROFILE:
                        //TODO check profile infos 
//                        Player p = (Player) msg.getContent();
//                        drawingPane.setModifiable(p.hasPartner());
                        break;
                    case GUESS:
                        // redundant code in drawer and guesser View 
                        GuessUpdate guessUpdate = (GuessUpdate) msg.getContent();
                        baseView.addGuess((String) guessUpdate.getLastGuess());
                        break;
                    case GAME_STATE:
                        // redundant code in drawer and guesser View 
                        //(could be upgraded if baseview is Observer of model too)
                        GameState state = (GameState) msg.getContent();
                        this.baseView.setGameState(state.toString());
                        drawingPane.setModifiable(state == GameState.IN_GAME);
                        break;
                }
            });
        }
    }

    public void clearValues() {
        word.setText("");
        baseView.clearValues();
    }

}
