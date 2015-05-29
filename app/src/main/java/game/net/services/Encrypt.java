package game.net.services;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.SecretKey;

import game.UserData;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.connection_encryption.KeyConverter;
import game.net.connection_encryption.KeyWrapper;
import game.net.connection_encryption.SymmetricKeyGenerator;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.EncryptFields;
import game.net.fieldsnames.ServicesFields;
import game.net.interaction.FieldsNames;

/**
 * @author Noris
 * @date 28/04/2015
 */
public class Encrypt implements Service {

    private static final String LOG_TAG = "encryption";

    private Handler handler;

    private JSONObject jsonRequest;
    private JSONObject jsonResponse;

    @Override
    public void start(JSONObject json) {
        //TODO è giusto??
        this.jsonResponse=json;

        try {
            String publicKey = jsonResponse.getString(EncryptFields.PUBLIC_KEY.toString());
            SymmetricKeyGenerator keyGenerator = new SymmetricKeyGenerator();
            keyGenerator.generateKey();
            SecretKey privateKey = new SymmetricKeyGenerator().getKey();
            KeyWrapper wrapper = new KeyWrapper(privateKey);

            wrapper.wrapKey(KeyConverter.stringToPublicKey(publicKey));
            String privateWrapped = wrapper.getWrappedKeyString();

            RequestMaker requestMaker = UserData.DATA.getRequestMaker();

            JSONObject request = requestMaker.getNewRequest(new JSONd(ServicesFields.SERVICE, ServicesFields.ENCRYPT),
                    new JSONd(ServicesFields.SERVICE_TYPE, EncryptFields.WRAPPED_KEY),
                    new JSONd(EncryptFields.WRAPPED_KEY, privateWrapped));

            try {
                ServerCommunicationThread.getInstance().send(request);
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


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
