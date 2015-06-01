package game.net.handling;

import android.os.Handler;
import android.os.Message;

import com.example.alessandro.computergraphicsexample.MainActivity;

/**
 * {@link Handler} for handle encryption state.
 */
public class EncryptionHandler extends Handler{

    private MainActivity activity;

    /**
     * Creates a new <code>EncryptionHandler</code>.
     * @param activity the <code>MainActivity</code> to which unblock login when ended encryption.
     */
    public EncryptionHandler(MainActivity activity){
        this.activity=activity;
    }

    @Override
    public void handleMessage(Message msg) {
        //Log.d(LOG_TAG, "handleMessage");
        if ((boolean)msg.obj)
            activity.letLogin();
    }

}
