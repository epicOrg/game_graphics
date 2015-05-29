package game.net.connection_encryption;

import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encrypter {

    private byte[] cryptedData;
    private Key asymmetricKey;

    public Encrypter(Key asymmetricKey) {
        super();
        this.asymmetricKey = asymmetricKey;
    }

    /**
     * @param uncryptedString String to encrypt.
     */
    public void encrypt(String uncryptedString) {

        byte[] uncryptedData = uncryptedString.getBytes();

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, asymmetricKey);
            cryptedData = cipher.doFinal(uncryptedData);
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

    public byte[] getEncryptedData() {
        return cryptedData;
    }

    public String getEncryptedString() {
        return Hex.encodeHexString(cryptedData);
    }

}
