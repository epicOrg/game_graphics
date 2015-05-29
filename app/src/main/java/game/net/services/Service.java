package game.net.services;

import android.os.Handler;

import org.json.JSONObject;

/**
 * A general service: it needs a JSONObject to start and a Hanlder.
 */

public interface Service {

    public void start(JSONObject json);

    public void setHandler(Handler handler);

}