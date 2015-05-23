package game.net;

import org.json.JSONObject;
import game.JSONd;
import game.RequestMaker;
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
    public static final long waitTime=100;

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
                 String[] names=new String[]{FieldsNames.GAME_X, FieldsNames.GAME_Y, FieldsNames.GAME_Z};
                 JSONObject request = requestMaker.getNewRequest(new JSONd(FieldsNames.SERVICE, FieldsNames.GAME),
                        new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_POSITIONS),
                        new JSONd(FieldsNames.USERNAME, player.getName()),
                        new JSONd(FieldsNames.ROOM_NAME, roomName),
                        new JSONd(FieldsNames.GAME_POSITION, requestMaker.getNewRequest(names, playerStatus.getPosition())),
                        new JSONd(FieldsNames.GAME_DIRECTION, requestMaker.getNewRequest(names, playerStatus.getDirection()))

                );
                try {
                    serverCommunicationThread.send(request);
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(waitTime);
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
