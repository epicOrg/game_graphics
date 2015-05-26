package game;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by depa on 19/05/15.
 */
public class JSONd {

    private String name;
    private Object value;

    public JSONd(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public void putrequest(JSONObject request) throws JSONException {
        request.put(name, value);
    }

    public String toString(){
        return "["+name+";"+value+"]";
    }

}
