package game.net.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.communication.NotConnectedException;
import game.net.communication.ServerCommunicationThread;
import game.net.interaction.FieldsNames;

/**
 * Polling service respond to Server request, in order to confirm that I am alive.
 *
 * @author Micieli
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public void setHandler(Handler handler) {

    }
}
