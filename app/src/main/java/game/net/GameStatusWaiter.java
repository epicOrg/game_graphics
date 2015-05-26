package game.net;

import game.net.communication.JSONd;
import game.net.communication.RequestMaker;
import game.Waiter;
import game.net.communication.NotConnectedException;
import game.net.communication.ServerCommunicationThread;
import game.net.interaction.FieldsNames;

/**
 * Class which sends a "GAME_READY" message when asked.
 *
 * @author Torlaschi
 */
public class GameStatusWaiter implements Waiter {

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private RequestMaker requestMaker;

    /**
     * Construct a new GameStatusWaiter.
     *
     * @param requestMaker RequestMaker to be used to create the message
     */
    public GameStatusWaiter(RequestMaker requestMaker) {
        this.requestMaker = requestMaker;
    }

    @Override
    public void unleash() {
        try {
            serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE, FieldsNames.GAME),
                    new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_STATUS),
                    new JSONd(FieldsNames.GAME_READY, true)));
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

    }
}
