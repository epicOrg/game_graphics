package game.net.connection_encryption;

import javax.crypto.SecretKey;

/**
 * Created by Noris on 27/04/15.
 */
public interface ISymmetricKeyGenerator {

    void generateKey();

    SecretKey getKey();
}
