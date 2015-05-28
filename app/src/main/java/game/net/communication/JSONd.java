package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that encapsulates a JSON request, an automatically translates it to a JSONObject.
 *
 * @author Stefano De Pace
 */
public class JSONd {

    //private String name;
    private Enum name;
    private Object value;

    /**
     * Creates a new JSONd, with represents a request with the given name and value.
     *
     * @param name  String that maps the value of the request.
     * @param value Value of the request.
     */
    public JSONd(Enum name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Creates a JSON request with the data given in the constructor.
     *
     * @param request JSONObject that represents the request.
     * @throws JSONException
     */
    public void putrequest(JSONObject request) throws JSONException {
        request.put(name.toString(), value);
    }

    public String toString() {
        return "[" + name + ";" + value + "]";
    }

}
