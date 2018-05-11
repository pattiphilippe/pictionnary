package client;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MessageClearDraw;
import message.MessageCreate;
import message.MessageDrawLine;
import message.MessageError;
import message.MessageExit;
import message.MessageExitTable;
import message.MessageGuess;
import message.MessageJoin;
import message.Type;
import message.MessageProfile;
import message.util.Table;
import pictionnary.drawingPane.DrawingInfos;

/**
 *
 * @author Philippe
 */
public class Client extends Model {

    private final List<Table> tables;
    private boolean closing;

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
        this.closing = false;
        this.tables = new ArrayList<>();
    }

    @Override
    protected void connectionClosed() {
        if (!closing) {
            notifyChange(new MessageError(new Exception("Client connection closed! "
                    + "The server could've crashed. Sorry for the inconvenience."
                    + " Please try to reconnect later")));
            try {
                quit();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        Type type = message.getType();
        System.out.println("client:: message received of type : " + type);
        switch (type) {
            case TABLES:
                updateTables((List<Table>) message.getContent());
            case PROFILE:
            case GAME_INIT:
            case ERROR:
            case DRAW_LINE:
            case CLEAR_DRAW:
            case GUESS:
            case WON:
            case SERVER_CLOSED:
            case GAME_STATE:
            case STATS:
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
    public void guess(String guess) throws IOException {
        sendToServer(new MessageGuess(guess));
    }

    @Override
    public void drawLine(DrawingInfos drawingInfos) throws IOException {
        Message msg = new MessageDrawLine(drawingInfos);
        sendToServer(msg);
    }

    @Override
    public void exitTable() throws IOException {
        sendToServer(new MessageExitTable());
    }

    @Override
    public void exit() {
        closing = true;
        try {
            if (isConnected()) {
                sendToServer(new MessageExit());
            }
        } catch (IOException ex) {
        } finally {
            try {
                closeConnection();
            } catch (IOException ex) {
            } finally {
            }
        }
    }

    public void drawLine() throws IOException {
        sendToServer(new MessageClearDraw());
    }

}
