package game.net.handling;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.alessandro.computergraphicsexample.RoomsActivity;
import java.util.ArrayList;
import game.data.UserData;
import game.net.fieldsnames.CommonFields;
import game.net.services.Login;

/**
 * {@link Handler} which allows the communication between login thread, activated
 * when server response is received, and login {@link android.app.Activity} when login data are received.
 */
public class LoginHandler extends Handler {

    private Context context;

    private ArrayList<LoginHandlerListener> loginHandlerListeners = new ArrayList<>();

    /**
     * Creates a new <code>LoginHandler</code>.
     *
     * @param context <code>Context</code> from which to start the <code>RoomsActivity</code> after login successfully.
     */
    public LoginHandler(Context context) {
        super();
        this.context = context;
    }

    /**
     * Adds a <code>LoginHandlerListener</code> to be called on completing login operation.
     *
     * @param l <code>LoginHandlerListener</code> to be called on completing login operation.
     */
    public void addLoginHandlerListeners(LoginHandlerListener l) {
        loginHandlerListeners.add(l);
    }

    /**
     * Removes a <code>LoginHandlerListener</code>.
     *
     * @param l the <code>LoginHandlerListener</code> to remove; in case it does't exists, does nothing.
     */
    public void removeLoginHandlerListeners(LoginHandlerListener l) {
        loginHandlerListeners.remove(l);
    }

    @Override
    public void handleMessage(Message msg) {
        Login.LoginResult result = (Login.LoginResult) msg.obj;
        if (result.isOk()) {
            for (LoginHandlerListener l : loginHandlerListeners)
                l.onLoginComplete(true);

            Intent intent = new Intent(context, RoomsActivity.class);
            UserData.DATA.addData(CommonFields.USERNAME, result.getUsername());
            UserData.DATA.addData(CommonFields.HASHCODE, result.getHashcode());
            context.startActivity(intent);
        } else {
            for (LoginHandlerListener l : loginHandlerListeners)
                l.onLoginComplete(false);
        }
        Log.d("RESULT", String.valueOf(result.isOk()));
    }

}