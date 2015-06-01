package game.net.interpreters;

import android.os.Message;
import android.util.Log;

import java.util.HashMap;

import game.data.Room;
import game.data.Team;
import game.net.services.Game;
import game.player.Player;

/**
 * Responsible for interpreting position data and updating all the other {@link Player}'s {@link game.player.PlayerStatus}.
 *
 * @see Room
 */
public class PositionsInterpreter implements Interpreter {

    public static final String LOG_TAG = "PositionsInterpreter";

    private Room room;

    /**
     * Creates a new <code>PositionInterpreter</code>.
     *
     * @param room <code>Room</code> which contains players to which update position data.
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
