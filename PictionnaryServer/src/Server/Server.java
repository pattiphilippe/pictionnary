package Server;

import Model.Model;
import Model.Player;
import Model.Table;
import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import message.Message;
import message.MessageError;
import message.MessageTables;

/**
 * Pictionnary Server
 *
 * @author Philippe
 */
public class Server extends AbstractServer {

    private static final int PORT = 10_000;
    static final String PLAYER_MAPINFO = "PLAYER";
    static final String TABLE_MAPINFO = "TABLE";

    private static InetAddress getLocalAddress() {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (InterfaceAddress f : b.nextElement().getInterfaceAddresses()) {
                    if (f.getAddress().isSiteLocalAddress()) {
                        return f.getAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "NetworkInterface error", e);
        }
        return null;
    }

    private int clientId; //to give to client... or username here
    private final List<Table> tables;

    public Server() throws IOException {
        super(PORT);

        tables = new ArrayList<>();
        clientId = 0;
        this.listen();
    }

    /**
     * Return the list of models.
     *
     * @return the list of models.
     */
    public List<message.util.Table> getTables() {
        return this.tables.stream()
                .map(model -> new message.util.Table(model.getId(), model.isOpen(), model.getPlayers()))
                .collect(Collectors.toList());
    }

    /**
     * Return the server IP address.
     *
     * @return the server IP address.
     */
    public String getIP() {
        if (getLocalAddress() == null) {
            return "Unknown";
        }
        return getLocalAddress().getHostAddress();
    }

    /**
     * Quits the server and closes all aspects of the connection to clients.
     *
     * @throws IOException
     */
    public void quit() throws IOException {
        this.stopListening();
        this.close();
    }

    /**
     * Return the next client id.
     *
     * @return the next client id.
     */
    final synchronized int getNextId() {
        clientId++;
        return clientId;
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        System.out.println("ClientConnected with name : " + client.getName());
        client.setInfo(PLAYER_MAPINFO, new Player(getNextId() + ""));
        try {
            client.sendToClient(new MessageTables(getTables()));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        switch (message.getType()) {
            case CREATE:
                createTable((String) message.getContent(), client);
                break;
            case PROFILE:
                updateName((String) message.getContent(), client);
                break;
            default:
                clientException(client, new IllegalArgumentException("Message not handled"));
                break;
        }
    }

    private void updateName(String name, ConnectionToClient client) {
        if (validId(name)) {
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            p.setUsername(name);
            client.setInfo(PLAYER_MAPINFO, p);
        } else {
            clientException(client, new IllegalArgumentException("Username already choosen on the server!"));
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createTable(String tableId, ConnectionToClient client) {
        if (!validTableId(tableId)) {
            clientException(client, new IllegalArgumentException("Table id already choosen!"));
        } else {
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            Table t = new Table(tableId, p);
            client.setInfo(PLAYER_MAPINFO, p);
            client.setInfo(TABLE_MAPINFO, t);
            tables.add(t);
            Message msg = new MessageTables(getTables());
            sendToAllClients(msg);
            setChanged();
            notifyObservers(msg);
        }
    }

    private boolean validId(String name) {
        for (Thread th : this.getClientConnections()) {
            ConnectionToClient client = (ConnectionToClient) th;
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            if (p != null && p.isUsername(name)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void clientException(ConnectionToClient client, Throwable ex) {
        super.clientException(client, ex);
        try {
            client.sendToClient(new MessageError(ex));
        } catch (IOException ex1) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private boolean validTableId(String tableId) {
        for (Thread th : this.getClientConnections()) {
            ConnectionToClient client = (ConnectionToClient) th;
            Table t = (Table) client.getInfo(TABLE_MAPINFO);
            if (t != null && t.is(tableId)) {
                return false;
            }
        }
        return true;
    }

}
