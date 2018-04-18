package Server;

import Model.Player;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.MessageTables;
import message.util.Tables;

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
    private final Tables tables;

    public Server() throws IOException {
        super(PORT);
        tables = new Tables();
        clientId = 0;
        this.listen();
    }

    /**
     * Return the list of tables.
     *
     * @return the list of tables.
     */
    public Tables getTables() {
        return tables;
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
        if (!validId(client.getName())) {
            try {
                clientException(client, new IllegalArgumentException("Username already choosen on the server!"));
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            super.clientConnected(client);
            client.setInfo(PLAYER_MAPINFO, new Player(client.getName()));
            try {
                client.sendToClient(new MessageTables(tables));
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        //TODO IMPLEMENT
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
        //TODO envoyer message error
    }

}
