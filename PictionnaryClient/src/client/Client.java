package client;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MessageCreate;
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
        return tables;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        Type type = message.getType();
        switch (type) {
            case TABLES:
                updateTables((List<Table>) message.getContent());
                notifyChange(message);
                break;
            case ERROR:
                //show error with alert
                //check if socket not null
                if (!isConnected()) {
                    try {
                        quit();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
        //TODO à supprimer
        StringBuilder msg = new StringBuilder();
        msg.append("Tables:").append("\n");
        for (Table table : tables) {
            msg.append(table.getTableId())
                    .append(", isOpen : ").append(table.isOpen())
                    .append(", drawer name : ").append(table.getDrawerName())
                    .append(", guesser : ").append(table.getGuesserName())
                    .append("\n");

        }
        System.out.println(msg);
    }

    @Override
    public final void updateName(String name) {
        try {
            sendToServer(new MessageProfile(name));
        } catch (IOException ex) {
            //TODO gérer erreur
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createTable(String tableId) {
        try {
            sendToServer(new MessageCreate(tableId));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void joinTable(String tableId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guess(String guess) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(DrawingInfos drawingInfos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exitTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
