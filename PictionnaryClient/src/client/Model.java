package client;

import java.io.IOException;
import java.util.List;
import message.util.Table;
import pictionnary.drawingPane.DrawingInfos;

/**
 *
 * @author G43197
 */
public interface Model {

    public List<Table> getTables();

    public void updateName(String name) throws IOException;

    public void createTable(String tableId) throws IOException;

    public void joinTable(String tableId);

    public void guess(String guess);

    public void draw(DrawingInfos drawingInfos);

    public void exitTable();

    public void exit();
}
