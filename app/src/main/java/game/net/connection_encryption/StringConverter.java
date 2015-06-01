package game.net.connection_encryption;

import android.util.Base64;

/**
 * @author Noris
 * @date 2015/06/01
 */

public class StringConverter {

    /*
     * The base 64 encoding method.
     */
    private static final int FLAG = Base64.NO_WRAP;

    /**
     * Converts an array of bytes into a string, using Base 64 encoding schemes
     * (with a FLAG encoding).
     *
     * @param bytes an array of bytes
     * @return a string who codify the bytes array in Base64
     */
    public static String bytesToString(byte[] bytes) {
        return Base64.encodeToString(bytes, FLAG);
    }

    /**
     * Converts a string encoded with Base 64 encoding schemes (in the FLAG encoding)
     * into the original bytes array.
     *
     * @param string a string encoded using using Base 64 encoding schemes
     * @return the original array of bytes
     */
    public static byte[] stringToBytes(String string) {
        return Base64.decode(string, FLAG);
    }

}
