package game.net.data;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

/**
 * Created by Luca on 27/03/2015.
 */
public class LoginData {

    public static final int MIN_LENGHT=8;

    private String username;
    private String password;

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private boolean isPasswordLognEnought() {
        return password.length() >= MIN_LENGHT;
    }

    private boolean isPasswordValid() {
        return password.matches(".*\\d+.*") && password.matches(".*[a-zA-Z]+.*");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkData(Context context, HashMap<Integer, View> views) {
        boolean cancel = false;

        TextView passwordField = (TextView) views.get(R.id.password);
        TextView userField = (TextView) views.get(R.id.username);

        if (!isPasswordValid()) {
            passwordField.setError(context.getString(R.string.error_invalid_password));
            passwordField.requestFocus();
            cancel = true;
        }

        if (!TextUtils.isEmpty(getPassword()) && !isPasswordLognEnought()) {
            passwordField.setError(context.getString(R.string.error_short_password));
            passwordField.requestFocus();
            cancel = true;
        }

        if (TextUtils.isEmpty(getUsername())) {
            userField.setError(context.getString(R.string.error_field_required));
            userField
                    .requestFocus();
            cancel = true;
        }
        return cancel;
    }
}



