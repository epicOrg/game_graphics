package game.net.handling;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

import game.net.interpreters.Interpreter;


/**
 * Class containing a Map of Interpreter to read the messages sent from the server about the Game.
 *
 * @author Torlaschi
 */
public class GameHandler extends Handler {

    public static final String LOG_TAG = "GameHandler";
    private HashMap<Integer, Interpreter> map;

    /**
     * Construct a new GameHandler.
     *
     * @param interpreters list of Interpreter to read messages from the server
     */
    public GameHandler(Interpreter... interpreters) {
        map = new HashMap<>();
        for (Interpreter i : interpreters)
            map.put(i.getKey(), i);
    }

    @Override
    public void handleMessage(Message msg) {
        //Log.d(LOG_TAG, "handleMessage");
        map.get(msg.what).interpret(msg);
    }

}