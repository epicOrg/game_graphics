package game.net.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.communication.NotConnectedException;
import game.net.communication.ServerCommunicationThread;
import game.net.interaction.FieldsNames;

/**
 *
 * Polling service respond to Server request, in order to
 *
 * Created by Luca on 11/05/2015.
 */
public class Polling implements Service {


    public Polling(JSONObject json) {

    }

    @Override
    public void start() {
        try {
            ServerCommunicationThread.getInstance().send(generatePollingResponse());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private JSONObject generatePollingResponse() {
        JSONObject response = new JSONObject();

        try {
            response.put(FieldsNames.SERVICE,FieldsNames.POLLING);
            //response.put(FieldsNames.USERNAME,username);
            //response.put(FieldsNames.HASHCODE,hascode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public void setHandler(Handler handler) {

    }
}
