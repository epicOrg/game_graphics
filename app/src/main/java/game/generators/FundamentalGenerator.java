package game.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import game.graphics.MaterialKeeper;
import game.graphics.ModelKeeper;
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
 * Utility class with static methods which are used to generate Model and Node.
 *
 * @author Torlaschi
 */
public class FundamentalGenerator {

    /**
     * Builds a Model from a file with a texture.
     *
     * @param context   Context from which to load the file.
     * @param program   Shader to use in the Material of the Model.
     * @param textureId Id of the texture to use for the Material of the Model.
     * @param obj       Name of the file that contains the Model geometry.
     * @return The generated Model.
     */
    public static Model getModel(Context context, ShadingProgram program, int textureId, String obj) {
        return getModelFromFileAndMaterial(context, MaterialKeeper.MATERIAL_KEEPER.getMaterial(context, program, textureId), obj);
    }

    /**
     * Builds a Model from a file with a specified Material.
     *
     * @param context  Context from which resources can be loaded.
     * @param material Material with which the Model is built.
     * @param obj      Name of the file which contains Model's geometry.
     */
    public static Model getModelFromFileAndMaterial(Context context, Material material, String obj) {
        return ModelKeeper.MODEL_KEEPER.getModel(context, obj, material);
    }

    /**
     * Builds a Node from an ArrayObject and a Bitmap as texture.
     *
     * @param arrayObject ArrayObject from which Node Model's geometry is get.
     * @param bitmap      Bitmap to be used as texture for Node Material.
     * @param program     ShadingProgram with who Node Model's Material is built.
     */
    public static Node generateNode(ArrayObject arrayObject, Bitmap bitmap, ShadingProgram program) {
        Node nodePos = new Node();
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGBA, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture bitmapTexture = BitmapTexture.loadBitmapTexture(bitmap, textureModel);
        bitmapTexture.init();

        Material material = new Material(program);
        material.getTextures().add(bitmapTexture);
        nodePos.setModel(ModelKeeper.MODEL_KEEPER.getModelFromArrayObjectAndMaterial(arrayObject, material));

        return nodePos;
    }

}
