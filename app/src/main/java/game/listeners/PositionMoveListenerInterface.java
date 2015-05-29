package game.listeners;

/**
 * Listener for the events about positions during the movement.
 *
 * @author Torlaschi
 * @date 28/03/2015
 */
public interface PositionMoveListenerInterface {

    void move(float angleXZ, float angleYZ, long delta);

}
