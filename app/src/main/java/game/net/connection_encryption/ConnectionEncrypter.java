package game.net.connection_encryption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.SecretKey;

import game.net.fieldsnames.ServicesFields;

/**
 * This class manages the encryption of the connection:
 * it encrypts the request and decrypts the response.
 *
 * @author Noris
 * @date 27/04/2015
 * @see ISymmetricKeyGenerator
 * @see Encrypter
 * @see Decrypter
 */

public class ConnectionEncrypter {

    private static SecretKey symmetricKey;
    private static Encrypter encrypter;
    private static Decrypter decrypter;

    public static void init(ISymmetricKeyGenerator keyGenerator) {

        keyGenerator.generateKey();
        ConnectionEncrypter.symmetricKey = keyGenerator.getKey();

        encrypter = new Encrypter(symmetricKey);
        decrypter = new Decrypter(symmetricKey);
    }

    public static SecretKey getSymmetricKey() {
        return symmetricKey;
    }

    public static String encryptRequest(String request) {

        encrypter.encrypt(request);
        return encrypter.getEncryptedString();

    }

    public static String decryptResponse(String response) {

        if (isJSONObject(response)) {

            try {

                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.has(ServicesFields.SERVICE.toString()) && (
                        jsonResponse.getString(ServicesFields.SERVICE.toString())
                                .equals(ServicesFields.ENCRYPT.toString()) ||
                                jsonResponse.getString(ServicesFields.SERVICE.toString())
                                        .equals(ServicesFields.POLLING.toString()) ||
                                jsonResponse.getString(ServicesFields.SERVICE.toString())
                                        .equals(ServicesFields.CURRENT_ROOM.toString())))
                    return response;

            } catch (JSONException e) {
            }
        }

        decrypter.decrypt(response);
        return decrypter.getDecryptedString();

    }

    private static boolean isJSONObject(String string) {

        try {
            new JSONObject(string);
        } catch (JSONException e1) {
            try {
                new JSONArray(string);
            } catch (JSONException e2) {
                return false;
            }
        }

        return true;
    }

}