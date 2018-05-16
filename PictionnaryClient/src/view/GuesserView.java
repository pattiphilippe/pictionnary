package view;

import Controller.Controller;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import message.Message;
import message.util.GameState;
import message.util.GuessUpdate;
import pictionnary.drawingPane.DrawingInfos;
import pictionnary.drawingPane.DrawingPane;

/**
 *
 * @author Philippe
 */
public class GuesserView extends VBox implements Observer {

    private final BasePictionnaryView baseView;
    private final TextField guess;
    private final DrawingPane drawingPane;
    private final Button guessBtn;
    private Controller controller;

    public GuesserView(Controller controller) {
        this.setPadding(new Insets(15));
        this.controller = controller;
        this.baseView = new BasePictionnaryView();
        baseView.setController(controller);
        this.drawingPane = new DrawingPane();
        this.drawingPane.setModifiable(false);
        this.baseView.setCenter(drawingPane);
        this.baseView.setGameState("In Game");
        this.baseView.setGuessTitle("Guess :");
        this.getChildren().add(baseView);
        this.guess = new TextField();
        guess.getText();
        this.guessBtn = new Button("OK");
        this.guessBtn.setWrapText(true);
        this.guessBtn.setFont(new Font("Berlin Sans FB", 17));
        guessBtn.setOnAction(e -> {
            Platform.runLater(() -> {
                String guess = this.guess.getText();
                if (!guess.equals("")) {
                    this.controller.guess(guess);
                }
            });
        });
        HBox guessBox = new HBox(10, guess, guessBtn);

        baseView.addToWordToGuessBox(guessBox);
        guess.getText();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case DRAW_LINE:
                        drawingPane.addLine((DrawingInfos) msg.getContent());
                        break;
                    case CLEAR_DRAW:
                        drawingPane.clearPane();
                        break;
                    case GUESS:
                        GuessUpdate guessUpdate = (GuessUpdate) msg.getContent();
                        baseView.addGuess((String) guessUpdate.getLastGuess());
                        break;
                    case GAME_STATE:
                        // only redundant code in drawer and guesser View 
                        //(could be upgraded if baseview is Observer of model too)
                        GameState state = (GameState) msg.getContent();
                        this.baseView.setGameState(state.toString());
                        this.guessBtn.setDisable(state != GameState.IN_GAME);
                        break;
                }
            });
        }
    }

    public void clearValues() {
        guess.setText("");
        baseView.clearValues();
    }
}
