package game.net;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import game.Room;
import game.player.Player;
import game.player.PlayerStatus;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;

/**
 * Created by Andrea on 29/04/2015.
 */
public class GamePositionSender {

    public static final String LOG_TAG = "GamePositionSender";

    private Player player;
    private String roomName;
    private CountDownLatch startSignal;

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private boolean sending = true;

    public GamePositionSender(Player player, String roomName, CountDownLatch startSignal) {
        this.player = player;
        this.roomName = roomName;
        this.startSignal = startSignal;

        new Thread(sendPositionRunnable).start();
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }

    private Runnable sendPositionRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                startSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (sending) {
                JSONObject request = new JSONObject();
                JSONObject posObject = null;
                JSONObject dirObject = null;
                try {
                    request.put(FieldsNames.SERVICE, FieldsNames.GAME);
                    request.put(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_POSITIONS);
                    request.put(FieldsNames.USERNAME, player.getName());
                    request.put(FieldsNames.ROOM_NAME, roomName);

                    posObject = new JSONObject();
                    dirObject = new JSONObject();

                    PlayerStatus playerStatus = player.getStatus();
                    posObject.put(FieldsNames.GAME_X, playerStatus.getPosition().getX());
                    posObject.put(FieldsNames.GAME_Y, playerStatus.getPosition().getY());
                    posObject.put(FieldsNames.GAME_Z, playerStatus.getPosition().getZ());
                    dirObject.put(FieldsNames.GAME_X, playerStatus.getDirection().getX());
                    dirObject.put(FieldsNames.GAME_Y, playerStatus.getDirection().getY());
                    dirObject.put(FieldsNames.GAME_Z, playerStatus.getDirection().getZ());

                    request.put(FieldsNames.GAME_POSITION, posObject);
                    request.put(FieldsNames.GAME_DIRECTION, dirObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    serverCommunicationThread.send(request);
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
