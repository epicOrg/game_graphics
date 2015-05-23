package login.services;

import android.os.Handler;

import org.json.JSONObject;

/**
 * Questa classe è provvisoria, se il refactoring di ServiceChooser funziona, non servirà più nemmeno quello.
 */
public class GenericService implements Service{

    private Handler handler;
    private JSONObject json;

    public GenericService(JSONObject json) {
        this.json = json;
    }

    @Override
    public void setHandler(Handler handler) {
        this.handler=handler;
    }

    @Override
    public void start() {
        handler.obtainMessage(0, json).sendToTarget();
    }
}
