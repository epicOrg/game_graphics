package game.net.connection_encryption;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Generate a symmetric {@link java.security.Key} using {@link KeyGenerator}.
 *
 * @author Noris
 * @date 27/04/2015
 * @see KeyGenerator
 */

public class SymmetricKeyGenerator implements ISymmetricKeyGenerator {

    private SecretKey key;

    public void generateKey() {
        try {

            // SecureRandom secureRandom = new SecureRandom();

            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionConst.SYMMETRIC_ALGORITHM);
            keyGenerator.init(EncryptionConst.SYMMETRIC_KEYSIZE);
            key = keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public SecretKey getKey() {
        return key;
    }

}
