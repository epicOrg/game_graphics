package game.net.communication;

import com.example.alessandro.computergraphicsexample.R;

/**
 * Enum which contains the possible states of the communication with the server.
 *
 * @author Torlaschi
 * @date 24/04/2015
 */
public enum ServerCommunicationThreadState {
    CONNECTING(R.string.main_status_connecting),
    CONNECTED(R.string.main_status_connected),
    ENCRYPTING(R.string.main_status_encrypting),
    NOT_CONNECTED(R.string.main_status_not_connected);

    public final int state;

    ServerCommunicationThreadState(int state) {
        this.state = state;
    }

}
