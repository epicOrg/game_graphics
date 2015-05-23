package game.Interpreters;

import android.os.Message;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import game.Room;
import game.player.Player;
import login.interaction.FieldsNames;
import login.services.Game;
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
    public int getKey() {
        return Game.POSITIONS;
    }

    @Override
    public void interpret(Message msg) {
        //Log.d(LOG_TAG, "processPositionsMessage");
        Game.GamePositionsResult results = (Game.GamePositionsResult) msg.obj;
        HashMap<String, Game.GamePositionsObject> gamePositionsObjectHashMap = results.getGamePositionsObjectHashMap();

        for (String s : gamePositionsObjectHashMap.keySet()) {
            Player p = room.getPlayerByUsername(s);
            p.getStatus().getPosition().set(gamePositionsObjectHashMap.get(s).pos);
            p.getStatus().getDirection().set(gamePositionsObjectHashMap.get(s).dir);
        }
    }
}
