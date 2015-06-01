package game.net.connection_encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Wraps an asymmetric {@link java.security.Key} into a package encrypted with a public <code>Key</code>.
 *
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

    /**
     * Wraps an asymmetric <code>Key</code> passed to the constructor into
     * a package encrypted with the public <code>Key</code>.
     *
     * @param publicKey the {@link PublicKey} used to wrap
     *                  the symmetric <code>Key</code>.
     */
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

    /**
     * @return the wrapped symmetric <code>Key</code>.
     */
    public byte[] getWrappedKeyBytes() {
        return wrappedKey;
    }

    /**
     * @return the wrapped symmetric <code>Key</code> in <code>String</code> format.
     */
    public String getWrappedKeyString() {
        return StringConverter.bytesToString(wrappedKey);
    }

}
