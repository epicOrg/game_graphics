package game.net.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import game.net.interaction.FieldsNames;

/**
 * @author Micieli
 * @date 31/03/2015
 */

public class Rooms implements Service {

    public static final String LOG_TAG = "Rooms";

    public static final int LIST = 0;
    public static final int JOIN = 1;
    public static final int ERRORS = 2;

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
                    message = getRoomListMessage();
                    break;
                case FieldsNames.ROOM_CREATE:
                    message = getRoomCreateMessage();
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

    private Message getRoomListMessage() throws JSONException {
        JSONObject object = json.getJSONObject(FieldsNames.LIST);
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

    private Message getRoomCreateMessage() throws JSONException {
        String[] errorsResults;
        if (json.getBoolean(FieldsNames.NO_ERRORS)) {
            errorsResults = new String[]{};
            Log.d(LOG_TAG, "Room created");
        } else {
            JSONObject errorsObject = json.getJSONObject(FieldsNames.ERRORS);
            JSONArray errorsArray = errorsObject.getJSONArray(FieldsNames.ERRORS);
            errorsResults = new String[errorsArray.length()];
            for (int i = 0; i < errorsArray.length(); i++) {
                errorsResults[i] = errorsArray.getString(i);
            }
        }

        return handler.obtainMessage(ERRORS, errorsResults);
    }

    private Message getJoinMessage() throws JSONException {
        String name = json.getString(FieldsNames.ROOM_NAME);
        boolean result = json.getBoolean(FieldsNames.RESULT);

        return handler.obtainMessage(JOIN, new RoomJoinResult(name, result));
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

    public class RoomJoinResult {

        private String name;
        private boolean result;

        public RoomJoinResult(String name, boolean result) {
            this.name = name;
            this.result = result;
        }

        public String getName() {
            return name;
        }

        public boolean getResult() {
            return result;
        }

    }

}
