package game.net.communication;

import org.json.JSONException;
import org.json.JSONObject;

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
        switch (json.getString(FieldsNames.SERVICE)) {
            case FieldsNames.REGISTER:
                return new Register(json);
            case FieldsNames.LOGIN:
                return new Login(json);
            case FieldsNames.ROOMS:
                return new Rooms(json);
            case FieldsNames.CURRENT_ROOM:
                return new CurrentRoom(json);
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
