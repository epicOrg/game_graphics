package login.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.alessandro.computergraphicsexample.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import login.interaction.FieldsNames;
import login.interaction.ProgressShower;
import login.interaction.RegistrationErrorStrings;

/**
 * Created by depa on 23/05/15.
 */
public class RegisterHandler extends Handler{

    private Activity activity;
    private ProgressShower progressShower;

    public RegisterHandler(Activity activity, ProgressShower progressShower) {
        this.activity = activity;
        this.progressShower = progressShower;
    }

    @Override
    public void handleMessage(Message msg) {
        try{
            JSONObject json=(JSONObject) msg.obj;
            if (json.getString(FieldsNames.SERVICE).equals(FieldsNames.REGISTER)){
                if (json.getBoolean(FieldsNames.NO_ERRORS)) {
                    activity.finish();
                } else {
                    ArrayList<String> errors = extractErrors(json);
                    showAlertDialog(generateErrorString(errors));
                }
                progressShower.showProgress(false);
                Log.d("RESULT", String.valueOf(json.getBoolean(FieldsNames.NO_ERRORS)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void showAlertDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(error).setTitle(activity.getString(R.string.dialog_error));
        builder.setPositiveButton(activity.getString(R.string.dialog_try_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private ArrayList<String> extractErrors(JSONObject json) throws JSONException {
        ArrayList<String> errorKinds = new ArrayList<>();
        JSONObject errorsObj = json.getJSONObject(FieldsNames.ERRORS);
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

    private String generateErrorString(ArrayList<String> errors) {
        String error = "";
        RegistrationErrorStrings strings = new RegistrationErrorStrings();
        for (String string : errors) {
            error += activity.getString(strings.getStringIdByError(string)) + "\n";
        }
        return error;
    }
}
