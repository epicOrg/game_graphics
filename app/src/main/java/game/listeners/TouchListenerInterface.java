package game.listeners;

import android.view.MotionEvent;

/**
 * Created by Andrea on 01/04/2015.
 */
public interface TouchListenerInterface {

    void onTouchEvent(MotionEvent event);

    void setReadyToPlay(boolean readyToPlay);

}
