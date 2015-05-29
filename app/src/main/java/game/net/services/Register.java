package game.net.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import game.net.fieldsnames.CommonFields;


public class Register implements Service {
	
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
            Log.d("REGSTER_RESPONSE", json.toString());
            RegistrationResult result;
            if(value) {
                result = new RegistrationResult(true, null);
            }else{
                ArrayList<String> errors;
                errors = extractErrors();
                result = new RegistrationResult(false,errors );
            }
            Message message= handler.obtainMessage(0,result);
            message.sendToTarget();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> extractErrors() throws JSONException {
        ArrayList<String> errorKinds = new ArrayList<>();
        JSONObject errorsObj = json.getJSONObject(CommonFields.ERRORS.toString());
        Iterator<String> errors = errorsObj.keys();
        while (errors.hasNext()){
            String errorName = errors.next();
            JSONArray errorTypes = errorsObj.getJSONArray(errorName);
            for(int i = 0; i< errorTypes.length(); i++){
                errorKinds.add(errorName +" "+ errorTypes.getString(i));
            }
        }
        return errorKinds;
    }

    @Override
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class RegistrationResult {

        private boolean ok;
        private ArrayList<String> errors;

        public RegistrationResult(boolean result, ArrayList<String> errors) {
            this.ok = result;
            this.errors = errors;
        }

        public boolean isOk() {
            return ok;
        }

        public ArrayList<String> getErrors() {
            return errors;
        }
    }

}
