package game.net.interpreters;

import android.graphics.Color;
import android.os.Message;
import android.util.Log;

import com.example.alessandro.computergraphicsexample.R;

import java.util.Arrays;
import java.util.LinkedList;

import game.Conf;
import game.net.GameHandlerListener;
import game.net.GamePositionSender;
import game.net.fieldsnames.GameFields;
import game.net.services.Game;
import game.views.MessageScreen;

public class StatusInterpreter implements Interpreter {

    public static final String LOG_TAG = "StatusInterpreter";
    public static final long waitTime = Conf.CONF.getInt(R.integer.endWaitTime);
    private MessageScreen messageScreen;
    private GamePositionSender gamePositionSender;
    private LinkedList<GameHandlerListener> gameHandlerListeners = new LinkedList<>();

    /**
     * Creates a new StatusInterpreter.
     *
     * @param messageScreen        MessageScreen that shows the status.
     * @param gamePositionSender   GamePositionSender to stop sending position data.
     * @param gameHandlerListeners GameHandlerListeners to call at game start and end.
     */
    public StatusInterpreter(MessageScreen messageScreen, GamePositionSender gamePositionSender, GameHandlerListener... gameHandlerListeners) {
        this.messageScreen = messageScreen;
        this.gamePositionSender = gamePositionSender;
        this.gameHandlerListeners = new LinkedList<>(Arrays.asList(gameHandlerListeners));
    }

    @Override
    public int getKey() {
        return Game.STATUS;
    }

    /**
     * Interprets status data.
     */
    @Override
    public void interpret(Message msg) {
//        Log.d(LOG_TAG, "processStatusMessage");
        Game.GameStatusResult results = (Game.GameStatusResult) msg.obj;

        if (results.go)
            Log.d(LOG_TAG, "results.isGo");
        for (GameHandlerListener l : gameHandlerListeners)
            l.onGameGo();
        if (results.gameEnd != null) {
            Log.d(LOG_TAG, "results.getGameEnd");
            String gameEnd = results.gameEnd;
            switch (GameFields.valueOf(gameEnd)) {
                case GAME_WIN:
                    messageScreen.setText(R.string.messageWin, Color.GREEN);
                    break;
                case GAME_DRAW:
                    messageScreen.setText(R.string.messageDraw, Color.BLUE);
                    break;
                case GAME_LOSE:
                    messageScreen.setText(R.string.messageLose, Color.RED);
                    break;
                case GAME_INTERRUPTED:
                    messageScreen.setText(R.string.messageInterrupted, Color.BLACK);
                    break;
            }

            gamePositionSender.setSending(false);
            messageScreen.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (GameHandlerListener l : gameHandlerListeners)
                        l.onGameFinish();
                }
            }).start();
        }
    }

}
