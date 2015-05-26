package game.net;

import org.json.JSONObject;

import game.net.communication.JSONd;
import game.net.communication.RequestMaker;
import game.Waiter;
import game.player.Player;
import game.player.PlayerStatus;
import game.net.communication.NotConnectedException;
import game.net.communication.ServerCommunicationThread;
import game.net.interaction.FieldsNames;

/**
 * Class containing a thread which sends the current player's position and direction to the server.
 *
 * @author Torlaschi
 */
public class GamePositionSender implements Waiter {

    public static final String LOG_TAG = "GamePositionSender";

    public static final long waitTime = 100;

    private Player player;
    private String roomName;

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private boolean sending = true;
    private RequestMaker requestMaker = new RequestMaker();

    /**
     * Constructs a sender for the specified player.
     *
     * @param player   player containing the position and direction to send
     * @param roomName room containing the specified player
     */
    public GamePositionSender(Player player, String roomName) {
        this.player = player;
        this.roomName = roomName;
    }

    /**
     * Set the status of the thread.
     * <p>
     * When stopped it can't be resumed.
     *
     * @param sending status of the thread
     */
    public void setSending(boolean sending) {
        this.sending = sending;
    }

    private Runnable sendPositionRunnable = new Runnable() {
        @Override
        public void run() {
            while (sending) {
                PlayerStatus playerStatus = player.getStatus();
                String[] names = new String[]{FieldsNames.GAME_X, FieldsNames.GAME_Y, FieldsNames.GAME_Z};
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
