package game.net.services;

import android.os.Handler;

import org.json.JSONObject;

public interface Service {

    public void start(JSONObject json);

    public void setHandler(Handler handler);

}