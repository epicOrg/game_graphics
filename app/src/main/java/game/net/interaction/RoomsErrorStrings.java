package game.net.interaction;


import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

import game.net.fieldsnames.RoomFields;
import game.net.fieldsnames.RoomsFields;

/**
 * This class manages the error message shown in case of error in room context.
 *
 * @author Micieli
 * @since 31/03/2015
 */

public class RoomsErrorStrings {

    private HashMap<String, Integer> errors = new HashMap<String, Integer>();

    public RoomsErrorStrings() {
        createMap();
    }

    private void createMap() {
        errors.put(RoomsFields.ROOM_CREATE_ERROR_ALREADY_PRESENT.toString(), R.string.rooms_create_error_alreadypresent);
        errors.put(RoomsFields.ROOM_CREATE_ERROR_INVALID_NAME.toString(), R.string.rooms_create_error_invalidname);
    }

    public int getStringIdByError(String error) {
        return errors.get(error);
    }

}
