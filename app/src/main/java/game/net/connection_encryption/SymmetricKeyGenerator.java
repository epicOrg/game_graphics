package game.net.connection_encryption;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SymmetricKeyGenerator implements ISymmetricKeyGenerator {

    private SecretKey key;

    public void generateKey() {
        try {

            // SecureRandom secureRandom = new SecureRandom();

            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionConst.SYMMETRIC_ALGORITHM);
            keyGenerator.init(128);
            key = keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public SecretKey getKey() {
        return key;
    }

}
