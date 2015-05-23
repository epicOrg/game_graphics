package login.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.alessandro.computergraphicsexample.GameActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import game.GameManager;
import game.JSONd;
import game.RequestMaker;
import game.Room;
import game.Team;
import game.player.Player;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;

/**
 * Created by depa on 23/05/15.
 */
public class RooMHandler extends Handler {

    private Room currentRoom;
    private String username, roomName;
    private int hashcode;
    private LinearLayout roomListsContainer;
    private Activity activity;
    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();
    private TextView roomStatus;
    private RequestMaker requestMaker;

    public RooMHandler(Room currentRoom, String username, String roomName, int hashcode, LinearLayout roomListsContainer,
                       Activity activity, TextView roomStatus, RequestMaker requestMaker) {
        this.currentRoom = currentRoom;
        this.username = username;
        this.roomName = roomName;
        this.hashcode = hashcode;
        this.roomListsContainer = roomListsContainer;
        this.activity = activity;
        this.roomStatus = roomStatus;
        this.requestMaker = requestMaker;
    }

    @Override
    public void handleMessage(Message msg) {
        JSONObject json=(JSONObject) msg.obj;
        try{
            if (json.getString(FieldsNames.SERVICE).equals(FieldsNames.CURRENT_ROOM)){
                switch (json.getString(FieldsNames.SERVICE_TYPE)) {
                    case FieldsNames.ROOM_PLAYER_LIST:
                        processListMessage(json);
                        break;
                    case FieldsNames.ROOM_ACTIONS:
                        switch (json.getString(FieldsNames.ROOM_ACTION)) {
                            case FieldsNames.ROOM_START:
                                processStartMessage(json);
                                break;
                            case FieldsNames.ROOM_EXIT:
                                processExitMessage(json);
                                break;
                        }
                        break;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void processListMessage(JSONObject json) throws JSONException{
        boolean firstTime = false;
        if (currentRoom == null)
            firstTime = true;

        ArrayList<Team> teams = new ArrayList<>();
        JSONArray jTeams = json.getJSONArray(FieldsNames.ROOM_TEAM);
        for (int i = 0; i < jTeams.length(); i++) {
            JSONObject jTeam = jTeams.getJSONObject(i);
            String name = jTeam.getString(FieldsNames.ROOM_NAME);
            int color = Integer.parseInt(jTeam.getString(FieldsNames.ROOM_TEAM_COLOR));

            Team team = new Team(name, color);

            ArrayList<Player> players = new ArrayList<>();
            JSONArray jPlayers = jTeam.getJSONArray(FieldsNames.LIST);
            for (int j = 0; j < jPlayers.length(); j++) {
                JSONObject jPlayer = jPlayers.getJSONObject(j);
                String playerName = jPlayer.getString(FieldsNames.USERNAME);

                players.add(new Player(playerName));
            }

            team.setPlayers(players);
            teams.add(team);
        }
        int maxPlayers=json.getInt(FieldsNames.ROOM_MAX_PLAYERS);

        currentRoom = new Room(roomName, maxPlayers, teams);

        int currentPlayers = 0;

        roomListsContainer.removeAllViews();
        for (Team t : currentRoom.getTeams()) {
            ListView listView = new ListView(activity);
            roomListsContainer.addView(listView);
            ArrayAdapter<Player> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, t.getPlayers());

            TextView textView = new TextView(activity);
            textView.setText(t.getName());
            listView.addHeaderView(textView);
            listView.setAdapter(adapter);

            currentPlayers += t.getPlayers().size();
        }

        roomStatus.setText("(" + currentPlayers + " / " + maxPlayers + ")");

        if(firstTime) {
            try {
                serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_ACTIONS),
                        new JSONd(FieldsNames.ROOM_ACTION, FieldsNames.ROOM_LIST_RECEIVED)));
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processExitMessage(JSONObject json) throws JSONException{

        if (json.getBoolean(FieldsNames.NO_ERRORS)) {
            activity.finish();
        }
    }

    private void processStartMessage(JSONObject json) throws JSONException{

        if (json.getBoolean(FieldsNames.NO_ERRORS)) {
            GameManager gameManager = GameManager.MANAGER;
            gameManager.setRoom(currentRoom);

            Intent intent = new Intent(activity.getApplicationContext(), GameActivity.class);
            intent.putExtra(FieldsNames.USERNAME, username);
            intent.putExtra(FieldsNames.HASHCODE, hashcode);
            activity.startActivity(intent);
        }
    }

}
