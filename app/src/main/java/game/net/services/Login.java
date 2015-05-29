package game.net.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.fieldsnames.CommonFields;

/**
 * @author Micieli
 * @date 31/03/2015
 */

public class Login implements Service {

    private JSONObject json;
    private Handler handler;

    @Override
    public void start(JSONObject json) {
        this.json=json;
        readFields();
    }

    private void readFields() {
        try {
            boolean value = json.getBoolean(CommonFields.NO_ERRORS.toString());

            LoginResult result;
            String username = json.getString(CommonFields.USERNAME.toString());
            int hashcode = json.getInt(CommonFields.HASHCODE.toString());
            result = new LoginResult(value, username, hashcode);

            Message message = handler.obtainMessage(0, result);
            message.sendToTarget();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class LoginResult {

        private boolean ok;
        private String username;
        private int hashcode;

        public LoginResult(boolean ok, String username, int hashcode) {
            this.ok = ok;
            this.username = username;
            this.hashcode = hashcode;
        }

        public boolean isOk() {
            return ok;
        }

        public String getUsername() {
            return username;
        }

        public int getHashcode() {
            return hashcode;
        }

    }
}
