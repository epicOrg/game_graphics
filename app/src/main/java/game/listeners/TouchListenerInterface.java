package game.listeners;

import android.view.MotionEvent;

/**
 * Listener for the touch events.
 *
 * @author Torlaschi
 * @date 01/04/2015
 */
public interface TouchListenerInterface {

    void onTouchEvent(MotionEvent event);

    void setReadyToPlay(boolean readyToPlay);

}
