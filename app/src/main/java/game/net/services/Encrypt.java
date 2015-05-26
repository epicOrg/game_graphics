package game.net.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.interaction.FieldsNames;

/**
 * Created by Noris on 28/04/15.
 */
public class Encrypt implements Service {

    private Handler handler;

    private JSONObject jsonRequest;
    private JSONObject jsonResponse;

    public Encrypt(JSONObject jsonResponse) {
        super();
        this.jsonResponse = jsonResponse;
    }

    @Override
    public void start() {
        //TODO
    }

    @Override
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private JSONObject encryptionRequest() throws JSONException {

        JSONObject encryptionRequest = new JSONObject();
        encryptionRequest.put(FieldsNames.SERVICE, FieldsNames.ENCRYPT);
        encryptionRequest.put(FieldsNames.SERVICE_TYPE, FieldsNames.PUBLIC_KEY_REQUEST);

        return encryptionRequest;
    }

}
