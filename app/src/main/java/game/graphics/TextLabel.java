package game.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.util.Log;

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
 * Created by depa on 05/05/15.
 */
public class TextLabel {

    private ArrayObject board;
    private Node node;

    private ArrayObject setBoard(float width, float height){
        this.board=new ArrayObject(
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
        return board;
    }

    public TextLabel(float width, float height, SFVertex3f position, SFVertex3f position0, SFVertex3f direction, String text){
        setBoard(width, height);
        node=generateNode(board, getTextBitmap(text,Color.BLUE));
//        this.position=position;
        this.direction=direction;
        this.position0=position0;
        Log.d("ControlDIrection", "" + (float) Math.atan2(direction.getZ(), direction.getX()));
    }

    private Node generateNode(ArrayObject arrayObject, Bitmap bitmap) {
        Mesh meshPos = new Mesh(arrayObject);
        meshPos.init();
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);

        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGBA, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture bitmapTexture = BitmapTexture.loadBitmapTextureWithAlpha(bitmap, textureModel);
        bitmapTexture.init();
        Material material = new Material(ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER));
        material.getTextures().add(bitmapTexture);

        modelPos.setMaterialComponent(material);
        Node nodePos = new Node();
        nodePos.setModel(modelPos);

        return nodePos;
    }

    public Bitmap getTextBitmap(String text, int color){
        Bitmap bitmap=Bitmap.createBitmap(128,128,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setColor(color);
        paint.setAntiAlias(false);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, 64, 64, paint);
        return bitmap;
    }

    private SFVertex3f position, direction,position0;

    public void draw() {
        node.getRelativeTransform().setPosition(position0.getX(), position0.getY(), position0.getZ());
        float angle=-(float)Math.atan2(direction.getZ(),direction.getX())+(float)(1*Math.PI/2);
//        SFVertex3f vector=new SFVertex3f(position0);
//        vector.subtract(position);
//
//        float angle=-(float)Math.atan2(vector.getZ(),vector.getX())+(float)(3*Math.PI/2)+3*(float)(Math.PI/2);
        node.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY(angle));
        node.updateTree(new SFTransform3f());
        node.draw();
    }

}
