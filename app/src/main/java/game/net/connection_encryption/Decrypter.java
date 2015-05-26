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
 * @author Noris
 * @date 27/04/2015
 */
public class Decrypter {

    private Key asymmetricKey;
    private byte[] decryptedData;

    public Decrypter(Key asymmetricKey) {
        this.asymmetricKey = asymmetricKey;
    }

    public void decrypt(String encryptedString) {

        byte[] encryptedData = StringConverter.decodeHexString(encryptedString);

        try {

            Cipher cipher = Cipher.getInstance("AES");
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

    public byte[] getDecryptedData() {
        return decryptedData;
    }

    public String getDecryptedString() {
        try {

            return new String(decryptedData, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
