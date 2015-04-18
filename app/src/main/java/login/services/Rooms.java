package login.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import login.interaction.FieldsNames;

/**
 * created by Luca on 31/03/2015
 */

public class Rooms implements Service {

    public static final String LOG_TAG = "Rooms";

    public static final int LIST = 0;
    public static final int JOIN = 1;

    private JSONObject json;
    private Handler handler;

    public Rooms(JSONObject json) {
        super();
        this.json = json;
    }

    @Override
    public void start() {
        readFields();
    }

    private void readFields() {
        try {
            Message message = null;
            switch (json.getString(FieldsNames.SERVICE_TYPE)) {
                case FieldsNames.ROOMS_LIST:
                    message = getRoomsListMessage();
                    break;
                case FieldsNames.ROOM_JOIN:
                    message = getJoinMessage();
                    break;
            }
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Message getRoomsListMessage() throws JSONException {
        JSONObject object = json.getJSONObject(FieldsNames.ROOMS_LIST);
        RoomsResult[] roomsResults = new RoomsResult[object.length()];
        Iterator<String> iterator = object.keys();

        int count = 0;
        while (iterator.hasNext()) {
            String name = iterator.next();

            JSONObject curObj = object.getJSONObject(name);

            int maxPlayers = curObj.getInt(FieldsNames.ROOM_MAX_PLAYERS);
            int currentPlayers = curObj.getInt(FieldsNames.ROOM_CURRENT_PLAYERS);
            roomsResults[count++] = new RoomsResult(name, maxPlayers, currentPlayers);
        }

        return handler.obtainMessage(LIST, roomsResults);
    }

    private Message getJoinMessage() throws JSONException {
        boolean result = json.getBoolean(FieldsNames.RESULT);

        return handler.obtainMessage(JOIN, result);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class RoomsResult {

        private String name;
        private int maxPlayers, currentPlayers;

        public RoomsResult(String name, int maxPlayers, int currentPlayers) {
            this.name = name;
            this.maxPlayers = maxPlayers;
            this.currentPlayers = currentPlayers;
        }

        public String getName() {
            return name;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }

        public int getCurrentPlayers() {
            return currentPlayers;
        }

    }

}