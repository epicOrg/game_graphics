package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import game.GameManager;
import game.Room;
import game.Team;
import game.WaiterGroup;
import game.graphics.Map;
import game.musics.BackgroundSound;
import game.musics.GameSoundtracks;
import game.net.GameHandler;
import game.net.GameHandlerListener;
import game.net.GamePositionSender;
import game.net.GameStatusWaiter;
import game.physics.Circle;
import game.player.Player;
import game.player.PlayerStatus;
import game.views.MessageScreen;
import game.views.SettingsScreen;
import game.views.SplashScreen;
import login.audio.AudioCallManager;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;
import shadow.math.SFVertex3f;

public class GameActivity extends Activity implements GameHandlerListener {

    public static final String LOG_TAG = "GameActivity";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();
    private BackgroundSound backgroundSound;
    private GameManager gameManager;
    private GameHandler gameHandler;

    private CountDownLatch startSignal = new CountDownLatch(1);
    private GraphicsView graphicsView;
    private LinearLayout messageContainer;
    private LinearLayout menuContainer;

    private MessageScreen messageScreen;
    private SettingsScreen settingsScreen;

    private String username;
    private int hashcode;

    private Player me;
    private ArrayList<Player> otherPlayers = new ArrayList<>();

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);
        context = this;

        messageContainer = (LinearLayout) findViewById(R.id.game_message_container);
        menuContainer = (LinearLayout) findViewById(R.id.game_menu_container);

        messageScreen = new MessageScreen(this, Color.argb(128, 255, 0, 0), messageContainer);
        settingsScreen = new SettingsScreen(this, menuContainer);

        Intent intent = getIntent();
        boolean noServer = intent.getBooleanExtra("NO_SERVER", false);
        username = intent.getStringExtra(FieldsNames.USERNAME);
        hashcode = intent.getIntExtra(FieldsNames.HASHCODE, 0);

        gameManager = GameManager.getInstance();
        if (noServer) {
            gameManager.setRoom(new Room("TestRoom", 10, 2));
        }
        gameHandler = new GameHandler(messageScreen);
        gameHandler.addGameHandlerListeners(this);

        backgroundSound = new BackgroundSound(context, new GameSoundtracks(R.raw.soundtrack_01, R.raw.soundtrack_02).getSoundtracks(context));

        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        me = new Player(new PlayerStatus(direction, new Circle(position, 0.75)), "Me");
        Room room = gameManager.getRoom();
        ArrayList<Team> teams = room.getTeams();
        if (teams != null)
            for (Team team : teams)
                otherPlayers.addAll(team.getPlayers());

        Iterator<Player> iterator = otherPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getName().equals(username)) {
                me = p;
                iterator.remove();
            }
        }

        WaiterGroup waiterGroup = new WaiterGroup(startSignal);

        Log.d(LOG_TAG, "Starting SplashScreen..");
        SplashScreen splashScreen = new SplashScreen(this, R.id.game_splash_container, R.id.game_splash_image, R.id.game_splash_text);
        splashScreen.animate();
        waiterGroup.addWaiter(splashScreen);
        messageScreen.setText("Waiting for other players..", Color.BLUE);
        waiterGroup.addWaiter(messageScreen);

        if (!noServer) {
            Log.d(LOG_TAG, "Starting GamePositionSender..");
            GamePositionSender gamePositionSender = new GamePositionSender(me, room.getName());
            waiterGroup.addWaiter(gamePositionSender);

            waiterGroup.addWaiter(new GameStatusWaiter(room.getName(), username, hashcode));
        }

        waiterGroup.startWaiting();

        if (noServer) {
            Map map = new Map();
            DebugUtils.fillMap(map, 20);
            DebugUtils.startGame(this, otherPlayers, map, 20, 20, startSignal);
        } else {
            serverCommunicationThread.setHandler(gameHandler);
            Log.d(LOG_TAG, "Asking Map..");
            initAudioSetting();
            Log.d(LOG_TAG, "Init Audio...");
            try {
                serverCommunicationThread.send(createMapRequest());
            } catch (NotConnectedException e) {
                Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }

    private void initAudioSetting() {
        AudioCallManager audioCallManager = AudioCallManager.getInstance();
        audioCallManager.setContext(context);
        audioCallManager.initAudioGroup();
        try {
            int audioPort = audioCallManager.newAudioStream();
            serverCommunicationThread.send(getCallRequest(audioPort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private JSONObject getCallRequest(int audioport) {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.AUDIO);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
            request.put(FieldsNames.AUDIO_PORT_CLIENT, audioport);
            request.put(FieldsNames.ROOM_NAME, gameManager.getRoom().getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    public void onMapReceived() {
        Log.d(LOG_TAG, "onMapReceived");
        gameManager.setMap(gameHandler.getMap());

        Log.d(LOG_TAG, "Starting GraphicsView..");
        int width = gameHandler.getGroundWidth();
        int height = gameHandler.getGroundHeight();
        graphicsView = new GraphicsView(context, me, gameManager.getRoom().getTeams(), gameManager.getMap(), startSignal, width, height, messageScreen, settingsScreen);
        LinearLayout graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);
        graphicsContainerLayout.addView(graphicsView);
    }

    @Override
    public void onGameGo() {
        Log.d(LOG_TAG, "onGameGo");
        messageScreen.hide();

        graphicsView.onGameGo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //backgroundSound.start();

        if (graphicsView != null)
            graphicsView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundSound.stop();

        if (graphicsView != null)
            graphicsView.onPause();
    }

    private JSONObject createMapRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.GAME);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_MAP);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

}
