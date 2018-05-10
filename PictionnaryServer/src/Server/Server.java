package Server;

import DB.db.DbException;
import MultiPModel.MultiPlayerFacade;
import MultiPModel.MultiPlayerModel;
import OneVOneModel.GameException;
import static OneVOneModel.GameState.*;
import OneVOneModel.Model;
import OneVOneModel.Player;
import static OneVOneModel.PlayerRole.DRAWER;
import static OneVOneModel.PlayerRole.NONE;
import OneVOneModel.Table;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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
import message.MessageGameState;
import message.MessageGuess;
import message.MessageProfile;
import message.MessageServerClosed;
import message.MessageTables;
import message.MessageWon;
import static message.Type.GAME_INIT;
import message.util.PlayerRole;
import message.util.GameState;

/**
 * Pictionnary Server
 *
 * @author Philippe
 */
public class Server extends AbstractServer implements Observer {

    private static final int PORT = 10_000;
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

    public Server() throws IOException {
        super(PORT);

        model = new MultiPlayerModel();
        model.addObserver(this);
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
                t.getId(), toMsgGameState(t.getState()), t.getPlayerNames()))
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
        sendToAllClients(new MessageServerClosed());
        this.stopListening();
        this.close();
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        //TODO print things to view
    }

    @Override
    protected void clientException(ConnectionToClient client, Throwable ex) {
        sendToClient(client, new MessageError(ex));
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
        Message message = (Message) msg;
        try {
            System.out.println("Server:: message received of type : " + message.getType());
            switch (message.getType()) {
                case CREATE:
                    model.createTable((String) client.getInfo(USERNAME_MAPINFO), (String) message.getContent());
                    break;
                case JOIN:
                    joinTable(client, message);
                    break;
                case PROFILE:
                    profile(client, message);
                    break;
                case DRAW_LINE:
                case CLEAR_DRAW:
                    draw(client, message);
                    break;
                case GUESS:
                    model.guess((String) client.getInfo(USERNAME_MAPINFO), (String) message.getContent());
                    break;
                case EXIT_TABLE:
                    model.leaveTable((String) client.getInfo(USERNAME_MAPINFO));
                    break;
                case EXIT:
                    String username = (String) client.getInfo(USERNAME_MAPINFO);
                    client.close();
                    model.leaveTable(username);
                    model.exitGame(username);
                    break;
                default:
                    throw new IllegalArgumentException("Message not handled");
            }
        } catch (DbException | GameException | IOException | IllegalArgumentException e) {
            if (e instanceof DbException || e instanceof GameException) {
                clientException(client, new Exception(e.getClass().getName() + " : " + e.getMessage()));
            } else {
                clientException(client, e);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MultiPlayerFacade) {
            Thread th = new Thread(() -> {
                if (arg instanceof Player) {
                    //TODO send a player Dto, not a player
                    Player p = (Player) arg;
                    ConnectionToClient client = getClientById(p.getUsername());
                    updatePlayerInfo(p, client);
                } else if (arg instanceof Model) {
                    Model t = (Model) arg;
                    Message msg = null;
                    switch (t.getState()) {
                        case INIT:
                            msg = new MessageGameInit(t.getWordToGuess());
                            break;
                        case IN_GAME:
                            String lastGuess = t.getLastGuess();
                            if (lastGuess != null) {
                                msg = new MessageGuess(lastGuess);
                            }
                            break;
                        case WON:
                            String[] names = t.getPlayerNames();
                            msg = new MessageWon(names[0], names[1], t.getWordToGuess(), t.getGuesses().size());
                            break;
                    }
                    //for players on changed table
                    for (String name : t.getPlayerNames()) {
                        ConnectionToClient client = getClientById(name);
                        sendToClient(client, new MessageGameState(toMsgGameState(t.getState())));
                        try {
                            if (msg != null && (msg.getType() != GAME_INIT || model.getRole(name) == DRAWER)) {
                                sendToClient(client, msg);
                            }
                        } catch (DbException ex) {
                            clientException(client, ex);
                        }
                    }
                    sendTables();
                }
            });
            th.run();
        }
    }

    private void updatePlayerInfo(Player p, ConnectionToClient client) {
        //TODO optimize
        boolean hasPartner = model.getPartnerUsername((String) client.getInfo(USERNAME_MAPINFO)) != null;
        PlayerRole role = PlayerRole.valueOf(p.getRole().toString());
        sendToClient(client, new MessageProfile(p.getUsername(), role, hasPartner));
        if (p.getRole() == OneVOneModel.PlayerRole.NONE) {
            sendToClient(client, new MessageTables(getTables()));
        }
    }

    private void joinTable(ConnectionToClient client, Message message) throws GameException {
        String username = (String) client.getInfo(USERNAME_MAPINFO);
        model.joinTable(username, (String) message.getContent());
        String partnerUsername = model.getPartnerUsername(username);
        ConnectionToClient partner = getClientById(partnerUsername);
        client.setInfo(PARTNER_CLIENT_INFO, partner);
        partner.setInfo(PARTNER_CLIENT_INFO, client);
    }

    private void profile(ConnectionToClient client, Message message) throws DbException {
        String oldUsername = (String) client.getInfo(USERNAME_MAPINFO);
        String newUsername = ((message.util.Player) message.getContent()).getUsername();
        client.setInfo(USERNAME_MAPINFO, newUsername); //not the criteria for id : if name update fails, we don't care
        try {
            if (oldUsername == null || oldUsername.equals("")) {
                model.createPlayer(newUsername);
            } else {
                model.updateUsername(oldUsername, newUsername);
            }
        } catch (DbException dbException) {
            clientException(client, dbException);
            client.setInfo(USERNAME_MAPINFO, oldUsername);
            throw dbException;
        }
    }

    private void draw(ConnectionToClient client, Message msg) throws GameException {
        ConnectionToClient partner = (ConnectionToClient) client.getInfo(PARTNER_CLIENT_INFO);
        if (partner == null) {
            throw new GameException("No Partner to play!");
        } else {
            Table t = model.getTable((String) client.getInfo(USERNAME_MAPINFO));
            if (t.getState() != IN_GAME) {
                throw new GameException("Can't send draw, not currently in game!");
            } else {
                sendToClient(partner, msg);
            }
        }
    }

    private ConnectionToClient getClientById(String playerId) {
        for (Thread t : getClientConnections()) {
            ConnectionToClient client = (ConnectionToClient) t;
            String username = (String) client.getInfo(USERNAME_MAPINFO);
            if (username != null && username.equals(playerId)) {
                return client;
            }
        }
        return null;
    }

    private void sendTables() {
        Message msg = new MessageTables(getTables());
        for (Thread t : super.getClientConnections()) {
            ConnectionToClient client = (ConnectionToClient) t;
            try {
                if (model.getRole((String) client.getInfo(USERNAME_MAPINFO)) == NONE) {
                    sendToClient(client, msg);
                }
            } catch (DbException ex) {
            }
        }
    }

    private GameState toMsgGameState(OneVOneModel.GameState state) {
        return GameState.valueOf(state.toString());
    }
}
