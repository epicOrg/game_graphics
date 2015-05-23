package game.Interpreters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import game.graphics.Map;
import game.graphics.MapObjects;
import game.net.GameHandlerListener;
import game.player.PlayerStatus;
import login.interaction.FieldsNames;

/**
 * Created by depa on 23/05/15.
 */
public class MapInterpreter implements Interpreter{

    public static final String LOG_TAG = "MapInterpreter";
    private int groundWidth, groundHeight;
    private PlayerStatus status;
    private Map map;
    private LinkedList<GameHandlerListener> gameHandlerListeners=new LinkedList<>();

    public MapInterpreter(PlayerStatus status, GameHandlerListener... gameHandlerListeners) {
        this.status = status;
        this.gameHandlerListeners = new LinkedList<>(Arrays.asList(gameHandlerListeners));
    }

    @Override
    public String getKey() {
        return FieldsNames.GAME_MAP;
    }

    @Override
    public void interpret(JSONObject json) {

        map = new Map();

        try {
            JSONArray jItems = json.getJSONArray(FieldsNames.GAME_ITEMS);
            for (int i = 0; i < jItems.length(); i++) {
                JSONObject jObject = jItems.getJSONObject(i);
                String object = jObject.getString(FieldsNames.GAME_OBJECT);
                String texture = jObject.getString(FieldsNames.GAME_TEXTURE);
                String position = jObject.getString(FieldsNames.GAME_POSITION);
                String size = jObject.getString(FieldsNames.GAME_SIZE);
                map.addObjects(MapObjects.MAP.getObjectFromNameAndData(object, position, size, texture));
            }

            groundWidth = json.getInt(FieldsNames.GAME_WIDTH);
            groundHeight = json.getInt(FieldsNames.GAME_HEIGHT);

            String jPlayerPosition = json.getString(FieldsNames.GAME_PLAYER_POSITION);
            float playerPositionX = Float.parseFloat(jPlayerPosition.split(" ")[0]);
            float playerPositionY = Float.parseFloat(jPlayerPosition.split(" ")[1]);
            float playerPositionZ = Float.parseFloat(jPlayerPosition.split(" ")[2]);

            status.getPosition().set3f(playerPositionX, playerPositionY, playerPositionZ);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (GameHandlerListener l : gameHandlerListeners)
            l.onMapReceived();
    }

    public Map getMap() {
        return map;
    }

    public int getGroundWidth() {
        return groundWidth;
    }

    public int getGroundHeight() {
        return groundHeight;
    }

}
