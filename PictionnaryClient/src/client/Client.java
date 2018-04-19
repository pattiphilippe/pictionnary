package client;

import java.io.IOException;
import java.util.List;
import message.Message;
import message.Type;
import Model.Model;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.MessageProfile;

/**
 *
 * @author Philippe
 */
public class Client extends AbstractClient {

    private final List<Model> tables;

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

    public List<Model> getTables() {
        return tables;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        Type type = message.getType();
        switch (type) {
            case TABLES:
                updateTables((List<Model>) message.getContent());
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

    private void updateTables(List<Model> tables) {
        this.tables.clear();
        System.out.println("Tables:");
        for (Model t : tables) {
            this.tables.add(t);
            System.out.print(t.getId() + ", isOpen : " + t.isOpen() + " , players : ");
            for (String p : t.getPlayers()) {
                System.out.print("  " + p);
            }
            System.out.println("");
        }
    }

    private void updateName(String name) throws IOException {
        sendToServer(new MessageProfile(name));
    }

}
