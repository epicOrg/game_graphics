package game.net.data;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

/**
 * This class gets the registration data.
 *
 * @author Micieli
 * @date 27/03/2015
 */
public class RegistrationData extends LoginData {

    private String email;
    private String confirmPassword;

    public RegistrationData(String username,String email, String password, String confirmPassword) {
        super(username, password);
        this.email = email;
        this.confirmPassword = confirmPassword;
    }

    public boolean passwordsMatches(){
        return confirmPassword.equals(super.getPassword());
    }

    public boolean isEmailValid(){
        return email.contains("@") && email.contains(".");
    }

    public String getEmail() {
        return email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public boolean checkData(Context context, HashMap<Integer, View> views) {
        boolean cancel =  super.checkData(context, views);
        EditText mConfirmPasswordView = ((EditText)views.get(R.id.confirm_password));
        EditText mEmailView = ((EditText)views.get(R.id.email));

        if (!passwordsMatches()) {
            mConfirmPasswordView.setError(context.getString(R.string.error_passwords_different));
            mConfirmPasswordView.requestFocus();
            cancel = true;
        }

        if (TextUtils.isEmpty(getEmail())) {
            mEmailView.setError(context.getString(R.string.error_field_required));
            mEmailView.requestFocus();
            cancel = true;
        } else if (!isEmailValid()) {
            mEmailView.setError(context.getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            cancel = true;
        }
        return cancel;
    }
}
