package login.audio;

import android.content.Context;
import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import login.communication.ServerCommunicationThread;

/**
 * Created by Luca on 30/03/2015.
 */
public class AudioCallManager {

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
        audioStream = new AudioStream(InetAddress.getByName(
                ServerCommunicationThread.getLocalIpAddress()));
        audioStream.setCodec(AudioCodec.PCMU);
        audioStream.setMode(RtpStream.MODE_NORMAL);
        return audioStream.getLocalPort();
    }

    public void associateStream(){
        audioStream.associate(serverIp, serverPort);
        audioStream.join(audioGroup);
        AudioManager Audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if(Audio.isWiredHeadsetOn())
            Audio.setSpeakerphoneOn(false);
    }

    public void releaseResources(){
        audioStream.join(null);
        audioStream.release();
        audioStream = null;
        AudioManager Audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Audio.setMode(AudioManager.MODE_NORMAL);
        Audio.setSpeakerphoneOn(false);
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
