package login.services;

import android.os.Handler;

import org.json.JSONObject;

/**
 * Created by Luca on 02/05/2015.
 */
public class Audio implements Service {

    private JSONObject jsonRequest;

    public Audio(JSONObject jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    @Override
    public void start() {

    }

    @Override
    public void setHandler(Handler handler) {

    }
}
