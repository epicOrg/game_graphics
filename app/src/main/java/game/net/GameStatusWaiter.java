package game.net;

import org.json.JSONException;
import org.json.JSONObject;

import game.Waiter;
import login.audio.AudioCallManager;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;

/**
 * Created by Andrea on 04/05/2015.
 */
public class GameStatusWaiter implements Waiter {

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private String roomname;
    private String username;
    private int hashcode;

    public GameStatusWaiter(String roomname, String username, int hashcode) {
        this.roomname = roomname;
        this.username = username;
        this.hashcode = hashcode;
    }

    private JSONObject createStatusMessage() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.GAME);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_STATUS);
            request.put(FieldsNames.ROOM_NAME, roomname);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
            request.put(FieldsNames.GAME_READY, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    public void unleash() {
        try {
            serverCommunicationThread.send(createStatusMessage());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

        AudioCallManager.getInstance().associateStream();
    }
}
