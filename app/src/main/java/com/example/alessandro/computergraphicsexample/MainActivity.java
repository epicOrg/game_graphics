package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import game.JSONd;
import game.RequestMaker;
import game.net.LogHandler;
import game.net.LoginHandler;
import game.net.LoginHandlerListener;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.communication.ServerCommunicationThreadListener;
import login.communication.ServerCommunicationThreadState;
import login.data.LoginData;
import login.interaction.FieldsNames;
import login.interaction.ProgressShower;

/**
 * Activity di LogIn in cui l'utente inserisce l'username e la password per
 * essere riconosciuto dal server
 */
public class MainActivity extends ActionBarActivity implements ServerCommunicationThreadListener, LoginHandlerListener {

    public static final String LOG_TAG = "MainActivity";

    private Activity activity;
    private ServerCommunicationThread serverCommunicationThread;
    private HashMap<Integer, View> views = new HashMap<>();

    private ProgressShower progressShower;
    private SharedPreferences loginPreference;
    private LoginData loginData;

    private boolean doubleBackToExitPressedOnce;
    private RequestMaker requestMaker=new RequestMaker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        getViews();

        loginPreference = getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        progressShower = new ProgressShower(views.get(R.id.login_progress), views.get(R.id.login_form), getResources().getInteger(android.R.integer.config_shortAnimTime));

        views.get(R.id.main_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThreadIfNotStarted();
            }
        });

        checkRememberMe();
        startThreadIfNotStarted();
    }

    private void startThreadIfNotStarted() {
        if (serverCommunicationThread != null)
            serverCommunicationThread.exit();

        serverCommunicationThread = ServerCommunicationThread.getInstance();
        serverCommunicationThread.addServerCommunicationThreadListener(this);
        setThreadHandler();
        try {
            serverCommunicationThread.start();
        } catch (IllegalThreadStateException e) {
            e.printStackTrace();
            updateThreadState(serverCommunicationThread.getThreadState());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setThreadHandler();
    }

    private void setThreadHandler() {
//        LoginHandler loginHandler = new LoginHandler(this);
//
//        loginHandler.addLoginHandlerListeners(this);
//        serverCommunicationThread.setHandler(loginHandler);

        LogHandler logHandler = new LogHandler(this);
        logHandler.addLoginHandlerListeners(this);
        serverCommunicationThread.setHandler(logHandler);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.login_exit_message), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void checkRememberMe() {
        if (loginPreference.getBoolean("Remember", false)) {
            ((TextView) views.get(R.id.username)).setText(loginPreference.getString(FieldsNames.USERNAME, "user"));
            ((TextView) views.get(R.id.password)).setText(loginPreference.getString(FieldsNames.PASSWORD, "pass"));
            ((CheckBox) views.get(R.id.remeberMeBox)).setChecked(true);
        }
    }

    /**
     * Invocato per attivare/disattivare la funzione RememberMe
     */
    public void rememberMe(View view) {
        CheckBox rememberBox = (CheckBox) findViewById(R.id.remeberMeBox);
        SharedPreferences.Editor editor = loginPreference.edit();
        if (rememberBox.isChecked())
            editor.putBoolean("Remember", true);
        else
            editor.putBoolean("Remember", false);
        editor.commit();
    }

    private void saveRememberMeData() {
        if (loginPreference.getBoolean("Remember", false)) {
            SharedPreferences.Editor editor = loginPreference.edit();
            editor.putString(FieldsNames.USERNAME, loginData.getUsername());
            editor.putString(FieldsNames.PASSWORD, loginData.getPassword());
            editor.commit();
            Log.d("REMEMBER", "fields saved");
        }
    }

    /**
     * Passa all'Activity di registrazione, invocato dalll'apposto bottone
     */
    public void notRegistered(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * Avvia il Login inviando la richiesta al server
     */
    public void attemptLogin(View view) {
        ((TextView) views.get(R.id.username)).setError(null);
        ((TextView) views.get(R.id.password)).setError(null);
        loginData = getData();
        boolean cancel = loginData.checkData(getApplicationContext(), views);

        if (!cancel) {
            progressShower.showProgress(true);
            try {
                serverCommunicationThread.send(requestMaker.getNewRequest(new JSONd(FieldsNames.SERVICE, FieldsNames.LOGIN),
                        new JSONd(FieldsNames.USERNAME, loginData.getUsername()),
                        new JSONd(FieldsNames.PASSWORD, loginData.getPassword())));
            } catch (NotConnectedException e) {
                progressShower.showProgress(false);
                Toast.makeText(activity, getString(R.string.error_not_connected), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    // recupera i dati dai campi di testo
    private LoginData getData() {
        String username = ((TextView) views.get(R.id.username)).getText().toString();
        String password = ((TextView) views.get(R.id.password)).getText().toString();
        return new LoginData(username, password);
    }

    @Override
    public void onThreadStateChanged(final ServerCommunicationThreadState state) {
        Log.d(LOG_TAG, "onThreadStateChanged");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateThreadState(state);
            }
        });
    }

    private void updateThreadState(ServerCommunicationThreadState state) {
        TextView statusTextView = ((TextView) views.get(R.id.main_status));
        switch (state) {
            case CONNECTING:
                statusTextView.setText(getString(R.string.main_status_connecting));
                findViewById(R.id.log_in).setEnabled(false);
                findViewById(R.id.not_registered).setEnabled(false);
                break;
            case CONNECTED:
                statusTextView.setText(getString(R.string.main_status_connected));
                findViewById(R.id.log_in).setEnabled(true);
                findViewById(R.id.not_registered).setEnabled(true);
                break;
            case NOT_CONNECTED:
                statusTextView.setText(getString(R.string.main_status_not_connected));
                findViewById(R.id.log_in).setEnabled(false);
                findViewById(R.id.not_registered).setEnabled(false);
                break;
        }
    }

    private void getViews() {
        putViewIntoMap(R.id.main_status);
        putViewIntoMap(R.id.main_refresh);
        putViewIntoMap(R.id.username);
        putViewIntoMap(R.id.password);
        putViewIntoMap(R.id.remeberMeBox);
        putViewIntoMap(R.id.login_form);
        putViewIntoMap(R.id.login_progress);
    }

    private void putViewIntoMap(int id) {
        views.put(id, findViewById(id));
    }

    @Override
    public void onLoginComplete(boolean result) {
        if (result) {
            saveRememberMeData();
        } else {
            showAlertDialog(activity.getString(R.string.login_failed));
        }
        progressShower.showProgress(false);
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

}