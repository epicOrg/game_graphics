package game.net.interaction;


import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.RegisterFields;

/**
 * This class manages the error messages shown in case of error during the registration.
 *
 * @author Micieli
 * @date 31/03/2015
 */

public class RegistrationErrorStrings {

    private HashMap<String, Integer> errors = new HashMap<String, Integer>();

    public RegistrationErrorStrings() {
        createMap();
    }

    private void createMap() {
        errors.put(CommonFields.PASSWORD + " " + RegisterFields.REGISTER_SHORT, R.string.error_short_password);
        errors.put(CommonFields.PASSWORD + " " + RegisterFields.REGISTER_LONG, R.string.password_long);
        errors.put(CommonFields.PASSWORD + " " + CommonFields.INVALID, R.string.error_invalid_password);
        errors.put(CommonFields.USERNAME + " " + RegisterFields.REGISTER_SHORT, R.string.username_short);
        errors.put(CommonFields.USERNAME + " " + RegisterFields.REGISTER_LONG, R.string.username_long);
        errors.put(CommonFields.USERNAME + " " + RegisterFields.REGISTER_INVALID_CHAR, R.string.username_invalid_char);
        errors.put(CommonFields.USERNAME + " " + RegisterFields.REGISTER_ALREADY_USED, R.string.username_already_used);
        errors.put(RegisterFields.EMAIL + " " + CommonFields.INVALID, R.string.email_not_valid);
        errors.put(RegisterFields.EMAIL + " " + RegisterFields.REGISTER_INVALID_DOMAIN, R.string.domain_not_valid);
        errors.put(RegisterFields.EMAIL + " " + RegisterFields.REGISTER_ALREADY_USED, R.string.email_already_used);
    }

    public int getStringIdByError(String error) {
        return errors.get(error);
    }

}
