package game.net.communication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.LinkedList;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 19/05/15.
 */
public class RequestMaker {

    public static final String LOG_TAG = "RequestMaker";
    private LinkedList<JSONd> defaultRequests;

    public RequestMaker(JSONd... defaultRequests) {
        this.defaultRequests=new LinkedList<>(Arrays.asList(defaultRequests));
    }

    public JSONObject getNewRequestWithDefaultRequests(JSONd...jsoNcouples) {
        JSONObject request = getNewRequest(jsoNcouples);
        try {
            for (JSONd d: defaultRequests){
                Log.d(LOG_TAG,"JSONd: "+d);
            }
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

    public JSONObject getNewRequest(String[] names, SFVertex3f values) {
        JSONObject request = new JSONObject();
        try {
            if (names.length<3)
                throw new JSONException("ERROR with names");
            new JSONd(names[0],values.getX()).putrequest(request);
            new JSONd(names[1],values.getY()).putrequest(request);
            new JSONd(names[2],values.getZ()).putrequest(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

}
