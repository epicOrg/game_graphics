package login.communication;

import org.json.JSONException;
import org.json.JSONObject;

import login.interaction.FieldsNames;
import login.services.Login;
import login.services.Register;
import login.services.Service;
import login.services.Unknown;

/**
 * @author Noris
 * @since 2015-03-26
 */

public class ServiceChooser {

    public Service setService(JSONObject json) throws JSONException {
        switch (json.getString(FieldsNames.SERVICE)) {
            case FieldsNames.REGISTER:
                return new Register(json);
            case FieldsNames.LOGIN:
                return new Login(json);
            default:
                //TODO
                return new Unknown();
        }
    }
}