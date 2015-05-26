package game.Interpreters;

import android.graphics.Color;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;

import game.net.GameHandlerListener;
import game.net.GamePositionSender;
import game.net.interaction.FieldsNames;
import game.net.services.Game;
import game.views.MessageScreen;

/**
 * Interpreter to interpret status data.
 *
 * @author Stefano De Pace
 */
public class StatusInterpreter implements Interpreter {

    public static final String LOG_TAG = "StatusInterpreter";
    public static final long waitTime = 5000;
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
            switch (gameEnd) {
                case FieldsNames.GAME_WIN:
                    messageScreen.setText("YOU WIN!", Color.GREEN);
                    break;
                case FieldsNames.GAME_DRAW:
                    messageScreen.setText("YOU DIDN'T WIN and YOU DIDN'T LOSE!", Color.BLUE);
                    break;
                case FieldsNames.GAME_LOSE:
                    messageScreen.setText("YOU LOSE!", Color.RED);
                    break;
                case FieldsNames.GAME_INTERRUPTED:
                    messageScreen.setText("INTERRUPTED!", Color.BLACK);
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
