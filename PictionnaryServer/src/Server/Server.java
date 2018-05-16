package Server;

import DB.business.DbBusinessException;
import DB.business.VisitorFacade;
import DB.db.DbException;
import DB.dto.GameDto;
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import message.MessageStats;
import message.MessageTables;
import message.MessageWon;
import static message.Type.GAME_INIT;
import message.util.PlayerRole;
import message.util.GameState;
import message.util.GuessUpdate;
import message.util.InitInfos;
import message.util.Stats;

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

    //TODO SET FINAL
    private MultiPlayerFacade model;

    public Server() throws IOException {
        super(PORT);
        model = null;
        try {
            model = new MultiPlayerModel();
            model.addObserver(this);
        } catch (DbBusinessException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                t.getTableName(), toMsgGameState(t.getState()), t.getPlayerNames()))
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
                    GuessUpdate guessUpdate = (GuessUpdate) message.getContent();
                    model.guess((String) client.getInfo(USERNAME_MAPINFO), (String) guessUpdate.getLastGuess());
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
        } catch (GameException | DbBusinessException | IOException | IllegalArgumentException e) {
            if (e instanceof GameException || e instanceof DbBusinessException) {
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
                            int avgWrongProps;
                            try {
                                avgWrongProps = this.model.getAvgWrongProps(t);
                                msg = new MessageGameInit(new InitInfos(t.getWordToGuess(), avgWrongProps));
                            } catch (DbBusinessException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        case IN_GAME:
                            String lastGuess = t.getLastGuess();
                            if (lastGuess != null) {
                                try {
                                    msg = new MessageGuess(new GuessUpdate(lastGuess, model.getPropsWithCount(t)));
                                } catch (DbBusinessException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                        case WON:
                            String[] names = t.getPlayerNames();
                            if (names[0] != null && names[1] != null) {
                                msg = new MessageWon(names[0], names[1], t.getWordToGuess(), t.getGuesses().size());
                            }
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

    private GameState toMsgGameState(OneVOneModel.GameState state) {
        return GameState.valueOf(state.toString());
    }

    private void updatePlayerInfo(Player p, ConnectionToClient client) {
        boolean hasPartner = model.getPartnerUsername((String) client.getInfo(USERNAME_MAPINFO)) != null;
        PlayerRole role = PlayerRole.valueOf(p.getRole().toString());
        sendToClient(client, new MessageProfile(p.getUsername(), role, hasPartner));
        if (p.getRole() == OneVOneModel.PlayerRole.NONE) {
            Stats stats = getStats(client);
            if (stats != null) {
                sendToClient(client, new MessageStats(getStats(client)));
            }
            sendToClient(client, new MessageTables(getTables()));
        }
    }

    private Stats getStats(ConnectionToClient client) {
        try {
            String username = (String) client.getInfo(USERNAME_MAPINFO);
            Collection<GameDto> games = VisitorFacade.getGames(username);
            int clientId = VisitorFacade.getPlayer(username).getId();
            int nbWon = 0, nbLeft = 0, nbPartnerLeft = 0, totGames = 0, nbDrawed = 0, partnerId;
            Map<Integer, Integer> nbPlayedWith = new HashMap<>();
            for (GameDto game : games) {
                totGames++;
                if (game.getEndTime() != null) {
                    if (game.getStopPlayer() == 0) {
                        nbWon++;
                    } else {
                        if (game.getStopPlayer() == clientId) {
                            nbLeft++;
                        } else {
                            nbPartnerLeft++;
                        }
                    }
                }
                if (game.getDrawer() == clientId) {
                    nbDrawed++;
                    partnerId = game.getPartner();
                } else {
                    partnerId = game.getDrawer();
                }
                if (nbPlayedWith.containsKey(partnerId)) {
                    nbPlayedWith.put(partnerId, nbPlayedWith.get(partnerId) + 1);
                } else {
                    nbPlayedWith.put(partnerId, 1);
                }
            }
            int nbFailed = totGames - (nbWon + nbLeft + nbPartnerLeft), nbGuessed = totGames - nbDrawed;
            Map<String, Integer> timesPlayedWith = new HashMap<>();
            nbPlayedWith.forEach((id, value) -> {
                try {
                    timesPlayedWith.put(VisitorFacade.getPlayer(id).getName(), value);
                } catch (DbBusinessException ex) {
                }
            });
            return new Stats(totGames, nbWon, nbLeft, nbPartnerLeft, nbFailed, nbDrawed, nbGuessed, timesPlayedWith);
        } catch (DbBusinessException e) {
            return null;
        }
    }

    private void joinTable(ConnectionToClient client, Message message) throws GameException, DbBusinessException {
        String username = (String) client.getInfo(USERNAME_MAPINFO);
        model.joinTable(username, (String) message.getContent());
        String partnerUsername = model.getPartnerUsername(username);
        ConnectionToClient partner = getClientById(partnerUsername);
        client.setInfo(PARTNER_CLIENT_INFO, partner);
        partner.setInfo(PARTNER_CLIENT_INFO, client);
    }

    private void profile(ConnectionToClient client, Message message) throws GameException {
        String oldUsername = (String) client.getInfo(USERNAME_MAPINFO);
        String newUsername = ((message.util.Player) message.getContent()).getUsername();
        client.setInfo(USERNAME_MAPINFO, newUsername); //not the criteria for id : if name update fails, we don't care
        try {
            if (oldUsername == null || oldUsername.equals("")) {
                model.createPlayer(newUsername);
            } else {
                model.updateUsername(oldUsername, newUsername);
            }
        } catch (GameException dbException) {
//            clientException(client, dbException);
            client.setInfo(USERNAME_MAPINFO, oldUsername);
            throw dbException;
        } catch (DbBusinessException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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

}
