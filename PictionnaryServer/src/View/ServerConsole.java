package View;

import Model.Model;
import Server.Server;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 *
 * @author Philippe
 */
public class ServerConsole implements Observer {

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
        } catch (IOException ex) {
            Logger.getLogger(ServerConsole.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
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
            switch (msg.getType()) {
                case CREATE:
                case TABLES:
                case JOIN:
                    printTables(((Server) o).getModels());
                default:
                    break;
            }
        }
    }

    private void printTables(List<Model> models) {
        System.out.println("Tables:");
        for (Model model : models) {
            System.out.print(model.getId() + ", isOpen : " + model.isOpen() + " , players : ");
            for (String p : model.getPlayers()) {
                System.out.print("  " + p);
            }
            System.out.println("");
        }
    }
}
