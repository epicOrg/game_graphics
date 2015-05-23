package login.services;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.alessandro.computergraphicsexample.R;
import com.example.alessandro.computergraphicsexample.RoomActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import game.Room;
import login.interaction.FieldsNames;
import login.interaction.RoomsErrorStrings;

/**
 * Created by depa on 23/05/15.
 */
public class RoomSHandler extends android.os.Handler {

    public static final String LOG_TAG = "RoomSHandler";
    private ArrayList<Room> rooms;
    private Context context;
    private ListView roomsList;
    private String username;
    private int hashcode;

    public RoomSHandler(ArrayList<Room> rooms, Context context, ListView roomsList, String username, int hashcode) {
        this.rooms = rooms;
        this.context = context;
        this.roomsList = roomsList;
        this.username = username;
        this.hashcode = hashcode;
    }

    @Override
    public void handleMessage(Message msg) {
        JSONObject json=(JSONObject) msg.obj;
        try{
            if (json.getString(FieldsNames.SERVICE).equals(FieldsNames.ROOMS)){
                switch (json.getString(FieldsNames.SERVICE_TYPE)) {
                    case FieldsNames.ROOMS_LIST:
                        processListMessage(json);
                        break;
                    case FieldsNames.ROOM_JOIN:
                        processJoinMessage(json);
                        break;
                    case FieldsNames.ROOM_CREATE:
                        processErrorMessage(json);
                        break;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void processListMessage(JSONObject json) throws JSONException{
        rooms.clear();

        JSONObject object = json.getJSONObject(FieldsNames.LIST);

        Iterator<String> iterator = object.keys();

        while (iterator.hasNext()) {
            String name = iterator.next();

            JSONObject curObj = object.getJSONObject(name);

            int maxPlayers = curObj.getInt(FieldsNames.ROOM_MAX_PLAYERS);
            int currentPlayers = curObj.getInt(FieldsNames.ROOM_CURRENT_PLAYERS);
            rooms.add(new Room(name, maxPlayers, currentPlayers));
        }

        ArrayAdapter<Room> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, rooms);
        roomsList.setAdapter(adapter);
    }

    private void processJoinMessage(JSONObject json) throws JSONException{
        if (json.getBoolean(FieldsNames.RESULT)) {
            Intent intent = new Intent(context, RoomActivity.class);
            intent.putExtra(FieldsNames.USERNAME, username);
            intent.putExtra(FieldsNames.HASHCODE, hashcode);
            intent.putExtra(FieldsNames.ROOM_NAME, json.getString(FieldsNames.ROOM_NAME));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, context.getString(R.string.rooms_join_error), Toast.LENGTH_LONG).show();
        }
    }

    private void processErrorMessage(JSONObject json) throws JSONException{
        String[] errorsResults;
        RoomsErrorStrings roomsErrorStrings = new RoomsErrorStrings();
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

        String errors = "";

        for (String s : errorsResults) {
            errors += context.getString(roomsErrorStrings.getStringIdByError(s)) + "\n";
        }

        Toast.makeText(context, errors, Toast.LENGTH_LONG).show();

    }

}

