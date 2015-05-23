package login.communication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import login.interaction.FieldsNames;
import login.services.Audio;
import login.services.CurrentRoom;
import login.services.Game;
import login.services.GenericService;
import login.services.Login;
import login.services.Polling;
import login.services.Register;
import login.services.Rooms;
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
                Log.d("SERVICECHOOSER","REGISTER");
                return new GenericService(json);//return new Register(json);
            case FieldsNames.LOGIN:
                Log.d("SERVICECHOOSER","LOGIN");
                return new GenericService(json);//return new Login(json);
            case FieldsNames.ROOMS:
                Log.d("SERVICECHOOSER","ROOMS");
                return new GenericService(json);//return new Rooms(json);
            case FieldsNames.CURRENT_ROOM:
                Log.d("SERVICECHOOSER","CURRENTROOM");
                return new GenericService(json);//return new CurrentRoom(json);
            case FieldsNames.GAME:
                return new Game(json);
            case FieldsNames.AUDIO:
                return new Audio(json);
            case FieldsNames.POLLING:
                return new Polling(json);
            default:
                //TODO
                return new Unknown();
        }
    }
}
