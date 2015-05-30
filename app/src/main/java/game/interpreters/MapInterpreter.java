package game.interpreters;

import android.os.Message;

import java.util.Arrays;
import java.util.LinkedList;

import game.graphics.Map;
import game.graphics.MapObjects;
import game.net.GameHandlerListener;
import game.net.services.Game;
import game.player.PlayerStatus;

public class MapInterpreter implements Interpreter {

    public static final String LOG_TAG = "MapInterpreter";
    private int groundWidth, groundHeight;
    private PlayerStatus status;
    private Map map;
    private LinkedList<GameHandlerListener> gameHandlerListeners = new LinkedList<>();

    /**
     * Creates a new MapInterpreter.
     *
     * @param status               PlayerStatus to which assign the position given with map data.
     * @param gameHandlerListeners GameHandlerListeners to call when the map data is received.
     */
    public MapInterpreter(PlayerStatus status, GameHandlerListener... gameHandlerListeners) {
        this.status = status;
        this.gameHandlerListeners = new LinkedList<>(Arrays.asList(gameHandlerListeners));
    }

    @Override
    public int getKey() {
        return Game.MAP;
    }

    /**
     * Interprets the map.
     */
    @Override
    public void interpret(Message msg) {
        //Log.d(LOG_TAG, "processMapMessage");
        Game.GameMapResult results = (Game.GameMapResult) msg.obj;
        map = new Map();

        for (Game.GameMapObject o : results.gameMapObjects) {
            map.addObjects(MapObjects.MAP.getObjectFromNameAndData(o.object, o.position, o.size, o.texture));
        }

        groundWidth = results.width;
        groundHeight = results.height;

        status.setPositionValue(results.playerPositionX, results.playerPositionY, results.playerPositionZ);

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
