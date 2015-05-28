package game.net.connection_encryption;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.fieldsnames.EncryptFields;
import game.net.fieldsnames.ServicesFields;
import game.net.interaction.FieldsNames;

/**
 * @author Noris
 * @date 27/04/2015
 */
public class SecureConnectionApplicator {

    private ConnectionEncrypter connectionEncrypter;

    public SecureConnectionApplicator(ISymmetricKeyGenerator symmetricKeyGenerator) {
        connectionEncrypter = new ConnectionEncrypter(symmetricKeyGenerator);
    }

    public JSONObject encrypt(JSONObject jsonRequest) throws JSONException {

        if (jsonRequest.has(ServicesFields.SERVICE.toString()) &&
                jsonRequest.getString(ServicesFields.SERVICE.toString()).equals(ServicesFields.ENCRYPT.toString()))
            return jsonRequest;

        String uncryptedRequest = jsonRequest.getString(EncryptFields.ENCRYPTED_MESSAGE.toString());
        String encryptedRequest = connectionEncrypter.encryptRequest(uncryptedRequest);

        JSONObject jsonEncryptedRequest = new JSONObject();
        jsonEncryptedRequest.put(EncryptFields.ENCRYPTED_MESSAGE.toString(), encryptedRequest);

        return jsonEncryptedRequest;
    }

    public JSONObject decrypt(JSONObject jsonResponse) throws JSONException {

        if (jsonResponse.has(EncryptFields.PUBLIC_KEY.toString())) {

            connectionEncrypter.setPublicKey(jsonResponse
                    .getString(EncryptFields.PUBLIC_KEY.toString()));

            return jsonResponse;
        }

        String encryptedResponse = jsonResponse.getString(EncryptFields.ENCRYPTED_MESSAGE.toString());
        String uncryptedResponse = connectionEncrypter.decryptResponse(encryptedResponse);

        return new JSONObject(uncryptedResponse);
    }

}