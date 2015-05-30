package game.net.connection_encryption;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * @author Noris
 * @date 2015/04/28
 */

public class KeyConverter {

    public static String keyToString(Key key) {
        return Base64.encodeToString(key.getEncoded(), Base64.CRLF);
    }

    public static PublicKey stringToPublicKey(String key) {

        byte[] decodedKey = Base64.decode(key, Base64.CRLF);

        X509EncodedKeySpec eks = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactor = null;

        try {
            keyFactor = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            return keyFactor.generatePublic(eks);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Key stringToSymmetricKey(String key) {

        byte[] decodedKey = Base64.decode(key, Base64.CRLF);
        return new SecretKeySpec(decodedKey, "AES");

    }

}
