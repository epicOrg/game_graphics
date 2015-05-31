package game.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import game.net.communication.JSONd;
import game.net.communication.RequestMaker;

/**
 * Singleton which maintains user data while advancing in the activities. It also creates RequestMaker with
 * all the added data, or with the requested data.
 *
 * @author De Pace
 */
public enum UserData {
    DATA;

    public static final String LOG_TAG = "UserData";
    private volatile HashMap<Enum, Object> dataMap = new HashMap<>();

    /**
     * Adds some data, mapped with a name.
     *
     * @param name  name to map the data.
     * @param value data to add to the UserData.
     */
    public void addData(Enum name, Object value) {
        dataMap.put(name, value);
    }

    /**
     * Returns some data from a given name.
     *
     * @param name name that maps the requested data.
     * @return the data requested, mapped by tha given name, or null if no mapping is present with that name.
     */
    public Object getData(Enum name) {
        return dataMap.get(name);

    }

    /**
     * Creates a new RequestMaker with default requests given by all the added data.
     *
     * @return the new generated RequestMaker.
     */
    public RequestMaker getRequestMaker() {
        return getRequestMaker(dataMap.keySet());
    }

    /**
     * Creates a new RequestMaker with default requests given by the data mapped by the given names.
     *
     * @param names names of the data previously added to use as default requests for the RequestMaker.
     * @return the new generated RequestMaker.
     */
    public RequestMaker getRequestMakerWithData(Enum... names) {
        return getRequestMaker(Arrays.asList(names));
    }

    private RequestMaker getRequestMaker(Iterable<Enum> it) {
        LinkedList<JSONd> jsonds = new LinkedList<>();
        for (Enum s : it)
            jsonds.add(new JSONd(s, dataMap.get(s)));
        return new RequestMaker(jsonds.toArray(new JSONd[0]));
    }

}
