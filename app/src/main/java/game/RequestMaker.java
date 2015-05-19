package game;

import org.json.JSONException;
import org.json.JSONObject;

import login.interaction.FieldsNames;

/**
 * Created by depa on 19/05/15.
 */
public class RequestMaker {

    private JSONd[] defaultRequests;

    public RequestMaker() {
    }

    public RequestMaker(JSONd... defaultRequests) {
        this.defaultRequests=defaultRequests;
    }

    public JSONObject getNewRequestWithDefaultRequests(JSONd...jsoNcouples) {
        JSONObject request = getNewRequest(jsoNcouples);
        try {
            for (JSONd d: defaultRequests){
                d.putrequest(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    public JSONObject getNewRequest(JSONd...jsoNcouples) {
        JSONObject request = new JSONObject();
        try {
            for (JSONd d: jsoNcouples){
                d.putrequest(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

}
