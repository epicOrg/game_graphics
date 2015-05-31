package game.net.interpreters;

import android.graphics.Color;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;

import game.Configurations;
import game.net.GameHandlerListener;
import game.net.GamePositionSender;
import game.net.fieldsnames.GameFields;
import game.net.services.Game;
import game.views.MessageScreen;

public class StatusInterpreter implements Interpreter {

    public static final String LOG_TAG = "StatusInterpreter";
    public static final long waitTime = Configurations.CONF.getInt("miscellaneous","endWaitTime");
    public static final String messageWin=Configurations.CONF.getString("miscellaneous", "messageWin"),
        messageLose=Configurations.CONF.getString("miscellaneous","messageLose"),
        messageDraw=Configurations.CONF.getString("miscellaneous","messageDraw"),
        messageInterrupted=Configurations.CONF.getString("miscellaneous","messageInterrupted");
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
                    messageScreen.setText(messageWin, Color.GREEN);
                    break;
                case GAME_DRAW:
                    messageScreen.setText(messageDraw, Color.BLUE);
                    break;
                case GAME_LOSE:
                    messageScreen.setText(messageLose, Color.RED);
                    break;
                case GAME_INTERRUPTED:
                    messageScreen.setText(messageInterrupted, Color.BLACK);
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
