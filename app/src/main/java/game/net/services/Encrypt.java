package game.net.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.EncryptFields;
import game.net.fieldsnames.ServicesFields;
import game.net.interaction.FieldsNames;

/**
 * @author Noris
 * @date 28/04/2015
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
        encryptionRequest.put(ServicesFields.SERVICE.toString(), ServicesFields.ENCRYPT.toString());
        encryptionRequest.put(ServicesFields.SERVICE_TYPE.toString(), EncryptFields.PUBLIC_KEY_REQUEST.toString());

        return encryptionRequest;
    }

}
