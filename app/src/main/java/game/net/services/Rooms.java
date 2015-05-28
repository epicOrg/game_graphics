package game.net.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.RoomFields;
import game.net.fieldsnames.RoomsFields;
import game.net.fieldsnames.ServicesFields;
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
            switch (RoomsFields.valueOf(json.getString(ServicesFields.SERVICE_TYPE.toString()))) {
                case ROOMS_LIST:
                    message = getRoomListMessage();
                    break;
                case ROOM_CREATE:
                    message = getRoomCreateMessage();
                    break;
                case ROOM_JOIN:
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
        JSONObject object = json.getJSONObject(RoomsFields.ROOMS_LIST.toString());
        RoomsResult[] roomsResults = new RoomsResult[object.length()];
        Iterator<String> iterator = object.keys();

        int count = 0;
        while (iterator.hasNext()) {
            String name = iterator.next();

            JSONObject curObj = object.getJSONObject(name);

            int maxPlayers = curObj.getInt(RoomsFields.ROOM_MAX_PLAYERS.toString());
            int currentPlayers = curObj.getInt(RoomsFields.ROOM_CURRENT_PLAYERS.toString());
            roomsResults[count++] = new RoomsResult(name, maxPlayers, currentPlayers);
        }

        return handler.obtainMessage(LIST, roomsResults);
    }

    private Message getRoomCreateMessage() throws JSONException {
        String[] errorsResults;
        if (json.getBoolean(CommonFields.NO_ERRORS.toString())) {
            errorsResults = new String[]{};
            Log.d(LOG_TAG, "Room created");
        } else {
            JSONObject errorsObject = json.getJSONObject(CommonFields.ERRORS.toString());
            JSONArray errorsArray = errorsObject.getJSONArray(CommonFields.ERRORS.toString());
            errorsResults = new String[errorsArray.length()];
            for (int i = 0; i < errorsArray.length(); i++) {
                errorsResults[i] = errorsArray.getString(i);
            }
        }

        return handler.obtainMessage(ERRORS, errorsResults);
    }

    private Message getJoinMessage() throws JSONException {
        String name = json.getString(RoomFields.ROOM_NAME.toString());
        boolean result = json.getBoolean(CommonFields.RESULT.toString());

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
