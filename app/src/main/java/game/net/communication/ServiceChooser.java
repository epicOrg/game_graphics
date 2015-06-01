package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import game.net.fieldsnames.ServicesFields;
import game.net.services.Service;

/**
 * Selector of a {@link Service} from the {@link JSONObject}.
 *
 * @author Noris
 * @date 26/03/2015
 * @see Service
 */

public class ServiceChooser {

    private HashMap<Enum, Service> servicesMap = new HashMap<>();

    /**
     * Construct a new <code>ServiceChooser</code> using a <code>ServiceInitializer</code>.
     */
    public ServiceChooser() {
        servicesMap = new ServiceInitializer().mapServices();
    }

    /**
     * Sets the <code>Service</code> needed from a given <code>JSONObject</code>'s parameter <code>ServicesFields.SERVICE</code>.
     *
     * @param json <code>JSONObject</code> that is used to identify the needed <code>Service</code>.
     * @return the chosen <code>Service</code> or <code>ServicesFields.UNKNOWN</code> if no <code>Service</code> is mapped.
     * @throws JSONException
     */
    public Service setService(JSONObject json) throws JSONException {
        Service service = servicesMap.get(ServicesFields.valueOf(json.getString(ServicesFields.SERVICE.toString())));
        if (service != null)
            return service;
        else
            return servicesMap.get(ServicesFields.UNKNOWN);
    }

}
