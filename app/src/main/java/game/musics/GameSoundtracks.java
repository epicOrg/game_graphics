package game.musics;

import android.content.Context;
import android.net.Uri;

/**
 * This class manages the soundtrack files.
 *
 * @author Torlaschi
 * @date 17/04/2015
 * @see Uri
 */
public class GameSoundtracks {

    public static final String LOG_TAG = "GameSoundtracks";
    private int[] soundtrackIds;

    public GameSoundtracks(int... soundtrackIds){
        this.soundtrackIds=soundtrackIds;
    }

    /**
     * @param context <code>Context</code> from which to load the soundtracks.
     * @return Vector which contains the Uri of every soundtrack files.
     */
    public Uri[] getSoundtracks(Context context) {
        int n=soundtrackIds.length;
        Uri[] soundtracks=new Uri[n];
        for(int i=0; i<n;i++){
            soundtracks[i]=Uri.parse("android.resource://" + context.getPackageName() + "/" + soundtrackIds[i]);
        }
        return soundtracks;
    }

}
