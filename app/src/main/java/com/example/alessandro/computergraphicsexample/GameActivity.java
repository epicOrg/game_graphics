package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import game.GameManager;
import game.Room;
import game.Team;
import game.musics.BackgroundSound;
import game.musics.GameSoundtracks;
import game.net.GameHandler;
import game.net.GameHandlerListener;
import game.physics.Circle;
import game.player.Player;
import game.player.PlayerStatus;
import game.views.SplashScreen;
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

        backgroundSound = new BackgroundSound(context, new GameSoundtracks(R.raw.soundtrack_01, R.raw.soundtrack_02).getSoundtracks(context));

        Intent intent = getIntent();
        username = intent.getStringExtra(FieldsNames.USERNAME);
        hashcode = intent.getIntExtra(FieldsNames.HASHCODE, 0);

        Log.d(LOG_TAG, "Starting SplashScreen..");
        SplashScreen splashScreen = new SplashScreen(this, R.id.game_splash_container, R.id.game_splash_image, R.id.game_splash_text, startSignal);
        splashScreen.animate();

        gameManager = GameManager.getInstance();
        gameManager.setRoom(new Room("TestRoom", 10, 2)); //DEBUG
        gameHandler = new GameHandler();
        gameHandler.addGameHandlerListeners(this);

        serverCommunicationThread.setHandler(gameHandler);
        Log.d(LOG_TAG, "Asking Map..");
        try {
            serverCommunicationThread.send(createMapRequest());
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReceived() {
        Log.d(LOG_TAG, "onMapReceived");
        gameManager.setMap(gameHandler.getMap());

        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        me = new Player(new PlayerStatus(direction, new Circle(position, 0.75)), "Me");
        for (Team team : gameManager.getRoom().getTeams())
            otherPlayers.addAll(team.getPlayers());

        Iterator<Player> iterator = otherPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getName().equals(username)) {
                me = p;
                iterator.remove();
            }
        }

        Log.d(LOG_TAG, "Starting GraphicsView..");
        LinearLayout graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);
        int width = gameHandler.getGroundWidth();
        int height = gameHandler.getGroundHeight();
        graphicsView = new GraphicsView(context, me, otherPlayers, gameManager.getMap(), startSignal, width, height);
        graphicsContainerLayout.addView(graphicsView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundSound.start();

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
