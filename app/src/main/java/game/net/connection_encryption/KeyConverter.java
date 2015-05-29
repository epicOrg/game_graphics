package game.net.connection_encryption;

import android.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

/**
 * @author Noris
 * @date 28/04/2015
 */
public class KeyConverter {

    public static String keyToString(Key key) {
        return Hex.encodeHexString(key.getEncoded());
    }

    public static Key stringToKey(String key, String algorithm) {

            return new SecretKeySpec(Base64.decode(key,Base64.DEFAULT ), algorithm);

    }

}
