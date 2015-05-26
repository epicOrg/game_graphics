package game.Interpreters;

import android.os.Message;

/**
 * Interface for interpreting messages.
 *
 * @author Stefano De Pace
 */
public interface Interpreter {

    /**
     * Gives a standard key value to map this interpret; used to distinguish between different interpreters.
     *
     * @return key value.
     */
    int getKey();

    /**
     * Interpretes a message.
     *
     * @param msg Message to interpret.
     */
    void interpret(Message msg);

}
