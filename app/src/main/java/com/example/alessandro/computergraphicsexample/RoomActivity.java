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
import game.JSONd;
import game.RequestMaker;
import game.Room;
import game.Team;
import game.UserData;
import game.player.Player;
import game.net.communication.NotConnectedException;
import game.net.communication.ServerCommunicationThread;
import game.net.interaction.FieldsNames;
import game.net.services.CurrentRoom;

/**
 * Room screen: every user can see who is in the same room.
 * Here teams are casually built.
 *
 * @author Torlaschi
 * @date 18/04/2015
 */
public class RoomActivity extends ActionBarActivity {

    public static final String LOG_TAG = "RoomActivity";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private TextView roomStatus;
    private LinearLayout roomListsContainer;

    private Room currentRoom;

    private Context context;
    private RequestMaker requestMaker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_room);
        context = this;

        UserData.DATA.addData(FieldsNames.SERVICE, FieldsNames.CURRENT_ROOM);
        requestMaker= UserData.DATA.getRequestMaker();

        roomStatus = (TextView) findViewById(R.id.room_status);
        roomListsContainer = (LinearLayout) findViewById(R.id.room_lists_container);
        ImageButton fabImageButton = (ImageButton) findViewById(R.id.action_new_room);

        serverCommunicationThread.setHandler(new RoomHandler());

        try {
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_PLAYER_LIST)));
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        getSupportActionBar().setTitle(getString(R.string.room_title) + ": " + UserData.DATA.getData(FieldsNames.ROOM_NAME));
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
            currentRoom = new Room((String)UserData.DATA.getData(FieldsNames.ROOM_NAME), results.getMaxPlayer(), results.getTeams());

            int currentPlayers = 0;

            roomListsContainer.removeAllViews();
            for (Team t : currentRoom.getTeams()) {
                ListView listView = new ListView(context);
                roomListsContainer.addView(listView);
                ArrayAdapter<Player> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, t.getPlayers());

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
                UserData.DATA.addData(FieldsNames.CURRENT_ROOM,currentRoom);

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        }

    }
}
