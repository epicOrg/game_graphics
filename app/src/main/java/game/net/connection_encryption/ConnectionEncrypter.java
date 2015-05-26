package game.net.connection_encryption;

import java.security.Key;

/**
 * @author Noris
 * @date 27/04/2015
 */
public class ConnectionEncrypter {

    private ISymmetricKeyGenerator symmetricKeyGenerator;

    private Encrypter encrypter;
    private Decrypter decrypter;

    private Key publicKey;

    public ConnectionEncrypter(ISymmetricKeyGenerator symmetricKeyGenerator) {

        this.symmetricKeyGenerator = symmetricKeyGenerator;
        this.symmetricKeyGenerator.generateKey();

        encrypter = new Encrypter(getSymmetricKey());
        decrypter = new Decrypter(getSymmetricKey());
    }

    private Key getSymmetricKey() {
        return symmetricKeyGenerator.getKey();
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = KeyConverter.stringToKey(publicKey);
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
