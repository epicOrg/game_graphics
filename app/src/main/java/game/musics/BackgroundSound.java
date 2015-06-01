package game.musics;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * This class manages the background sound the player will hear
 * during the loading.
 *
 * @author Torlaschi
 * @date 14/04/2015
 */
public class BackgroundSound {

    public static final String LOG_TAG = "BackgroundSound";

    private Context context;
    private Uri[] soundtracks;

    private MediaPlayer mediaPlayer;
    private int position;

    /**
     * Create a new Soundtracks player
     *
     * @param context     Context
     * @param soundtracks URIs of the soundtracks
     */
    public BackgroundSound(Context context, Uri[] soundtracks) {
        this.context = context;
        this.soundtracks = soundtracks;

        setup();
    }

    private void setup() {
        position = 0;
        mediaPlayer = MediaPlayer.create(context, soundtracks[position]);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(LOG_TAG, "Soundtrack finished.");

                position = (position + 1) % soundtracks.length;
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(context, soundtracks[position]);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
    }

    /**
     * Plays the music.
     */
    public void start() {
        mediaPlayer.start();
        Log.d(LOG_TAG, "Soundtrack started.");
    }

    /**
     * Pauses the music.
     */
    public void pause() {
        mediaPlayer.pause();
        Log.d(LOG_TAG, "Soundtrack paused.");
    }

    /**
     * Stops the music.
     */
    public void stop() {
        mediaPlayer.stop();
        Log.d(LOG_TAG, "Soundtrack stopped.");
    }

    /**
     * Set music volume to 0.
     */
    public void mute() {
        mediaPlayer.setVolume(0, 0);
    }

    /**
     * Unmutes music.
     */
    public void unMute() {
        mediaPlayer.setVolume(1, 1);
    }

    /**
     * Releases the {@link MediaPlayer} used for playing music.
     */
    public void release() {
        mediaPlayer.release();
    }

}