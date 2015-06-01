package game.net.interaction;


import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

import game.net.fieldsnames.RoomsFields;

/**
 * This class manages the error message shown in case of error in room context.
 *
 * @author Micieli
 * @since 31/03/2015
 */

public class RoomsErrorStrings {

    private HashMap<String, Integer> errors = new HashMap<>();

    /**
     * Creates a new <code>RoomsErrorStrings</code> and maps every error descriptions with a corrisponding error <code>String</code>.
     */
    public RoomsErrorStrings() {
        createMap();
    }

    private void createMap() {
        errors.put(RoomsFields.ROOM_CREATE_ERROR_ALREADY_PRESENT.toString(), R.string.rooms_create_error_alreadypresent);
        errors.put(RoomsFields.ROOM_CREATE_ERROR_INVALID_NAME.toString(), R.string.rooms_create_error_invalid_name);
    }

    /**
     * Returns an error <code>String</code> id given an error description.
     *
     * @param error <code>String</code> that describes the error.
     * @return the error <code>String</code> id.
     */
    public int getStringIdByError(String error) {
        return errors.get(error);
    }

}
