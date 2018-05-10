package client;

import java.io.IOException;
import java.util.List;
import message.util.Table;
import pictionnary.drawingPane.DrawingInfos;

/**
 *
 * @author G43197
 */
public abstract class Model extends AbstractClient {

    public Model(String host, int port) {
        super(host, port);
    }

    public abstract List<Table> getTables();

    public abstract void updateName(String name) throws IOException;

    public abstract void createTable(String tableId) throws IOException;

    public abstract void joinTable(String tableId) throws IOException;

    public abstract void guess(String guess) throws IOException;

    public abstract void drawLine(DrawingInfos drawingInfos) throws IOException;

    public abstract void drawLine() throws IOException;

    public abstract void exitTable() throws IOException;

    public abstract void exit();
}
