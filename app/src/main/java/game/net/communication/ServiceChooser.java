package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import game.net.fieldsnames.ServicesFields;
import game.net.services.Service;

/**
 * Selector of a service from the JSONObject.
 *
 * @author Noris
 * @date 26/03/2015
 */

public class ServiceChooser {

    private HashMap<Enum, Service> servicesMap=new HashMap<>();

    public ServiceChooser(){
        servicesMap=new ServiceInitializer().mapServices();
    }

    public Service setService(JSONObject json) throws JSONException{
        Service service=servicesMap.get(ServicesFields.valueOf(json.getString(ServicesFields.SERVICE.toString())));
        if (service!=null)
            return service;
        else
            return servicesMap.get(ServicesFields.UNKNOWN);
    }

}
