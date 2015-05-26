package game.net.services;

import android.content.Context;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.interaction.FieldsNames;

/**
 * @author Noris
 * @since 2015-03-26
 */

public class Unknown implements Service {

    @Override
    public void start() {
        //return getResponse().toString();
    }

    @Override
    public void setHandler(Handler handler) {

    }

    public void setContext(Context context) {

    }

    private JSONObject getResponse() {

        JSONObject jsonResponse = new JSONObject();

        try {

            jsonResponse.put(FieldsNames.SERVICE, FieldsNames.UNKNOWN);
            return jsonResponse;

        } catch (JSONException e) {
            //TODO
            return new JSONObject();
        }

    }

}
