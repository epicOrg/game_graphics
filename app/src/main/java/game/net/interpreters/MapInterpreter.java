package game.net.interpreters;

import android.os.Message;
import java.util.Arrays;
import java.util.LinkedList;
import game.graphics.Map;
import game.graphics.MapObjects;
import game.net.handling.GameHandlerListener;
import game.net.services.Game;
import game.player.PlayerStatus;

/**
 * Responsible to create a <code>Map</code> from a <code>Message</code>.
 */
public class MapInterpreter implements Interpreter {

    public static final String LOG_TAG = "MapInterpreter";
    private int groundWidth, groundHeight;
    private PlayerStatus status;
    private Map map;
    private LinkedList<GameHandlerListener> gameHandlerListeners = new LinkedList<>();

    /**
     * Creates a new <code>MapInterpreter</code>.
     *
     * @param status               <code>PlayerStatus</code> to which assign the position given with map data.
     * @param gameHandlerListeners <code>GameHandlerListener</code> to call when the map data is received.
     */
    public MapInterpreter(PlayerStatus status, GameHandlerListener... gameHandlerListeners) {
        this.status = status;
        this.gameHandlerListeners = new LinkedList<>(Arrays.asList(gameHandlerListeners));
    }

    @Override
    public int getKey() {
        return Game.MAP;
    }

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

    /**
     * @return the interpreted <code>Map</code>. If not already interpreted, returns null.
     */
    public Map getMap() {
        return map;
    }

    /**
     * @return the ground width in map data.
     */
    public int getGroundWidth() {
        return groundWidth;
    }

    /**
     * @return the ground height in map data.
     */
    public int getGroundHeight() {
        return groundHeight;
    }

}
