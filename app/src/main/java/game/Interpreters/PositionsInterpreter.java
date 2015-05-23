package game.Interpreters;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import game.Room;
import game.player.Player;
import login.interaction.FieldsNames;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 23/05/15.
 */
public class PositionsInterpreter implements  Interpreter{

    public static final String LOG_TAG = "PositionsInterpreter";
    private Room room;

    public PositionsInterpreter(Room room) {
        this.room = room;
    }

    @Override
    public String getKey() {
        return FieldsNames.GAME_POSITIONS;
    }

    @Override
    public void interpret(JSONObject json) {
        Log.d(LOG_TAG, "processPositionsMessage");

        try {
            JSONArray jPlayers = json.getJSONArray(FieldsNames.GAME_PLAYERS);

            for (int i = 0; i < jPlayers.length(); i++) {
                JSONObject jPlayer = jPlayers.getJSONObject(i);
                String username = jPlayer.getString(FieldsNames.USERNAME);
                JSONObject pos = jPlayer.getJSONObject(FieldsNames.GAME_POSITION);
                JSONObject dir = jPlayer.getJSONObject(FieldsNames.GAME_DIRECTION);

                float xPos = Float.parseFloat(pos.getString(FieldsNames.GAME_X));
                float yPos = Float.parseFloat(pos.getString(FieldsNames.GAME_Y)) - 1.5f;
                float zPos = Float.parseFloat(pos.getString(FieldsNames.GAME_Z));
                float xDir = Float.parseFloat(dir.getString(FieldsNames.GAME_X));
                float yDir = Float.parseFloat(dir.getString(FieldsNames.GAME_Y)) - 1.5f;
                float zDir = Float.parseFloat(dir.getString(FieldsNames.GAME_Z));

                Player p = room.getPlayerByUsername(username);
                p.getStatus().getPosition().set(new SFVertex3f(xPos,yPos,zPos));
                p.getStatus().getDirection().set(new SFVertex3f(xDir,yDir,zDir));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
