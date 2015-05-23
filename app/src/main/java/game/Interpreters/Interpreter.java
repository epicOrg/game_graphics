package game.Interpreters;

import org.json.JSONObject;

/**
 * Created by depa on 23/05/15.
 */
public interface Interpreter {

    String getKey();
    void interpret(JSONObject json);

}
