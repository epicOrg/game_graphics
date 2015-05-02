package login.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

import login.call.audio.AudioCallManager;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;

/**
 * Created by Luca on 02/05/2015.
 */
public class Audio implements Service {

    private JSONObject jsonRequest;

    public Audio(JSONObject jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    @Override
    public void start() {
        AudioCallManager audioCallManager = AudioCallManager.getInstance();
        try {
            int serverPort = jsonRequest.getInt(FieldsNames.AUDIO_PORT);
            InetAddress serverIp = InetAddress.getByName(ServerCommunicationThread.SERVER_ADDRESS);
            audioCallManager.associateStream(serverIp,serverPort);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setHandler(Handler handler) {

    }
}
