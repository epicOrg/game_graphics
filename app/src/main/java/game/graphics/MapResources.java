package game.graphics;

import android.util.Log;

import java.util.HashMap;

/**
 * Generic class used to map generic resources from Strings.
 *
 * @author De Pace
 */
public class MapResources<R> {

    private HashMap<String, R> map = new HashMap();
    private R defaultValue;
    private String resourceType, logtag;

    /**
     * Creates a new <code>MapResources</code>  for a given type.
     *
     * @param defaultValue Value of the default parameter to return from getResource in case of not mapped <code>String</code> .
     * @param resourceType <code>String</code>  that describes the type of resource of this <code>MapResources</code>.
     * @param logtag       <code>Log</code>  tag for error messages in case a resource is not mapped.
     */
    public MapResources(R defaultValue, String resourceType, String logtag) {
        this.defaultValue = defaultValue;
        this.resourceType = resourceType;
        this.logtag = logtag;
    }

    /**
     * Returns a mapped resource from String.
     *
     * @param name Name of the resource needed.
     * @return the mapped resource. If not mapped, returns the default one, and add a message to the <code>Log</code> which indicates the name
     * of the not mapped resource. If the default one is null, throws a <code><RuntimeException</code> with the same message.
     */
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

    /**
     * Maps a new Resource with the given name.
     *
     * @param name     Name of the resource to map.
     * @param resource Resource needed to be mapped.
     */
    public void mapResource(String name, R resource) {
        map.put(name, resource);
    }

}
