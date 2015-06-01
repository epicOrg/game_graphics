package game.net.interpreters;

import android.os.Message;

/**
 * Interface for interpreting a <code>Message</code>.
 *
 * @author De Pace
 */
public interface Interpreter {

    /**
     * Gives a standard key value to map this interpret; used to distinguish between different interpreters.
     *
     * @return key value.
     */
    int getKey();

    /**
     * Interprets a <code>Message</code>.
     *
     * @param msg <code>Message</code> to interpret.
     */
    void interpret(Message msg);

}
