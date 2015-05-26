package game.Interpreters;

import android.os.Message;

import java.util.HashMap;

import game.Room;
import game.player.Player;
import game.net.services.Game;

/**
 * Interpreter that interpretes position data.
 */
public class PositionsInterpreter implements  Interpreter{

    public static final String LOG_TAG = "PositionsInterpreter";
    private Room room;

    /**
     * Creates a new PositionInterpreter.
     * @param room Room which contains players to which update position data.
     */
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
