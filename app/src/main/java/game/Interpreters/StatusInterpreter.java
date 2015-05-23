package game.Interpreters;

import android.graphics.Color;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import game.net.GameHandlerListener;
import game.net.GamePositionSender;
import game.views.MessageScreen;
import login.interaction.FieldsNames;

/**
 * Created by depa on 23/05/15.
 */
public class StatusInterpreter implements Interpreter{

    public static final String LOG_TAG = "StatusInterpreter";
    private MessageScreen messageScreen;
    private GamePositionSender gamePositionSender;
    private LinkedList<GameHandlerListener> gameHandlerListeners =new LinkedList<>();

    public StatusInterpreter(MessageScreen messageScreen, GamePositionSender gamePositionSender, GameHandlerListener... gameHandlerListeners) {
        this.messageScreen = messageScreen;
        this.gamePositionSender = gamePositionSender;
        this.gameHandlerListeners = new LinkedList<>(Arrays.asList(gameHandlerListeners));
    }

    @Override
    public String getKey() {
        return FieldsNames.GAME_STATUS;
    }

    @Override
    public void interpret(JSONObject json) {
        Log.d(LOG_TAG, "processStatusMessage");

        try{
            if (json.has(FieldsNames.GAME_GO) && json.getBoolean(FieldsNames.GAME_GO))
                Log.d(LOG_TAG, "results.isGo");
            for (GameHandlerListener l : gameHandlerListeners)
                l.onGameGo();
            if (json.has(FieldsNames.GAME_END) && (json.getString(FieldsNames.GAME_END) != null)) {
                Log.d(LOG_TAG, "results.getGameEnd");
                String gameEnd = json.getString(FieldsNames.GAME_END);
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
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (GameHandlerListener l : gameHandlerListeners)
                            l.onGameFinish();
                    }
                }).start();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
