package game.net;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import game.JSONd;
import game.RequestMaker;
import game.Room;
import game.Waiter;
import game.player.Player;
import game.player.PlayerStatus;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;

/**
 * Created by Andrea on 29/04/2015.
 */
public class GamePositionSender implements Waiter {

    public static final String LOG_TAG = "GamePositionSender";

    private Player player;
    private String roomName;

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private boolean sending = true;
    private RequestMaker requestMaker=new RequestMaker();

    public GamePositionSender(Player player, String roomName) {
        this.player = player;
        this.roomName = roomName;
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }

    private Runnable sendPositionRunnable = new Runnable() {
        @Override
        public void run() {
             while (sending) {
                 PlayerStatus playerStatus = player.getStatus();
                JSONObject request = requestMaker.getNewRequest(new JSONd(FieldsNames.SERVICE, FieldsNames.GAME),
                        new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_POSITIONS),
                        new JSONd(FieldsNames.USERNAME, player.getName()),
                        new JSONd(FieldsNames.ROOM_NAME, roomName),

                        new JSONd(FieldsNames.GAME_POSITION, requestMaker.getNewRequest(
                                new JSONd(FieldsNames.GAME_X, playerStatus.getPosition().getX()),
                                new JSONd(FieldsNames.GAME_Y, playerStatus.getPosition().getY()),
                                new JSONd(FieldsNames.GAME_Z, playerStatus.getPosition().getZ()))),
                        new JSONd(FieldsNames.GAME_DIRECTION, requestMaker.getNewRequest(
                                new JSONd(FieldsNames.GAME_X, playerStatus.getDirection().getX()),
                                new JSONd(FieldsNames.GAME_Y, playerStatus.getDirection().getY()),
                                new JSONd(FieldsNames.GAME_Z, playerStatus.getDirection().getZ())))
                );

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

    @Override
    public void unleash() {
        new Thread(sendPositionRunnable).start();
    }
}
