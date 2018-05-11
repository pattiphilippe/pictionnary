package view;

import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import message.Message;
import message.util.Stats;

/**
 *
 * @author Philippe
 */
public class StatsView extends HBox implements Observer {

    private final ObservableList<PieChart.Data> gamesEndedData;
    private final PieChart gamesEnded;
    private final ObservableList<PieChart.Data> gameRolesData;
    private final PieChart gameRoles;

    public StatsView(double d) {
        super(d);
        gamesEndedData = FXCollections.observableArrayList(
                new PieChart.Data("Games won", 0),
                new PieChart.Data("Games left", 0),
                new PieChart.Data("Games partner left", 0),
                new PieChart.Data("Games with error", 0));
        gamesEnded = new PieChart(gamesEndedData);
        gamesEnded.setTitle("Games Endings");
        gameRolesData = FXCollections.observableArrayList(
                new PieChart.Data("Guesser", 0),
                new PieChart.Data("Drawer", 0));
        gameRoles = new PieChart(gameRolesData);
        gameRoles.setTitle("Game Roles");
        this.getChildren().addAll(gamesEnded, gameRoles);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message msg = (Message) arg;
            Platform.runLater(() -> {
                switch (msg.getType()) {
                    case STATS:
                        Stats stats = (Stats) msg.getContent();
                        gamesEndedData.get(0).setPieValue(stats.getNbWon());
                        gamesEndedData.get(1).setPieValue(stats.getNbLeft());
                        gamesEndedData.get(2).setPieValue(stats.getNbPartnerLeft());
                        gamesEndedData.get(3).setPieValue(stats.getNbFailed());
                        gameRolesData.get(0).setPieValue(stats.getNbDrawed());
                        gameRolesData.get(1).setPieValue(stats.getNbGuessed());
                }
            });
        }
    }

}
