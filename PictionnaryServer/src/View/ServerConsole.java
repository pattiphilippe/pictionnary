package View;

import Server.Server;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.util.Player;
import message.util.Table;
import message.util.WonInfos;

/**
 *
 * @author Philippe
 */
public class ServerConsole implements Observer {

    private static final Scanner CLAVIER = new Scanner(System.in);

    /**
     * Entry points to the instant messaging server side.
     *
     * @param args no arguments needed.
     */
    public static void main(String[] args) {
        try {
            Server model = new Server();
            ServerConsole console = new ServerConsole(model);
            model.addObserver(console);
            System.out.println("Server started");
            System.out.println("Server IP : " + model.getIP());
            System.out.println("Server Port : " + model.getPort());
            System.out.println("");
            System.out.println("Enter \"exit\" to shut the server down.");
            boolean exit = false;
            while (!exit) {
                exit = readExit();
            }
            model.quit();
            //TODO COMMAND QUIT
        } catch (IOException ex) {
            Logger.getLogger(ServerConsole.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    private static boolean readExit() {
        String cmd = CLAVIER.next();
        return cmd.toLowerCase().equals("exit");
    }

    private final Server model;

    /**
     * Constructs the console view. Subscribes to the instant messaging server.
     *
     * @param model instant messaging server.
     */
    public ServerConsole(Server model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO implement
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            System.out.println("Server::message send of type : " + msg.getType());
            switch (msg.getType()) {
                case CREATE:
                case TABLES:
                case JOIN:
                    printTables(((Server) o).getTables());
                    break;
                case PROFILE:
                    Player p = (Player) msg.getContent();
                    System.out.println("Profil of " + p.getUsername() + " updated\n");
                    break;
                case WON:
                    WonInfos wonInfos = (WonInfos) msg.getContent();
                    System.out.println("Game won by " + wonInfos.getDrawerName()
                            + " (Drawer) and " + wonInfos.getGuesserName() + " (Guesser).");
                    break;
                case ERROR:
                    Exception ex = (Exception) msg.getContent();
                    System.out.println("Error : " + ex.getMessage());
                    ex.printStackTrace();
                default:
                    break;
            }
        }
    }

    private void printTables(List<Table> tables) {
        StringBuilder msg = new StringBuilder();
        msg.append("Tables:").append("\n");
        for (Table table : tables) {
            msg.append(table.getTableId())
                    .append(", state : ").append(table.getState())
                    .append(", drawer name : ").append(table.getDrawerName())
                    .append(", guesser : ").append(table.getGuesserName())
                    .append("\n");

        }
        System.out.println(msg);
    }
}
