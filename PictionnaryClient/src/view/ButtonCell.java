package view;

import Controller.Controller;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

/**
 *
 * @author Philippe
 */
public class ButtonCell extends TableCell<TableItem, Boolean> {

    private final Button cellButton;

    public ButtonCell(TableView<TableItem> tableView, Controller controller) {
        this.cellButton = new Button("Join");
        setGraphic(cellButton);
        setText(null);
        //TODO set font et style for button
        cellButton.setOnAction(e -> {
            int rowNb = getTableRow().getIndex();
            String tableId = (String) tableView.getColumns().get(0).getCellData(rowNb);
            controller.join(tableId);
        });
    }

}
