package message.util;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Philippe
 */
public class Stats implements Serializable {

    private final int totGames;
    private final int nbWon;
    private final int nbLeft;
    private final int nbPartnerLeft;
    private final int nbFailed;
    private final int nbDrawed;
    private final int nbGuessed;
    private final Map<String, Integer> timesPlayedWith;

    public Stats(int totGames, int nbWon, int nbLeft, int nbPartnerLeft, int nbFailed, int nbDrawed, int nbGuessed, Map<String, Integer> timesPlayedWith) {
        this.totGames = totGames;
        this.nbWon = nbWon;
        this.nbLeft = nbLeft;
        this.nbPartnerLeft = nbPartnerLeft;
        this.nbFailed = nbFailed;
        this.nbDrawed = nbDrawed;
        this.nbGuessed = nbGuessed;
        this.timesPlayedWith = timesPlayedWith;
    }

    public int getTotGames() {
        return totGames;
    }

    public int getNbWon() {
        return nbWon;
    }

    public int getNbLeft() {
        return nbLeft;
    }

    public int getNbPartnerLeft() {
        return nbPartnerLeft;
    }

    public int getNbFailed() {
        return nbFailed;
    }

    public int getNbDrawed() {
        return nbDrawed;
    }

    public int getNbGuessed() {
        return nbGuessed;
    }

    public Map<String, Integer> getTimesPlayedWith() {
        return timesPlayedWith;
    }

}
