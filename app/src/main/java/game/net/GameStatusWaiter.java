package game.net;

import org.json.JSONException;
import org.json.JSONObject;

import game.JSONd;
import game.RequestMaker;
import game.Waiter;
import login.audio.AudioCallManager;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;

/**
 * Created by Andrea on 04/05/2015.
 */
public class GameStatusWaiter implements Waiter {

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private RequestMaker requestMaker;

    public GameStatusWaiter(RequestMaker requestMaker) {
        this.requestMaker=requestMaker;
    }

    @Override
    public void unleash() {
        try {
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE, FieldsNames.GAME),
                    new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_STATUS),
                    new JSONd(FieldsNames.GAME_READY, true)));
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

    }
}
