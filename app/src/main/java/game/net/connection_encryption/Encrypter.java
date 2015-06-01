package game.net.connection_encryption;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Encrypts a {@link String} using a symmetric {@link Key}.
 *
 * @author Noris
 * @date 27/04/2015
 */

public class Encrypter {

    private byte[] encryptedData;
    private Key asymmetricKey;

    public Encrypter(Key asymmetricKey) {
        this.asymmetricKey = asymmetricKey;
    }

    /**
     * Encrypts a <code>String</code> using a symmetric <code>Key</code>.
     *
     * @param unencryptedString the <code>String</code> to encrypt.
     */
    public void encrypt(String unencryptedString) {

        byte[] unencryptedData = unencryptedString.getBytes();

        try {

            Cipher cipher = Cipher.getInstance(EncryptionConst.SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, asymmetricKey);
            encryptedData = cipher.doFinal(unencryptedData);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the encrypted data.
     */
    public byte[] getEncryptedData() {
        return encryptedData;
    }

    /**
     * @return the encrypted data in string format.
     */
    public String getEncryptedString() {
        return StringConverter.bytesToString(encryptedData);
    }

}
