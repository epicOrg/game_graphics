package game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by depa on 26/05/15.
 */
public enum UserData {
    DATA;

    public static final String LOG_TAG = "UserData";
    private HashMap<String, Object> dataMap=new HashMap<>();

    public void addData(String name, Object value){
        dataMap.put(name, value);
    }

    public Object getData(String name){
        return dataMap.get(name);

    }

    public RequestMaker getRequestMaker() {
        return getRequestMaker(dataMap.keySet());
    }

    public RequestMaker getRequestMakerWithData(String... names){
        return getRequestMaker(Arrays.asList(names));
    }

    private RequestMaker getRequestMaker(Iterable<String> it){
        LinkedList<JSONd> jsonds=new LinkedList<>();
        for (String s: it)
            jsonds.add(new JSONd(s, dataMap.get(s)));
        return new RequestMaker(jsonds.toArray(new JSONd[0]));
    }

}
