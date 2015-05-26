package game.audio;

import android.content.Context;
import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.util.Log;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import game.net.communication.ServerCommunicationThread;

/**
 * @author Micieli
 * @date 30/03/2015
 */
public class AudioCallManager {

    public static final String LOG_TAG = "AudioCallManager";

    private static AudioCallManager instance = new AudioCallManager();
    private AudioGroup audioGroup;
    private AudioStream audioStream;
    private Context context;

    private InetAddress serverIp;
    private int serverPort;

    private AudioCallManager() {
        initAudioGroup();
    }

    public static AudioCallManager getInstance() {
        return instance;
    }

    public void initAudioGroup() {
        audioGroup = new AudioGroup();
        audioGroup.setMode(AudioGroup.MODE_NORMAL);
    }

    public int newAudioStream() throws UnknownHostException, SocketException {
        InetAddress localIpAddress = InetAddress.getByName(ServerCommunicationThread.getLocalIpAddress());
        audioStream = new AudioStream(localIpAddress);
        audioStream.setCodec(AudioCodec.PCMU);
        audioStream.setMode(RtpStream.MODE_NORMAL);
        int localPort = audioStream.getLocalPort();
        Log.d(LOG_TAG, "NewStream:");
        Log.d(LOG_TAG, "localIp: " + localIpAddress);
        Log.d(LOG_TAG, "localPort: " + localPort);
        return localPort;
    }

    public void associateStream() {
        Log.d(LOG_TAG, "Associate:");
        Log.d(LOG_TAG, "ServerIp: " + serverIp);
        Log.d(LOG_TAG, "ServerPort: " + serverPort);
        audioStream.associate(serverIp, serverPort);
        audioStream.join(audioGroup);
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audio.isWiredHeadsetOn())
            audio.setSpeakerphoneOn(false);
    }

    public void releaseResources() {
        audioStream.join(null);
        audioStream.release();
        audioStream = null;
        AudioManager Audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Audio.setMode(AudioManager.MODE_NORMAL);
        Audio.setSpeakerphoneOn(false);
        Audio.setMicrophoneMute(false);
    }

    public void muteUnMute() {
        AudioManager Audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Audio.isMicrophoneMute())
            Audio.setMicrophoneMute(false);
        else
            Audio.setMicrophoneMute(true);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setServerIp(InetAddress serverIp) {
        this.serverIp = serverIp;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }


}
