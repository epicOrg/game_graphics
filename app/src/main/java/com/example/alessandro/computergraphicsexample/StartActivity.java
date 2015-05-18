package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import login.communication.ServerCommunicationThread;

public class StartActivity extends ActionBarActivity {

    public static final String LOG_TAG = "MainActivity";

    private static final String KEY_SERVER_ADDRESS = "serverAddress";

    private Activity activity;
    private ServerCommunicationThread serverCommunicationThread;

    private EditText serverAddress;
    private Button serverAddressButton;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        activity = this;

        serverAddress = (EditText) findViewById(R.id.start_server_address);
        serverAddressButton = (Button) findViewById(R.id.start_server_address_button);

        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = preferences.edit();

        serverAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverAddressString = serverAddress.getText().toString().trim();
                serverCommunicationThread = ServerCommunicationThread.getInstance();
                serverCommunicationThread.setServerAddres(serverAddressString);

                editor.putString(KEY_SERVER_ADDRESS, serverAddressString);
                editor.apply();

                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        serverAddress.setText(preferences.getString(KEY_SERVER_ADDRESS, ""));
    }
}