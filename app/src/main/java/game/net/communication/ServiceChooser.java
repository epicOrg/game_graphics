package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.fieldsnames.ServicesFields;
import game.net.interaction.FieldsNames;
import game.net.services.Audio;
import game.net.services.CurrentRoom;
import game.net.services.Game;
import game.net.services.Login;
import game.net.services.Polling;
import game.net.services.Register;
import game.net.services.Rooms;
import game.net.services.Service;
import game.net.services.Unknown;

/**
 * @author Noris
 * @date 26/03/2015
 */

public class ServiceChooser {

    public Service setService(JSONObject json) throws JSONException {
        switch (ServicesFields.valueOf(json.getString(ServicesFields.SERVICE.toString()))) {
            case REGISTER:
                return new Register(json);
            case LOGIN:
                return new Login(json);
            case ROOMS:
                return new Rooms(json);
            case CURRENT_ROOM:
                return new CurrentRoom(json);
            case GAME:
                return new Game(json);
            case AUDIO:
                return new Audio(json);
            case POLLING:
                return new Polling(json);
            default:
                //TODO
                return new Unknown();
        }
    }
}
