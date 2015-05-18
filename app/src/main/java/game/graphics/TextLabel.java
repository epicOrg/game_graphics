package game.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;

import game.generators.FundamentalGenerator;
import game.player.PlayerStatus;
import sfogl.integration.ArrayObject;
import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Rappresenta una Label di testo orientata dinamicamente secondo una direzione data.
 */
public class TextLabel {

    private Node node;
    private SFVertex3f direction,position;
    private float h;

    /**
     * Crea una nuova TextLabel.
     * @param width Larghezza della TextLabel.
     * @param height Altezza della TextLabel.
     * @param h Altezza in y a cui la TextLabel si posiziona.
     * @param direction La TextLabel si orienta in modo da essere visibile guardando verso questa direzione.
     * @param position Posizione della TextLabel (vengono considerate solo le componenti x,z la componente y è data da h).
     * @param text Testo che appare sulla TextLabel.
     * @param color Colore del testo.
     */
    public TextLabel(float width, float height, float h, SFVertex3f direction, SFVertex3f position, String text, int color){
//        node=generateNode(getBoard(width,height), getTextBitmap(text,color));
        node= FundamentalGenerator.generateNode(getBoard(width,height), getTextBitmap(text, color),ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER));
        this.direction=direction;
        this.position=position;
        this.h=h;
    }

    /**
     * Cambia la posizione della TextLabel.
     * @param position posizione della TextLabel.
     */
    public void setPosition(SFVertex3f position){
        this.position=position;
    }

    /**
     * Disegna la TextLabel.
     */
    public void draw() {
        node.getRelativeTransform().setPosition(new SFVertex3f(position.getX(), h, position.getZ()));
        float angle=-(float)Math.atan2(direction.getZ(),direction.getX())+(float)(1*Math.PI/2);
        node.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY(angle));
        node.updateTree(new SFTransform3f());
        node.draw();
    }

    private ArrayObject getBoard(float width, float height){
        return new ArrayObject(
                new float[]{
                        -width/2, -height/2, 0,
                        -width/2, +height/2, 0,
                        +width/2, -height/2, 0,
                        +width/2, +height/2, 0
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
                        3,2,0,0,1,3
                }
        );
    }

    private Bitmap getTextBitmap(String text, int color){
        int ww,hh,textSize;
        ww=hh=128;
        textSize=24;
        Bitmap bitmap=Bitmap.createBitmap(ww,hh,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setColor(color);
        paint.setAntiAlias(false);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, ww/2, (hh - paint.ascent()) / 2, paint);
        return bitmap;
    }

}
