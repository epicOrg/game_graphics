package game.net.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import game.data.Team;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.RoomFields;
import game.net.fieldsnames.RoomsFields;
import game.net.fieldsnames.ServicesFields;
import game.player.Player;

/**
 * @author Micieli
 * @date 31/03/2015
 */

public class CurrentRoom implements Service {

    public static final String LOG_TAG = "CurrentRoom";

    public static final int LIST = 0;
    public static final int START = 1;
    public static final int EXIT = 2;

    private JSONObject json;
    private Handler handler;

    @Override
    public void start(JSONObject json) {
        this.json=json;
        readFields();
    }

    private void readFields() {
        try {
            Message message = null;
            switch (RoomFields.valueOf(json.getString(ServicesFields.SERVICE_TYPE.toString()))) {
                case ROOM_PLAYER_LIST:
                    message = getPlayerListMessage();
                    break;
                case ROOM_ACTIONS:
                    message = getActionsMessage();
                    break;
            }
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Message getPlayerListMessage() {
        CurrentRoomResult currentRoomResult = null;
        try {
            int maxPlayers = json.getInt(RoomsFields.ROOM_MAX_PLAYERS.toString());

            ArrayList<Team> teams = new ArrayList<>();
            JSONArray jTeams = json.getJSONArray(RoomFields.ROOM_TEAM.toString());
            for (int i = 0; i < jTeams.length(); i++) {
                JSONObject jTeam = jTeams.getJSONObject(i);
                String name = jTeam.getString(RoomFields.ROOM_NAME.toString());
                int color = Integer.parseInt(jTeam.getString(RoomFields.ROOM_TEAM_COLOR.toString()));

                Team team = new Team(name, color);

                ArrayList<Player> players = new ArrayList<>();
                JSONArray jPlayers = jTeam.getJSONArray(CommonFields.LIST.toString());
                for (int j = 0; j < jPlayers.length(); j++) {
                    JSONObject jPlayer = jPlayers.getJSONObject(j);
                    String playerName = jPlayer.getString(CommonFields.USERNAME.toString());

                    players.add(new Player(playerName));
                }

                team.setPlayers(players);
                teams.add(team);
            }
            currentRoomResult = new CurrentRoomResult(maxPlayers, teams);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return handler.obtainMessage(LIST, currentRoomResult);
    }

    private Message getActionsMessage() {
        try {
            boolean result = json.getBoolean(CommonFields.NO_ERRORS.toString());
            switch (RoomFields.valueOf(json.getString(RoomFields.ROOM_ACTION.toString()))) {
                case ROOM_START:
                    return handler.obtainMessage(START, result);
                case ROOM_EXIT:
                    return handler.obtainMessage(EXIT, result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class CurrentRoomResult {

        private int maxPlayer;
        private ArrayList<Team> teams;

        public CurrentRoomResult(int maxPlayer, ArrayList<Team> teams) {
            this.maxPlayer = maxPlayer;
            this.teams = teams;
        }

        public int getMaxPlayer() {
            return maxPlayer;
        }

        public ArrayList<Team> getTeams() {
            return teams;
        }
    }

}
