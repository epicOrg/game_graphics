package game.net.handling;

import android.os.Handler;
import android.os.Message;

import com.example.alessandro.computergraphicsexample.MainActivity;

import game.net.data.Startable;

/**
 * {@link Handler} for handle encryption state.
 */
public class EncryptionHandler extends Handler {

    private Startable startable;

    /**
     * Creates a new <code>EncryptionHandler</code>.
     *
     * @param startable the <code>Startable</code> which start when ended encryption.
     */
    public EncryptionHandler(Startable startable) {
        this.startable = startable;
    }

    @Override
    public void handleMessage(Message msg) {
        //Log.d(LOG_TAG, "handleMessage");
        if ((boolean) msg.obj)
            startable.start(null);
    }

}
