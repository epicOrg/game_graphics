package game.net.communication;

import java.util.HashMap;

import game.net.communication.ServerCommunicationThreadState;

import static game.net.communication.ServerCommunicationThreadState.*;


/**
 * Class that maps the {@link ServerCommunicationThreadState} to corrisponding state values.
 */
public class ConnectionStateMap {

    private HashMap<ServerCommunicationThreadState, ConnectionStateValues> map;

    public ConnectionStateMap() {
        map = new HashMap<>();
        map.put(NOT_CONNECTED, new ConnectionStateValues(false, false));
        map.put(CONNECTING, new ConnectionStateValues(false, false));
        map.put(ENCRYPTING, new ConnectionStateValues(false, false));
        map.put(CONNECTED, new ConnectionStateValues(true, true));
    }

    public boolean getConnectionStateLogin(ServerCommunicationThreadState state) {
        return map.get(state).loginEnabled;
    }

    public boolean getConnectionStateRegistered(ServerCommunicationThreadState state) {
        return map.get(state).notRegistered;
    }

    private class ConnectionStateValues {

        private boolean loginEnabled, notRegistered;

        public ConnectionStateValues(boolean loginEnabled, boolean notRegistered) {
            this.loginEnabled = loginEnabled;
            this.notRegistered = notRegistered;
        }
    }

}
