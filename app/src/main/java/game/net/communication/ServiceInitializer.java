package game.net.communication;

import java.util.HashMap;
import game.net.fieldsnames.ServicesFields;
import game.net.services.Audio;
import game.net.services.CurrentRoom;
import game.net.services.Encrypt;
import game.net.services.Game;
import game.net.services.Login;
import game.net.services.Polling;
import game.net.services.Register;
import game.net.services.Rooms;
import game.net.services.Service;
import game.net.services.Unknown;

/**
 * Created by depa on 29/05/15.
 */
public class ServiceInitializer {

    public HashMap<Enum, Service> mapServices(){
        HashMap<Enum, Service> map=new HashMap<>();
        map.put(ServicesFields.LOGIN,new Login());
        map.put(ServicesFields.REGISTER,new Register());
        map.put(ServicesFields.ROOMS,new Rooms());
        map.put(ServicesFields.CURRENT_ROOM,new CurrentRoom());
        map.put(ServicesFields.GAME,new Game());
        map.put(ServicesFields.AUDIO,new Audio());
        map.put(ServicesFields.POLLING,new Polling());
        map.put(ServicesFields.ENCRYPT,new Encrypt());
        map.put(ServicesFields.UNKNOWN,new Unknown());
        return map;
    }

}
