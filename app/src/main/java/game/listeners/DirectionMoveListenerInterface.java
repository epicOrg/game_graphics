package game.listeners;

/**
 * Listener for the events about movement's direction.
 *
 * @author Torlaschi
 * @date 28/03/2015
 */
public interface DirectionMoveListenerInterface {

    /**
     * Updates the listener parameters when the screen changes size.
     *
     * @param width  width of the screen.
     * @param height height of the screen.
     */
    public void update(int width, int height);

    /**
     * Performs a move direction action.
     *
     * @param dx horizontal motion component.
     * @param dy vertical motion component.
     */
    void move(float dx, float dy);

}
