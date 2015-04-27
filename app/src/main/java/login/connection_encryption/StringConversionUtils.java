package login.connection_encryption;

/**
 * Created by Noris on 27/04/15.
 */
public class StringConversionUtils {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Convert a byte array into an hexadecimal string.
     *
     * @param bytes array
     * @return hexadecimal string
     * @see <http://stackoverflow.com/a/9855338>
     */
    public static String encodeHexString(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];
       for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
