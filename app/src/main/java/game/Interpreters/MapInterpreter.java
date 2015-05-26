package game.Interpreters;

import android.os.Message;

import java.util.Arrays;
import java.util.LinkedList;
import game.graphics.Map;
import game.graphics.MapObjects;
import game.net.GameHandlerListener;
import game.player.PlayerStatus;
import game.net.services.Game;

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
    public int getKey() {
        return Game.MAP;
    }

    @Override
    public void interpret(Message msg) {
        //Log.d(LOG_TAG, "processMapMessage");
        Game.GameMapResult results = (Game.GameMapResult) msg.obj;
        map = new Map();

        for (Game.GameMapObject o : results.getGameMapObjects()) {
            map.addObjects(MapObjects.MAP.getObjectFromNameAndData(o.object, o.position, o.size, o.texture));
        }

        groundWidth = results.getWidth();
        groundHeight = results.getHeight();

        status.getPosition().set3f(results.playerPositionX, results.playerPositionY, results.playerPositionZ);

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
