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
 * It represents a text label dynamically oriented on the basis of a specified direction.
 *
 * @author De Pace
 */
public class TextLabel {

    private Node node;
    private SFVertex3f direction, position;
    private float h;

    /**
     * Creates a new textLabel
     *
     * @param textSize  Text quality (dimension in pixels).
     * @param height    <code>TextLabel</code> height.
     * @param h         Vertical <code>TextLabel</code> position.
     * @param direction <code>TextLabel</code> is oriented in order to be visible looking int this direction.
     * @param position  <code>TextLabel</code> position.
     * @param text      <code>TextLabel</code> text.
     * @param color     Text color.
     */
    public TextLabel(int textSize, float height, float h, SFVertex3f direction, SFVertex3f position, String text, int color) {
        this.direction = direction;
        this.position = position;
        this.h = h;
        setup(height, textSize, color, text);
    }

    /**
     * Changes the <code>TextLabel</code> position.
     */
    public void setPosition(SFVertex3f position) {
        this.position = position;
    }

    /**
     * Draws the <code>TextLabel</code>.
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
