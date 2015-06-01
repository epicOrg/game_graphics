package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that encapsulates a JSON request, and automatically translates it to a {@link JSONObject}.
 *
 * @author De Pace
 */
public class JSONd {

    private Enum name;
    private Object value;

    /**
     * Creates a new <code>JSONd</code>, with represents a request with the given name and value.
     *
     * @param name  <code>Enum</code> that maps the value of the request.
     * @param value Value of the request.
     */
    public JSONd(Enum name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Creates a JSON request with the data given in the constructor.
     *
     * @param request <code>JSONObject</code> that represents the request.
     * @throws <code>JSONException</code>.
     */
    public void putRequest(JSONObject request) throws JSONException {
        request.put(name.toString(), value);
    }

    public String toString() {
        return "[" + name + ";" + value + "]";
    }

}
