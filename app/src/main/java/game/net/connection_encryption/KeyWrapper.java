package game.net.connection_encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * @author Noris
 * @author 27/04/2015
 */

public class KeyWrapper {

    private SecretKey symmetricKey;
    private byte[] wrappedKey;

    public KeyWrapper(SecretKey symmetricKey) {
        super();
        this.symmetricKey = symmetricKey;
    }

    public void wrapKey(PublicKey publicKey) {

        try {

            Cipher cipher = Cipher.getInstance(EncryptionConst.ASYMMETRIC_DECODE);
            cipher.init(Cipher.WRAP_MODE, publicKey);
            wrappedKey = cipher.wrap(symmetricKey);

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
        return StringConverter.bytesToString(wrappedKey);
    }

}
