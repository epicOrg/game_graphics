package game.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import game.generators.FundamentalGenerator;
import sfogl.integration.ArrayObject;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Rappresenta una Label di testo orientata dinamicamente secondo una direzione data.
 *
 * @author Stefano De Pace
 */
public class TextLabel {

    private Node node;
    private SFVertex3f direction, position;
    private float h;

    /**
     * Crea una nuova TextLabel.
     *
     * @param textSize  Dimensione del testo: influenza la qualità in pixel, non le dimensioni effettive.
     * @param height    Altezza della TextLabel: è un valore medio, la dimensione effettiva si adatta
     *                  rispetto ad un testo con lettere più o meno alte.
     * @param h         Altezza in y a cui la TextLabel si posiziona.
     * @param direction La TextLabel si orienta in modo da essere visibile guardando verso questa direzione.
     * @param position  Posizione della TextLabel (vengono considerate solo le componenti x,z la componente y è data da h).
     * @param text      Testo che appare sulla TextLabel.
     * @param color     Colore del testo.
     */
    public TextLabel(int textSize, float height, float h, SFVertex3f direction, SFVertex3f position, String text, int color) {
        this.direction = direction;
        this.position = position;
        this.h = h;
        setup(height, textSize, color, text);
    }

    /**
     * Cambia la posizione della TextLabel.
     *
     * @param position posizione della TextLabel.
     */
    public void setPosition(SFVertex3f position) {
        this.position = position;
    }

    /**
     * Disegna la TextLabel.
     */
    public void draw() {
        node.getRelativeTransform().setPosition(new SFVertex3f(position.getX(), h, position.getZ()));
        float angle = -(float) Math.atan2(direction.getZ(), direction.getX()) + (float) (1 * Math.PI / 2);
        node.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY(angle));
        node.updateTree(new SFTransform3f());
        node.draw();
    }

    private ArrayObject getBoard(float width, float height) {
        return new ArrayObject(
                new float[]{
                        -width / 2, -height / 2, 0,
                        -width / 2, +height / 2, 0,
                        +width / 2, -height / 2, 0,
                        +width / 2, +height / 2, 0
                },
                new float[]{
                        0, 0, -1
                },
                new float[]{
                        1, 0, 0,
                        1, 1, 0,
                        0, 0, 0,
                        0, 1, 0
                },
                new short[]{
                        3, 2, 0, 0, 1, 3
                }
        );
    }

    private Paint getPaint(int textSize, int color) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(false);
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    private void setup(float height, int textSize, int color, String text) {
        Paint paint = getPaint(textSize, color);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int ww = rect.width(), hh = rect.height();
        Bitmap bitmap = getBitmap(ww, hh, text, paint, rect);
        float hb = height * hh / 16;
        node = FundamentalGenerator.generateNode(getBoard(hb * (float) ww / hh, hb), bitmap, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER));
    }

    private Bitmap getBitmap(int ww, int hh, String text, Paint paint, Rect rect) {
        Bitmap bitmap = Bitmap.createBitmap(ww, hh, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, ww / 2.0f, hh / 2.0f - (rect.bottom + rect.top) / 2.0f, paint);
        return bitmap;
    }

}
