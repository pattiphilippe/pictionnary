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
        this.baseView = new BasePictionnaryView();
        this.drawingPane = new DrawingPane();
        this.drawingPane.setModifiable(false);
        this.baseView.setCenter(drawingPane);
        this.baseView.setGuessTitle("To guess :");
        this.getChildren().add(baseView);
        this.guess = new TextField();
        this.guessBtn = new Button("OK");
        this.guessBtn.setWrapText(true);
        this.guessBtn.setFont(new Font("Berlin Sans FB", 17));
        guessBtn.setOnAction(e -> {
            //TODO BUG guess is null
            this.controller.guess(guess.getText());
        });
        HBox guessBox = new HBox(10, guess, guessBtn);

        baseView.addToWordToGuessBox(guessBox);
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
                    case DRAW_LINE:
                        System.out.println("before draw line");
                        drawingPane.addLine((DrawingInfos) msg.getContent());
                        System.out.println("after draw line");
                        break;
                }
            });
        }
    }
}
