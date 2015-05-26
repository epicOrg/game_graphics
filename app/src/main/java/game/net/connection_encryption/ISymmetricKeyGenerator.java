package game.net.connection_encryption;

import javax.crypto.SecretKey;

/**
 * @author Noris
 * @date 27/04/2015
 */
public interface ISymmetricKeyGenerator {

    void generateKey();

    SecretKey getKey();
}
