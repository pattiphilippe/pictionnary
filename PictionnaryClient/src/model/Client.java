package model;

import client.AbstractClient;
import java.io.IOException;
import java.util.Collections;
import message.*;
import message.util.Table;
import message.util.Tables;

/**
 *
 * @author Philippe
 */
public class Client extends AbstractClient {

    private final Tables tables;

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
        this.tables = new Tables();
    }

    public Tables getTables() {
        //TODO protéger plus
        return tables;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        Type type = message.getType();
        switch (type) {
            case TABLES:
                updateTables((Tables) message.getContent());
                notifyChange(message);
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

    void showMessage(Message message) {
        notifyChange(message);
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }

    private void notifyChange(Message message) {
        setChanged();
        notifyObservers(message);
    }

    private void updateTables(Tables tables) {
        this.tables.clear();
        for (Table t : tables) {
            this.tables.add(t);
        }
        setChanged();
        notifyChange();
    }

}
