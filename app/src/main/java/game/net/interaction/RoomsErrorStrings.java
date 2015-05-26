package game.net.interaction;


import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

/**
 * @author Luca
 * @since 2015-03-31
 */

public class RoomsErrorStrings {

    private HashMap<String, Integer> errors = new HashMap<String, Integer>();

    public RoomsErrorStrings() {
        createMap();
    }

    private void createMap() {
        errors.put(FieldsNames.ROOM_CREATE_ERROR_ALREADY_PRESENT, R.string.rooms_create_error_alreadypresent);
        errors.put(FieldsNames.ROOM_CREATE_ERROR_INVALID_NAME, R.string.rooms_create_error_invalidname);
    }

    public int getStringIdByError(String error) {
        return errors.get(error);
    }

}
