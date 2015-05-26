package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;

import shadow.math.SFVertex3f;

/**
 * Class used to automatize JSON requests, encapsulating default requests.
 *
 * @author Stefano De Pace
 */
public class RequestMaker {

    public static final String LOG_TAG = "RequestMaker";
    private LinkedList<JSONd> defaultRequests;

    /**
     * Creates a new RequestMaker, optionally with some default requests to call implicitly.
     *
     * @param defaultRequests JSONds that encapsulates the default requests for this RequestMaker.
     */
    public RequestMaker(JSONd... defaultRequests) {
        this.defaultRequests = new LinkedList<>(Arrays.asList(defaultRequests));
    }

    /**
     * Creates a new JSONObjet that contains the default requests added in the constructor and the given requests.
     *
     * @param jsoNcouples JSONds that encapsulate the given requests.
     * @return the generated JSONObject that contains all the requests.
     */
    public JSONObject getNewRequestWithDefaultRequests(JSONd... jsoNcouples) {
        JSONObject request = getNewRequest(jsoNcouples);
        try {
            for (JSONd d : defaultRequests) {
                d.putrequest(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * Creates a new JSONObjet that contains only the given requests.
     *
     * @param jsoNcouples JSONds that encapsulate the given requests.
     * @return the generated JSONObject that contains the given requests.
     */
    public JSONObject getNewRequest(JSONd... jsoNcouples) {
        JSONObject request = new JSONObject();
        try {
            for (JSONd d : jsoNcouples) {
                d.putrequest(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * Creates a new JSONObjet that contains three requests mapped by names, and with values given by the component of a SFVertex3f.
     *
     * @param names  String array that maps the three components of the SFVertex3f; must have length at least 3, otherwise it calls an exception.
     * @param values SFVertex3f that contains the values for the request.
     * @return the generated JSONObject that contains the given requests.
     */
    public JSONObject getNewRequest(String[] names, SFVertex3f values) {
        JSONObject request = new JSONObject();
        try {
            if (names.length < 3)
                throw new JSONException("ERROR with names");
            new JSONd(names[0], values.getX()).putrequest(request);
            new JSONd(names[1], values.getY()).putrequest(request);
            new JSONd(names[2], values.getZ()).putrequest(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

}
