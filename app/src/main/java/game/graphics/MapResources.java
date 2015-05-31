package game.graphics;

import android.util.Log;
import java.util.HashMap;

/**
 * Created by depa on 31/05/15.
 */
public class MapResources<R> {

    private HashMap<String, R> map = new HashMap();
    private R defaultValue;
    private String resourceType, logtag;

    public MapResources(R defaultValue, String resourceType, String logtag) {
        this.defaultValue = defaultValue;
        this.resourceType = resourceType;
        this.logtag = logtag;
    }

    public R getResourceFromName(String name) {
        if (map.containsKey(name))
            return map.get(name);
        else {
            String message = resourceType + " " + name + " not mapped!";
            if (defaultValue != null) {
                Log.d(logtag, message);
                return defaultValue;
            } else
                throw new RuntimeException(message);
        }
    }

    public void mapResource(String name, R resource) {
        map.put(name, resource);
    }

}
