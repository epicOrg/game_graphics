package login.connection_encryption;

import org.json.JSONException;
import org.json.JSONObject;

import login.interaction.FieldsNames;

/**
 * Created by Noris on 27/04/15.
 */
public class SecureConnectionApplicator {

    private ConnectionEncrypter connectionEncrypter;

    public SecureConnectionApplicator(ISymmetricKeyGenerator symmetricKeyGenerator) {
        connectionEncrypter = new ConnectionEncrypter(symmetricKeyGenerator);
    }

    public JSONObject encrypt(JSONObject jsonRequest) throws JSONException {

        if (jsonRequest.has(FieldsNames.SERVICE) &&
                jsonRequest.getString(FieldsNames.SERVICE).equals(FieldsNames.ENCRYPT))
            return jsonRequest;

        String uncryptedRequest = jsonRequest.getString(FieldsNames.ENCRYPTED_MESSAGE);
        String encryptedRequest = connectionEncrypter.encryptRequest(uncryptedRequest);

        JSONObject jsonEncryptedRequest = new JSONObject();
        jsonEncryptedRequest.put(FieldsNames.ENCRYPTED_MESSAGE, encryptedRequest);

        return jsonEncryptedRequest;
    }

    public JSONObject decrypt(JSONObject jsonResponse) throws JSONException {

        if (jsonResponse.has(FieldsNames.PUBLIC_KEY)) {

            connectionEncrypter.setPublicKey(jsonResponse
                    .getString(FieldsNames.PUBLIC_KEY));

            return jsonResponse;
        }

        String encryptedResponse = jsonResponse.getString(FieldsNames.ENCRYPTED_MESSAGE);
        String uncryptedResponse = connectionEncrypter.decryptResponse(encryptedResponse);

        return new JSONObject(uncryptedResponse);
    }

}