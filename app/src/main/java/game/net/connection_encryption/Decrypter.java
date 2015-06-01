package game.net.connection_encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Decrypts a {@link String} using the symmetric {@link Key}.
 *
 * @author Noris
 * @date 27/04/2015
 */

public class Decrypter {

    private Key asymmetricKey;
    private byte[] decryptedData;

    public Decrypter(Key asymmetricKey) {
        this.asymmetricKey = asymmetricKey;
    }

    /**
     * Decrypts a <code>String</code> using the symmetric <code>Key</code> (obviously the <code>Key</code> must be the
     * same used to encrypt the <code>String</code>).
     *
     * @param encryptedString the encrypted <code>String</code>.
     */
    public void decrypt(String encryptedString) {

        byte[] encryptedData = StringConverter.stringToBytes(encryptedString);

        try {

            Cipher cipher = Cipher.getInstance(EncryptionConst.SYMMETRIC_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, asymmetricKey);
            decryptedData = cipher.doFinal(encryptedData);

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
     * @return the decrypted data.
     */
    public byte[] getDecryptedData() {
        return decryptedData;
    }

    /**
     * @return the decrypted data in string format.
     */
    public String getDecryptedString() {
        try {
            return new String(decryptedData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
