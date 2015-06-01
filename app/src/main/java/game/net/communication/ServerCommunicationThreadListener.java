package game.net.communication;

/**
 * Interface for listening to connection state changes.
 * @author Torlaschi
 * @date 18/04/2015
 */
public interface ServerCommunicationThreadListener {

    /**
     * Called on chaning of connection state.
     * @param state actual state of the connection.
     */
    public void onThreadStateChanged(ServerCommunicationThreadState state);

}
