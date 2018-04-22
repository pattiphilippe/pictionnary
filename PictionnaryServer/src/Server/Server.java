package Server;

import OneVOneModel.GameException;
import OneVOneModel.GameState;
import OneVOneModel.Model;
import OneVOneModel.Player;
import static OneVOneModel.PlayerRole.DRAWER;
import OneVOneModel.Table;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import message.Message;
import message.MessageError;
import message.MessageGameInit;
import message.MessageProfile;
import message.MessageTables;
import message.util.PlayerRole;

/**
 * Pictionnary Server
 *
 * @author Philippe
 */
public class Server extends AbstractServer implements Observer {

    private static final int PORT = 10_000;
    static final String PLAYER_MAPINFO = "PLAYER";
    static final String TABLE_MAPINFO = "TABLE";
    static final String PARTNER_CLIENT_INFO = "PARTNER";

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
                .map(model -> new message.util.Table(
                model.getId(), model.isOpen(), model.getPlayerNames()))
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
        //TODO sendToAllClient(new MessageServerClose());
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
        //TODO print things to view
        super.clientConnected(client);
        client.setInfo(PLAYER_MAPINFO, new Player(getNextId() + ""));
        sendToClient(client, new MessageTables(getTables()));
    }

    @Override
    protected void clientException(ConnectionToClient client, Throwable ex) {
        sendToClient(client, new MessageError(ex));
        super.clientException(client, ex);
    }

    private void sendToClient(ConnectionToClient client, Message msg) {
        try {
            if (client.isConnected()) {
                client.sendToClient(msg);
            }
            setChanged();
            notifyObservers(msg);
        } catch (IOException ex) {
            clientException(client, ex);
        }
    }

    @Override
    public void sendToAllClients(Object msg) {
        super.sendToAllClients(msg);
        setChanged();
        notifyObservers(msg);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        //TODO check if possible RunMessage(Server, Message) class
        Message message = (Message) msg;
        switch (message.getType()) {
            case CREATE:
                createTable((String) message.getContent(), client);
                break;
            case JOIN:
                joinTable((String) message.getContent(), client);
                break;
            case PROFILE:
                String name = (String) ((message.util.Player) message.getContent()).getUsername();
                updateName(name, client);
                break;
            case DRAW_LINE:
                drawLine(client, message);
                break;
            case EXIT_TABLE:
                removePlayer(client);
                break;
            case EXIT:
                try {
                    removePlayer(client);
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                clientException(client, new IllegalArgumentException("Message not handled"));
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Model) {
            Thread t = new Thread(() -> {
                Model table = (Model) o;
                if (table.getState() == GameState.WON || table.getState() == GameState.LOST) {
                    //TODO implement
                } else {
                    if (table.isEmpty()) {
                        tables.remove(tables.indexOf(table));
                    }
                    sendToAllClients(new MessageTables(getTables()));
                }
            });
            t.run();
        }
    }

    private void updatePlayerInfo(Player p, ConnectionToClient client) {
        client.setInfo(PLAYER_MAPINFO, p);
        boolean hasPartner = client.getInfo(PARTNER_CLIENT_INFO) != null;
        PlayerRole role = PlayerRole.valueOf(p.getRole().toString());
        sendToClient(client, new MessageProfile(p.getUsername(), role, hasPartner));
    }

    private void updateName(String name, ConnectionToClient client) {
        if (validId(name)) {
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            p.setUsername(name);
            updatePlayerInfo(p, client);
        } else {
            clientException(client, new IllegalArgumentException("Username already choosen on the server!"));
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean validId(String name) {
        for (Thread th : this.getClientConnections()) {
            ConnectionToClient client = (ConnectionToClient) th;
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            if (p != null && p.is(name)) {
                return false;
            }
        }
        return true;
    }

    private void createTable(String tableId, ConnectionToClient client) {
        if (!validTableId(tableId)) {
            clientException(client, new IllegalArgumentException("Table id already choosen!"));
        } else if (client.getInfo(TABLE_MAPINFO) != null) {
            clientException(client, new GameException("Already in a game"));
        } else {
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            Table t = new Table(tableId, p);
            t.addObserver(this);
            tables.add(t);
            client.setInfo(TABLE_MAPINFO, t);
            sendToAllClients(new MessageTables(getTables()));
            updatePlayerInfo(p, client);
        }
    }

    private void joinTable(String tableId, ConnectionToClient client) {
        if (client.getInfo(TABLE_MAPINFO) != null) {
            clientException(client, new GameException("Already in a game"));
        } else {
            Table t = getTableById(tableId);
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            if (t == null) {
                clientException(client, new IllegalArgumentException("No such table!"));
            } else if (!t.isOpen()) {
                clientException(client, new IllegalArgumentException("Table closed!"));
            } else if (p.getRole() != OneVOneModel.PlayerRole.NONE) {
                clientException(client, new GameException("Player already on a table!"));
            } else {
                try {
                    t.addGuesser(p);
                    client.setInfo(TABLE_MAPINFO, t);
                    ConnectionToClient partner = getClientById(t.getPlayerNames()[0]);
                    Player partnerP = (Player) partner.getInfo(PLAYER_MAPINFO);
                    client.setInfo(PARTNER_CLIENT_INFO, partner);
                    partner.setInfo(PARTNER_CLIENT_INFO, client);
                    updatePlayerInfo(p, client);
                    updatePlayerInfo(partnerP, partner);
                    sendToClient(client, new MessageGameInit(t.getWordToGuess()));
                } catch (GameException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Table getTableById(String tableId) {
        for (Table t : tables) {
            if (t.is(tableId)) {
                return t;
            }
        }
        return null;
    }

    private ConnectionToClient getClientById(String playerId) {
        for (Thread t : getClientConnections()) {
            ConnectionToClient client = (ConnectionToClient) t;
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            if (p.is(playerId)) {
                return client;
            }
        }
        return null;
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

    private void removePlayer(ConnectionToClient client) {
        Table t = (Table) client.getInfo(TABLE_MAPINFO);
        if (t != null) {
            Player p = (Player) client.getInfo(PLAYER_MAPINFO);
            if (t.isOnTable(p)) {
                try {
                    t.removePlayer(p);
                    client.setInfo(TABLE_MAPINFO, null);
                    client.setInfo(PARTNER_CLIENT_INFO, null);
                    int idxPartnerName = p.getRole() == DRAWER ? 1 : 0;
                    ConnectionToClient partner = getClientById(t.getPlayerNames()[idxPartnerName]);
                    if (partner != null) {
                        partner.setInfo(PARTNER_CLIENT_INFO, null);
                        updatePlayerInfo(p, client);
                        //TODO sendToClient(partner, new MessageLeft());
                    }
                } catch (GameException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void drawLine(ConnectionToClient client, Message msg) {
        ConnectionToClient partner = (ConnectionToClient) client.getInfo(PARTNER_CLIENT_INFO);
        if (partner == null) {
            clientException(client, new GameException("No Partner to play"));
        } else {
            sendToClient(partner, msg);
        }
    }

}
