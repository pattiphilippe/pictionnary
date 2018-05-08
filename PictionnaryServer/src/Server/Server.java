package Server;

import DB.db.DbException;
import MultiPModel.MultiPlayerFacade;
import MultiPModel.MultiPlayerModel;
import OneVOneModel.GameException;
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
import message.MessageWon;
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
    static final String USERNAME_MAPINFO = "USERNAME";

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

    private final MultiPlayerFacade model;
    private int clientId; //to give to client... or username here
    private final List<Model> tables;

    public Server() throws IOException {
        super(PORT);

        model = new MultiPlayerModel();
        model.addObserver(this);
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
        return model.getTables().stream()
                .map(t -> new message.util.Table(
                t.getId(), t.isOpen(), t.getPlayerNames()))
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
        //client.setInfo(PLAYER_MAPINFO, new Player(getNextId() + ""));
        try {
            client.sendToClient(new MessageTables(getTables()));
        } catch (IOException ex) {
            clientException(client, ex);
        }
    }

    @Override
    protected void clientException(ConnectionToClient client, Throwable ex) {
        sendToClient(client, new MessageError(ex));
        super.clientException(client, ex);
    }

    private void sendToClient(ConnectionToClient client, Message msg) {
        try {
            if (client != null && client.isConnected()) {
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
        try {
            switch (message.getType()) {
                case CREATE:
                    model.createTable((String) client.getInfo(USERNAME_MAPINFO), (String) message.getContent());
                    //createTable((String) message.getContent(), client);
                    break;
                case JOIN:
                    model.joinTable((String) client.getInfo(USERNAME_MAPINFO), (String) message.getContent());
                    //joinTable((String) message.getContent(), client);
                    break;
                case PROFILE:
                    profile(client, message);
//                String name = (String) ((message.util.Player) message.getContent()).getUsername();
//                updateName(name, client);
                    break;
                case DRAW_LINE:
                    drawLine(client, message);
                    break;
                case GUESS:
                    guess(client, message);
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
        } catch (DbException | GameException e) {
            clientException(client, e);
        }
    }

    private void profile(ConnectionToClient client, Message message) throws DbException {
        String oldUsername = (String) client.getInfo(USERNAME_MAPINFO);
        String newUsername = ((message.util.Player) message.getContent()).getUsername();
        if (oldUsername == null || oldUsername.equals("")) {
            client.setInfo(USERNAME_MAPINFO, newUsername);
            model.createPlayer(newUsername);
            sendToClient(client, new MessageTables(getTables()));
        } else {
            model.updateUsername(oldUsername, newUsername);
            client.setInfo(USERNAME_MAPINFO, newUsername);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Model) {
            Thread th = new Thread(() -> {
                Model t = (Model) o;
                if (t.isFinished()) {
                    String[] names = t.getPlayerNames();
                    Message msg = new MessageWon(names[0], names[1], t.getWordToGuess(), t.getGuesses().size());
                    for (String name : t.getPlayerNames()) {
                        ConnectionToClient client = getClientById(name);
                        sendToClient(client, msg);
                    }
                } else if (t.isEmpty()) {
                    tables.remove(tables.indexOf(t));
                    sendToAllClients(new MessageTables(getTables()));
                }
            });
            th.run();
        } else if (o instanceof MultiPlayerFacade) {
            Thread th = new Thread(() -> {
                if (arg instanceof Player) {
                    Player p = (Player) arg;
                    ConnectionToClient client = getClientById(p.getUsername());
                    updatePlayerInfo(p, client);
                }
            });
            th.run();
        }
    }

    private void updatePlayerInfo(Player p, ConnectionToClient client) {
        //client.setInfo(PLAYER_MAPINFO, p);
        //boolean hasPartner = client.getInfo(PARTNER_CLIENT_INFO) != null;
        //TODO optimize
        boolean hasPartner = p.getTable() != null && ((Table) p.getTable()).getPlayerNames().length == 2;
        PlayerRole role = PlayerRole.valueOf(p.getRole().toString());
        sendToClient(client, new MessageProfile(p.getUsername(), role, hasPartner));
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
                    updatePlayerInfo(p, client);
                    int idxPartnerName = p.getRole() == DRAWER ? 1 : 0;
                    ConnectionToClient partner = getClientById(t.getPlayerNames()[idxPartnerName]);
                    if (partner != null) {
                        Player partnerP = (Player) partner.getInfo(PLAYER_MAPINFO);
                        partner.setInfo(PARTNER_CLIENT_INFO, null);
                        updatePlayerInfo(partnerP, partner);
                    }
                    sendToAllClients(new MessageTables(getTables()));

                } catch (GameException ex) {
                    Logger.getLogger(Server.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void drawLine(ConnectionToClient client, Message msg) throws GameException {
        //ConnectionToClient partner = (ConnectionToClient) client.getInfo(PARTNER_CLIENT_INFO);
        String username = (String) client.getInfo(USERNAME_MAPINFO);
        String partnerUsername = model.getPartnerUsername(username);
        ConnectionToClient partner = getClientById(partnerUsername);
        if (partner == null) {
            throw new GameException("No Partner to play!");
        } else {
            Table t = model.getTable(username);
            if (t.isFinished()) {
                throw new GameException("Game is finished!");
            } else {
                sendToClient(partner, msg);
            }
        }
    }

    private void guess(ConnectionToClient client, Message msg) {
        Table t = (Table) client.getInfo(TABLE_MAPINFO);
        Player p = (Player) client.getInfo(PLAYER_MAPINFO);
        int idxPartnerName = p.getRole() == DRAWER ? 1 : 0;
        ConnectionToClient partner = getClientById(t.getPlayerNames()[idxPartnerName]);
        if (partner == null) {
            clientException(client, new GameException("No Partner to play"));
        } else if (t.isFinished()) {
            clientException(client, new GameException("Game is finished!"));
        } else {
            try {
                t.guess(p, (String) msg.getContent());
                sendToClient(client, msg);
                sendToClient(partner, msg);
            } catch (GameException ex) {
                clientException(client, ex);
            }
        }
    }

    /*
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
                Logger.getLogger(Server.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     */

 /*
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
     */

 /*
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
     */
 /*
    private void joinTable(String tableId, ConnectionToClient client) {
        if (client.getInfo(TABLE_MAPINFO) != null) {
            clientException(client, new GameException("Already in a game"));
        } else {
            Model t = getTableById(tableId);
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
                    sendToAllClients(new MessageTables(getTables()));
                    sendToClient(partner, new MessageGameInit(t.getWordToGuess()));

                } catch (GameException ex) {
                    Logger.getLogger(Server.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
     */

 /*
    private Model getTableById(String tableId) {
        for (Model t : tables) {
            if (t.is(tableId)) {
                return t;
            }
        }
        return null;
    }
     */
    private ConnectionToClient getClientById(String playerId) {
        for (Thread t : getClientConnections()) {
            ConnectionToClient client = (ConnectionToClient) t;
            String username = (String) client.getInfo(USERNAME_MAPINFO);
            if (username.equals(playerId)) {
                return client;
            }
        }
        return null;
    }

    /*
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
     */
}
