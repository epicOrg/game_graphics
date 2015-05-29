package game.net.services;

import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import game.UserData;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.ServicesFields;
import game.net.interaction.FieldsNames;

/**
 * Polling service respond to Server request, in order to confirm that I am alive.
 *
 * @author Micieli
 */
public class Polling implements Service {

    public static final String LOG_TAG = "Polling";

    @Override
    public void start(JSONObject json) {
        try {
            ServerCommunicationThread.getInstance().send(generatePollingResponse());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private JSONObject generatePollingResponse() {
        RequestMaker maker = UserData.DATA.getRequestMakerWithData(CommonFields.USERNAME, CommonFields.HASHCODE);
        JSONObject response = maker.getNewRequestWithDefaultRequests(new JSONd(ServicesFields.SERVICE, ServicesFields.POLLING));
        //Log.d(LOG_TAG, response.toString());
        return response;
    }

    @Override
    public void setHandler(Handler handler) {

    }

}
