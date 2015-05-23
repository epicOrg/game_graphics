package game.net;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.alessandro.computergraphicsexample.RoomsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import login.interaction.FieldsNames;
import login.services.Login;

/**
 * Handler che permette la comunicazione tra il Thread di login attivato alla ricezione della risposta dal server e
 * l'Activity di login stessa ricevendo i risultati del Login
 */
public class LogHandler extends Handler {

    private Context context;

    private ArrayList<LoginHandlerListener> loginHandlerListeners = new ArrayList<>();

    public LogHandler(Context context) {
        super();
        this.context = context;
    }

    public void addLoginHandlerListeners(LoginHandlerListener l) {
        loginHandlerListeners.add(l);
    }

    public void removeLoginHandlerListeners(LoginHandlerListener l) {
        loginHandlerListeners.remove(l);
    }

    @Override
    public void handleMessage(Message msg) {
        JSONObject json=(JSONObject) msg.obj;
        try{
            if (json.getString(FieldsNames.SERVICE).equals(FieldsNames.LOGIN)){
                if (json.getBoolean(FieldsNames.NO_ERRORS)) {
                    for (LoginHandlerListener l : loginHandlerListeners)
                        l.onLoginComplete(true);
                    Intent intent = new Intent(context, RoomsActivity.class);
                    intent.putExtra(FieldsNames.USERNAME, json.getString(FieldsNames.USERNAME));
                    intent.putExtra(FieldsNames.HASHCODE, json.getInt(FieldsNames.HASHCODE));
                    context.startActivity(intent);
                } else {
                    for (LoginHandlerListener l : loginHandlerListeners)
                        l.onLoginComplete(false);
                }
                Log.d("RESULT", String.valueOf(json.getBoolean(FieldsNames.NO_ERRORS)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}