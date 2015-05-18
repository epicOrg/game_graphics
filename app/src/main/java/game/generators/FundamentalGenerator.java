package game.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import game.graphics.MaterialKeeper;
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
 * Classe utility con vari metodi statici usati spesso per generare Model e Node.
 */
public class FundamentalGenerator {

    /**
     * Costruisce un Model caricandolo da un file con una texture.
     * @param context Context da cui caricare le risorse.
     * @param program ShadingProgram con cui costruire il Material del Model.
     * @param textureId Indice della texture per il Model.
     * @param obj Nome del file contenente la geometria del Model.
     * @return Model generato.
     */
    public static Model getModel(Context context, ShadingProgram program, int textureId, String obj) {
        return getModelFromFileAndMaterial(context, MaterialKeeper.getInstance().getMaterial(context, program, textureId), obj);
    }

    /**
     * Costruisce un Model da un ArrayObject e da un Material dati.
     * @param arrayObject ArrayObject da cui prendere la geometria del Model.
     * @param material Material per il Model.
     * @return Model generato.
     */
    public static Model getModelFromArrayObjectAndMaterial(ArrayObject arrayObject, Material material){
        Mesh meshPos = new Mesh(arrayObject);
        meshPos.init();
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);
        modelPos.setMaterialComponent(material);
        return modelPos;
    }

    /**
     * Genera un Model caricandolo da file, ma con un Material dato.
     * @param context Context da cui caricare le risorse.
     * @param material Material con cui costruire il Model.
     * @param obj Nome del file contenente la geometria del Model.
     * @return Model generato.
     */
    public static Model getModelFromFileAndMaterial(Context context, Material material, String obj){
        return getModelFromArrayObjectAndMaterial(ObjLoader.arrayObjectFromFile(context, obj)[0], material);
    }

    /**
     * Costruisce un Node dato un ArrayObject e una Bitmap come texture.
     * @param arrayObject ArrayObject da cui prendere la geometria del Model del Node.
     * @param bitmap Bitmap da usare come texture per il Material del Node.
     * @param program ShadingProgram con cui costruire il Material del Model del Node.
     * @return Node generato.
     */
    public static Node generateNode(ArrayObject arrayObject, Bitmap bitmap, ShadingProgram program) {
        Node nodePos = new Node();
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGBA, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture bitmapTexture = BitmapTexture.loadBitmapTextureWithAlpha(bitmap, textureModel);
        bitmapTexture.init();

        Material material = new Material(program);
        material.getTextures().add(bitmapTexture);
        nodePos.setModel(getModelFromArrayObjectAndMaterial(arrayObject, material));

        return nodePos;
    }
}
