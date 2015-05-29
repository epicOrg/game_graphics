package game.net.connection_encryption;

import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Noris
 * @author 27/04/2015
 */

public class KeyWrapper {

    private Key symmetricKey;
    private byte[] wrappedKey;

    public KeyWrapper(Key symmetricKey) {
        super();
        this.symmetricKey = symmetricKey;
    }

    public void wrapKey(Key publicKey) {

        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.WRAP_MODE, publicKey);
            wrappedKey = cipher.wrap(symmetricKey);


            Log.d("KEYGENWRApped", wrappedKey.toString());

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }

    public byte[] getWrappedKeyBytes() {
        return wrappedKey;
    }

    public String getWrappedKeyString() {
        Log.d("KEYGENWRApped", wrappedKey.toString());
        return Base64.encodeBase64String(wrappedKey);
    }

}
