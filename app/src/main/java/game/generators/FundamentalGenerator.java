package game.generators;

import android.content.Context;

import game.graphics.MaterialKeeper;
import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.ShadingProgram;

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

}
