package message.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Philippe
 */
public class Tables implements Iterable<Table>, Serializable {

    private final List<Table> tables;

    public Tables() {
        tables = new ArrayList<>();
    }

    @Override
    public Iterator<Table> iterator() {
        return tables.iterator();
    }

    /**
     * Creates a new instance of an table and add this table to the list.
     *
     * @param id tableId of the new table.
     * @param isOpen
     * @param players
     * @return the tableId of the new table.
     * @throws java.lang.Exception see Table()
     */
    public String add(String id, boolean isOpen, User... players) throws Exception {
        Table t = new Table(id, isOpen, players);
        add(t);
        return t.getId();
    }

    /**
     * Add a table to the list of tables.
     *
     */
    public void add(Table table) {
        tables.add(table);
    }

    public void remove(String id) {
        Iterator<Table> it = tables.iterator();
        boolean find = false;
        Table current = null;
        while (it.hasNext() && !find) {
            current = it.next();
            find = current.is(id);
        }
        if (find) {
            tables.remove(current);
        }
    }

    public int size() {
        return tables.size();
    }

    public void clear() {
        tables.clear();
    }

}
