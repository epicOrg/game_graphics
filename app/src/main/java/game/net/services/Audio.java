package game.net.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

import game.net.audio.AudioCallManager;
import game.net.communication.ServerCommunicationThread;
import game.net.interaction.FieldsNames;

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
            int serverPort = jsonRequest.getInt(FieldsNames.AUDIO_PORT_SERVER);
            InetAddress serverIp = InetAddress.getByName(ServerCommunicationThread.getServerAddres());
            audioCallManager.setServerPort(serverPort);
            audioCallManager.setServerIp(serverIp);
            audioCallManager.associateStream();
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
