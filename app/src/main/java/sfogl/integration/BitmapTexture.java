package sfogl.integration;

import android.graphics.Bitmap;

import sfogl2.SFOGLTexture2D;
import shadow.graphics.SFBitmap;
import shadow.graphics.SFImageFormat;
import shadow.system.SFInitiable;

public class BitmapTexture implements SFInitiable {

    public static final String LOG_TAG = "BitmapTexture";

    private SFOGLTexture2D texture;
    private SFBitmap bitmap;
    private int textureModel;

    public BitmapTexture(SFBitmap bitmap, int textureModel) {
        super();
        this.bitmap = bitmap;
        this.textureModel = textureModel;
    }

    public static BitmapTexture loadBitmapTexture(Bitmap image, int textureModel) {

        int width = image.getWidth();
        int height = image.getHeight();

        int[] values = new int[width * height * 3];
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = image.getPixel(i, height - j - 1);
                values[index] = (color & 0xff0000) >> 16;
                index++;
                values[index] = (color & 0xff00) >> 8;
                index++;
                values[index] = (color & 0xff);
                index++;
            }
        }

        SFBitmap bitmap = new SFBitmap();
        bitmap.generateBitmap(width, height, values, SFImageFormat.RGB);
        return new BitmapTexture(bitmap, textureModel);
    }

    public static BitmapTexture loadBitmapTextureWithAlpha(Bitmap image, int textureModel) {

        int width = image.getWidth();
        int height = image.getHeight();

        int[] values = new int[width * height * 4];
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = image.getPixel(i, height - j - 1);
                values[index] = (color & 0xff000000) >> 24;
                index++;
                values[index] = (color & 0xff0000) >> 16;
                index++;
                values[index] = (color & 0xff00) >> 8;
                index++;
                values[index] = (color & 0xff);
                index++;
            }
        }

        SFBitmap bitmap = new SFBitmap();
        bitmap.generateBitmap(width, height, values, SFImageFormat.RGBA);
        return new BitmapTexture(bitmap, textureModel);
    }

    public SFOGLTexture2D getTexture() {
        return texture;
    }

    public void setTexture(SFOGLTexture2D texture) {
        this.texture = texture;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init() {
        texture = new SFOGLTexture2D(textureModel);
        texture.setup(bitmap);

        bitmap = null;
    }
}
