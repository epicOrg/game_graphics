package game.net;

/**
 * Interface for objects containing actions to perform when some events about the Game happen.
 *
 * @author Torlaschi
 */
public interface GameHandlerListener {

    /**
     * Perform actions after map was received from server.
     */
    void onMapReceived();

    /**
     * Perform actions after "GAME_GO" message was received from server.
     */
    void onGameGo();

    /**
     * Perform actions after "GAME_EXIT" message was received from server.
     */
    void onGameFinish();

}