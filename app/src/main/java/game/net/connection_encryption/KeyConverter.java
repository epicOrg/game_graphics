package game.net.connection_encryption;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.spec.SecretKeySpec;

/**
 * Some useful {@link Key} conversion methods.
 *
 * @author Noris
 * @date 2015/04/28
 * @see StringConverter
 * @see Key
 */

public class KeyConverter {

    /**
     * Converts a <code>Key</code> into a <code>String</code>.
     *
     * @param key a generic <code>Key</code>.
     * @return a <code>String</code> encoding the <code>Key</code>.
     */
    public static String keyToString(Key key) {
        return StringConverter.bytesToString(key.getEncoded());
    }

    /**
     * Convert a <code>String</code>, that encodes a <code>PublicKey</code>, into the <code>PublicKey</code>.
     *
     * @param key a <code>String</code> that encodes a <code>PublicKey</code>.
     * @return the <code>PublicKey</code>.
     */
    public static PublicKey stringToPublicKey(String key) {

        byte[] decodedKey = StringConverter.stringToBytes(key);

        X509EncodedKeySpec eks = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactor = null;

        try {
            keyFactor = KeyFactory.getInstance(EncryptionConst.ASYMMETRIC_ALGORITHM);
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

    /**
     * Convert a <code>String</code>, that encodes a generic <code>Key</code>, into the <code>Key</code>.
     *
     * @param key a <code>String</code> that encodes a <code>Key</code>.
     * @return the <code>Key</code>.
     */
    public static Key stringToSymmetricKey(String key) {

        byte[] decodedKey = StringConverter.stringToBytes(key);
        return new SecretKeySpec(decodedKey, EncryptionConst.SYMMETRIC_ALGORITHM);

    }

}
