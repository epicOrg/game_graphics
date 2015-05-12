package game.listeners;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;

import game.controls.Button;
import game.controls.ButtonsControl;

/**
 * Created by Andrea on 01/04/2015.
 */
public class TouchListener implements TouchListenerInterface {

    public static final String LOG_TAG = "TouchListener";

    public static final long TIME_SLEEP = 10;

    private ButtonsControl buttonsControl;
    private DirectionMoveListenerInterface directionMoveListener;

    private int positionId = -1;
    private volatile boolean isPressing = false;
    private int directionId = -1;
    private volatile boolean isMoving = false;

    private float previousX, previousY;
    private Thread moveThread;

    private boolean readyToPlay = false;

    public TouchListener(ButtonsControl buttonsControl, DirectionMoveListenerInterface directionMoveListener) {
        this.buttonsControl = buttonsControl;
        this.directionMoveListener = directionMoveListener;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    @Override
    public void onTouchEvent(final MotionEvent event) {
        if (!readyToPlay)
            return;

        final int action = MotionEventCompat.getActionMasked(event);
        final int index = MotionEventCompat.getActionIndex(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                final float touchX = MotionEventCompat.getX(event, index);
                final float touchY = MotionEventCompat.getY(event, index);

                if (buttonsControl.isInsideAButton(touchX, touchY) && !isPressing) {
                    startPressing(event, index, touchX, touchY);
                } else if (!isMoving) {
                    startMoving(event, index, touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (isPressing && (MotionEventCompat.getPointerId(event, index) == positionId)) {
                    stopPressing();
                } else if (isMoving && (MotionEventCompat.getPointerId(event, index) == directionId)) {
                    stopMoving();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                actionMovePositionProcessor(event);
                actionMoveDirectionProcessor(event);
                break;
        }
    }

    private void actionMovePositionProcessor(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (event.getPointerId(i) == positionId && isPressing) {
                final float touchX = MotionEventCompat.getX(event, i);
                final float touchY = MotionEventCompat.getY(event, i);

                if (!buttonsControl.isInsideAButton(touchX, touchY)) {
                    stopPressing();
                }
            } else if (event.getPointerId(i) != directionId && !isPressing) {
                final float touchX = MotionEventCompat.getX(event, i);
                final float touchY = MotionEventCompat.getY(event, i);

                if (buttonsControl.isInsideAButton(touchX, touchY)) {
                    startPressing(event, i, touchX, touchY);
                }
            }
        }
    }

    private void actionMoveDirectionProcessor(MotionEvent event) {
        if (isMoving) {
            for (int i = 0; i < event.getPointerCount(); i++) {
                if (event.getPointerId(i) == directionId) {
                    final float touchX = MotionEventCompat.getX(event, i);
                    final float touchY = MotionEventCompat.getY(event, i);

                    float dx = touchX - previousX;
                    float dy = touchY - previousY;
                    previousX = touchX;
                    previousY = touchY;

                    directionMoveListener.move(dx, dy);
                    return;
                }
            }
        }
    }

    private void startPressing(MotionEvent event, int index, final float touchX, final float touchY) {
        isPressing = true;
        positionId = MotionEventCompat.getPointerId(event, index);

        moveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPressing) {
                    Button pressedButton = buttonsControl.getPressedButton(touchX, touchY);

                    if (pressedButton.isNeedToBeReady() && !readyToPlay) {
                        return;
                    }

                    if (pressedButton != null)
                        pressedButton.execute(TIME_SLEEP);
                    else
                        Log.d(LOG_TAG, "Null button");

                    if (!pressedButton.isContinuousPressing())
                        return;

                    try {
                        Thread.sleep(TIME_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        );
        moveThread.start();
    }

    private void startMoving(MotionEvent event, int index, float touchX, float touchY) {
        isMoving = true;
        directionId = MotionEventCompat.getPointerId(event, index);

        previousX = touchX;
        previousY = touchY;
    }

    private void stopPressing() {
        isPressing = false;
        positionId = -1;
        try {
            moveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopMoving() {
        isMoving = false;
        directionId = -1;
    }

}
