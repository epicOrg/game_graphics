package game.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import game.graphics.MaterialKeeper;
import game.graphics.ShadersKeeper;
import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;

/**
 * Created by Andrea on 27/03/2015.
 */
public class FundamentalGenerator {

    public static Model getModel(Context context, ShadingProgram program, int textureId, String obj) {
        ArrayObject[] object = ObjLoader.arrayObjectFromFile(context, obj);
        Mesh mesh = new Mesh(object[0]);
        mesh.init();
        Material material = MaterialKeeper.getInstance().getMaterial(context, program, textureId);
        Model model = new Model();
        model.setRootGeometry(mesh);
        model.setMaterialComponent(material);

        return model;
    }

    public static Model getColorModel(Context context, ShadingProgram program, int color, String obj) {
        ArrayObject[] object = ObjLoader.arrayObjectFromFile(context, obj);
        Mesh mesh = new Mesh(object[0]);
        mesh.init();
        Material material = MaterialKeeper.getInstance().getColorMaterial(context, program, color);
        Model model = new Model();
        model.setRootGeometry(mesh);
        model.setMaterialComponent(material);

        return model;
    }

    public static Node generateNode(ArrayObject arrayObject, Bitmap bitmap, ShadingProgram program) {
        Mesh meshPos = new Mesh(arrayObject);
        meshPos.init();
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);

        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGBA, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture bitmapTexture = BitmapTexture.loadBitmapTextureWithAlpha(bitmap, textureModel);
        bitmapTexture.init();

        Material material = new Material(program);
        material.getTextures().add(bitmapTexture);

        modelPos.setMaterialComponent(material);
        Node nodePos = new Node();
        nodePos.setModel(modelPos);

        return nodePos;
    }
}
