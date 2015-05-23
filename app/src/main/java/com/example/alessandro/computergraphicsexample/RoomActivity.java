package com.example.alessandro.computergraphicsexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import game.GameManager;
import game.JSONd;
import game.RequestMaker;
import game.Room;
import game.Team;
import game.player.Player;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;
import login.services.CurrentRoom;
import login.services.RooMHandler;

/**
 * Created by Andrea on 18/04/2015.
 */
public class RoomActivity extends ActionBarActivity {

    public static final String LOG_TAG = "RoomActivity";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private TextView roomStatus;
    private LinearLayout roomListsContainer;

    private Room currentRoom;

    private String username, roomName;
    private int hashcode;

    private Context context;
    private RequestMaker requestMaker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_room);
        context = this;

        Intent intent = getIntent();
        username = intent.getStringExtra(FieldsNames.USERNAME);
        hashcode = intent.getIntExtra(FieldsNames.HASHCODE, 0);
        roomName = intent.getStringExtra(FieldsNames.ROOM_NAME);
        requestMaker=new RequestMaker(new JSONd(FieldsNames.USERNAME, username),
                new JSONd(FieldsNames.HASHCODE, hashcode),
                new JSONd(FieldsNames.ROOM_NAME, roomName),
                new JSONd(FieldsNames.SERVICE, FieldsNames.CURRENT_ROOM));

        roomStatus = (TextView) findViewById(R.id.room_status);
        roomListsContainer = (LinearLayout) findViewById(R.id.room_lists_container);
        ImageButton fabImageButton = (ImageButton) findViewById(R.id.action_new_rom);

//        serverCommunicationThread.setHandler(new RoomHandler());
        serverCommunicationThread.setHandler(new RooMHandler(currentRoom, username, roomName, hashcode, roomListsContainer, this, roomStatus, requestMaker));
        try {
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_PLAYER_LIST)));
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        getSupportActionBar().setTitle(getString(R.string.room_title) + ": " + roomName);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(getString(R.string.room_exit_message));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_ACTIONS),
                                    new JSONd(FieldsNames.ROOM_ACTION, FieldsNames.ROOM_EXIT)));
                        } catch (NotConnectedException e) {
                            Toast.makeText(context, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public class RoomHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CurrentRoom.LIST:
                    processListMessage(msg);
                    break;
                case CurrentRoom.EXIT:
                    processExitMessage(msg);
                    break;
                case CurrentRoom.START:
                    processStartMessage(msg);
                    break;
            }
        }

        private void processListMessage(Message msg) {
            boolean firstTime = false;
            if (currentRoom == null)
                firstTime = true;

            CurrentRoom.CurrentRoomResult results = (CurrentRoom.CurrentRoomResult) msg.obj;
            currentRoom = new Room(roomName, results.getMaxPlayer(), results.getTeams());

            int currentPlayers = 0;

            roomListsContainer.removeAllViews();
            for (Team t : currentRoom.getTeams()) {
                ListView listView = new ListView(context);
                roomListsContainer.addView(listView);
                ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(context, android.R.layout.simple_list_item_1, t.getPlayers());

                TextView textView = new TextView(context);
                textView.setText(t.getName());
                listView.addHeaderView(textView);
                listView.setAdapter(adapter);

                currentPlayers += t.getPlayers().size();
            }

            roomStatus.setText("(" + currentPlayers + " / " + results.getMaxPlayer() + ")");

            if(firstTime) {
                try {
                    serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_ACTIONS),
                            new JSONd(FieldsNames.ROOM_ACTION, FieldsNames.ROOM_LIST_RECEIVED)));
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processExitMessage(Message msg) {
            boolean result = (boolean) msg.obj;

            if (result) {
                finish();
            }
        }

        private void processStartMessage(Message msg) {
            boolean result = (boolean) msg.obj;

            if (result) {
                GameManager gameManager = GameManager.MANAGER;
                gameManager.setRoom(currentRoom);

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra(FieldsNames.USERNAME, username);
                intent.putExtra(FieldsNames.HASHCODE, hashcode);
                startActivity(intent);
            }
        }

    }
}
