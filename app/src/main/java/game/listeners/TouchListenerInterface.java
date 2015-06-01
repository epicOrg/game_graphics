package game.listeners;

import android.view.MotionEvent;

/**
 * Listener for the touch events.
 *
 * @author Torlaschi
 * @date 01/04/2015
 */
public interface TouchListenerInterface {

    /**
     * Called when the screen is touched.
     *
     * @param event represents a motion event.
     */
    void onTouchEvent(MotionEvent event);

    /**
     * This listener could be blocked and toggled with this method.
     *
     * @param readyToPlay whether the listener should respont to touch methods or not.
     */
    void setReadyToPlay(boolean readyToPlay);

}
