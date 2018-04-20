package view;

import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.Alert;
import message.Message;
import message.Type;

/**
 *
 * @author G43197
 */
public class MyAlert extends Alert implements Observer {

    public MyAlert(AlertType alertType) {
        super(alertType);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            if (msg.getType() == Type.ERROR) {
                Exception ex = (Exception) msg.getContent();
                this.setHeaderText(ex.getMessage());
            }
        }
    }

}
