package game.Interpreters;

import android.os.Message;

/**
 * Created by depa on 23/05/15.
 */
public interface Interpreter {

    int getKey();
    void interpret(Message msg);

}
