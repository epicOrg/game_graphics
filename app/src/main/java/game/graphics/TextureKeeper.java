package game.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;

import sfogl.integration.BitmapTexture;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;

/**
 * Gestisce le texture caricate dalle risorse; permette di accedervi  senza doverle ricaricare più volte,
 * o di ricaricarle automaticamente tutte in caso di necessità.
 * @author Stefano De Pace
 */
public enum TextureKeeper{
    TEXTURE_KEEPER;

    public final String LOG_TAG = "TextureKeeper";

    private  HashMap<Integer, BitmapTexture> mapFromResources = new HashMap<>();
    private  HashMap<Integer, BitmapTexture> mapFromColors = new HashMap<>();

    TextureKeeper(){;}

    /**
     * Carica una nuova texture da un'immagine nelle risorse, o restituisce la BitmapTexture
     * se l'immagine è già stata caricata.
     * @param context Context per ottenere le risorse.
     * @param textureId indice dell'immagine nelle risorse.
     * @return BitmapTexture che rappresenta la texture caricata.
     */
    public BitmapTexture getTexture(Context context, int textureId) {
        if (mapFromResources.containsKey(textureId))
            return mapFromResources.get(textureId);
        else
            return loadTextureFromId(context, textureId, "Loaded Texture: " + textureId);
    }

    /**
     * Genera una texture da un colore e la memorizza; rappresenta un colore uniforme.
     * @param color colore da cui ottenere la texture, rappresentato con un intero a 4 bit: (R,G,B,A).
     * @return BitmapTexture che rappresenta la texture caricata.
     */
    public BitmapTexture getColorTexture(int color) {
        if (mapFromColors.containsKey(color))
            return mapFromColors.get(color);
        else
            return loadColorTexture(color, "Loaded ColorTexture: " + color);
    }

    /**
     * Ricarica tutte le immagini già caricate in precedenza, in caso di necessità.
     * @param context Context per ottenere le risorse.
     */
    public void reload(Context context) {
        for (int i : mapFromResources.keySet()) {
            loadTextureFromId(context, i, "Reloaded Texture: " + i);
        }
        for (int color : mapFromColors.keySet()) {
            loadColorTexture(color, "Reloaded ColorTexture: " + color);
        }
    }

    private BitmapTexture loadColorTexture(int color, String message){
        BitmapTexture tex = getBitmapTextureFromColor(color);
        if (message.length()>0)
            Log.d(LOG_TAG, message);
        mapFromColors.put(color, tex);
        return tex;
    }

    private BitmapTexture loadTextureFromId(Context context, int textureId, String message){
        BitmapTexture tex = getBitmapTextureFromResource(context, textureId);
        if (message.length()>0)
            Log.d(LOG_TAG, message);
        mapFromResources.put(textureId, tex);
        return tex;
    }

    private BitmapTexture getBitmapTextureFromResource(Context context, int textureId) {
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture tex = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), textureId), textureModel);
        tex.init();
        return tex;
    }

    private BitmapTexture getBitmapTextureFromColor(int color) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(64, 64, conf);
        bitmap.eraseColor(color);

        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture tex = BitmapTexture.loadBitmapTexture(bitmap, textureModel);
        tex.init();
        return tex;
    }

    public void clear() {
        mapFromResources.clear();
        mapFromColors.clear();
    }
}