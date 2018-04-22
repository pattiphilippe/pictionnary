package client;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.input.MouseEvent;
import message.Message;
import message.MessageCreate;
import message.MessageDrawLine;
import message.MessageExit;
import message.MessageExitTable;
import message.MessageJoin;
import message.Type;
import message.MessageProfile;
import message.util.Table;
import pictionnary.drawingPane.DrawingInfos;

/**
 *
 * @author Philippe
 */
public class Client extends AbstractClient implements Model {

    private final List<Table> tables;

    /**
     * Constructs the client. Opens the connection with the server. Sends the
     * user name inside a <code> MessageProfile </code> to the server. Builds an
     * empty list of users.
     *
     * @param host the server's host name.
     * @param port the port number.
     * @param name the name of the user.
     * @throws IOException if an I/O error occurs when opening.
     */
    public Client(String host, int port, String name) throws IOException {
        super(host, port);
        openConnection();
        updateName(name);
        this.tables = new ArrayList<>();
    }

    @Override
    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        System.out.println("message received of type : " + message.getType());
        Type type = message.getType();
        switch (type) {
            case TABLES:
                updateTables((List<Table>) message.getContent());
            case PROFILE:
            case GAME_INIT:
            case ERROR:
            case DRAW_LINE:
                notifyChange(message);
                break;
            default:
                throw new IllegalArgumentException("Message type unknown " + type);
        }
    }

    /**
     * Quits the client and closes all aspects of the connection to the server.
     *
     * @throws IOException if an I/O error occurs when closing.
     */
    public void quit() throws IOException {
        closeConnection();
    }

    private void notifyChange(Message message) {
        setChanged();
        notifyObservers(message);
    }

    private void updateTables(List<Table> tables) {
        this.tables.clear();
        for (Table t : tables) {
            this.tables.add(t);
        }
    }

    @Override
    public final void updateName(String name) throws IOException {
        sendToServer(new MessageProfile(name));
    }

    @Override
    public void createTable(String tableId) throws IOException {
        sendToServer(new MessageCreate(tableId));
    }

    @Override
    public void joinTable(String tableId) throws IOException {
        sendToServer(new MessageJoin(tableId));
    }

    @Override
    public void guess(String guess) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drawLine(DrawingInfos drawingInfos) throws IOException {
        System.out.println("Client::drawLine()");
        Message msg = new MessageDrawLine(drawingInfos);
        sendToServer(msg);
    }

    @Override
    public void exitTable() throws IOException {
        sendToServer(new MessageExitTable());
    }

    @Override
    public void exit() throws IOException {
        sendToServer(new MessageExit());
    }

}
