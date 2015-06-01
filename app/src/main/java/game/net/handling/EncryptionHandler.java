package game.net.handling;

import android.os.Handler;
import android.os.Message;

import com.example.alessandro.computergraphicsexample.MainActivity;

/**
 * Created by depa on 01/06/15.
 */
public class EncryptionHandler extends Handler{

    private MainActivity activity;

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
