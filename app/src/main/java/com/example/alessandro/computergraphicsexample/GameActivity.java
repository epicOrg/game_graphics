package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import game.miscellaneous.FloatLoader;
import game.data.Room;
import game.data.Team;
import game.data.UserData;
import game.miscellaneous.WaiterGroup;
import game.audio.AudioCallManager;
import game.audio.HeadsetListener;
import game.net.interpreters.MapInterpreter;
import game.net.interpreters.PositionsInterpreter;
import game.net.interpreters.StatusInterpreter;
import game.musics.BackgroundSound;
import game.musics.GameSoundtracks;
import game.net.handling.GameHandler;
import game.net.handling.GameHandlerListener;
import game.net.handling.GamePositionSender;
import game.net.handling.GameStatusWaiter;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.fieldsnames.AudioFields;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.GameFields;
import game.net.fieldsnames.RoomFields;
import game.net.fieldsnames.ServicesFields;
import game.physics.Circle;
import game.player.Player;
import game.player.PlayerStatus;
import game.views.MessageScreen;
import game.views.SettingsScreen;
import game.views.SplashScreen;
import shadow.math.SFVertex3f;

public class GameActivity extends Activity implements GameHandlerListener {

    public static final String LOG_TAG = "GameActivity";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();
    private GameHandler gameHandler;

    private CountDownLatch startSignal = new CountDownLatch(1);
    private GraphicsView graphicsView;
    private LinearLayout messageContainer;
    private LinearLayout menuContainer;

    private HeadsetListener headsetListener;
    private BackgroundSound backgroundSound;

    private MessageScreen messageScreen;
    private SettingsScreen settingsScreen;

    private Player me;
    private ArrayList<Player> otherPlayers = new ArrayList<>();

    private Context context;
    private RequestMaker requestMaker;
    private MapInterpreter mapInterpreter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);
        context = this;
        Resources res=context.getResources();
        FloatLoader fl=new FloatLoader(context);

        headsetListener = new HeadsetListener(context);
        headsetListener.init();

        requestMaker = UserData.DATA.getRequestMakerWithData(CommonFields.USERNAME, CommonFields.HASHCODE, RoomFields.ROOM_NAME);

        messageContainer = (LinearLayout) findViewById(R.id.game_message_container);
        menuContainer = (LinearLayout) findViewById(R.id.game_menu_container);
        messageScreen = new MessageScreen(this, Color.argb(res.getInteger(R.integer.mScreenColA), res.getInteger(R.integer.mScreenColR),
                res.getInteger(R.integer.mScreenColG), res.getInteger(R.integer.mScreenColB)), messageContainer);
        settingsScreen = new SettingsScreen(this, menuContainer, requestMaker);

        backgroundSound = new BackgroundSound(context, new GameSoundtracks(R.raw.soundtrack_01, R.raw.soundtrack_02).getSoundtracks(context));
        backgroundSound.start();

        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        me = new Player(new PlayerStatus(direction, new Circle(position, fl.getFloat(R.dimen.playerRadius))), "Me");
        Room room = (Room) UserData.DATA.getData(ServicesFields.CURRENT_ROOM);
        ArrayList<Team> teams = room.getTeams();
        if (teams != null)
            for (Team team : teams)
                otherPlayers.addAll(team.getPlayers());

        Iterator<Player> iterator = otherPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getName().equals(UserData.DATA.getData(CommonFields.USERNAME))) {
                me = p;
                iterator.remove();
            }
        }

        WaiterGroup waiterGroup = new WaiterGroup(startSignal);

        Log.d(LOG_TAG, "Starting SplashScreen..");
        SplashScreen splashScreen = new SplashScreen(this, R.id.game_splash_container, R.id.game_splash_image, R.id.game_splash_text);
        splashScreen.animate();
        waiterGroup.addWaiter(splashScreen);
        messageScreen.setText(res.getString(R.string.messageWait), getResources().getColor(R.color.primary_text));
        waiterGroup.addWaiter(messageScreen);

        Log.d(LOG_TAG, "Starting GamePositionSender..");
        GamePositionSender gamePositionSender = new GamePositionSender(me, room.getName());

        mapInterpreter = new MapInterpreter(me.getStatus(), this);
        gameHandler = new GameHandler(new StatusInterpreter(context, messageScreen, gamePositionSender, this), mapInterpreter,
                new PositionsInterpreter((Room) UserData.DATA.getData(ServicesFields.CURRENT_ROOM)));

        waiterGroup.addWaiter(gamePositionSender);
        waiterGroup.addWaiter(new GameStatusWaiter(requestMaker));

        waiterGroup.startWaiting();

        serverCommunicationThread.setHandler(gameHandler);
        Log.d(LOG_TAG, "Starting Audio..");
        initAudioSetting();
        Log.d(LOG_TAG, "Asking Map..");
        try {
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(ServicesFields.SERVICE, ServicesFields.GAME.toString()),
                    new JSONd(ServicesFields.SERVICE_TYPE, GameFields.GAME_MAP.toString())));
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void initAudioSetting() {
        AudioCallManager audioCallManager = AudioCallManager.getInstance();
        audioCallManager.setContext(context);
        audioCallManager.initAudioGroup();
        try {
            int audioPort = audioCallManager.newAudioStream();
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(ServicesFields.SERVICE, ServicesFields.AUDIO.toString()),
                    new JSONd(AudioFields.AUDIO_PORT_CLIENT, audioPort)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReceived() {
        Log.d(LOG_TAG, "onMapReceived");

        Log.d(LOG_TAG, "Starting GraphicsView..");
        int width = mapInterpreter.getGroundWidth();
        int height = mapInterpreter.getGroundHeight();

        graphicsView = new GraphicsView(context, me, ((Room) UserData.DATA.getData(ServicesFields.CURRENT_ROOM)).getTeams(),
                mapInterpreter.getMap(), startSignal, width, height, settingsScreen);

        LinearLayout graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);
        graphicsContainerLayout.addView(graphicsView);
    }

    @Override
    public void onGameGo() {
        Log.d(LOG_TAG, "onGameGo");
        messageScreen.hide();
        backgroundSound.stop();
        graphicsView.onGameGo();
    }

    @Override
    public void onGameFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserData.DATA.removeData(ServicesFields.CURRENT_ROOM);
                UserData.DATA.removeData(ServicesFields.SERVICE);
                UserData.DATA.removeData(RoomFields.ROOM_NAME);
                finish();
                AudioCallManager.getInstance().releaseResources();
                headsetListener.release();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (graphicsView != null)
            graphicsView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (graphicsView != null)
            graphicsView.onPause();
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

}
