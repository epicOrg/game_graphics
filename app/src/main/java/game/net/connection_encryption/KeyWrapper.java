package game.net.connection_encryption;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Noris on 27/04/15.
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

        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public byte[] getWrappedKeyBytes() {
        return wrappedKey;
    }

    public String getWrappedKeyString() {
        return StringConverter.encodeHexString(wrappedKey);
    }

}
