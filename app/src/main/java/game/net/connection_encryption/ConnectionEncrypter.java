package game.net.connection_encryption;

import android.util.Log;

import java.security.Key;
import java.security.PublicKey;

/**
 * This class manages the encryption of the connection:
 * it encrypts the request and decrypts the response.
 *
 * @author Noris
 * @date 27/04/2015
 */
public class ConnectionEncrypter {

    private ISymmetricKeyGenerator symmetricKeyGenerator;

    private Encrypter encrypter;
    private Decrypter decrypter;

    private PublicKey publicKey;

    public ConnectionEncrypter(ISymmetricKeyGenerator symmetricKeyGenerator) {

        this.symmetricKeyGenerator = symmetricKeyGenerator;
        this.symmetricKeyGenerator.generateKey();

        Log.d("superlol", symmetricKeyGenerator.getKey().toString());


        encrypter = new Encrypter(getSymmetricKey());
        decrypter = new Decrypter(getSymmetricKey());
    }

    private Key getSymmetricKey() {
        return symmetricKeyGenerator.getKey();
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = KeyConverter.stringToPublicKey(publicKey);
    }

    public String getWrappedKey() {
        KeyWrapper keyWrapper = new KeyWrapper(getSymmetricKey());
        keyWrapper.wrapKey(publicKey);
        return keyWrapper.getWrappedKeyString();
    }

    public String encryptRequest(String request) {
        encrypter.encrypt(request);
        return encrypter.getEncryptedString();
    }

    public String decryptResponse(String response) {
        decrypter.decrypt(response);
        return decrypter.getDecryptedString();
    }

}
