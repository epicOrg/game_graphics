package game.data;

import java.util.ArrayList;

import game.player.Player;

/**
 * Represents a team of players.
 *
 * @author Torlaschi
 * @date 23/04/2015
 * @see Player
 */
public class Team {

    private String name;
    private int color;

    private ArrayList<Player> players = new ArrayList<>();

    /**
     * Creates a new <code>Team</code> with given name and color.
     *
     * @param name  the name of the <code>Team</code>.
     * @param color the color which identifies the <code>Team</code>.
     */
    public Team(String name, int color) {
        this.name = name;
        this.color = color;
    }

    /**
     * @return the name of the <code>Team</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the color which identifies the <code>Team</code>.
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the current <code>Player</code> list in this <code>Team</code>.
     *
     * @param players the <code>Player</code> list for this <code>Team</code>.
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * @return the current <code>Player</code> list in this <code>Team</code>.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

}
