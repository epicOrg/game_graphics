package game.musics;

import android.content.Context;
import android.net.Uri;

/**
 * @author Torlaschi
 * @date 17/04/2015
 */
public class GameSoundtracks {

    public static final String LOG_TAG = "GameSoundtracks";
    private int[] soundtrackIds;

    public GameSoundtracks(int... soundtrackIds){
        this.soundtrackIds=soundtrackIds;
    }

    public Uri[] getSoundtracks(Context context) {
        int n=soundtrackIds.length;
        Uri[] soundtracks=new Uri[n];
        for(int i=0; i<n;i++){
            soundtracks[i]=Uri.parse("android.resource://" + context.getPackageName() + "/" + soundtrackIds[i]);
        }
        return soundtracks;
    }

}
