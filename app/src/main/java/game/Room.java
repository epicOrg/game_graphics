package game;

import java.util.ArrayList;

import game.player.Player;

/**
 * This class represents the room in which the users enter.
 * Here the teams are created.
 *
 * @author Torlaschi
 * @date 18/04/2015
 */
public class Room {

    private String name;
    private int maxPlayers, currentPlayers;
    private ArrayList<Team> teams;

    public Room(String name, int maxPlayers, int currentPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
    }

    public Room(String name, int maxPlayers, ArrayList<Team> teams) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.teams = teams;

        currentPlayers = 0;
        for(Team t : teams){
            for(Player p : t.getPlayers()){
                currentPlayers++;
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team t){
        teams.add(t);
    }

    public Player getPlayerByUsername(String username){
        for(Team t: teams)
            for(Player p : t.getPlayers())
                if(p.getName().equals(username))
                    return p;

        return null;
    }

    @Override
    public String toString() {
        return getName() + " (" + getCurrentPlayers() + " / " + getMaxPlayers() + ")";
    }

}
