package game.listeners;

/**
 * Listener for the events about movement's direction.
 *
 * @author Torlaschi
 * @date 28/03/2015
 */
public interface DirectionMoveListenerInterface {

    public void update(int width, int height);
    void move(float dx, float dy);

}
