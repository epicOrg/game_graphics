package game.net.connection_encryption;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Noris on 28/04/15.
 */
public class KeyConverter {

    public static String keyToString(Key key) {
        return StringConverter.encodeHexString(key.getEncoded());
    }

    public static Key stringToKey(String key) {
        return new SecretKeySpec(StringConverter.decodeHexString(key), "AES");
    }

}
