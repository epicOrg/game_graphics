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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import game.data.Room;
import game.data.Team;
import game.data.UserData;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.fieldsnames.RoomFields;
import game.net.fieldsnames.ServicesFields;
import game.net.services.CurrentRoom;
import game.player.Player;

/**
 * Room screen: every user can see who is in the same room.
 * Here teams are casually built.
 *
 * @author Torlaschi
 * @date 18/04/2015
 */
public class RoomActivity extends ActionBarActivity {

    public static final String LOG_TAG = "RoomActivity";

    private int startDelay;

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private Room currentRoom;
    private RequestMaker requestMaker;
    private boolean isStartingGame = false;
    private boolean isExitingGame = false;
    private Timer startTimer;

    private Context context;
    private TextView roomStatus;
    private LinearLayout roomListsContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_room);
        context = this;
        startDelay = getResources().getInteger(R.integer.startWaitTime);

        UserData.DATA.addData(ServicesFields.SERVICE, ServicesFields.CURRENT_ROOM);
        requestMaker = UserData.DATA.getRequestMaker();

        roomStatus = (TextView) findViewById(R.id.room_status);
        roomListsContainer = (LinearLayout) findViewById(R.id.room_lists_container);

        serverCommunicationThread.setHandler(new RoomHandler());

        try {
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(ServicesFields.SERVICE_TYPE, RoomFields.ROOM_PLAYER_LIST.toString())));
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        getSupportActionBar().setTitle(getString(R.string.room_title) + ": " + UserData.DATA.getData(RoomFields.ROOM_NAME));
    }

    @Override
    public void onBackPressed() {
        if (startedGame){
            Toast.makeText(this, getString(R.string.game_already_started), Toast.LENGTH_LONG).show();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder.setTitle(getString(R.string.room_exit_message));
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sendExitRequest();
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
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!isStartingGame && !isExitingGame) {
            sendExitRequest();
        }
    }

    private boolean startedGame = false;


    private void sendExitRequest() {
        if (startTimer != null) {
            startTimer.cancel();
        }
        try {
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(ServicesFields.SERVICE_TYPE, RoomFields.ROOM_ACTIONS.toString()),
                    new JSONd(RoomFields.ROOM_ACTION, RoomFields.ROOM_EXIT.toString())));
        } catch (NotConnectedException e) {
            Toast.makeText(context, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
            currentRoom = new Room((String) UserData.DATA.getData(RoomFields.ROOM_NAME), results.getMaxPlayer(), results.getTeams());

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

            if (firstTime) {
                try {
                    serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(ServicesFields.SERVICE_TYPE, RoomFields.ROOM_ACTIONS.toString()),
                            new JSONd(RoomFields.ROOM_ACTION, RoomFields.ROOM_LIST_RECEIVED.toString())));
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processExitMessage(Message msg) {
            boolean result = (boolean) msg.obj;

            if (result) {
                isExitingGame = true;
                UserData.DATA.removeData(ServicesFields.SERVICE);
                UserData.DATA.removeData(RoomFields.ROOM_NAME);
                UserData.DATA.removeData(ServicesFields.CURRENT_ROOM);
                finish();
            }
        }

        private void processStartMessage(Message msg) {
            boolean result = (boolean) msg.obj;

            if (result) {
                UserData.DATA.addData(ServicesFields.CURRENT_ROOM, currentRoom);
                startedGame = true;
                startTimer = new Timer();
                startTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        isStartingGame = true;
                        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                        startActivity(intent);
                    }
                }, startDelay);
            }
        }

    }

}
