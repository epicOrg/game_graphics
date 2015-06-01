package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;

import shadow.math.SFVertex3f;

/**
 * Class used to automatize JSON requests, encapsulating default requests.
 *
 * @author De Pace
 */
public class RequestMaker {

    public static final String LOG_TAG = "RequestMaker";
    private LinkedList<JSONd> defaultRequests;

    /**
     * Creates a new <code>RequestMaker</code>, optionally with some default requests to call implicitly.
     *
     * @param defaultRequests <code>JSONd</code> that encapsulates the default requests for this <code>RequestMaker</code>
     */
    public RequestMaker(JSONd... defaultRequests) {
        this.defaultRequests = new LinkedList<>(Arrays.asList(defaultRequests));
    }

    /**
     * Creates a new <code>JSONObject</code> that contains the default requests added in the constructor and the given requests.
     *
     * @param jsoNcouples <code>JSONd</code> that encapsulate the given requests.
     * @return the generated <code>JSONObject</code> that contains all the requests.
     */
    public JSONObject getNewRequestWithDefaultRequests(JSONd... jsoNcouples) {
        JSONObject request = getNewRequest(jsoNcouples);
        try {
            for (JSONd d : defaultRequests) {
                d.putRequest(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * Creates a new <code>JSONObject</code> that contains only the given requests.
     *
     * @param jsoNcouples <code>JSONd</code> that encapsulate the given requests.
     * @return the generated <code>JSONObject</code> that contains the given requests.
     */
    public JSONObject getNewRequest(JSONd... jsoNcouples) {
        JSONObject request = new JSONObject();
        try {
            for (JSONd d : jsoNcouples) {
                d.putRequest(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * Creates a new J<code>JSONObject</code> that contains three requests mapped by names, and with values given by the component of a <code>SFVertex3f</code>.
     *
     * @param names  <code>String</code> array that maps the three components of the <code>SFVertex3f</code>; must have length at least 3,
     *               otherwise it calls an <code>JSONException</code>.
     * @param values <code>SFVertex3f</code> that contains the values for the request.
     * @return the generated <code>JSONObject</code> that contains the given requests.
     */
    public JSONObject getNewRequest(Enum[] names, SFVertex3f values) {
        JSONObject request = new JSONObject();
        try {
            if (names.length < 3)
                throw new JSONException("ERROR with names");
            new JSONd(names[0], values.getX()).putRequest(request);
            new JSONd(names[1], values.getY()).putRequest(request);
            new JSONd(names[2], values.getZ()).putRequest(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

}
